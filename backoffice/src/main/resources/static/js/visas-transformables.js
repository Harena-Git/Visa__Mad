/* =========================================================
   Visa Transformable Manager (vtm)
   Gère le formulaire multi-étapes et le CRUD complet.
   ========================================================= */

const DOCS_BY_TYPE = {
    ETUDIANT: [
        { label: 'Lettre d\'inscription de l\'établissement scolaire', required: true },
        { label: 'Certificat de scolarité en cours de validité', required: true },
        { label: 'Justificatif de ressources (parent/bourse)', required: true },
        { label: 'Passeport valide', required: true },
        { label: 'Photos d\'identité (4)', required: true },
        { label: 'Assurance maladie', required: false },
    ],
    TRAVAILLEUR: [
        { label: 'Contrat de travail signé', required: true },
        { label: 'Autorisation de travail délivrée par le Ministère du Travail', required: true },
        { label: 'Extrait de casier judiciaire (moins de 3 mois)', required: true },
        { label: 'Certificat médical', required: true },
        { label: 'Passeport valide', required: true },
        { label: 'Photos d\'identité (4)', required: true },
        { label: 'Justificatif de logement', required: false },
    ],
    MISSIONNAIRE: [
        { label: 'Lettre de mission de l\'organisation', required: true },
        { label: 'Accréditation de l\'organisation reconnue par l\'État', required: true },
        { label: 'Extrait de casier judiciaire (moins de 3 mois)', required: true },
        { label: 'Certificat médical', required: true },
        { label: 'Passeport valide', required: true },
        { label: 'Photos d\'identité (4)', required: true },
        { label: 'Lettre de garantie financière', required: false },
    ],
};

const vtm = {
    currentStep: 1,
    totalSteps: 4,
    editId: null,
    allVisas: [],       // tous les visas chargés pour le filtre client
    allPassports: [],
    allDemandeurs: [],
    allVisasNormaux: [],

    /* ── Chargement initial ── */
    init: async function () {
        await this.loadReferenceData();
        await this.loadAllVisas();
        // écouter le changement de type de radio
        document.querySelectorAll('input[name="vtType"]').forEach(r =>
            r.addEventListener('change', () => vtm.onTypeChange(r.value))
        );
    },

    loadReferenceData: async function () {
        try {
            const [demandeurs, passports, visas] = await Promise.all([
                utils.apiRequest('/demandeurs'),
                utils.apiRequest('/passports'),
                utils.apiRequest('/visas'),
            ]);
            this.allDemandeurs = demandeurs;
            this.allPassports = passports;
            this.allVisasNormaux = visas;

            // Remplir select demandeur
            const selD = document.getElementById('vtDemandeur');
            selD.innerHTML = '<option value="">Sélectionner un demandeur...</option>';
            demandeurs.forEach(d => selD.appendChild(new Option(`${d.nom} ${d.prenom || ''}`, d.id)));

            // Remplir select visa original
            const selV = document.getElementById('vtVisaOriginal');
            selV.innerHTML = '<option value="">Aucun / Non lié</option>';
            visas.forEach(v => selV.appendChild(new Option(`${v.refVisa} (fin: ${utils.formatDate(v.dateFin)})`, v.id, false, false)));
        } catch (e) { console.error('loadReferenceData', e); }
    },

    loadAllVisas: async function () {
        try {
            const tbody = document.querySelector('#visas-transformablesTable tbody');
            if (tbody) tbody.innerHTML = '<tr><td colspan="8" class="text-center">Chargement...</td></tr>';
            this.allVisas = await utils.apiRequest('/visas-transformables');
            this.renderTable(this.allVisas);
        } catch (e) {
            utils.showAlert('Erreur lors du chargement des visas transformables', 'danger');
        }
    },

    /* ── Rendu table ── */
    renderTable: function (visas) {
        let tbody = document.querySelector('#visas-transformablesTable tbody');
        if (!tbody) {
            tbody = document.createElement('tbody');
            document.getElementById('visas-transformablesTable').appendChild(tbody);
        }
        tbody.innerHTML = '';
        if (!visas.length) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center">Aucun visa transformable trouvé</td></tr>';
            return;
        }
        visas.forEach(v => {
            const type = v.typeTransformable || '';
            const badge = type === 'ETUDIANT' ? `<span class="badge-etudiant">${type}</span>`
                        : type === 'TRAVAILLEUR' ? `<span class="badge-travailleur">${type}</span>`
                        : type === 'MISSIONNAIRE' ? `<span class="badge-missionnaire">${type}</span>`
                        : type;
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${v.refVisa || ''}</td>
                <td>${badge}</td>
                <td>${v.demandeur ? v.demandeur.nom + ' ' + (v.demandeur.prenom || '') : ''}</td>
                <td>${v.passport?.numero || ''}</td>
                <td>${v.visaOriginal?.refVisa || '<em style="color:#999">-</em>'}</td>
                <td>${utils.formatDate(v.dateDebut)}</td>
                <td>${utils.formatDate(v.dateFin)}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-info btn-sm" onclick="vtm.imprimer('${v.id}')">Imprimer</button>
                        <button class="btn btn-warning btn-sm" onclick="vtm.editVisa('${v.id}')">Modifier</button>
                        <button class="btn btn-danger btn-sm" onclick="vtm.deleteVisa('${v.id}')">Supprimer</button>
                    </div>
                </td>`;
            tbody.appendChild(row);
        });
    },

    applyFilter: function () {
        const type = document.getElementById('filterType').value.toUpperCase();
        const ref  = (document.getElementById('filterRef').value || '').toLowerCase();
        const filtered = this.allVisas.filter(v => {
            const matchType = !type || (v.typeTransformable || '') === type;
            const matchRef  = !ref  || (v.refVisa || '').toLowerCase().includes(ref);
            return matchType && matchRef;
        });
        this.renderTable(filtered);
    },

    /* ── Stepper navigation ── */
    goStep: function (step) {
        if (step > this.currentStep && !this.validateStep(this.currentStep)) return;

        for (let i = 1; i <= this.totalSteps; i++) {
            document.getElementById(`s${i}`).className = 'step' + (i < step ? ' done' : i === step ? ' active' : '');
            const panel = document.getElementById(`panel${i}`);
            panel.classList.toggle('active', i === step);
        }
        this.currentStep = step;
    },

    validateStep: function (step) {
        if (step === 1) {
            if (!document.getElementById('vtDemandeur').value) {
                utils.showAlert('Veuillez sélectionner un demandeur.', 'warning');
                return false;
            }
        }
        if (step === 2) {
            if (!document.getElementById('vtPassport').value) {
                utils.showAlert('Veuillez sélectionner un passeport.', 'warning');
                return false;
            }
        }
        if (step === 3) {
            if (!document.getElementById('vtRef').value || !document.getElementById('vtDateDebut').value || !document.getElementById('vtDateFin').value) {
                utils.showAlert('Veuillez remplir tous les champs obligatoires.', 'warning');
                return false;
            }
        }
        return true;
    },

    /* ── Events sur les selects ── */
    onDemandeurChange: function () {
        const id = document.getElementById('vtDemandeur').value;
        const d = this.allDemandeurs.find(x => x.id === id);
        const card = document.getElementById('demandeurCard');
        if (d) {
            card.style.display = 'block';
            card.innerHTML = `
                <dl>
                    <dt>Nom :</dt><dd>${d.nom} ${d.prenom || ''}</dd>
                    <dt>Nationalité :</dt><dd>${d.nationalite?.libelle || '-'}</dd>
                    <dt>Situation familiale :</dt><dd>${d.situationFamille?.libelle || '-'}</dd>
                    <dt>Email :</dt><dd>${d.email || '-'}</dd>
                    <dt>Téléphone :</dt><dd>${d.telephone || '-'}</dd>
                </dl>`;
            // Filtrer les passeports par demandeur
            const selP = document.getElementById('vtPassport');
            selP.innerHTML = '<option value="">Sélectionner un passeport...</option>';
            this.allPassports
                .filter(p => p.demandeur?.id === id)
                .forEach(p => selP.appendChild(new Option(`${p.numero} (expire: ${utils.formatDate(p.expireLe)})`, p.id)));
        } else {
            card.style.display = 'none';
        }
    },

    onPassportChange: function () {
        const id = document.getElementById('vtPassport').value;
        const p = this.allPassports.find(x => x.id === id);
        const card = document.getElementById('passportCard');
        if (p) {
            card.style.display = 'block';
            card.innerHTML = `
                <dl>
                    <dt>Numéro :</dt><dd>${p.numero}</dd>
                    <dt>Délivré le :</dt><dd>${utils.formatDate(p.delivreLe)}</dd>
                    <dt>Expire le :</dt><dd>${utils.formatDate(p.expireLe)}</dd>
                </dl>`;
        } else {
            card.style.display = 'none';
        }
    },

    onVisaOriginalChange: function () {
        const id = document.getElementById('vtVisaOriginal').value;
        const dateDebutVal = document.getElementById('vtDateDebut').value;
        const warn = document.getElementById('visaOriginalWarning');
        if (!id || !dateDebutVal) { warn.style.display = 'none'; return; }
        const v = this.allVisasNormaux.find(x => x.id === id);
        if (v && v.dateFin) {
            const finNormal = new Date(v.dateFin);
            const debut = new Date(dateDebutVal);
            warn.style.display = debut > finNormal ? 'block' : 'none';
        }
    },

    onTypeChange: function (type) {
        const section = document.getElementById('docsSection');
        const list = document.getElementById('docList');
        const docs = DOCS_BY_TYPE[type];
        if (!docs) { section.style.display = 'none'; return; }
        section.style.display = 'block';
        list.innerHTML = '';
        docs.forEach(doc => {
            const li = document.createElement('li');
            li.innerHTML = `<span>${doc.label}</span><span class="doc-badge ${doc.required ? '' : 'optional'}">${doc.required ? 'Obligatoire' : 'Optionnel'}</span>`;
            list.appendChild(li);
        });
    },

    /* ── CRUD ── */
    showForm: function (visa = null) {
        this.editId = visa ? visa.id : null;
        document.getElementById('vtModalTitle').textContent = visa ? 'Modifier Visa Transformable' : 'Nouveau Visa Transformable';
        document.getElementById('vtForm').reset();
        document.getElementById('demandeurCard').style.display = 'none';
        document.getElementById('passportCard').style.display = 'none';
        document.getElementById('docsSection').style.display = 'none';
        document.getElementById('visaOriginalWarning').style.display = 'none';

        if (visa) {
            if (visa.demandeur?.id) {
                document.getElementById('vtDemandeur').value = visa.demandeur.id;
                this.onDemandeurChange();
            }
            if (visa.passport?.id) document.getElementById('vtPassport').value = visa.passport.id;
            document.getElementById('vtRef').value = visa.refVisa || '';
            document.getElementById('vtDateDebut').value = visa.dateDebut || '';
            document.getElementById('vtDateFin').value = visa.dateFin || '';
            if (visa.visaOriginal?.id) document.getElementById('vtVisaOriginal').value = visa.visaOriginal.id;
            if (visa.typeTransformable) {
                const radio = document.querySelector(`input[name="vtType"][value="${visa.typeTransformable}"]`);
                if (radio) { radio.checked = true; this.onTypeChange(visa.typeTransformable); }
            }
        }
        this.goStep(1);
        document.getElementById('vtModal').style.display = 'block';
    },

    closeModal: function () { document.getElementById('vtModal').style.display = 'none'; },

    submitForm: async function () {
        const type = document.querySelector('input[name="vtType"]:checked')?.value;
        if (!type) { utils.showAlert('Veuillez sélectionner un type de visa.', 'warning'); return; }

        const visaOriginalId = document.getElementById('vtVisaOriginal').value;
        const data = {
            refVisa: document.getElementById('vtRef').value,
            typeTransformable: type,
            dateDebut: document.getElementById('vtDateDebut').value,
            dateFin: document.getElementById('vtDateFin').value,
            passport: { id: document.getElementById('vtPassport').value },
            demandeur: { id: document.getElementById('vtDemandeur').value },
            visaOriginal: visaOriginalId ? { id: visaOriginalId } : null,
        };

        try {
            if (this.editId) {
                await utils.apiRequest(`/visas-transformables/${this.editId}`, { method: 'PUT', body: JSON.stringify(data) });
                utils.showAlert('Visa transformable mis à jour', 'success');
            } else {
                await utils.apiRequest('/visas-transformables', { method: 'POST', body: JSON.stringify(data) });
                utils.showAlert('Visa transformable créé avec succès', 'success');
            }
            this.closeModal();
            await this.loadAllVisas();
        } catch (e) {
            utils.showAlert(e.message || 'Erreur lors de l\'enregistrement', 'danger');
        }
    },

    editVisa: async function (id) {
        try {
            const visa = await utils.apiRequest(`/visas-transformables/${id}`);
            this.showForm(visa);
        } catch (e) { utils.showAlert('Erreur', 'danger'); }
    },

    deleteVisa: async function (id) {
        if (!confirm('Confirmer la suppression de ce visa transformable ?')) return;
        try {
            await utils.apiRequest(`/visas-transformables/${id}`, { method: 'DELETE' });
            utils.showAlert('Visa transformable supprimé', 'success');
            await this.loadAllVisas();
        } catch (e) { utils.showAlert('Erreur lors de la suppression', 'danger'); }
    },

    /* ── Impression ── */
    imprimer: async function (id) {
        try {
            const v = await utils.apiRequest(`/visas-transformables/${id}`);
            const demandeur = v.demandeur || {};
            const passport  = v.passport  || {};
            const typeLabel = { ETUDIANT: 'Étudiant', TRAVAILLEUR: 'Travailleur', MISSIONNAIRE: 'Missionnaire' }[v.typeTransformable] || v.typeTransformable;

            const printArea = document.getElementById('printArea');
            printArea.innerHTML = `
                <div class="print-doc">
                    <h2>VISA TRANSFORMABLE</h2>
                    <table>
                        <tr><td>Référence</td><td>${v.refVisa}</td></tr>
                        <tr><td>Type</td><td>${typeLabel}</td></tr>
                        <tr><td>Nom du titulaire</td><td>${demandeur.nom || ''} ${demandeur.prenom || ''}</td></tr>
                        <tr><td>Nationalité</td><td>${demandeur.nationalite?.libelle || '-'}</td></tr>
                        <tr><td>Numéro de passeport</td><td>${passport.numero || '-'}</td></tr>
                        <tr><td>Date de début de validité</td><td>${utils.formatDate(v.dateDebut)}</td></tr>
                        <tr><td>Date de fin de validité</td><td>${utils.formatDate(v.dateFin)}</td></tr>
                        ${v.visaOriginal ? `<tr><td>Visa normal d'origine</td><td>${v.visaOriginal.refVisa}</td></tr>` : ''}
                    </table>
                    <p style="margin-top:20px;font-style:italic;font-size:.9rem;">Délivré par le Ministère de l'Intérieur — République de Madagascar</p>
                </div>
                <div class="print-doc">
                    <h2>CARTE DE RÉSIDENT</h2>
                    <table>
                        <tr><td>Référence visa</td><td>${v.refVisa}</td></tr>
                        <tr><td>Type séjour</td><td>${typeLabel}</td></tr>
                        <tr><td>Nom</td><td>${demandeur.nom || ''} ${demandeur.prenom || ''}</td></tr>
                        <tr><td>Nationalité</td><td>${demandeur.nationalite?.libelle || '-'}</td></tr>
                        <tr><td>Passeport N°</td><td>${passport.numero || '-'}</td></tr>
                        <tr><td>Valable du</td><td>${utils.formatDate(v.dateDebut)}</td></tr>
                        <tr><td>Valable au</td><td>${utils.formatDate(v.dateFin)}</td></tr>
                    </table>
                    <p style="margin-top:20px;font-style:italic;font-size:.9rem;">À conserver avec le passeport — En cas de perte, faire une demande de duplicata</p>
                </div>`;
            printArea.style.display = 'block';
            window.print();
            printArea.style.display = 'none';
        } catch (e) { utils.showAlert('Erreur lors de l\'impression', 'danger'); }
    },
};

document.addEventListener('DOMContentLoaded', function () {
    if (document.body.dataset.page === 'visas-transformables') {
        vtm.init();
    }
});
