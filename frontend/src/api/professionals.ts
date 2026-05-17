import http from './http'

export interface ProfessionalRegistrationResponse {
  category: string
  state: string
  type: string
  registrationNumber: string
}

export interface ProfessionalResponse {
  id: number
  name: string
  workSchedule: number
  registration: ProfessionalRegistrationResponse
}

export interface ProfessionalRequest {
  name: string
  workSchedule: number
  registration: {
    category: string
    state: string
    type: string
    registrationNumber: string
  }
}

export const listProfessionals = () =>
  http.get<ProfessionalResponse[]>('/api/professionals').then(r => r.data)

export const listByCategory = (category: string) =>
  http.get<ProfessionalResponse[]>('/api/professionals/category', { params: { category } }).then(r => r.data)

export const registerProfessional = (data: ProfessionalRequest) =>
  http.post<number>('/api/professionals', data).then(r => r.data)
