const { createApp } = Vue;

const trackingApp = {
    data() {
        return {
            activeTab: 'passport',
            passportNumber: '',
            trackingToken: '',
            demandeIdInput: '',
            loading: false,
            errorMessage: '',
            successMessage: '',
            results: [],
            resultsTitle: 'Résultats de recherche',
            searchPerformed: false,
            generationResult: {
                success: false,
                token: '',
                url: '',
                qrCode: ''
            },
            tabs: [
                { id: 'passport', label: '🛂 Par Numéro de Passeport' },
                { id: 'token', label: '🔗 Par Lien / Code' },
                { id: 'generate', label: '🎯 Générer un Lien' }
            ],
            apiBaseUrl: '/visa-backoffice/api/public/tracking'
        };
    },
    methods: {
        // Rechercher par numéro de passeport
        async searchByPassport() {
            if (!this.passportNumber.trim()) {
                this.showError('Veuillez entrer un numéro de passeport');
                return;
            }

            const url = `${this.apiBaseUrl}/passport/${encodeURIComponent(this.passportNumber)}`;
            await this.performSearch(url, 'Vos demandes');
        },

        // Rechercher par token de suivi
        async searchByToken() {
            if (!this.trackingToken.trim()) {
                this.showError('Veuillez entrer un code de suivi');
                return;
            }

            let token = this.trackingToken;

            // Si c'est une URL ou contient des paramètres, extraire le token
            if (token.includes('?token=')) {
                const match = token.match(/[?&]token=([^&]*)/);
                if (match && match[1]) {
                    token = match[1];
                } else {
                    this.showError('URL invalide : paramètre "token" non trouvé');
                    return;
                }
            } else if (token.includes('?')) {
                this.showError('URL invalide : paramètre "token" attendu');
                return;
            }

            const url = `${this.apiBaseUrl}/token/${encodeURIComponent(token)}`;
            await this.performSearchByToken(url, 'Détails de votre demande');
        },

        // Effectuer la recherche (pour passeport)
        async performSearch(url, title) {
            this.loading = true;
            this.errorMessage = '';
            this.successMessage = '';
            this.results = [];
            this.searchPerformed = true;

            try {
                const response = await fetch(url);
                const data = await response.json();

                if (response.ok && data.demandes && Array.isArray(data.demandes)) {
                    if (data.demandes.length > 0) {
                        this.results = data.demandes;
                        this.resultsTitle = title;
                        this.showSuccess('Demandes trouvées avec succès');
                    } else {
                        this.showError('Aucune demande trouvée');
                    }
                } else {
                    this.showError(data.error || 'Aucune demande trouvée');
                }
            } catch (error) {
                console.error('Erreur:', error);
                this.showError('Erreur lors de la recherche. Veuillez réessayer.');
            } finally {
                this.loading = false;
            }
        },

        // Effectuer la recherche (pour token)
        async performSearchByToken(url, title) {
            this.loading = true;
            this.errorMessage = '';
            this.successMessage = '';
            this.results = [];
            this.searchPerformed = true;

            try {
                const response = await fetch(url);
                const data = await response.json();

                if (response.ok && data.demandeId) {
                    // L'API retourne directement l'objet demande enrichi, pas dans un array
                    this.results = [data];  // Wrapper dans un array pour compatibilité
                    this.resultsTitle = title;
                    this.showSuccess('Demande trouvée avec succès');
                } else if (response.ok && (data.error === 'Demande non trouvée' || data.error)) {
                    this.showError(data.error || 'Aucune demande trouvée');
                } else {
                    this.showError('Aucune demande trouvée pour ce code de suivi');
                }
            } catch (error) {
                console.error('Erreur:', error);
                this.showError('Erreur lors de la recherche. Veuillez vérifier votre code de suivi.');
            } finally {
                this.loading = false;
            }
        },

        // Générer un lien de suivi avec QR code
        async generateTrackingLink() {
            if (!this.demandeIdInput.trim()) {
                this.showError('Veuillez entrer un numéro de demande');
                return;
            }

            this.loading = true;
            this.errorMessage = '';
            this.successMessage = '';
            this.generationResult = { success: false, token: '', url: '', qrCode: '' };

            try {
                // La baseUrl doit être l'URL complète avec le paramètre token
                const baseUrl = window.location.origin + '/visa-backoffice/tracking-vue.html?token=';
                const apiUrl = '/visa-backoffice/api/demandes/' + encodeURIComponent(this.demandeIdInput)
                    + '/generate-qr?mode=url&baseUrl=' + encodeURIComponent(baseUrl);

                const response = await fetch(apiUrl, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    // Construire l'URL de suivi complète
                    const fullTrackingUrl = data.trackingUrl || (baseUrl + data.trackingToken);
                    
                    this.generationResult = {
                        success: true,
                        token: data.trackingToken,
                        url: fullTrackingUrl,
                        qrCode: data.qrCode
                    };
                    this.showSuccess('QR code généré avec succès !');
                    this.demandeIdInput = '';
                } else {
                    this.showError(data.error || 'Erreur lors de la génération du lien');
                }
            } catch (error) {
                console.error('Erreur lors de la génération QR:', error);
                this.showError('Erreur lors de la génération. Veuillez vérifier le numéro de demande.');
            } finally {
                this.loading = false;
            }
        },

        // Copier dans le presse-papiers
        copyToClipboard(field) {
            let text = '';
            if (field === 'token') {
                text = this.generationResult.token;
            } else if (field === 'url') {
                text = this.generationResult.url;
            }

            if (text) {
                navigator.clipboard.writeText(text).then(() => {
                    this.showSuccess('✅ Copié dans le presse-papiers !');
                }).catch(err => {
                    console.error('Erreur lors de la copie:', err);
                });
            }
        },

        // Formater la date
        formatDate(dateString) {
            if (!dateString) return 'N/A';
            const date = new Date(dateString);
            return date.toLocaleDateString('fr-FR', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
        },

        // Obtenir la classe CSS du statut
        getStatusClass(statut) {
            const upper = (statut || '').toUpperCase();

            if (upper.includes('APPROUVÉ') || upper.includes('CRÉÉE')) {
                return 'completed';
            } else if (upper.includes('REJETÉ')) {
                return 'rejected';
            } else if (upper.includes('SCAN') || upper.includes('EN_COURS')) {
                return 'in-progress';
            }

            return 'created';
        },

        // Afficher les messages
        showError(message) {
            this.errorMessage = '❌ ' + message;
            setTimeout(() => {
                this.errorMessage = '';
            }, 5000);
        },

        showSuccess(message) {
            this.successMessage = '✅ ' + message;
            setTimeout(() => {
                this.successMessage = '';
            }, 3000);
        }
    },
    computed: {
        fade() {
            return {
                enterActiveClass: 'v-enter-active',
                leaveActiveClass: 'v-leave-active',
                enterFromClass: 'v-enter-from',
                leaveToClass: 'v-leave-to'
            };
        }
    }
};

// Créer et monter l'application Vue
createApp(trackingApp).mount('#app');
