/* =========================================================
   Visa Manager — gère titre simple et duplicata
   ========================================================= */

const visaManager = {
    allVisas: [],
    allPassports: [],
    allDemandes: [],

    init: async function () {
        await this.loadReferenceData();
        await this.loadAllVisas();
        // Toggle duplicata section when radio changes
        document.querySelectorAll('input[name="visaCategorie"]').forEach(r =>
            r.addEventListener('change', () => visaManager.onCategorieChange(r.value))
        );
    },

    loadReferenceData: async function () {
        try {
            const [passports, demandes, visas] = await Promise.all([
                utils.apiRequest('/passports'),
                utils.apiRequest('/demandes'),
                utils.apiRequest('/visas'),
            ]);
            this.allPassports = passports;
            this.allDemandes  = demandes;
            this.allVisas     = visas;

            this.populateSelect('passport', passports, 'id', p => `${p.numero} (${p.demandeur?.nom || '?'})`);
            this.populateSelect('demande',  demandes,  'id', d => `${d.id} — ${d.typeVisa?.libelle || ''} / ${d.categorie?.libelle || ''}`);
            this.populateSelect('visaOriginal', visas, 'id', v => `${v.refVisa} (fin: ${utils.formatDate(v.dateFin)})`);
        } catch (e) { console.error('loadReferenceData', e); }
    },

    loadAllVisas: async function () {
        try {
            const tbody = document.querySelector('#visasTable tbody');
            if (tbody) tbody.innerHTML = '<tr><td colspan="8" class="text-center">Chargement...</td></tr>';
            this.allVisas = await utils.apiRequest('/visas');
            this.renderTable(this.allVisas);
        } catch (e) {
            utils.showAlert('Erreur lors du chargement des visas', 'danger');
        }
    },

    /* ── Rendu table ── */
    renderTable: function (visas) {
        let tbody = document.querySelector('#visasTable tbody');
        if (!tbody) {
            tbody = document.createElement('tbody');
            document.getElementById('visasTable').appendChild(tbody);
        }
        tbody.innerHTML = '';
        if (!visas.length) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center">Aucun visa trouvé</td></tr>';
            return;
        }
        visas.forEach(v => {
            // On déduit la catégorie depuis la demande liée
            const catLib = v.demande?.categorie?.libelle || '';
            const isDuplicata = catLib.toUpperCase().includes('DUPLICATA');
            const badge = isDuplicata
                ? '<span class="badge-duplicata">DUPLICATA</span>'
                : '<span class="badge-titre-simple">TITRE SIMPLE</span>';

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${v.refVisa || ''}</td>
                <td>${badge}</td>
                <td>${v.demande?.demandeur?.nom || v.passport?.demandeur?.nom || ''}</td>
                <td>${v.passport?.numero || ''}</td>
                <td>${v.demande?.demandeLiee ? v.demande.demandeLiee.id : '<em style="color:#999">-</em>'}</td>
                <td>${utils.formatDate(v.dateDebut)}</td>
                <td>${utils.formatDate(v.dateFin)}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-info btn-sm" onclick="visaManager.imprimer('${v.id}')">Imprimer</button>
                        <button class="btn btn-warning btn-sm" onclick="visaManager.editVisa('${v.id}')">Modifier</button>
                        <button class="btn btn-danger btn-sm" onclick="visaManager.deleteVisa('${v.id}')">Supprimer</button>
                    </div>
                </td>`;
            tbody.appendChild(row);
        });
    },

    applyFilter: function () {
        const cat = document.getElementById('filterCategorie').value.toUpperCase();
        const ref = (document.getElementById('filterRef').value || '').toLowerCase();
        const filtered = this.allVisas.filter(v => {
            const catLib = (v.demande?.categorie?.libelle || '').toUpperCase();
            const matchCat = !cat
                || (cat === 'DUPLICATA'    && catLib.includes('DUPLICATA'))
                || (cat === 'TITRE_SIMPLE' && !catLib.includes('DUPLICATA'));
            const matchRef = !ref || (v.refVisa || '').toLowerCase().includes(ref);
            return matchCat && matchRef;
        });
        this.renderTable(filtered);
    },

    /* ── Formulaire ── */
    onCategorieChange: function (value) {
        document.getElementById('duplicataGroup').style.display = value === 'DUPLICATA' ? 'block' : 'none';
    },

    showVisaForm: function (visa = null) {
        document.getElementById('modalTitle').textContent = visa ? 'Modifier le Visa' : 'Nouveau Visa — Titre Simple';
        this._openForm(visa, 'TITRE_SIMPLE');
    },

    showDuplicataForm: function () {
        document.getElementById('modalTitle').textContent = 'Nouveau Duplicata de Visa';
        this._openForm(null, 'DUPLICATA');
    },

    _openForm: function (visa, defaultCat) {
        const form = document.getElementById('visaForm');
        form.reset();
        delete form.dataset.id;

        const cat = visa
            ? (visa.demande?.categorie?.libelle?.toUpperCase().includes('DUPLICATA') ? 'DUPLICATA' : 'TITRE_SIMPLE')
            : defaultCat;
        document.querySelector(`input[name="visaCategorie"][value="${cat}"]`).checked = true;
        this.onCategorieChange(cat);

        if (visa) {
            form.dataset.id = visa.id;
            document.getElementById('refVisa').value    = visa.refVisa || '';
            document.getElementById('dateDebut').value  = visa.dateDebut ? visa.dateDebut.split('T')[0] : '';
            document.getElementById('dateFin').value    = visa.dateFin  ? visa.dateFin.split('T')[0]   : '';
            document.getElementById('passport').value   = visa.passport?.id || '';
            document.getElementById('demande').value    = visa.demande?.id  || '';
            if (visa.demande?.demandeLiee)
                document.getElementById('visaOriginal').value = visa.demande.demandeLiee.id || '';
        }
        document.getElementById('visaModal').style.display = 'block';
    },

    closeModal: function () { document.getElementById('visaModal').style.display = 'none'; },

    submitForm: async function () {
        const form = document.getElementById('visaForm');
        const data = {
            refVisa:   form.refVisa.value,
            dateDebut: form.dateDebut.value + 'T00:00:00',
            dateFin:   form.dateFin.value   + 'T00:00:00',
            passport:  { id: form.passport.value },
            demande:   { id: form.demande.value  },
        };
        try {
            if (form.dataset.id) {
                await utils.apiRequest(`/visas/${form.dataset.id}`, { method: 'PUT', body: JSON.stringify(data) });
                utils.showAlert('Visa mis à jour', 'success');
            } else {
                await utils.apiRequest('/visas', { method: 'POST', body: JSON.stringify(data) });
                utils.showAlert('Visa créé avec succès', 'success');
            }
            this.closeModal();
            await this.loadAllVisas();
        } catch (e) { utils.showAlert(e.message || 'Erreur lors de l\'enregistrement', 'danger'); }
    },

    editVisa: async function (id) {
        try {
            const visa = await utils.apiRequest(`/visas/${id}`);
            this.showVisaForm(visa);
        } catch (e) { utils.showAlert('Erreur', 'danger'); }
    },

    deleteVisa: async function (id) {
        if (!confirm('Confirmer la suppression ?')) return;
        try {
            await utils.apiRequest(`/visas/${id}`, { method: 'DELETE' });
            utils.showAlert('Visa supprimé', 'success');
            await this.loadAllVisas();
        } catch (e) { utils.showAlert('Erreur lors de la suppression', 'danger'); }
    },

    /* ── Impression ── */
    imprimer: async function (id) {
        try {
            const v = await utils.apiRequest(`/visas/${id}`);
            const demandeur = v.demande?.demandeur || v.passport?.demandeur || {};
            const passport  = v.passport || {};
            const catLib    = v.demande?.categorie?.libelle || 'TITRE SIMPLE';
            const typeLib   = v.demande?.typeVisa?.libelle  || '-';
            const isDuplicata = catLib.toUpperCase().includes('DUPLICATA');

            const printArea = document.getElementById('printArea');
            printArea.innerHTML = `
                <div class="print-doc">
                    <h2>VISA NATIONAL${isDuplicata ? ' — DUPLICATA' : ''}</h2>
                    <table>
                        <tr><td>Référence</td><td>${v.refVisa}</td></tr>
                        <tr><td>Catégorie</td><td>${catLib}</td></tr>
                        <tr><td>Type de visa</td><td>${typeLib}</td></tr>
                        <tr><td>Nom du titulaire</td><td>${demandeur.nom || ''} ${demandeur.prenom || ''}</td></tr>
                        <tr><td>Nationalité</td><td>${demandeur.nationalite?.libelle || '-'}</td></tr>
                        <tr><td>Numéro de passeport</td><td>${passport.numero || '-'}</td></tr>
                        <tr><td>Date de début de validité</td><td>${utils.formatDate(v.dateDebut)}</td></tr>
                        <tr><td>Date de fin de validité</td><td>${utils.formatDate(v.dateFin)}</td></tr>
                        ${isDuplicata && v.demande?.demandeLiee ? `<tr><td>Demande d'origine</td><td>${v.demande.demandeLiee.id}</td></tr>` : ''}
                    </table>
                    <p style="margin-top:20px;font-style:italic;font-size:.9rem;">Délivré par le Ministère de l'Intérieur — République de Madagascar</p>
                </div>
                <div class="print-doc">
                    <h2>CARTE DE RÉSIDENT${isDuplicata ? ' — DUPLICATA' : ''}</h2>
                    <table>
                        <tr><td>Référence visa</td><td>${v.refVisa}</td></tr>
                        <tr><td>Type</td><td>${typeLib}</td></tr>
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

    /* ── Utils ── */
    populateSelect: function (selectId, options, valueField, labelFn) {
        const select = document.getElementById(selectId);
        if (!select) return;
        select.innerHTML = '<option value="">Sélectionner...</option>';
        options.forEach(opt => {
            const el = document.createElement('option');
            el.value = opt[valueField];
            el.textContent = typeof labelFn === 'function' ? labelFn(opt) : opt[labelFn];
            select.appendChild(el);
        });
    },
};

document.addEventListener('DOMContentLoaded', function () {
    if (document.body.dataset.page === 'visas') {
        visaManager.init();
    }
});
