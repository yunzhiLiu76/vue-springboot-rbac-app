import request from '@/utils/request'

/** *
 * 获取角色列表
 * **/
export function getRoleList(params) {
  return request({
    url: '/cryptauth/sys/role/getRoleList',
    params // 查询参数
  })
}

/** **
 * 新增角色
 * ***/

export function addRole(data) {
  return request({
    url: '/cryptauth/sys/role/createRole',
    method: 'post',
    data
  })
}

/**
 * 更新角色
 * ***/

export function updateRole(data) {
  return request({
    url: `/cryptauth/sys/role/updateRole/${data.id}`,
    method: 'put',
    data
  })
}

/** *
 * 删除角色
 * **/

export function delRole(id) {
  return request({
    url: `/cryptauth/sys/role/deleteRole/${id}`,
    method: 'delete'
  })
}

/**
 * 获取角色详情
 * **/

export function getRoleDetail(id) {
  return request({
    url: `/cryptauth/sys/role/getRoleById/${id}`
  })
}

/**
 * 给角色分配权限
 *
 * ***/

export function assignPerm(data) {
  return request({
    url: '/cryptauth/sys/role/assignPrem',
    method: 'post',
    data
  })
}

/**
 * 根据用户id查询该用户所具有的角色
 * @param {*} id
 */
export function getRoleByUserId(id) {
  return request({
    url: `/cryptauth/sys/role/getRoleByUserId/${id}`
  })
}
