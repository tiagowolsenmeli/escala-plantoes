<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue'
import { useProfessionalsStore } from '@/stores/professionals'

const store = useProfessionalsStore()

const form = reactive({
  name: '',
  workSchedule: 40,
  registration: { category: '', state: '', type: 'CRM', registrationNumber: '' },
})
const success = ref(false)
const formError = ref<string | null>(null)
const registrationNumberError = ref(false)

function handleRegistrationInput(event: Event) {
  const raw = (event.target as HTMLInputElement).value
  if (/\D/.test(raw)) {
    registrationNumberError.value = true
  }
  form.registration.registrationNumber = raw.replace(/\D/g, '')
}

watch(() => form.registration.category, (category) => {
  form.registration.type = category === 'MÉDICO' ? 'CRM' : 'COREN'
})

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

function categoryClass(category: string): string {
  const map: Record<string, string> = {
    'MÉDICO': 'badge-medico',
    'ENFERMEIRO': 'badge-enfermeiro',
    'TÉCNICO': 'badge-tecnico',
  }
  return `badge ${map[category] ?? 'badge-medico'}`
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
          <select v-model="form.registration.category" required>
            <option value="" disabled>Selecione...</option>
            <option value="MÉDICO">MÉDICO</option>
            <option value="ENFERMEIRO">ENFERMEIRO</option>
            <option value="TÉCNICO">TÉCNICO</option>
          </select>
        </div>
        <div class="field-row">
          <div class="field">
            <label>Estado (UF)</label>
            <select v-model="form.registration.state" required>
              <option value="" disabled>Selecione...</option>
              <option value="AC">AC</option>
              <option value="AL">AL</option>
              <option value="AP">AP</option>
              <option value="AM">AM</option>
              <option value="BA">BA</option>
              <option value="CE">CE</option>
              <option value="DF">DF</option>
              <option value="ES">ES</option>
              <option value="GO">GO</option>
              <option value="MA">MA</option>
              <option value="MT">MT</option>
              <option value="MS">MS</option>
              <option value="MG">MG</option>
              <option value="PA">PA</option>
              <option value="PB">PB</option>
              <option value="PR">PR</option>
              <option value="PE">PE</option>
              <option value="PI">PI</option>
              <option value="RJ">RJ</option>
              <option value="RN">RN</option>
              <option value="RS">RS</option>
              <option value="RO">RO</option>
              <option value="RR">RR</option>
              <option value="SC">SC</option>
              <option value="SP">SP</option>
              <option value="SE">SE</option>
              <option value="TO">TO</option>
            </select>
          </div>
          <div class="field" v-if="form.registration.category">
            <label>Tipo de registro</label>
            <span class="info-value">{{ form.registration.type }}</span>
          </div>
        </div>
        <div class="field">
          <label>Número de registro</label>
          <input
            v-model="form.registration.registrationNumber"
            inputmode="numeric"
            @input="handleRegistrationInput"
            required
          />
          <span v-if="registrationNumberError" class="field-error">Apenas números são permitidos.</span>
        </div>
        <p v-if="formError" class="error">{{ formError }}</p>
        <p v-if="success" class="success">Profissional cadastrado com sucesso.</p>
        <button type="submit">Cadastrar</button>
      </form>
    </section>

    <section class="card">
      <h2>Lista</h2>
      <div v-if="store.loading" class="loading-state">
        <span class="spinner"></span>
        Carregando...
      </div>
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
            <td><span :class="categoryClass(p.registration.category)">{{ p.registration.category }}</span></td>
            <td>{{ p.registration.type }} {{ p.registration.registrationNumber }}</td>
            <td>{{ p.registration.state }}</td>
            <td>{{ p.workSchedule }}h</td>
          </tr>
        </tbody>
      </table>
      <p v-else class="empty-state">Nenhum profissional cadastrado ainda.</p>
    </section>
  </div>
</template>
