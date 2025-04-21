// 这段代码是 角色管理（role）模块的路由配置，用于 Vue Router 的 动态路由。它定义了 角色管理页面 的路由规则，并会在用户 登录后动态加载。
import layout from '@/layout'
export default {
  path: '/role', // 一级路由路径
  name: 'role', // 路由名称
  component: layout, // 根组件，页面会嵌套在 `layout` 中
  // 子路由
  children: [{
    path: '', // 访问 /role 默认跳转到这个子路由
    name: 'role', // 路由名称
    component: () => import('@/views/role'), // 懒加载角色管理页面
    meta: {
      title: '角色', // 侧边栏 & 面包屑 显示的名称
      icon: 'setting' // 侧边栏显示的图标
    }
  }]
}
