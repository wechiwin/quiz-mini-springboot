Vue.component('header-component', {
    template: `
    <div class="header">
      <a :href="basePath" class="header-text">{{ getText('index.title') }}</a>

      <div class="pc-buttons">
        <button @click="$emit('to-manage')" class="fix-button-left">{{ getText('index.manage') }}🔧</button>
        <div class="fix-button-right-lang">
        <button @click="$emit('toggle-dark')">☀️/🌙</button>
        <select  v-model="selectedLanguage" @change="changeLanguage">
          <option value="en_US">English</option>
          <option value="it_IT">Italiano</option>
          <option value="zh_CN">中文</option>
        </select>
        </div>
      </div>

      <button class="mobile-menu-button" @click="toggleSidebar">☰</button>

      <div v-if="isSidebarOpen" class="sidebar">
        <button class="close-button" @click="toggleSidebar">✖</button>
        <button @click="$emit('toggle-dark')">☀️/🌙</button>
        <button @click="$emit('to-manage')">{{ getText('index.manage') }}🔧</button>
        <select v-model="selectedLanguage" @change="changeLanguage">
          <option value="en_US">English</option>
          <option value="it_IT">Italiano</option>
          <option value="zh_CN">中文</option>
        </select>
      </div>

      <div v-if="isSidebarOpen" class="overlay" @click="toggleSidebar"></div>
    </div>
  `,
    props: {
        basePath: {
            type: String,
            default: ''
        }
    },
    data() {
        return {
            isSidebarOpen: false,
            selectedLanguage: this.getCookie('ClientLanguage') || 'zh_CN'
        };
    },
    methods: {
        toggleSidebar() {
            this.isSidebarOpen = !this.isSidebarOpen;
        },
        changeLanguage() {
            this.setCookie('ClientLanguage', this.selectedLanguage, 365);
            window.location.href = '/quiz/?lang=' + this.selectedLanguage;
        },
        getCookie(name) {
            let nameEQ = name + "=";
            let ca = document.cookie.split(';');
            for (let i = 0; i < ca.length; i++) {
                let c = ca[i];
                while (c.charAt(0) === ' ') c = c.substring(1, c.length);
                if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
            }
            return null;
        },
        setCookie(name, value, days) {
            let expires = "";
            if (days) {
                let date = new Date();
                date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
                expires = "; expires=" + date.toUTCString();
            }
            document.cookie = name + "=" + (value || "") + expires + "; path=/";
        },
        getText(key) {
            return window.ThymeleafMessages?.[key] || key;
        }
    }
});
