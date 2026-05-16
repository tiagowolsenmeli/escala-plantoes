import { defineStore } from 'pinia'
import { ref } from 'vue'
import { listProfessionals, registerProfessional } from '@/api/professionals'
import type { ProfessionalResponse, ProfessionalRequest } from '@/api/professionals'

export const useProfessionalsStore = defineStore('professionals', () => {
  const professionals = ref<ProfessionalResponse[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetch() {
    loading.value = true
    error.value = null
    try {
      professionals.value = await listProfessionals()
    } catch (e: any) {
      error.value = e.response?.data?.message ?? 'Erro ao carregar profissionais'
    } finally {
      loading.value = false
    }
  }

  async function register(data: ProfessionalRequest) {
    await registerProfessional(data)
    await fetch()
  }

  return { professionals, loading, error, fetch, register }
})
