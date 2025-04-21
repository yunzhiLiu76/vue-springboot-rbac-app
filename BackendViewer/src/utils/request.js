// 引入 axios，用于发送 HTTP 请求
import axios from 'axios'
// 引入 Vuex 全局状态管理，获取 token 进行身份验证。
import store from '@/store'
// 使用 Element UI 的 Message 组件 提示错误消息。
import { Message } from 'element-ui'
import router from '@/router'
// axios.create({...})：创建 Axios 实例，方便后续统一管理 API 请求
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // 基础地址
  timeout: 10000
}) // 创建一个新的axios实例
// 成功1 失败2
// 请求拦截器 拦截所有请求，在 headers 注入 token（身份验证）。
service.interceptors.request.use((config) => {
  // 注入token
//  this.$store.getters
  // store.getters.token => 请求头里面
  if (store.getters.token) {
    config.headers.Authorization = `Bearer ${store.getters.token}`
  }
  return config
}, (error) => {
  // 失败执行promise
  return Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use((response) => {
  // axios默认包裹了data
  // 判断是不是Blob
  if (response.data instanceof Blob) return response.data // 返回了Blob对象
  const { data, message, success } = response.data // 默认json格式
  if (success) {
    return data
  } else {
    Message({ type: 'error', message })
    return Promise.reject(new Error(message))
  }
}, async(error) => {
  if (error.response.status === 401) {
    Message({ type: 'warning', message: 'token超时了' })
    // 说明token超时了
    await store.dispatch('user/logout') // 调用action 退出登录
    //  主动跳到登录页
    router.push('/login') // 跳转到登录页
    // 抛出错误，避免代码继续执行。
    return Promise.reject(error)
  }
  // error.message 弹出错误消息。
  Message({ type: 'error', message: error.message })
  // 抛出错误，避免代码继续执行。
  return Promise.reject(error)
})
export default service
