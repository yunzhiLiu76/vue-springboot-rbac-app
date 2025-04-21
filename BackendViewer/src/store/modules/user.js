// 管理 user 相关状态，比如用户信息、登录状态、路由权限等。它通过 state、mutations、actions 来管理用户数据，并与 API 交互来获取、存储和更新用户信息。
import { getToken, setToken, removeToken } from '@/utils/auth'
import { login, getUserInfo } from '@/api/user'
import { constantRoutes } from '@/router'
import { resetRouter } from '@/router'
const state = {
  // 从 getToken() 读取本地缓存的 token，如果缓存中没有，则 token 为空 ，
  // 身份验证，登录后必须携带 token 访问受保护的接口。
  token: getToken(),
  // 存储用户的基本信息，如 name、avatar、role（角色权限）等。
  // 登录后调用 getUserInfo() 获取。
  userInfo: {},
  routes: constantRoutes // 静态路由的数组
}

const mutations = {
  // 设置 token 并存入本地缓存。用于登录后，让用户能继续保持身份。
  setToken(state, token) {
    state.token = token
    // 同步到缓存
    setToken(token)
  },
  // 清除 token 并从本地缓存删除。用于退出登录，确保用户不能访问受保护的接口。
  removeToken(state) {
    // 删除Vuex的token
    state.token = null
    removeToken()
  },
  // 存储用户的基本信息（如 username、avatar、role 等）。
  // 用于 getUserInfo() 之后更新用户状态。
  setUserInfo(state, userInfo) {
    state.userInfo = userInfo
  },
  // 合并 constantRoutes（静态路由）和 newRoutes（动态权限路由）。
  // 用于权限管理，让用户在登录后获得额外权限。
  setRoutes(state, newRoutes) {
    // // 合并静态 + 动态路由
    state.routes = [...constantRoutes, ...newRoutes] // 静态路由 + 动态路由
  }
}

// 定义 actions（异步操作，间接修改 state）
const actions = {
  // context上下文，传入参数
  async login(context, data) {
    console.log(data)
    // todo: 调用登录接口
    // 调用 login(data) 请求服务器获取 token
    const token = await login(data)
    // 返回一个token 123456
    //  调用 mutation setToken，并传递 token 作为参数，用来 更新 Vuex 的 state。
    context.commit('setToken', token)
  },
  // 获取用户的基本资料
  async getUserInfo(context) {
    const result = await getUserInfo()
    context.commit('setUserInfo', result)
    return result // 返回数据
  },
  // 退出登录的action
  logout(context) {
    context.commit('removeToken') // 删除token
    context.commit('setUserInfo', {}) // 设置用户信息为空对象
    // // 重置路由（清除权限）
    resetRouter()
  }
}

export default {
  namespaced: true, // 开启命名空间
  state,
  mutations,
  actions
}
