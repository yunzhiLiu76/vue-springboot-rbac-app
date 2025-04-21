import layout from '@/layout'

export default {
  path: '/redis', // 一级路由路径
  name: 'redis', // 路由名称
  component: layout, // 根组件，页面会嵌套在 `layout` 中
  // 子路由
  children: [{
    path: '', // 访问 /redis 默认跳转到这个子路由
    name: 'redis', // 路由名称
    component: () => import('@/views/redis'), // 懒加载 Redis 管理页面
    meta: {
      title: 'Redis 管理', // 侧边栏 & 面包屑 显示的名称
      icon: 'el-icon-data-analysis' // 侧边栏显示的图标
    }
  }]
}
