// // 引入 Vue 框架
import Vue from 'vue'
// 引入 Vuex 状态管理库
// Vuex 是 Vue 官方推荐的 状态管理工具，用于管理应用的全局数据状态。
// Vue 本身不带 Vuex，需要单独安装并引入
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import user from './modules/user'

// Vuex 状态管理的配置文件，适用于 Vue 2 版本。它用于创建一个 Vuex store（仓库），管理应用程序的全局状态
// 启用 Vuex，在 Vue 中使用 Vuex，这样 Vue 组件才能使用 Vuex 提供的全局状态管理功能。
Vue.use(Vuex)

// new Vuex.Store({...})：创建一个 Vuex store（仓库）。
const store = new Vuex.Store({
  // modules 属性用于拆分 Vuex 的 state，让不同功能的状态管理在各自的模块中维护。
  // 这里注册了 app、settings、user 这 3 个模块，每个模块都可能包含 state、mutations、actions 和 getters。
  modules: {
    app,
    settings,
    user
  },
  // etters 用于集中管理计算属性，方便组件从 Vuex 访问派生的状态数据。
  getters
})

// 这行代码把 store 导出，以便在 Vue 项目中使用。
export default store
