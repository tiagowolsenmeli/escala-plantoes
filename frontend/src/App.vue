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
    <div class="sidebar-contact">
      <a href="tel:+553133992500" class="sidebar-phone">
        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
          <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12a19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 3.6 1.27h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 8.91a16 16 0 0 0 6.08 6.08l.98-.98a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 21.92 16.92z" />
        </svg>
        (31) 3399-2500
      </a>
      <span class="sidebar-hours">Seg–Sex: 7h30–12h / 13h30–17h</span>
    </div>
  </aside>
  <main class="main-content">
    <RouterView />
    <AppFooter />
  </main>
</template>

<style scoped>
.sidebar {
  width: 220px;
  flex-shrink: 0;
  background: #c5d9f2;
  display: flex;
  flex-direction: column;
  padding: 1.5rem 0;
}

.sidebar-brand {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 0 1rem 1.25rem;
  border-bottom: 1px solid var(--color-border);
  margin-bottom: 1rem;
}

.sidebar-logo {
  width: 100%;
  height: 46px;
  overflow: hidden;
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
  color: var(--color-primary);
  font-size: 0.72rem;
  font-weight: 500;
  text-align: center;
  letter-spacing: 0.02em;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 0.5rem;
  margin: 0 0.75rem;
  border: 1.5px solid var(--color-primary-dark);
  border-radius: 8px;
}

.sidebar-nav a {
  display: flex;
  align-items: center;
  gap: 0.625rem;
  padding: 0.625rem 0.75rem;
  color: var(--color-primary);
  text-decoration: none;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 500;
  transition:
    background 0.15s,
    color 0.15s;
}

.sidebar-nav a:hover {
  background: rgba(26, 92, 181, 0.12);
  color: var(--color-primary-dark);
}

.sidebar-nav a.router-link-active {
  background: rgba(26, 92, 181, 0.15);
  color: var(--color-primary-dark);
  font-weight: 600;
  border-left: 3px solid var(--color-primary);
  padding-left: calc(0.75rem - 3px);
}

.sidebar-contact {
  margin-top: auto;
  padding: 1rem 1rem 0;
  border-top: 1px solid var(--color-border);
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.sidebar-phone {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.8rem;
  font-weight: 600;
  color: var(--color-primary);
  text-decoration: none;
}

.sidebar-phone:hover {
  text-decoration: underline;
}

.sidebar-hours {
  font-size: 0.72rem;
  color: var(--color-text-muted);
  line-height: 1.4;
}
</style>
