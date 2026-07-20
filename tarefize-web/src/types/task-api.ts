import type { Priority } from '@/types/task'

export interface TaskApiResponse {
  id?: number
  taskId?: number
  idTask?: number
  taskName: string
  taskDescription: string
  priority: Priority
  createDate: string
  completed?: boolean
  dueDate?: string
}

export interface PageTaskApiResponse {
  content?: TaskApiResponse[]
  totalPages?: number
  totalElements?: number
  number?: number
  size?: number
}
