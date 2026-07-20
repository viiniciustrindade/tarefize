import { User } from "@/types/user";
import { apiAuthFetch, apiFetch } from '@/services/api';
import type { AuthResponse, LoginRequest, RegisterRequest } from '@/types/auth';
import Cookies from 'js-cookie';

const AUTH_URL = '/auth';
const REGISTER_URL = '/register';
const USER_URL = '/users';
const USER_EMAIL_COOKIE = 'tarefize_user_email';
const USER_NAME_COOKIE = 'tarefize_user_name';

function isSecureCookie() {
  return typeof window !== 'undefined' && window.location.protocol === 'https:'
}

function setUserEmailCookie(email: string) {
  Cookies.set(USER_EMAIL_COOKIE, email, {
    expires: 7,
    secure: isSecureCookie(),
    sameSite: 'strict',
    path: '/',
  })
}

function setUserNameCookie(name: string) {
  Cookies.set(USER_NAME_COOKIE, name, {
    expires: 7,
    secure: isSecureCookie(),
    sameSite: 'strict',
    path: '/',
  })
}

function setTokenCookie(token: string) {
  Cookies.set('tarefize_token', token, {
    expires: 7,
    secure: isSecureCookie(),
    sameSite: 'strict',
    path: '/',
  })
}

function setAuthCookies(token: string, email: string, name?: string) {
  setTokenCookie(token)
  setUserEmailCookie(email)

  if (name?.trim()) {
    setUserNameCookie(name.trim())
  }
}

async function postAuth<T>(path: string, body: unknown, errorMessage: string): Promise<T> {
  return apiFetch<T>(path, {
    method: 'POST',
    body: JSON.stringify(body),
  }, errorMessage)
}

export const authService = {
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const data = await postAuth<AuthResponse>(AUTH_URL, credentials, 'Credenciais inválidas! verifique seu email e senha.')

    if (!data.token) {
      throw new Error('Token não encontrado na resposta do servidor.')
    }

    setAuthCookies(data.token, credentials.email)

    return data
  },

  async register(credentials: RegisterRequest): Promise<AuthResponse> {
    const data = await postAuth<AuthResponse>(REGISTER_URL, credentials, 'Não foi possível registrar. Verifique os dados e tente novamente.')

    if (!data.token) {
      throw new Error('Token não encontrado na resposta do servidor.')
    }

    setAuthCookies(data.token, credentials.email, credentials.name)

    return data
  },

  setStoredUserName(name: string) {
    if (!name) return
    setUserNameCookie(name)
  },

  async findProfile(email: string): Promise<User> {
    return apiAuthFetch<User>(`${USER_URL}?email=${encodeURIComponent(email)}`, {
      method: 'GET',
    }, 'Não foi possível carregar o perfil do usuário.')
  },

  getStoredToken() {
    return Cookies.get('tarefize_token')
  },

  getStoredUserEmail() {
    return Cookies.get(USER_EMAIL_COOKIE) || ''
  },

  getStoredUserName() {
    return Cookies.get(USER_NAME_COOKIE) || ''
  },

  logout() {
    Cookies.remove('tarefize_token', {
      path: '/',
    })
    Cookies.remove(USER_EMAIL_COOKIE, {
      path: '/',
    })
    Cookies.remove(USER_NAME_COOKIE, {
      path: '/',
    })
  },
}
