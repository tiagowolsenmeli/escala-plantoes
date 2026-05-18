<script setup lang="ts">
import { ref, computed } from 'vue'
import { getEscala } from '@/api/escala'
import type { EscalaResponse } from '@/api/escala'

const today = new Date().toISOString().slice(0, 10)
const selectedDate = ref(today)
const escala = ref<EscalaResponse[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

const turnoLabel: Record<string, string> = { MANHA: 'Manhã', TARDE: 'Tarde', NOITE: 'Noite' }
const diasSemana = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb']
const turnoOrdem = ['MANHA', 'TARDE', 'NOITE']

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

const dias = computed(() => {
  const base = new Date(selectedDate.value + 'T00:00:00')
  return Array.from({ length: 7 }, (_, i) => {
    const d = new Date(base)
    d.setDate(base.getDate() + i)
    return d.toISOString().slice(0, 10)
  })
})

function diaSemana(data: string): string {
  return diasSemana[new Date(data + 'T00:00:00').getDay()]!
}

function formatDia(data: string): string {
  const [, m, d] = data.split('-')
  return `${d}/${m}`
}

function isWeekend(data: string): boolean {
  const day = new Date(data + 'T00:00:00').getDay()
  return day === 0 || day === 6
}

function turnosDoDia(prof: EscalaResponse, data: string): string[] {
  return prof.plantoes
    .filter(p => p.data === data)
    .map(p => p.turno)
    .sort((a, b) => turnoOrdem.indexOf(a) - turnoOrdem.indexOf(b))
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

    <div v-if="loading" class="loading-state">
      <span class="spinner"></span>
      Carregando escala...
    </div>
    <p v-else-if="error" class="error">{{ error }}</p>

    <section v-else-if="escala.length" class="card escala-card">
      <div class="escala-grid-wrap">
        <table class="escala-grid">
          <thead>
            <tr>
              <th class="col-prof">Profissional</th>
              <th
                v-for="dia in dias"
                :key="dia"
                :class="['col-dia', { weekend: isWeekend(dia) }]"
              >
                <span :class="['dia-semana', { 'is-weekend': isWeekend(dia) }]">{{ diaSemana(dia) }}</span>
                <span class="dia-data">{{ formatDia(dia) }}</span>
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="prof in escala" :key="prof.professionalId">
              <td class="col-prof">
                <span class="prof-nome">{{ prof.professionalName }}</span>
                <span class="prof-info">{{ prof.professionalCategory }} · {{ prof.professionalRegistrationNumber }}</span>
              </td>
              <td
                v-for="dia in dias"
                :key="dia"
                :class="['col-turnos', { weekend: isWeekend(dia) }]"
              >
                <template v-if="turnosDoDia(prof, dia).length">
                  <span
                    v-for="turno in turnosDoDia(prof, dia)"
                    :key="turno"
                    :class="['turno-badge', `turno-${turno.toLowerCase()}`]"
                  >{{ turnoLabel[turno] }}</span>
                </template>
                <span v-else class="cell-empty">—</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <p v-else-if="!loading && error === null" class="empty-state">
      Nenhum plantão encontrado para este período.
    </p>
  </div>
</template>
