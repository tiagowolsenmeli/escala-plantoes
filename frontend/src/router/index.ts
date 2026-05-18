import { createRouter, createWebHistory } from 'vue-router'
import ProfessionalsView from '../views/ProfessionalsView.vue'

const APP_TITLE = 'SPDATA – Escala de Plantões'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/professionals' },
    {
      path: '/professionals',
      name: 'professionals',
      component: ProfessionalsView,
      meta: { title: `Profissionais | ${APP_TITLE}` },
    },
    {
      path: '/plantoes',
      name: 'plantoes',
      component: () => import('../views/PlantaoView.vue'),
      meta: { title: `Plantões | ${APP_TITLE}` },
    },
    {
      path: '/escala',
      name: 'escala',
      component: () => import('../views/EscalaView.vue'),
      meta: { title: `Escala Semanal | ${APP_TITLE}` },
    },
  ],
})

router.afterEach((to) => {
  document.title = (to.meta.title as string) ?? APP_TITLE
})

export default router
