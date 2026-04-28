// Gestion des Passeports
const passportManager = {
    allPassports: [],

    // Charger la liste des passeports
    loadPassports: async function() {
        try {
            const tbody = document.querySelector('#passportsTable tbody');
            if (tbody) tbody.innerHTML = '<tr><td colspan="6" class="text-center">Chargement...</td></tr>';
            this.allPassports = await utils.apiRequest('/passports');
            this.renderTable(this.allPassports);
        } catch (error) {
            utils.showAlert('Erreur lors du chargement des passeports', 'danger');
        }
    },
    
    // Afficher les passeports dans le tableau
    renderTable: function(passports) {
        let tbody = document.querySelector('#passportsTable tbody');
        if (!tbody) {
            tbody = document.createElement('tbody');
            document.getElementById('passportsTable').appendChild(tbody);
        }
        
        tbody.innerHTML = '';
        
        if (!passports.length) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">Aucun passeport trouvé</td></tr>';
            return;
        }

        passports.forEach(passport => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${passport.id || ''}</td>
                <td><strong>${passport.numero || ''}</strong></td>
                <td>${utils.formatDate(passport.delivreLe)}</td>
                <td>${utils.formatDate(passport.expireLe)}</td>
                <td>${passport.demandeur?.nom || ''} ${passport.demandeur?.prenom || ''}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-warning btn-sm" onclick="passportManager.editPassport('${passport.id}')">Modifier</button>
                        <button class="btn btn-danger btn-sm" onclick="passportManager.deletePassport('${passport.id}')">Supprimer</button>
                    </div>
                </td>
            `;
            tbody.appendChild(row);
        });
    },

    searchPassports: function() {
        const searchTerm = document.getElementById('searchInput').value.toLowerCase();
        const filtered = this.allPassports.filter(p => p.numero.toLowerCase().includes(searchTerm));
        this.renderTable(filtered);
    },
    
    // Ajouter un passeport
    addPassport: async function(formData) {
        try {
            await utils.apiRequest('/passports', {
                method: 'POST',
                body: JSON.stringify(formData)
            });
            
            utils.showAlert('Passeport ajouté avec succès', 'success');
            this.closeModal();
            this.loadPassports();
        } catch (error) {
            utils.showAlert('Erreur lors de l\'ajout du passeport', 'danger');
        }
    },
    
    // Modifier un passeport
    editPassport: async function(id) {
        try {
            const passport = await utils.apiRequest(`/passports/${id}`);
            this.showPassportForm(passport);
        } catch (error) {
            utils.showAlert('Erreur lors du chargement du passeport', 'danger');
        }
    },
    
    // Mettre à jour un passeport
    updatePassport: async function(id, formData) {
        try {
            await utils.apiRequest(`/passports/${id}`, {
                method: 'PUT',
                body: JSON.stringify(formData)
            });
            
            utils.showAlert('Passeport mis à jour avec succès', 'success');
            this.closeModal();
            this.loadPassports();
        } catch (error) {
            utils.showAlert('Erreur lors de la mise à jour du passeport', 'danger');
        }
    },
    
    // Supprimer un passeport
    deletePassport: async function(id) {
        if (!confirm('Êtes-vous sûr de vouloir supprimer ce passeport ?')) {
            return;
        }
        
        try {
            await utils.apiRequest(`/passports/${id}`, {
                method: 'DELETE'
            });
            
            utils.showAlert('Passeport supprimé avec succès', 'success');
            this.loadPassports();
        } catch (error) {
            utils.showAlert('Erreur lors de la suppression du passeport', 'danger');
        }
    },
    
    // Afficher le formulaire de passeport
    showPassportForm: function(passport = null) {
        const modal = document.getElementById('passportModal');
        const form = document.getElementById('passportForm');
        const title = document.getElementById('modalTitle');
        
        if (!modal || !form) return;
        
        // Réinitialiser le formulaire
        form.reset();
        
        if (passport) {
            // Mode édition
            title.textContent = 'Modifier un Passeport';
            form.dataset.id = passport.id;

            // Remplir les champs
            document.getElementById('numero').value = passport.numero || '';
            document.getElementById('delivreLe').value = passport.delivreLe || '';
            document.getElementById('expireLe').value = passport.expireLe || '';
            document.getElementById('demandeur').value = passport.demandeur?.id || '';
        } else {
            // Mode création
            title.textContent = 'Ajouter un Passeport';
            delete form.dataset.id;
        }
        
        modal.style.display = 'block';
    },
    
    // Fermer le modal
    closeModal: function() {
        const modal = document.getElementById('passportModal');
        if (modal) {
            modal.style.display = 'none';
        }
    },
    
    // Soumettre le formulaire
    submitForm: async function() {
        const form = document.getElementById('passportForm');
        const formData = new FormData(form);
        
        const data = {
            numero: formData.get('numero'),
            delivreLe: formData.get('delivreLe'),
            expireLe: formData.get('expireLe'),
            demandeur: { id: formData.get('demandeur') }
        };
        
        if (form.dataset.id) {
            await this.updatePassport(form.dataset.id, data);
        } else {
            await this.addPassport(data);
        }
    },
    
    // Charger les données de référence
    loadReferenceData: async function() {
        try {
            const demandeurs = await utils.apiRequest('/demandeurs');
            const select = document.getElementById('demandeur');
            select.innerHTML = '<option value="">Sélectionner...</option>';
            demandeurs.forEach(d => {
                const opt = document.createElement('option');
                opt.value = d.id;
                opt.textContent = `${d.nom} ${d.prenom}`;
                select.appendChild(opt);
            });
        } catch (error) {
            console.error('Erreur lors du chargement des demandeurs:', error);
        }
    }
};

// Initialisation
document.addEventListener('DOMContentLoaded', () => {
    if (document.body.dataset.page === 'passports') {
        passportManager.loadReferenceData();
        passportManager.loadPassports();
    }
});
