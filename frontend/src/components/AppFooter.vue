<template>
  <footer class="app-footer">
    <div class="footer-info">
      <address class="footer-address">
        <span>Av. João Cesar de Oliveira, 5757 — Ed. Contemporâneo, Sala 907</span>
        <span>Beatriz, Contagem/MG</span>
      </address>
      <div class="footer-contact">
        <a href="tel:+553133992500">(31) 3399-2500</a>
        <span class="footer-hours">Seg–Sex: 7h30–12h / 13h30–17h</span>
      </div>
    </div>
    <p class="footer-copy">© {{ year }} SPDATA – Todos os direitos reservados.</p>
  </footer>
</template>

<script setup lang="ts">
const year = new Date().getFullYear()
</script>

<style scoped>
.app-footer {
  border-top: 1px solid var(--color-border);
  padding: 0.875rem 0 0.25rem;
  margin-top: 3rem;
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  justify-content: space-between;
  gap: 0.5rem 2rem;
  font-size: 0.78rem;
  color: var(--color-text-muted);
}

.footer-info {
  display: flex;
  flex-wrap: wrap;
  gap: 0.25rem 2rem;
}

.footer-address {
  font-style: normal;
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
  line-height: 1.5;
}

.footer-contact {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
  line-height: 1.5;
}

.footer-contact a {
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 600;
}

.footer-contact a:hover {
  text-decoration: underline;
}

.footer-hours {
  color: var(--color-text-light);
}

.footer-copy {
  align-self: flex-end;
  white-space: nowrap;
}
</style>
