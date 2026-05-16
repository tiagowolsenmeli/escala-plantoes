import { createRouter, createWebHistory } from 'vue-router'
import ProfessionalsView from '../views/ProfessionalsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/professionals' },
    { path: '/professionals', name: 'professionals', component: ProfessionalsView },
    { path: '/plantoes', name: 'plantoes', component: () => import('../views/PlantaoView.vue') },
    { path: '/escala', name: 'escala', component: () => import('../views/EscalaView.vue') },
  ],
})

export default router
