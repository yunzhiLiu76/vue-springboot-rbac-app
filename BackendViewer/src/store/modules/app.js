// 管理 app 相关状态，比如 UI 设置（如侧边栏是否展开）以及 设备类型（device） 的切换
// 这里引入了 js-cookie 库，用于在 浏览器 Cookie 中存储 sidebarStatus（侧边栏的展开/折叠状态），确保刷新页面时状态不会丢失。
import Cookies from 'js-cookie'

const state = {
  sidebar: {
    // +Cookies.get('sidebarStatus') 把字符串转换为数字。
    // !! 用于转换为布尔值（1 变 true，0 变 false）。
    // 如果 Cookie 里没有值，默认 true（侧边栏默认是展开的）
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    // 是否 不带动画 关闭侧边栏，默认 false
    withoutAnimation: false
  },
  // 当前设备类型，默认 'desktop'（桌面端）。
  device: 'desktop'
}

// 定义 mutations（同步修改 state）
const mutations = {
  // 切换侧边栏状态
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  // 关闭侧边栏
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  // 直接修改 state.device，可用于 响应式布局（比如移动端适配）。
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  }
}

// 定义 actions（异步操作，间接修改 state）
// actions 负责调用 mutations 进行状态修改，通常用于异步操作，但这里的 actions 只是简单地封装了 mutations 以便组件调用：
const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  // 允许组件传入 withoutAnimation 参数
  // 调用commit('CLOSE_SIDEBAR', withoutAnimation) 关闭侧边栏。
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  }
}

export default {
  // 开启命名空间，确保 Vuex 的 模块化管理，这样在组件中调用 Vuex 方法时，需要加上 模块名
  namespaced: true,
  state,
  mutations,
  actions
}
