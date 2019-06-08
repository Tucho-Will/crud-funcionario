import http from 'axios'
import {apiHost} from './config'

const BASE_URL = apiHost

export const axiosInstance = http.create({
  baseURL: BASE_URL,
  timeout: false,
  params: {} // não remova, é para adicionar parametros mais tarde
})

export default {
  getData (action) {
    return axiosInstance.get(action)
  },
  getWithParams (action, params) {
    return axiosInstance.get(action, {params})
  },
  postData (action, data) {
    return axiosInstance.post(action, data)
  },
  putData (action, data) {
    return axiosInstance.put(action, data)
  },
  deleteData (action) {
    return axiosInstance.delete(action)
  }
}
