import { defineStore } from 'pinia'
import { ref } from 'vue'
import { registerPlantao, deletePlantao } from '@/api/plantoes'
import type { PlantaoResponse, PlantaoRequest } from '@/api/plantoes'

export const usePlantoesStore = defineStore('plantoes', () => {
  const plantoes = ref<PlantaoResponse[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function register(data: PlantaoRequest) {
    loading.value = true
    error.value = null
    try {
      const plantao = await registerPlantao(data)
      plantoes.value.push(plantao)
    } catch (e: any) {
      error.value = e.response?.data?.message ?? 'Erro ao registrar plantão'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function remove(id: number) {
    loading.value = true
    error.value = null
    try {
      await deletePlantao(id)
      plantoes.value = plantoes.value.filter(p => p.id !== id)
    } catch (e: any) {
      error.value = e.response?.data?.message ?? 'Erro ao remover plantão'
      throw e
    } finally {
      loading.value = false
    }
  }

  return { plantoes, loading, error, register, remove }
})
