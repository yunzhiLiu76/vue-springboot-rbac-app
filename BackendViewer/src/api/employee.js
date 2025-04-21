import request from '@/utils/request'

/**
 * 获取员工列表
 * **/

export function getEmployeeList(params) {
  return request({
    url: '/cryptauth/sys/getUsersList',
    params // 地址参数 查询参数
  })
}

/**
 * 删除员工
 * **/

export function delEmployee(id) {
  return request({
    method: 'delete',
    url: `/cryptauth/sys/deleteUser/${id}`
  })
}

/**
 * 新增员工
 * ***/

export function addEmployee(data) {
  return request({
    url: '/cryptauth/sys/user/add',
    method: 'post',
    data
  })
}

/**
 *  获取员工详情
 * **/

export function getEmployeeDetail(id) {
  return request({
    url: `/cryptauth/sys/details`
  })
}

/**
 * 更新员工
 * ***/

export function updateEmployee(data) {
  return request({
    url: `/cryptauth/sys/user/updatePass/${data.id}`,
    method: 'put',
    data
  })
}

/**
 * 获取可用的角色
 * **/

export function getEnableRoleList() {
  return request({
    url: '/cryptauth/sys/role/list/enabled'
  })
}

/**
 * 分配员工角色
 * ***/

export function assignRole(data) {
  return request({
    url: '/cryptauth/sys/role/assignRoles',
    method: 'post',
    data
  })
}
