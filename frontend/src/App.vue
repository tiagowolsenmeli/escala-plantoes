<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import AppFooter from '@/components/AppFooter.vue'
</script>

<template>
  <aside class="sidebar">
    <div class="sidebar-brand">
      <div class="sidebar-logo">
        <a href="https://spdata.com.br/" target="_blank" rel="noopener noreferrer">
          <img src="@/assets/spdata.png" alt="SPDATA" />
        </a>
      </div>
      <span class="sidebar-product">Escala de Plantões</span>
    </div>
    <nav class="sidebar-nav">
      <RouterLink to="/professionals">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
          <circle cx="12" cy="7" r="4" />
        </svg>
        Profissionais
      </RouterLink>
      <RouterLink to="/plantoes">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
          <line x1="16" y1="2" x2="16" y2="6" />
          <line x1="8" y1="2" x2="8" y2="6" />
          <line x1="3" y1="10" x2="21" y2="10" />
        </svg>
        Plantões
      </RouterLink>
      <RouterLink to="/escala">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <rect x="3" y="3" width="7" height="7" />
          <rect x="14" y="3" width="7" height="7" />
          <rect x="14" y="14" width="7" height="7" />
          <rect x="3" y="14" width="7" height="7" />
        </svg>
        Escala
      </RouterLink>
    </nav>
  </aside>
  <main class="main-content">
    <RouterView />
    <AppFooter />
  </main>
</template>

<style scoped>
.sidebar {
  width: 240px;
  flex-shrink: 0;
  background: #c5d9f2;
  display: flex;
  flex-direction: column;
  padding: 1.5rem 0;
  box-shadow: 2px 0 16px rgba(29, 79, 145, 0.08);
  position: relative;
  z-index: 10;
}

.sidebar-brand {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 0 1rem 1.25rem;
  border-bottom: 1px solid rgba(29, 79, 145, 0.2);
  margin-bottom: 1.25rem;
}

.sidebar-logo {
  width: 100%;
  height: 46px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sidebar-logo a {
  display: flex;
  align-items: center;
  justify-content: center;
}

.sidebar-logo img {
  height: 70px;
  width: auto;
  display: block;
}

.sidebar-product {
  color: var(--color-primary-dark);
  font-size: 0.7rem;
  font-weight: 700;
  text-align: center;
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
  padding: 0.5rem;
  margin: 0 0.75rem;
  border: 1.5px solid rgba(29, 79, 145, 0.3);
  border-radius: var(--radius-md);
}

.sidebar-nav a {
  display: flex;
  align-items: center;
  gap: 0.625rem;
  padding: 0.625rem 0.75rem;
  color: var(--color-primary);
  text-decoration: none;
  border-radius: var(--radius-sm);
  font-size: 0.9rem;
  font-weight: 600;
  transition:
    background 0.15s,
    color 0.15s;
}

.sidebar-nav a:hover {
  background: rgba(29, 79, 145, 0.1);
  color: var(--color-primary-dark);
}

.sidebar-nav a.router-link-active {
  background: rgba(29, 79, 145, 0.14);
  color: var(--color-primary-dark);
  font-weight: 700;
  border-left: 3px solid var(--color-primary);
  padding-left: calc(0.75rem - 3px);
}

</style>
