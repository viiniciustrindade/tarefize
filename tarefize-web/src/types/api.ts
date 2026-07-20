export interface ApiErrorPayload {
  message?: string
  [key: string]: unknown
}

export interface PaginatedApiResponse<T> {
  content?: T[]
  totalPages?: number
  totalElements?: number
  number?: number
  size?: number
}
