import http from './http'
import type { PlantaoResponse } from './plantoes'

export const getEscala = (data: string) =>
  http.get<PlantaoResponse[]>('/escala', { params: { data } }).then(r => r.data)
