<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <aside class="sidebar">
    <div class="sidebar-brand">
      <div class="sidebar-logo">
        <img src="@/assets/spdata.png" alt="SPDATA" />
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
  </main>
</template>

<style scoped>
.sidebar {
  width: 220px;
  flex-shrink: 0;
  background: var(--color-primary);
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
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  margin-bottom: 1rem;
}

.sidebar-logo {
  background: white;
  border-radius: 8px;
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
  color: rgba(255, 255, 255, 0.65);
  font-size: 0.72rem;
  font-weight: 500;
  text-align: center;
  letter-spacing: 0.02em;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 0 0.75rem;
}

.sidebar-nav a {
  display: flex;
  align-items: center;
  gap: 0.625rem;
  padding: 0.625rem 0.75rem;
  color: rgba(255, 255, 255, 0.65);
  text-decoration: none;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 500;
  transition:
    background 0.15s,
    color 0.15s;
}

.sidebar-nav a:hover {
  background: rgba(255, 255, 255, 0.08);
  color: white;
}

.sidebar-nav a.router-link-active {
  background: rgba(255, 255, 255, 0.15);
  color: white;
}
</style>
