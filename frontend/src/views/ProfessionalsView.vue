<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useProfessionalsStore } from '@/stores/professionals'

const store = useProfessionalsStore()

const form = reactive({
  name: '',
  workSchedule: 40,
  registration: { category: '', state: '', type: 'CRM', registrationNumber: '' },
})
const success = ref(false)
const formError = ref<string | null>(null)

onMounted(() => store.fetch())

async function submit() {
  formError.value = null
  success.value = false
  try {
    await store.register({ ...form, registration: { ...form.registration } })
    success.value = true
    Object.assign(form, {
      name: '',
      workSchedule: 40,
      registration: { category: '', state: '', type: 'CRM', registrationNumber: '' },
    })
  } catch (e: any) {
    formError.value = e.response?.data?.message ?? 'Erro ao cadastrar profissional'
  }
}
</script>

<template>
  <div class="page">
    <h1>Profissionais</h1>

    <section class="card">
      <h2>Cadastrar</h2>
      <form @submit.prevent="submit">
        <div class="field">
          <label>Nome</label>
          <input v-model="form.name" required />
        </div>
        <div class="field">
          <label>Carga horária</label>
          <select v-model.number="form.workSchedule">
            <option :value="20">20h</option>
            <option :value="30">30h</option>
            <option :value="40">40h</option>
          </select>
        </div>
        <div class="field">
          <label>Categoria</label>
          <input v-model="form.registration.category" required />
        </div>
        <div class="field">
          <label>Estado (UF)</label>
          <input v-model="form.registration.state" maxlength="2" required />
        </div>
        <div class="field">
          <label>Tipo de registro</label>
          <select v-model="form.registration.type">
            <option value="CRM">CRM</option>
            <option value="COREN">COREN</option>
          </select>
        </div>
        <div class="field">
          <label>Número de registro</label>
          <input v-model="form.registration.registrationNumber" required />
        </div>
        <p v-if="formError" class="error">{{ formError }}</p>
        <p v-if="success" class="success">Profissional cadastrado com sucesso.</p>
        <button type="submit">Cadastrar</button>
      </form>
    </section>

    <section class="card">
      <h2>Lista</h2>
      <p v-if="store.loading">Carregando...</p>
      <p v-else-if="store.error" class="error">{{ store.error }}</p>
      <table v-else-if="store.professionals.length">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Categoria</th>
            <th>Registro</th>
            <th>UF</th>
            <th>Carga</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in store.professionals" :key="p.id">
            <td>{{ p.name }}</td>
            <td>{{ p.registration.category }}</td>
            <td>{{ p.registration.type }} {{ p.registration.registrationNumber }}</td>
            <td>{{ p.registration.state }}</td>
            <td>{{ p.workSchedule }}h</td>
          </tr>
        </tbody>
      </table>
      <p v-else>Nenhum profissional cadastrado.</p>
    </section>
  </div>
</template>
