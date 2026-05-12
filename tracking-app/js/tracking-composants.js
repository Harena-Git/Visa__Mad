const { createApp, defineComponent } = Vue;

// Composant pour la recherche par passeport
const SearchPassportComponent = defineComponent({
    template: `
        <div class="search-form">
            <div class="form-group">
                <label for="passportNumber">🛂 Numéro de Passeport</label>
                <input 
                    id="passportNumber"
                    v-model="passportNumber"
                    type="text" 
                    placeholder="Ex: N1234567"
                    @keyup.enter="handleSearch">
            </div>
            <button 
                class="btn-search"
                @click="handleSearch"
                :disabled="!passportNumber.trim() || isLoading">
                {{ isLoading ? 'Recherche en cours...' : 'Rechercher' }}
            </button>
        </div>
    `,
    data() {
        return {
            passportNumber: '',
            isLoading: false
        };
    },
    props: ['loading'],
    watch: {
        loading(newVal) {
            this.isLoading = newVal;
        }
    },
    methods: {
        handleSearch() {
            this.$emit('search', { type: 'passport', value: this.passportNumber });
            this.passportNumber = '';
        }
    }
});

// Composant pour la recherche par token
const SearchTokenComponent = defineComponent({
    template: `
        <div class="search-form">
            <div class="form-group">
                <label for="trackingToken">🔗 Lien ou Code de Suivi</label>
                <textarea 
                    id="trackingToken"
                    v-model="trackingToken"
                    placeholder="Collez votre lien ou code de suivi ici..."
                    rows="4"
                    @keydown.meta.enter="handleSearch"
                    @keydown.ctrl.enter="handleSearch"></textarea>
            </div>
            <button 
                class="btn-search"
                @click="handleSearch"
                :disabled="!trackingToken.trim() || isLoading">
                {{ isLoading ? 'Recherche en cours...' : 'Rechercher' }}
            </button>
        </div>
    `,
    data() {
        return {
            trackingToken: '',
            isLoading: false
        };
    },
    props: ['loading'],
    watch: {
        loading(newVal) {
            this.isLoading = newVal;
        }
    },
    methods: {
        handleSearch() {
            this.$emit('search', { type: 'token', value: this.trackingToken });
            this.trackingToken = '';
        }
    }
});

// Composant pour la génération de lien
const GenerateQRComponent = defineComponent({
    template: `
        <div class="generation-form">
            <div class="qr-instruction">
                📋 Entrez le numéro de la demande pour générer un lien de suivi et un code QR
            </div>
            <div class="form-group">
                <label for="demandeId">🎯 Numéro de Demande</label>
                <input 
                    id="demandeId"
                    v-model="demandeId"
                    type="text" 
                    placeholder="Ex: DEM_REQ002"
                    @keyup.enter="handleGenerate">
            </div>
            <button 
                class="btn-search"
                @click="handleGenerate"
                :disabled="!demandeId.trim() || isLoading">
                {{ isLoading ? 'Génération en cours...' : 'Générer le Lien' }}
            </button>

            <div v-if="qrResult.success" class="qr-code-container">
                <div class="token-label">✅ Génération réussie !</div>
                <div v-if="qrResult.qrCode" class="qr-code-image">
                    <img :src="qrResult.qrCode" alt="QR Code" style="width: 100%;">
                </div>
                <div class="qr-code-info">
                    <div>
                        <strong>Code:</strong> {{ qrResult.token }}
                        <button class="copy-btn" @click="$emit('copy', 'token')">Copier</button>
                    </div>
                    <div style="margin-top: 10px;">
                        <strong>Lien:</strong>
                        <button class="copy-btn" @click="$emit('copy', 'url')">Copier le lien</button>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            demandeId: '',
            isLoading: false,
            qrResult: { success: false, token: '', qrCode: '' }
        };
    },
    props: ['loading'],
    watch: {
        loading(newVal) {
            this.isLoading = newVal;
        }
    },
    methods: {
        handleGenerate() {
            this.$emit('generate', this.demandeId);
            this.demandeId = '';
        }
    }
});

// Composant pour afficher les résultats
const SearchResultCardComponent = defineComponent({
    template: `
        <div class="demande-card">
            <div class="demande-header">
                <span class="demande-id">📋 {{ demande.demandeId }}</span>
                <span class="status-badge" :class="getStatusClass(demande.statut)">
                    {{ demande.statut || 'INCONNU' }}
                </span>
            </div>
            <div class="demande-info">
                <div class="info-row">
                    <span class="info-label">Passeport</span>
                    <span class="info-value">{{ demande.numeroPasseport }}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">Demandeur</span>
                    <span class="info-value">{{ demande.nomDemandeur }}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">Date de Création</span>
                    <span class="info-value">{{ formatDate(demande.dateCreation) }}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">Dernière Mise à Jour</span>
                    <span class="info-value">{{ formatDate(demande.dateModification) }}</span>
                </div>
            </div>
            <div v-if="demande.timeline && demande.timeline.length > 0" class="timeline">
                <div class="timeline-title">📅 Historique</div>
                <div class="timeline-item" v-for="(event, index) in demande.timeline" :key="index">
                    <div class="timeline-content">
                        <div class="timeline-date">{{ formatDate(event.date) }}</div>
                        <div class="timeline-status">{{ event.statut }}</div>
                    </div>
                </div>
            </div>
        </div>
    `,
    props: {
        demande: {
            type: Object,
            required: true
        }
    },
    methods: {
        formatDate(dateString) {
            if (!dateString) return 'N/A';
            const date = new Date(dateString);
            return date.toLocaleDateString('fr-FR', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
        },
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
        }
    }
});

// Application principale
const trackingApp = {
    components: {
        SearchPassportComponent,
        SearchTokenComponent,
        GenerateQRComponent,
        SearchResultCardComponent
    },
    template: `
        <div id="tracking-app">
            <div class="header">
                <h1>🎫 Suivi des Demandes de Visa</h1>
                <nav>
                    <ul>
                        <li><a href="index.html" class="active">Accueil</a></li>
                        <li><a href="tracking-composants.html">Version Composants</a></li>
                    </ul>
                </nav>
            </div>

            <div class="main-container">
                <div class="tracking-container">
                    <div class="tracking-card">
                        <!-- Tabs -->
                        <div class="tabs">
                            <button 
                                v-for="tab in tabs" 
                                :key="tab.id"
                                @click="activeTab = tab.id"
                                :class="['tab-button', { active: activeTab === tab.id }]">
                                {{ tab.label }}
                            </button>
                        </div>

                        <!-- Tab Content: Passport -->
                        <div v-show="activeTab === 'passport'" class="tab-content active">
                            <search-passport-component 
                                :loading="loading"
                                @search="handlePassportSearch">
                            </search-passport-component>
                        </div>

                        <!-- Tab Content: Token -->
                        <div v-show="activeTab === 'token'" class="tab-content">
                            <search-token-component 
                                :loading="loading"
                                @search="handleTokenSearch">
                            </search-token-component>
                        </div>

                        <!-- Tab Content: Generate -->
                        <div v-show="activeTab === 'generate'" class="tab-content">
                            <generate-qr-component 
                                :loading="loading"
                                @generate="handleGenerateQR"
                                @copy="copyToClipboard">
                            </generate-qr-component>
                        </div>

                        <!-- Messages -->
                        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
                        <div v-if="successMessage" class="success-message">{{ successMessage }}</div>

                        <!-- Loading -->
                        <div v-if="loading" class="loading">
                            <div class="spinner"></div>
                            <div>Traitement en cours...</div>
                        </div>

                        <!-- Results -->
                        <div v-if="results.length > 0 && !loading" class="results-container">
                            <h2 style="margin-bottom: 20px;">{{ resultsTitle }}</h2>
                            <transition-group name="list">
                                <search-result-card-component 
                                    v-for="demande in results"
                                    :key="demande.demandeId"
                                    :demande="demande">
                                </search-result-card-component>
                            </transition-group>
                        </div>

                        <!-- Empty State -->
                        <div v-if="searchPerformed && results.length === 0 && !loading && !errorMessage" class="empty-state">
                            <div class="empty-state-icon">📭</div>
                            <div>Aucun résultat trouvé. Vérifiez votre requête et réessayez.</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            activeTab: 'passport',
            loading: false,
            errorMessage: '',
            successMessage: '',
            results: [],
            resultsTitle: 'Résultats de recherche',
            searchPerformed: false,
            tabs: [
                { id: 'passport', label: '🛂 Par Numéro de Passeport' },
                { id: 'token', label: '🔗 Par Lien / Code' },
                { id: 'generate', label: '🎯 Générer un Lien' }
            ],
            apiBaseUrl: window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
                ? '/visa-backoffice/api/public/tracking'
                : '/api/public/tracking'
        };
    },
    methods: {
        async handlePassportSearch(data) {
            if (!data.value.trim()) {
                this.showError('Veuillez entrer un numéro de passeport');
                return;
            }
            const url = `${this.apiBaseUrl}/passport/${encodeURIComponent(data.value)}`;
            await this.performSearch(url, 'Vos demandes');
        },

        async handleTokenSearch(data) {
            if (!data.value.trim()) {
                this.showError('Veuillez entrer un code de suivi');
                return;
            }

            let token = data.value;

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

        async handleGenerateQR(demandeId) {
            if (!demandeId.trim()) {
                this.showError('Veuillez entrer un numéro de demande');
                return;
            }

            this.loading = true;
            this.errorMessage = '';
            this.successMessage = '';

            try {
                const baseUrlPath = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
                    ? '/visa-backoffice/tracking-composants.html?token='
                    : '/tracking-composants.html?token=';
                
                const baseUrl = window.location.origin + baseUrlPath;
                const url = '/visa-backoffice/api/demandes/' + encodeURIComponent(demandeId)
                    + '/generate-qr?mode=data&baseUrl=' + encodeURIComponent(baseUrl);

                const response = await fetch(url, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    this.showSuccess('QR code généré avec succès !');
                    // Vous pouvez émettre l'événement pour afficher le QR code
                    // Ce code dépend de votre logique UI
                } else {
                    this.showError(data.error || 'Erreur lors de la génération du lien');
                }
            } catch (error) {
                console.error('Erreur:', error);
                this.showError('Erreur lors de la génération. Veuillez réessayer.');
            } finally {
                this.loading = false;
            }
        },

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
                    this.results = [data];
                    this.resultsTitle = title;
                    this.showSuccess('Demande trouvée avec succès');
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

        copyToClipboard(field) {
            // Récupérer depuis sessionStorage ou localStorage si nécessaire
            const text = field === 'token' ? this.token : this.url;
            if (text) {
                navigator.clipboard.writeText(text).then(() => {
                    this.showSuccess('✅ Copié dans le presse-papiers !');
                }).catch(err => {
                    console.error('Erreur:', err);
                });
            }
        },

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
    }
};

// Créer et monter l'application Vue
createApp(trackingApp).mount('#app');
