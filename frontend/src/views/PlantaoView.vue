<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { usePlantoesStore } from '@/stores/plantoes'
import { useProfessionalsStore } from '@/stores/professionals'
import type { Turno } from '@/api/plantoes'

const plantoesStore = usePlantoesStore()
const professionalsStore = useProfessionalsStore()

const today = new Date().toISOString().slice(0, 10)
const form = reactive({ professionalId: 0, data: today, turno: 'MANHA' as Turno })
const success = ref(false)

onMounted(() => professionalsStore.fetch())

async function submit() {
  success.value = false
  try {
    await plantoesStore.register({ ...form })
    success.value = true
  } catch {
    // erro já está em plantoesStore.error
  }
}
</script>

<template>
  <div class="page">
    <h1>Plantões</h1>

    <section class="card">
      <h2>Registrar plantão</h2>
      <form @submit.prevent="submit">
        <div class="field">
          <label>Profissional</label>
          <select v-model.number="form.professionalId" required>
            <option :value="0" disabled>Selecione...</option>
            <option v-for="p in professionalsStore.professionals" :key="p.id" :value="p.id">
              {{ p.name }} — {{ p.registration.category }}
            </option>
          </select>
        </div>
        <div class="field">
          <label>Data</label>
          <input type="date" v-model="form.data" :min="today" required />
        </div>
        <div class="field">
          <label>Turno</label>
          <select v-model="form.turno">
            <option value="MANHA">Manhã</option>
            <option value="TARDE">Tarde</option>
            <option value="NOITE">Noite</option>
          </select>
        </div>
        <p v-if="plantoesStore.error" class="error">{{ plantoesStore.error }}</p>
        <p v-if="success" class="success">Plantão registrado com sucesso.</p>
        <button type="submit" :disabled="plantoesStore.loading">Registrar</button>
      </form>
    </section>

    <section class="card">
      <h2>Plantões registrados nesta sessão</h2>
      <table v-if="plantoesStore.plantoes.length">
        <thead>
          <tr>
            <th>Profissional</th>
            <th>Data</th>
            <th>Turno</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in plantoesStore.plantoes" :key="p.id">
            <td>{{ p.professionalName }}</td>
            <td>{{ p.data }}</td>
            <td>{{ p.turno }}</td>
            <td><button class="btn-delete" @click="plantoesStore.remove(p.id)">Remover</button></td>
          </tr>
        </tbody>
      </table>
      <p v-else>Nenhum plantão registrado nesta sessão.</p>
    </section>
  </div>
</template>
