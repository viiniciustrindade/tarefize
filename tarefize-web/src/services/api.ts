import Cookies from 'js-cookie'
import type { ApiErrorPayload } from '@/types/api'

const API_BASE_URL = 'https://todolist-api-8hia.onrender.com/api'

function normalizeHeaders(initHeaders: HeadersInit = {}): Record<string, string> {
  const headers: Record<string, string> = {}

  if (initHeaders instanceof Headers) {
    initHeaders.forEach((value, key) => {
      headers[key] = value
    })
  } else if (Array.isArray(initHeaders)) {
    initHeaders.forEach(([key, value]) => {
      headers[key] = value
    })
  } else {
    Object.entries(initHeaders).forEach(([key, value]) => {
      if (typeof value === 'string') {
        headers[key] = value
      }
    })
  }

  return headers
}

function buildHeaders(initHeaders: HeadersInit = {}, body: unknown): HeadersInit {
  const headers = normalizeHeaders(initHeaders)

  if (body != null && !Object.keys(headers).some((key) => key.toLowerCase() === 'content-type')) {
    headers['Content-Type'] = 'application/json'
  }

  return headers
}

export function getAuthHeader(): HeadersInit {
  const token = Cookies.get('tarefize_token')

  if (!token) {
    throw new Error('Token de autenticação não encontrado no cookie "tarefize_token".')
  }

  return {
    Authorization: `Bearer ${token}`,
  }
}

export async function apiFetch<T>(path: string, init: RequestInit = {}, errorMessage = 'Erro na requisição'): Promise<T> {
  const body = init.body != null && typeof init.body !== 'string' ? JSON.stringify(init.body) : init.body
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    body,
    headers: buildHeaders(init.headers, body),
  })

  if (!response.ok) {
    let message = errorMessage

    const text = await response.text()
    if (text) {
      try {
        const payload = JSON.parse(text) as ApiErrorPayload
        if (payload?.message) {
          message = payload.message
        } else if (typeof payload === 'string') {
          message = payload
        }
      } catch {
        message = text
      }
    }

    throw new Error(message)
  }

  if (response.status === 204) {
    return undefined as unknown as T
  }

  const text = await response.text()
  if (!text) {
    return undefined as unknown as T
  }

  return JSON.parse(text) as T
}

export async function apiAuthFetch<T>(path: string, init: RequestInit = {}, errorMessage = 'Erro na requisição autenticada'): Promise<T> {
  return apiFetch<T>(path, {
    ...init,
    headers: {
      ...getAuthHeader(),
      ...(init.headers ?? {}),
    },
  }, errorMessage)
}
