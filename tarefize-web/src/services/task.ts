import type { Priority, Task, TaskRequest } from '@/types/task'
import type { PaginatedApiResponse } from '@/types/api'
import type { TaskApiResponse } from '@/types/task-api'
import { apiAuthFetch } from '@/services/api'

const TASKS_URL = '/tasks'
const DEFAULT_PAGE_SIZE = 6

export interface PaginatedTasks {
  tasks: Task[]
  totalPages: number
  totalElements: number
  currentPage: number
  pageSize: number
}

function mapTask(apiTask: TaskApiResponse): Task {
  const taskId = apiTask.id ?? apiTask.taskId ?? apiTask.idTask

  return {
    id: taskId ?? 0,
    taskName: apiTask.taskName,
    taskDescription: apiTask.taskDescription,
    createDate: apiTask.createDate,
    priority: apiTask.priority as Priority,
    completed: apiTask.completed ?? false,
    dueDate: apiTask.dueDate,
  }
}

function normalizePaginatedResponse(payload: PaginatedApiResponse<TaskApiResponse> | TaskApiResponse[], page: number, size: number): PaginatedTasks {
  const items = Array.isArray(payload) ? payload : payload.content ?? []
  const tasks = items.map(mapTask)

  return {
    tasks,
    totalPages: Array.isArray(payload)
      ? Math.max(Math.ceil(tasks.length / size), 1)
      : payload.totalPages ?? Math.max(Math.ceil(tasks.length / size), 1),
    totalElements: Array.isArray(payload)
      ? tasks.length
      : payload.totalElements ?? tasks.length,
    currentPage: Array.isArray(payload) ? page : payload.number ?? page,
    pageSize: Array.isArray(payload) ? size : payload.size ?? size,
  }
}

export const taskService = {
  async loadTasks(page = 0, size = DEFAULT_PAGE_SIZE): Promise<PaginatedTasks> {
    try {
      const payload = await apiAuthFetch<PaginatedApiResponse<TaskApiResponse>>(
        `${TASKS_URL}?page=${page}&size=${size}&sort=id,desc`,
        { method: 'GET' },
        'Falha ao carregar tarefas.',
      )

      if (payload?.content) {
        return normalizePaginatedResponse(payload, page, size)
      }
    } catch {
      // fallback
    }

    const fallbackPayload = await apiAuthFetch<PaginatedApiResponse<TaskApiResponse> | TaskApiResponse[]>(
      TASKS_URL,
      { method: 'GET' },
      'Falha ao carregar tarefas.',
    )

    return normalizePaginatedResponse(fallbackPayload, page, size)
  },

  async createTask(taskRequest: TaskRequest): Promise<Task> {
    const payload = await apiAuthFetch<TaskApiResponse>(TASKS_URL, {
      method: 'POST',
      body: JSON.stringify(taskRequest),
    }, 'Falha ao criar tarefa.')

    return mapTask(payload)
  },

  async completeTask(idTask: number): Promise<void> {
    await apiAuthFetch<void>(`${TASKS_URL}/${idTask}/complete`, {
      method: 'PATCH',
    }, 'Falha ao completar tarefa.')
  },

  async updateTask(idTask: number, taskPatch: Partial<TaskRequest>): Promise<Task> {
    const payload = await apiAuthFetch<TaskApiResponse>(`${TASKS_URL}/${idTask}`, {
      method: 'PATCH',
      body: JSON.stringify(taskPatch),
    }, 'Falha ao atualizar tarefa.')

    return mapTask(payload)
  },

  async deleteTask(idTask: number): Promise<void> {
    await apiAuthFetch<void>(`${TASKS_URL}/${idTask}`, {
      method: 'DELETE',
    }, 'Falha ao remover tarefa.')
  },
}
