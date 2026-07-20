export type Priority = 1 | 2 | 3 | 4 | 5
export type StatusFilter = 'all' | 'pending' | 'done'

export interface Task {
  id: number
  taskName: string
  taskDescription: string
  createDate: string
  priority: Priority
  completed: boolean
  dueDate?: string
}

export interface TaskFormState {
  taskName: string
  taskDescription: string
  priority: Priority
  dueDate: string
}

export interface TaskRequest {
  taskName: string
  taskDescription: string
  priority: Priority
}

export const PRIORITY_META: Record<
  Priority,
  { label: string; badge: string; dot: string; bar: string }
> = {
  1: {
    label: 'Muito baixa',
    badge: 'bg-emerald-50 text-emerald-700 ring-emerald-600/20',
    dot: 'bg-emerald-500',
    bar: 'bg-emerald-500',
  },
  2: {
    label: 'Baixa',
    badge: 'bg-teal-50 text-teal-700 ring-teal-600/20',
    dot: 'bg-teal-500',
    bar: 'bg-teal-500',
  },
  3: {
    label: 'Média',
    badge: 'bg-amber-50 text-amber-700 ring-amber-600/20',
    dot: 'bg-amber-500',
    bar: 'bg-amber-500',
  },
  4: {
    label: 'Alta',
    badge: 'bg-orange-50 text-orange-700 ring-orange-600/20',
    dot: 'bg-orange-500',
    bar: 'bg-orange-500',
  },
  5: {
    label: 'Crítica',
    badge: 'bg-red-50 text-red-700 ring-red-600/20',
    dot: 'bg-red-500',
    bar: 'bg-red-500',
  },
}
