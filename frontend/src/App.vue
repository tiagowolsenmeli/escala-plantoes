<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <nav class="navbar">
    <RouterLink to="/professionals">Profissionais</RouterLink>
    <RouterLink to="/plantoes">Plantões</RouterLink>
    <RouterLink to="/escala">Escala</RouterLink>
  </nav>
  <main>
    <RouterView />
  </main>
</template>

<style scoped>
.navbar {
  display: flex;
  gap: 1.5rem;
  padding: 1rem 2rem;
  background: #1a1a2e;
}

.navbar a {
  color: #ccc;
  text-decoration: none;
  font-weight: 500;
}

.navbar a.router-link-active {
  color: #fff;
  border-bottom: 2px solid #fff;
}

main {
  padding: 2rem;
}
</style>
