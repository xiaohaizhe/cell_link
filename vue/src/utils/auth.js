import { setStore,getStore,removeStore } from '@/utils/mUtils'

const TokenKey = 'token'

export function getToken() {
  return getStore(TokenKey)
}

export function setToken(token) {
  return setStore(TokenKey, token)
}

export function removeToken() {
  return removeStore(TokenKey)
}
