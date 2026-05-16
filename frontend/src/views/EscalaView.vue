<script setup lang="ts">
import { ref } from 'vue'
import { getEscala } from '@/api/escala'
import type { PlantaoResponse } from '@/api/plantoes'

const today = new Date().toISOString().slice(0, 10)
const selectedDate = ref(today)
const escala = ref<PlantaoResponse[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

const turnoLabel: Record<string, string> = { MANHA: 'Manhã', TARDE: 'Tarde', NOITE: 'Noite' }

async function buscar() {
  loading.value = true
  error.value = null
  try {
    escala.value = await getEscala(selectedDate.value)
  } catch (e: any) {
    error.value = e.response?.data?.message ?? 'Erro ao carregar escala'
  } finally {
    loading.value = false
  }
}

const dias = (data: string) => {
  const dias: Record<string, PlantaoResponse[]> = {}
  for (const p of data ? escala.value : []) {
    if (!dias[p.data]) dias[p.data] = []
    dias[p.data].push(p)
  }
  return Object.entries(dias).sort(([a], [b]) => a.localeCompare(b))
}
</script>

<template>
  <div class="page">
    <h1>Escala</h1>

    <section class="card">
      <form @submit.prevent="buscar" class="inline-form">
        <label>A partir de</label>
        <input type="date" v-model="selectedDate" required />
        <button type="submit">Buscar 7 dias</button>
      </form>
    </section>

    <p v-if="loading">Carregando...</p>
    <p v-else-if="error" class="error">{{ error }}</p>

    <section v-else-if="escala.length" class="card">
      <div v-for="[data, plantoes] in dias(selectedDate)" :key="data" class="day-block">
        <h3>{{ data }}</h3>
        <table>
          <thead>
            <tr><th>Turno</th><th>Profissional</th><th>Categoria</th><th>Registro</th></tr>
          </thead>
          <tbody>
            <tr v-for="p in plantoes" :key="p.id">
              <td>{{ turnoLabel[p.turno] }}</td>
              <td>{{ p.professionalName }}</td>
              <td>{{ p.professionalCategory }}</td>
              <td>{{ p.professionalRegistrationNumber }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <p v-else-if="escala.length === 0 && !loading && error === null && selectedDate !== today">
      Nenhum plantão encontrado para este período.
    </p>
  </div>
</template>
