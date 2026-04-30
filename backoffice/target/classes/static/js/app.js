// Application JavaScript pour le CRUD Visa Management

const API_BASE_URL = '/visa-backoffice/api';

// Utilitaires
const utils = {
    // Afficher les messages d'alerte
    showAlert: function(message, type = 'success', containerId = 'alertContainer') {
        const alertContainer = document.getElementById(containerId);
        if (!alertContainer) return;
        
        const alert = document.createElement('div');
        alert.className = `alert alert-${type}`;
        alert.textContent = message;
        
        alertContainer.appendChild(alert);
        
        // Auto-supprimer après 5 secondes
        setTimeout(() => {
            alert.remove();
        }, 5000);
    },
    
    // Afficher le spinner de chargement
    showLoading: function(elementId) {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = '<div class="loading"></div>';
        }
    },
    
    // Cacher le spinner
    hideLoading: function(elementId, content = '') {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = content;
        }
    },
    
    // Formater la date
    formatDate: function(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR');
    },
    
    // Effectuer une requête API
    apiRequest: async function(url, options = {}) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                },
                ...options
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }
};

// Gestion des Demandeurs
const demandeurManager = {
    // Charger la liste des demandeurs
    loadDemandeurs: async function() {
        try {
            const demandeurs = await utils.apiRequest('/demandeurs');
            this.displayDemandeurs(demandeurs);
        } catch (error) {
            utils.showAlert('Erreur lors du chargement des demandeurs', 'danger');
        }
    },
    
    // Afficher les demandeurs dans le tableau
    displayDemandeurs: function(demandeurs) {
        const tbody = document.querySelector('#demandeursTable tbody');
        if (!tbody) return;
        
        tbody.innerHTML = '';
        
        demandeurs.forEach(demandeur => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${demandeur.id || ''}</td>
                <td>${demandeur.nom || ''}</td>
                <td>${demandeur.prenom || ''}</td>
                <td>${demandeur.email || ''}</td>
                <td>${demandeur.telephone || ''}</td>
                <td>${demandeur.nationalite?.libelle || ''}</td>
                <td>${demandeur.situationFamille?.libelle || ''}</td>
                <td>${utils.formatDate(demandeur.createdAt)}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-warning btn-sm" onclick="demandeurManager.editDemandeur('${demandeur.id}')">Modifier</button>
                        <button class="btn btn-danger btn-sm" onclick="demandeurManager.deleteDemandeur('${demandeur.id}')">Supprimer</button>
                    </div>
                </td>
            `;
            tbody.appendChild(row);
        });
    },
    
    // Ajouter un demandeur
    addDemandeur: async function(formData) {
        try {
            await utils.apiRequest('/demandeurs', {
                method: 'POST',
                body: JSON.stringify(formData)
            });
            
            utils.showAlert('Demandeur ajouté avec succès', 'success');
            this.closeModal();
            this.loadDemandeurs();
        } catch (error) {
            utils.showAlert('Erreur lors de l\'ajout du demandeur', 'danger');
        }
    },
    
    // Modifier un demandeur
    editDemandeur: async function(id) {
        try {
            const demandeur = await utils.apiRequest(`/demandeurs/${id}`);
            this.showDemandeurForm(demandeur);
        } catch (error) {
            utils.showAlert('Erreur lors du chargement du demandeur', 'danger');
        }
    },
    
    // Mettre à jour un demandeur
    updateDemandeur: async function(id, formData) {
        try {
            await utils.apiRequest(`/demandeurs/${id}`, {
                method: 'PUT',
                body: JSON.stringify(formData)
            });
            
            utils.showAlert('Demandeur mis à jour avec succès', 'success');
            this.closeModal();
            this.loadDemandeurs();
        } catch (error) {
            utils.showAlert('Erreur lors de la mise à jour du demandeur', 'danger');
        }
    },
    
    // Supprimer un demandeur
    deleteDemandeur: async function(id) {
        if (!confirm('Êtes-vous sûr de vouloir supprimer ce demandeur ?')) {
            return;
        }
        
        try {
            await utils.apiRequest(`/demandeurs/${id}`, {
                method: 'DELETE'
            });
            
            utils.showAlert('Demandeur supprimé avec succès', 'success');
            this.loadDemandeurs();
        } catch (error) {
            utils.showAlert('Erreur lors de la suppression du demandeur', 'danger');
        }
    },
    
    // Afficher le formulaire de demandeur
    showDemandeurForm: function(demandeur = null) {
        const modal = document.getElementById('demandeurModal');
        const form = document.getElementById('demandeurForm');
        const title = document.getElementById('modalTitle');
        
        if (!modal || !form) return;
        
        // Réinitialiser le formulaire
        form.reset();
        
        if (demandeur) {
            // Mode édition
            title.textContent = 'Modifier un Demandeur';
            form.dataset.id = demandeur.id;

            // Remplir les champs
            document.getElementById('nom').value = demandeur.nom || '';
            document.getElementById('prenom').value = demandeur.prenom || '';
            document.getElementById('email').value = demandeur.email || '';
            document.getElementById('telephone').value = demandeur.telephone || '';
            document.getElementById('adresse').value = demandeur.adresseMada || '';
            document.getElementById('dtn').value = demandeur.dtn || '';
            document.getElementById('nomJeuneFille').value = demandeur.nomJeuneFille || '';
            document.getElementById('nationalite').value = demandeur.nationalite?.id || '';
            document.getElementById('situationFamille').value = demandeur.situationFamille?.id || '';
        } else {
            // Mode création
            title.textContent = 'Ajouter un Demandeur';
            delete form.dataset.id;
        }
        
        modal.style.display = 'block';
    },
    
    // Fermer le modal
    closeModal: function() {
        const modal = document.getElementById('demandeurModal');
        if (modal) {
            modal.style.display = 'none';
        }
    },
    
    // Soumettre le formulaire
    submitForm: async function() {
        const form = document.getElementById('demandeurForm');
        const formData = new FormData(form);
        
        const data = {
            nom: formData.get('nom'),
            prenom: formData.get('prenom'),
            email: formData.get('email'),
            telephone: formData.get('telephone'),
            adresseMada: formData.get('adresse'),
            dtn: formData.get('dtn'),
            nationalite: { id: formData.get('nationalite') },
            situationFamille: { id: formData.get('situationFamille') }
        };
        
        if (form.dataset.id) {
            await this.updateDemandeur(form.dataset.id, data);
        } else {
            await this.addDemandeur(data);
        }
    },
    
    // Charger les données de référence
    loadReferenceData: async function() {
        try {
            const [nationalites, situations] = await Promise.all([
                utils.apiRequest('/nationalites'),
                utils.apiRequest('/situations-famille')
            ]);
            
            this.populateSelect('nationalite', nationalites, 'id', 'libelle');
            this.populateSelect('situationFamille', situations, 'id', 'libelle');
        } catch (error) {
            console.error('Erreur lors du chargement des données de référence:', error);
        }
    },
    
    // Remplir un select
    populateSelect: function(selectId, options, valueField, labelField) {
        const select = document.getElementById(selectId);
        if (!select) return;
        
        select.innerHTML = '<option value="">Sélectionner...</option>';
        
        options.forEach(option => {
            const optionElement = document.createElement('option');
            optionElement.value = option[valueField];
            optionElement.textContent = option[labelField];
            select.appendChild(optionElement);
        });
    }
};

// Gestion des Demandes
const demandeManager = {
    // Charger la liste des demandes
    loadDemandes: async function() {
        try {
            const demandes = await utils.apiRequest('/demandes');
            // Stocker les demandes pour utilisation dans les fonctions Sprint 3
            this.allDemandes = demandes;
            this.displayDemandes(demandes);
        } catch (error) {
            utils.showAlert('Erreur lors du chargement des demandes', 'danger');
        }
    },
    
    // Afficher les demandes dans le tableau
    displayDemandes: function(demandes) {
        const tbody = document.querySelector('#demandesTable tbody');
        if (!tbody) return;
        
        tbody.innerHTML = '';
        
        demandes.forEach(demande => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${demande.id || ''}</td>
                <td>${demande.demandeur?.nom || ''} ${demande.demandeur?.prenom || ''}</td>
                <td>${demande.typeVisa?.libelle || ''}</td>
                <td>${demande.categorie?.libelle || ''}</td>
                <td>${utils.formatDate(demande.createdAt)}</td>
                <td>${utils.formatDate(demande.updatedAt)}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-warning btn-sm" onclick="demandeManager.editDemande('${demande.id}')">Modifier</button>
                        <button class="btn btn-danger btn-sm" onclick="demandeManager.deleteDemande('${demande.id}')">Supprimer</button>
                    </div>
                </td>
            `;
            tbody.appendChild(row);
        });
    },
    
    // Ajouter une demande
    addDemande: async function(formData) {
        try {
            await utils.apiRequest('/demandes', {
                method: 'POST',
                body: JSON.stringify(formData)
            });
            
            utils.showAlert('Demande ajoutée avec succès', 'success');
            this.closeModal();
            this.loadDemandes();
        } catch (error) {
            utils.showAlert('Erreur lors de l\'ajout de la demande', 'danger');
        }
    },
    
    // Modifier une demande
    editDemande: async function(id) {
        try {
            const demande = await utils.apiRequest(`/demandes/${id}`);
            this.showDemandeForm(demande);
        } catch (error) {
            utils.showAlert('Erreur lors du chargement de la demande', 'danger');
        }
    },
    
    // Mettre à jour une demande
    updateDemande: async function(id, formData) {
        try {
            await utils.apiRequest(`/demandes/${id}`, {
                method: 'PUT',
                body: JSON.stringify(formData)
            });
            
            utils.showAlert('Demande mise à jour avec succès', 'success');
            this.closeModal();
            this.loadDemandes();
        } catch (error) {
            utils.showAlert('Erreur lors de la mise à jour de la demande', 'danger');
        }
    },
    
    // Supprimer une demande
    deleteDemande: async function(id) {
        if (!confirm('Êtes-vous sûr de vouloir supprimer cette demande ?')) {
            return;
        }
        
        try {
            await utils.apiRequest(`/demandes/${id}`, {
                method: 'DELETE'
            });
            
            utils.showAlert('Demande supprimée avec succès', 'success');
            this.loadDemandes();
        } catch (error) {
            utils.showAlert('Erreur lors de la suppression de la demande', 'danger');
        }
    },
    
    // Afficher le formulaire de demande
    showDemandeForm: function(demande = null) {
        const modal = document.getElementById('demandeModal');
        const form = document.getElementById('demandeForm');
        const title = document.getElementById('modalTitle');
        
        if (!modal || !form) return;
        
        form.reset();
        
        if (demande) {
            title.textContent = 'Modifier une Demande';
            form.dataset.id = demande.id;
            
            document.getElementById('categorie').value = demande.categorie?.id || '';
            document.getElementById('typeVisa').value = demande.typeVisa?.id || '';
            document.getElementById('demandeur').value = demande.demandeur?.id || '';
        } else {
            title.textContent = 'Ajouter une Demande';
            delete form.dataset.id;
        }
        
        modal.style.display = 'block';
    },
    
    // Fermer le modal
    closeModal: function() {
        const modal = document.getElementById('demandeModal');
        if (modal) {
            modal.style.display = 'none';
        }
    },
    
    // Soumettre le formulaire
    submitForm: async function() {
        const form = document.getElementById('demandeForm');
        const formData = new FormData(form);
        
        const data = {
            categorie: { id: formData.get('categorie') },
            typeVisa: { id: formData.get('typeVisa') },
            demandeur: { id: formData.get('demandeur') }
        };
        
        if (form.dataset.id) {
            await this.updateDemande(form.dataset.id, data);
        } else {
            await this.addDemande(data);
        }
    },
    
    // Charger les données de référence
    loadReferenceData: async function() {
        try {
            const [categories, typesVisa, demandeurs] = await Promise.all([
                utils.apiRequest('/categories-demande'),
                utils.apiRequest('/types-visa'),
                utils.apiRequest('/demandeurs')
            ]);
            
            this.populateSelect('categorie', categories, 'id', 'libelle');
            this.populateSelect('typeVisa', typesVisa, 'id', 'libelle');
            this.populateSelect('demandeur', demandeurs, 'id', 'nom');
        } catch (error) {
            console.error('Erreur lors du chargement des données de référence:', error);
        }
    },
    
    // Remplir un select
    populateSelect: function(selectId, options, valueField, labelField) {
        const select = document.getElementById(selectId);
        if (!select) return;
        
        select.innerHTML = '<option value="">Sélectionner...</option>';
        
        options.forEach(option => {
            const optionElement = document.createElement('option');
            optionElement.value = option[valueField];
            optionElement.textContent = option[labelField];
            select.appendChild(optionElement);
        });
    }
};

// Initialisation au chargement de la page
document.addEventListener('DOMContentLoaded', function() {
    // Initialiser les gestionnaires d'événements
    const page = document.body.dataset.page;
    
    // Vérifier si les éléments DOM existent avant de les utiliser
    switch(page) {
        case 'demandeurs':
            if (document.getElementById('demandeursTable') && document.getElementById('nationalite')) {
                demandeurManager.loadReferenceData();
                demandeurManager.loadDemandeurs();
            }
            break;
        case 'demandes':
            if (document.getElementById('demandesTable') && document.getElementById('categorie')) {
                demandeManager.loadReferenceData();
                demandeManager.loadDemandes();
            }
            break;
    }
});

// Fonctions globales pour les onclick
window.demandeurManager = demandeurManager;
window.demandeManager = demandeManager;
