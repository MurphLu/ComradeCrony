import axios from 'axios'
import { Indicator } from 'mint-ui'
import store from '../../store'
import router from '../../router'


const ssoService = axios.create({
  baseURL: "http://localhost:18082",
  timeout: 100000,
  headers: {'Content-Type': 'application/json'}
})

ssoService.interceptors.request.use(config=> {
  Indicator.open({ text: '处理中', spinnerType: 'fading-circle' })
  return config
}

)
ssoService.interceptors.response.use(
  function(response) {
    Indicator.close()
    return Promise.resolve(response)
  },
  function (error) {
    Indicator.close()
    return Promise.reject(error.response)
  }
)
const service = axios.create({
  baseURL: "http://localhost:18081",
  timeout: 100000,
  headers: {'Content-Type': 'application/json'}
})

service.interceptors.request.use((config) => {
  Indicator.open({ text: '处理中', spinnerType: 'fading-circle' })

  config.headers['Authorization'] = store.state.token ?? ''
  console.log(config)

  return config
})

service.interceptors.response.use(
  function(response) {
    Indicator.close()
    console.log('============', response.status)
    return Promise.resolve(response)
  },
  function (error) {
    Indicator.close()
    const res = error.response
    console.log(error)
    if(res.status===401) {
      store.commit('tokenChanged', '')
      router.push({path:'/login'})
      return
    }
    return Promise.reject(error.response)
  }
)

const callSSOApi = (axiosData) => {
  let data={
    url: '',
    method: 'get',
    data: ''
  }

  Object.assign(data, axiosData)
  console.log(`call api location: ${data.method} ${data.url}`)
  return ssoService(data)
}

const callApi = (axiosData) => {
  console.log(axiosData)
  let data = {
    url: '',
    method: 'get',
    data: ''
  }
  // merge data
  Object.assign(data, axiosData)
  console.log(`call api location: ${data.method} ${data.url}`)
  return service(data)
}

export {
  callSSOApi,
  callApi
}
