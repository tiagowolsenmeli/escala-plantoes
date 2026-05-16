import http from './http'

export type Turno = 'MANHA' | 'TARDE' | 'NOITE'

export interface PlantaoRequest {
  professionalId: number
  data: string
  turno: Turno
}

export interface PlantaoResponse {
  id: number
  professionalId: number
  professionalName: string
  professionalCategory: string
  professionalRegistrationNumber: string
  data: string
  turno: Turno
}

export const registerPlantao = (data: PlantaoRequest) =>
  http.post<PlantaoResponse>('/plantoes', data).then(r => r.data)

export const deletePlantao = (id: number) =>
  http.delete(`/plantoes/${id}`)
