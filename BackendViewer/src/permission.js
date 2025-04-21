import router from '@/router'
import nprogress from 'nprogress'
import 'nprogress/nprogress.css'
import store from '@/store'
// 动态路由报表
import { asyncRoutes } from '@/router'

/**
 *前置守卫
 *
*/

const whiteList = ['/login', '/404']
router.beforeEach(async(to, from, next) => {
  nprogress.start()
  if (store.getters.token) {
    // 存在token
    if (to.path === '/login') {
      // 跳转到主页
      next('/') // 中转到主页
      // next（地址）并没有执行后置守卫
      nprogress.done()
    } else {
      // // 判断是否已获取用户信息（避免重复获取）
      // 若用户信息未获取，则拉取用户角色数据并动态添加路由
      if (!store.getters.userId) {
        // // 拉取用户信息
        // 获取当前用户角色信息，roles.menus 包含该用户可访问的菜单权限
        const { roles } = await store.dispatch('user/getUserInfo')
        // console.log(roles.menus) // 数组 不确定 可能是8个 1个 0个
        // console.log(asyncRoutes) // 数组 8个
        // // 根据权限过滤动态路由
        console.log(asyncRoutes)
        const filterRoutes = asyncRoutes.filter(item => {
          // return true/false
          // 只保留用户有权限的路由
          return roles.menus.includes(item.name)
        }) // 将筛选后的路由存入 Vuex
        store.commit('user/setRoutes', filterRoutes)
        // 动态添加路由 // 处理未匹配路由
        router.addRoutes([...filterRoutes, { path: '*', redirect: '/404', hidden: true }]) // 添加动态路由信息到路由表
        // 重新跳转当前路径，确保动态路由生效
        next(to.path)
      } else {
        next() // 放过
      }
    }
  } else {
    // 没有token
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next('/login') // 中转到登录页
      nprogress.done()
    }
  }
})

/** *
 * 后置守卫
 * **/
router.afterEach(() => {
  console.log('123')
  nprogress.done()
})
