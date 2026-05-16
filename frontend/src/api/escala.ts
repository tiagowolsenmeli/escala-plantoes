import http from './http'

export interface PlantaoEscalaItem {
  id: number
  data: string
  turno: string
}

export interface EscalaResponse {
  professionalId: number
  professionalName: string
  professionalCategory: string
  professionalRegistrationNumber: string
  plantoes: PlantaoEscalaItem[]
}

export const getEscala = (data: string) =>
  http.get<EscalaResponse[]>('/escala', { params: { data } }).then(r => r.data)
