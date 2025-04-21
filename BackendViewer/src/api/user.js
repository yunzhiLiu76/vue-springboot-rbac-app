import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/cryptauth/login',
    method: 'post',
    data
  })
}

export function getUserInfo() {
  return request({
    url: '/cryptauth/sys/details'
  })
}

/**
 * 更新密码
 * **/
export function updatePassword(data) {
  return request({
    url: '/sys/user/updatePass',
    method: 'put',
    data
  })
}
