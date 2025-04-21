import request from '@/utils/request'

/** *
 * 获取权限列表
 * **/

export function getPermissionList() {
  return request({
    url: '/cryptauth/sys/permission/getPermissionList'
  })
}
// 新增权限
export function addPermission(data) {
  return request({
    url: '/cryptauth/sys/permission/createPermission',
    method: 'post',
    data
  })
}

// 更新权限
export function updatePermission(data) {
  return request({
    url: `/cryptauth/sys/permission/updatePermission/${data.id}`,
    method: 'put',
    data
  })
}

// 删除权限
export function delPermission(id) {
  return request({
    url: `/cryptauth/sys/permission/deletePermission/${id}`,
    method: 'delete'
  })
}
// 获取权限详情（id是权限id）
export function getPermissionDetail(id) {
  return request({
    url: `/cryptauth/sys/permission/getPermissionById/${id}`
  })
}
