const { createApp, defineComponent, ref, computed } = Vue;

// ⭐ Composant : Recherche par Passeport
const SearchPassportComponent = defineComponent({
    name: 'SearchPassport',
    props: ['state'],
    emits: ['search', 'clear'],
    template: `
        <div class="search-form">
            <div class="form-group">
                <label for="passportNumber">Numéro de Passeport *</label>
                <input 
                    v-model="passportNumber" 
                    id="passportNumber"
                    type="text" 
                    placeholder="Ex: PAD123456789"
                    @keyup.enter="search"
                    :disabled="state.loading"
                >
            </div>
            <button 
                class="btn-search" 
                @click="search"
                :disabled="state.loading || !passportNumber.trim()"
            >
                🔍 Rechercher mes Demandes
            </button>
        </div>
    `,
    data() {
        return { passportNumber: '' };
    },
    methods: {
        search() {
            if (!this.passportNumber.trim()) {
                this.$emit('clear', { type: 'error', text: 'Veuillez entrer un numéro de passeport' });
                return;
            }
            this.$emit('search', {
                type: 'passport',
                value: this.passportNumber
            });
        }
    }
});

// ⭐ Composant : Recherche par Token
const SearchTokenComponent = defineComponent({
    name: 'SearchToken',
    props: ['state'],
    emits: ['search', 'clear'],
    template: `
        <div>
            <div class="qr-instruction">
                📱 Vous avez reçu un QR code ? Scannez-le ou collez le code de suivi ci-dessous.
            </div>
            <div class="search-form">
                <div class="form-group">
                    <label for="trackingToken">Code de Suivi ou Lien *</label>
                    <input 
                        v-model="trackingToken" 
                        id="trackingToken"
                        type="text" 
                        placeholder="Collez le code unique de suivi reçu"
                        @keyup.enter="search"
                        :disabled="state.loading"
                    >
                </div>
                <button 
                    class="btn-search" 
                    @click="search"
                    :disabled="state.loading || !trackingToken.trim()"
                >
                    🔗 Accéder à mon Suivi
                </button>
            </div>
        </div>
    `,
    data() {
        return { trackingToken: '' };
    },
    methods: {
        search() {
            if (!this.trackingToken.trim()) {
                this.$emit('clear', { type: 'error', text: 'Veuillez entrer un code de suivi' });
                return;
            }
            this.$emit('search', {
                type: 'token',
                value: this.trackingToken
            });
        }
    }
});

// ⭐ Composant : Générer QR Code
const GenerateQRComponent = defineComponent({
    name: 'GenerateQR',
    props: ['state'],
    emits: ['generate', 'copy', 'clear'],
    template: `
        <div>
            <div class="qr-instruction">
                🔑 Entrez le numéro de votre demande pour générer un lien de suivi unique avec QR code.
            </div>
            <div class="search-form">
                <div class="form-group">
                    <label for="demandeIdInput">Numéro de Demande *</label>
                    <input 
                        v-model="demandeId" 
                        id="demandeIdInput"
                        type="text" 
                        placeholder="Ex: REQ-SPRINT4-001"
                        @keyup.enter="generate"
                        :disabled="state.loading"
                    >
                </div>
                <button 
                    class="btn-search" 
                    @click="generate"
                    :disabled="state.loading || !demandeId.trim()"
                >
                    🎯 Générer le Lien de Suivi
                </button>
            </div>
            
            <!-- Résultat de génération -->
            <div v-if="state.generationResult.success" style="margin-top: 20px;">
                <h3 style="color: #333; margin-bottom: 15px;">✅ Lien généré avec succès</h3>
                
                <div class="token-label">Code de Suivi Unique :</div>
                <div class="input-copy-container">
                    <input 
                        type="text"
                        :value="state.generationResult.token"
                        readonly
                    >
                    <button class="copy-btn" @click="$emit('copy', 'token')">📋 Copier</button>
                </div>
                
                <div class="token-label" style="margin-top: 20px;">URL de Suivi Complète :</div>
                <div class="input-copy-container">
                    <input 
                        type="text"
                        :value="state.generationResult.url"
                        readonly
                        style="font-size: 12px;"
                    >
                    <button class="copy-btn" @click="$emit('copy', 'url')">📋 Copier</button>
                </div>
                
                <div class="qr-code-container">
                    <div class="qr-code-info">📲 Code QR pour partager</div>
                    <img :src="state.generationResult.qrCode" alt="QR Code" class="qr-code-image">
                </div>
            </div>
        </div>
    `,
    data() {
        return { demandeId: '' };
    },
    methods: {
        generate() {
            if (!this.demandeId.trim()) {
                this.$emit('clear', { type: 'error', text: 'Veuillez entrer un numéro de demande' });
                return;
            }
            this.$emit('generate', this.demandeId);
        }
    }
});

// ⭐ Composant : Résultat de Recherche
const SearchResultCardComponent = defineComponent({
    name: 'SearchResultCard',
    props: {
        result: { type: Object, required: true }
    },
    emits: ['copy'],
    template: `
        <div class="demande-card">
            <div class="demande-header">
                <div class="demande-id">{{ result.demandeId }}</div>
                <div :class="['status-badge', getStatusClass(result.statutActuel)]">
                    {{ result.statutActuel }}
                </div>
            </div>

            <div class="demande-info">
                <div class="info-row">
                    <span class="info-label">👤 Demandeur</span>
                    <span class="info-value">
                        {{ result.demandeur?.prenom }} {{ result.demandeur?.nom }}
                    </span>
                </div>
                <div class="info-row">
                    <span class="info-label">🛂 Type de Visa</span>
                    <span class="info-value">{{ result.typeVisa }}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">📂 Catégorie</span>
                    <span class="info-value">{{ result.categorie }}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">📅 Date de Création</span>
                    <span class="info-value">{{ formatDate(result.dateCreation) }}</span>
                </div>
            </div>

            <!-- QR Code -->
            <div v-if="result.qrCodeBase64" class="qr-code-container">
                <div class="qr-code-info">📲 Code QR pour suivi rapide</div>
                <img :src="'data:image/png;base64,' + result.qrCodeBase64" :alt="'QR Code - ' + result.demandeId" class="qr-code-image">
                <div class="qr-code-info">Token: {{ result.trackingToken }}</div>
            </div>

            <!-- Timeline -->
            <div v-if="result.historique && result.historique.length > 0" class="timeline">
                <div class="timeline-title">📋 Historique des Statuts</div>
                <div 
                    v-for="(item, index) in result.historique"
                    :key="index"
                    class="timeline-item"
                >
                    <div class="timeline-content">
                        <div class="timeline-date">{{ formatDate(item.date) }}</div>
                        <div class="timeline-status">{{ item.statut }}</div>
                    </div>
                </div>
            </div>
        </div>
    `,
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
            if (upper.includes('APPROUVÉ') || upper.includes('CRÉÉE')) return 'completed';
            if (upper.includes('REJETÉ')) return 'rejected';
            if (upper.includes('SCAN') || upper.includes('EN_COURS')) return 'in-progress';
            return 'created';
        }
    }
});

// ⭐ Application Principale
const trackingComponentApp = {
    components: {
        SearchPassportComponent,
        SearchTokenComponent,
        GenerateQRComponent,
        'search-result-card': SearchResultCardComponent
    },
    data() {
        return {
            activeTab: 'passport',
            loading: false,
            message: { text: '', type: '' },
            results: [],
            resultsTitle: 'Résultats',
            searchPerformed: false,
            generationResult: { success: false, token: '', url: '', qrCode: '' },
            tabs: [
                { id: 'passport', label: '🛂 Par Numéro de Passeport' },
                { id: 'token', label: '🔗 Par Lien / Code' },
                { id: 'generate', label: '🎯 Générer un Lien' }
            ],
            apiBaseUrl: '/visa-backoffice/api/public/tracking'
        };
    },
    computed: {
        currentTabComponent() {
            const components = {
                passport: 'SearchPassportComponent',
                token: 'SearchTokenComponent',
                generate: 'GenerateQRComponent'
            };
            return components[this.activeTab];
        },
        currentTabState() {
            return {
                loading: this.loading,
                generationResult: this.generationResult
            };
        }
    },
    methods: {
        selectTab(tabId) {
            this.activeTab = tabId;
            this.clearMessages();
            this.searchPerformed = false;
            this.results = [];
            this.generationResult = { success: false, token: '', url: '', qrCode: '' };
        },
        async handleSearch(searchData) {
            if (searchData.type === 'passport') {
                await this.searchByPassport(searchData.value);
            } else if (searchData.type === 'token') {
                await this.searchByToken(searchData.value);
            }
        },
        async searchByPassport(passportNumber) {
            const url = `${this.apiBaseUrl}/passport/${encodeURIComponent(passportNumber)}`;
            await this.performSearch(url, 'Vos demandes');
        },
        async searchByToken(token) {
            let extractedToken = token;
            
            if (token.includes('?token=')) {
                const match = token.match(/[?&]token=([^&]*)/);
                if (match && match[1]) {
                    extractedToken = match[1];
                } else {
                    this.showMessage('URL invalide : paramètre "token" non trouvé', 'error');
                    return;
                }
            } else if (token.includes('?')) {
                this.showMessage('URL invalide : paramètre "token" attendu', 'error');
                return;
            }

            const url = `${this.apiBaseUrl}/token/${encodeURIComponent(extractedToken)}`;
            await this.performSearchByToken(url, 'Détails de votre demande');
        },
        async performSearch(url, title) {
            this.loading = true;
            this.results = [];
            this.searchPerformed = true;

            try {
                const response = await fetch(url);
                const data = await response.json();

                if (response.ok && data.demandes && Array.isArray(data.demandes)) {
                    if (data.demandes.length > 0) {
                        this.results = data.demandes;
                        this.resultsTitle = title;
                        this.showMessage('Demandes trouvées avec succès', 'success');
                    } else {
                        this.showMessage('Aucune demande trouvée', 'info');
                    }
                } else {
                    this.showMessage(data.error || 'Aucune demande trouvée', 'error');
                }
            } catch (error) {
                console.error('Erreur:', error);
                this.showMessage('Erreur lors de la recherche. Veuillez réessayer.', 'error');
            } finally {
                this.loading = false;
            }
        },
        async performSearchByToken(url, title) {
            this.loading = true;
            this.results = [];
            this.searchPerformed = true;

            try {
                const response = await fetch(url);
                const data = await response.json();

                if (response.ok && data.demandeId) {
                    this.results = [data];
                    this.resultsTitle = title;
                    this.showMessage('Demande trouvée avec succès', 'success');
                } else if (response.ok && (data.error === 'Demande non trouvée' || data.error)) {
                    this.showMessage(data.error || 'Aucune demande trouvée pour ce code de suivi', 'error');
                } else {
                    this.showMessage('Aucune demande trouvée pour ce code de suivi', 'error');
                }
            } catch (error) {
                console.error('Erreur:', error);
                this.showMessage('Erreur lors de la recherche. Vérifiez votre code de suivi.', 'error');
            } finally {
                this.loading = false;
            }
        },
        async handleGenerate(demandeId) {
            await this.generateTrackingLink(demandeId);
        },
        async generateTrackingLink(demandeId) {
            this.loading = true;
            this.generationResult = { success: false, token: '', url: '', qrCode: '' };

            try {
                // Construire l'URL de suivi complète avec le paramètre token
                const baseUrl = window.location.origin + '/visa-backoffice/tracking-composants.html?token=';
                const apiUrl = '/visa-backoffice/api/demandes/' + encodeURIComponent(demandeId)
                    + '/generate-qr?mode=url&baseUrl=' + encodeURIComponent(baseUrl);

                const response = await fetch(apiUrl, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    // Construire l'URL de suivi complète si elle n'est pas fournie par l'API
                    const fullTrackingUrl = data.trackingUrl || (baseUrl + data.trackingToken);
                    
                    this.generationResult = {
                        success: true,
                        token: data.trackingToken,
                        url: fullTrackingUrl,
                        qrCode: data.qrCode
                    };
                    this.showMessage('QR code généré avec succès !', 'success');
                } else {
                    this.showMessage(data.error || 'Erreur lors de la génération du lien', 'error');
                }
            } catch (error) {
                console.error('Erreur lors de la génération QR:', error);
                this.showMessage('Erreur lors de la génération. Vérifiez le numéro de demande.', 'error');
            } finally {
                this.loading = false;
            }
        },
        copyToClipboard(field) {
            let text = '';
            if (field === 'token') {
                text = this.generationResult.token;
            } else if (field === 'url') {
                text = this.generationResult.url;
            }

            if (text) {
                navigator.clipboard.writeText(text).then(() => {
                    this.showMessage('✅ Copié dans le presse-papiers !', 'success');
                }).catch(err => {
                    console.error('Erreur:', err);
                });
            }
        },
        showMessage(text, type = 'info') {
            this.message = { text, type };
            if (type === 'success' || type === 'error') {
                setTimeout(() => {
                    this.message = { text: '', type: '' };
                }, type === 'success' ? 3000 : 5000);
            }
        },
        clearMessages() {
            this.message = { text: '', type: '' };
        }
    }
};

// Créer et monter l'application Vue
createApp(trackingComponentApp).mount('#app');
