
// 这段代码是 Vue Router 配置文件，主要用于定义 静态路由（constantRoutes） 和 动态路由（asyncRoutes），
// 以及创建 Vue Router 实例并提供 重置路由 的方法。
// 它是基于 Vue.js + Vue Router + Vuex 的 后台管理系统 路由配置。
import Vue from 'vue'
import Router from 'vue-router'

// Vue.use(Router) 让 Vue 使用 Vue Router 插件，使其具备路由功能。
Vue.use(Router)

/* Layout */
// Layout 组件 是整个后台管理系统的基础布局，所有有侧边栏的页面都会嵌套在这个布局中。
import Layout from '@/layout'
// 这些是 动态路由模块，用于管理不同功能模块的权限访问
import departmentRouter from './modules/department'
import employeeRouter from './modules/employee'
import permissionRouter from './modules/permission'
import roleRouter from './modules/role'
import rediRouter from './modules/redis'
import mongoRouter from './modules/mongo'
import mysqlRouter from './modules/mysql'
/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
// 静态路由（constantRoutes）：任何用户都可以访问，无需权限验证
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    // hidden: true → 该路由不会显示在侧边栏菜单中
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    // redirect: '/dashboard' → 访问 / 时，重定向到 dashboard
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      //  设置侧边栏标题和图标
      meta: { title: '首页', icon: 'dashboard' }
    }]
  }

  // 404 page must be placed at the end !!!

]
// 动态路由
// 需要 权限验证，不同用户角色可能有不同的访问权限
// 这些模块会在 用户登录并获取角色信息后，根据权限动态添加到 router
export const asyncRoutes = [
  departmentRouter,
  roleRouter,
  employeeRouter,
  permissionRouter,
  rediRouter,
  mongoRouter,
  mysqlRouter]
// 创建 Vue Router 实例
const createRouter = () => new Router({
  // mode: 'history' → 采用 History 模式（需要后端支持）
  mode: 'history', // require service support
  // scrollBehavior: () => ({ y: 0 }) → 页面切换时自动滚动到顶部
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes // 初始路由表只包含 静态路由
})

// 创建 router 实例，供全局使用
const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
// 用于清空路由表，一般在 用户退出登录 时调用
// 动态路由是根据 用户角色 生成的
// 如果用户切换账户，需要清除之前的动态路由，避免 旧用户的路由权限泄露
export function resetRouter() {
  const newRouter = createRouter()
  // 作用是 用一个新的 router 实例替换当前 router，从而 清除所有动态路由
  router.matcher = newRouter.matcher // reset router
}

export default router
