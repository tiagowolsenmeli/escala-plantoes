<template>
  <footer class="app-footer">
    <div class="footer-top-bar"></div>
    <div class="footer-body">
      <div class="footer-info">
        <address class="footer-address">
          <span>Av. João Cesar de Oliveira, 5757 — Ed. Contemporâneo, Sala 907</span>
          <span>Beatriz, Contagem/MG</span>
        </address>
        <div class="footer-contact">
          <a href="tel:+553133992500">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
              <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12a19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 3.6 1.27h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 8.91a16 16 0 0 0 6.08 6.08l.98-.98a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 21.92 16.92z" />
            </svg>
            (31) 3399-2500
          </a>
          <span class="footer-hours">Seg–Sex: 7h30–12h / 13h30–17h</span>
        </div>
      </div>

      <div class="footer-social">
        <a href="https://www.linkedin.com/company/spdata" target="_blank" rel="noopener noreferrer" aria-label="LinkedIn">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
            <path d="M16 8a6 6 0 0 1 6 6v7h-4v-7a2 2 0 0 0-2-2 2 2 0 0 0-2 2v7h-4v-7a6 6 0 0 1 6-6z"/>
            <rect x="2" y="9" width="4" height="12"/>
            <circle cx="4" cy="4" r="2"/>
          </svg>
        </a>
        <a href="https://www.facebook.com/spdata" target="_blank" rel="noopener noreferrer" aria-label="Facebook">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
            <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"/>
          </svg>
        </a>
        <a href="https://www.instagram.com/spdata" target="_blank" rel="noopener noreferrer" aria-label="Instagram">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <rect x="2" y="2" width="20" height="20" rx="5" ry="5"/>
            <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"/>
            <line x1="17.5" y1="6.5" x2="17.51" y2="6.5"/>
          </svg>
        </a>
        <a href="https://www.youtube.com/@spdata" target="_blank" rel="noopener noreferrer" aria-label="YouTube">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
            <path d="M22.54 6.42a2.78 2.78 0 0 0-1.95-1.96C18.88 4 12 4 12 4s-6.88 0-8.59.46a2.78 2.78 0 0 0-1.95 1.96A29 29 0 0 0 1 12a29 29 0 0 0 .46 5.58A2.78 2.78 0 0 0 3.41 19.6C5.12 20.06 12 20.06 12 20.06s6.88 0 8.59-.46a2.78 2.78 0 0 0 1.95-1.95A29 29 0 0 0 23 12a29 29 0 0 0-.46-5.58z"/>
            <polygon points="9.75 15.02 15.5 12 9.75 8.98 9.75 15.02" fill="white"/>
          </svg>
        </a>
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
  margin-top: 3rem;
  font-size: 0.78rem;
  color: var(--color-text-muted);
}

.footer-top-bar {
  height: 3px;
  background: linear-gradient(90deg, var(--color-primary) 0%, var(--color-accent) 100%);
  border-radius: 2px 2px 0 0;
  margin-bottom: 1rem;
}

.footer-body {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 1rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid var(--color-border);
}

.footer-info {
  display: flex;
  flex-wrap: wrap;
  gap: 0.25rem 2.5rem;
}

.footer-address {
  font-style: normal;
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
  line-height: 1.6;
}

.footer-contact {
  display: flex;
  flex-direction: column;
  gap: 0.15rem;
  line-height: 1.6;
}

.footer-contact a {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 700;
}

.footer-contact a:hover {
  text-decoration: underline;
}

.footer-hours {
  color: var(--color-text-light);
}

.footer-social {
  display: flex;
  align-items: center;
  gap: 0.625rem;
}

.footer-social a {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: var(--color-primary-light);
  color: var(--color-primary);
  transition: background 0.15s, color 0.15s;
  text-decoration: none;
}

.footer-social a:hover {
  background: var(--color-primary);
  color: #fff;
}

.footer-copy {
  margin-top: 0.6rem;
  text-align: center;
  color: var(--color-text-light);
  font-size: 0.75rem;
}
</style>
