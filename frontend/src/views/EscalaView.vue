<script setup lang="ts">
import { ref } from 'vue'
import { getEscala } from '@/api/escala'
import type { EscalaResponse } from '@/api/escala'

const today = new Date().toISOString().slice(0, 10)
const selectedDate = ref(today)
const escala = ref<EscalaResponse[]>([])
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

interface DiaEntry {
  professional: EscalaResponse
  plantaoId: number
  turno: string
}

function porDia(): [string, DiaEntry[]][] {
  const dias: Record<string, DiaEntry[]> = {}
  for (const professional of escala.value) {
    for (const plantao of professional.plantoes) {
      if (!dias[plantao.data]) dias[plantao.data] = []
      dias[plantao.data].push({ professional, plantaoId: plantao.id, turno: plantao.turno })
    }
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
      <div v-for="[data, entradas] in porDia()" :key="data" class="day-block">
        <h3>{{ data }}</h3>
        <table>
          <thead>
            <tr><th>Turno</th><th>Profissional</th><th>Categoria</th><th>Registro</th></tr>
          </thead>
          <tbody>
            <tr v-for="e in entradas" :key="e.plantaoId">
              <td>{{ turnoLabel[e.turno] }}</td>
              <td>{{ e.professional.professionalName }}</td>
              <td>{{ e.professional.professionalCategory }}</td>
              <td>{{ e.professional.professionalRegistrationNumber }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <p v-else-if="!loading && error === null && selectedDate !== today">
      Nenhum plantão encontrado para este período.
    </p>
  </div>
</template>
