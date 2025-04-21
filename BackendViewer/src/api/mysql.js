import request from '@/utils/request'

export function getMysqlList(userId) {
  return request({
    url: '/scaffold/addressBook/list/${userId}',
    method: 'get'
  })
}

export function addMysqlData(data) {
  return request({
    url: '/scaffold/addressBook/save',
    method: 'post',
    data
  })
}

export function delMysqlData(id) {
  return request({
    url: `/scaffold/addressBook/deleteById/${id}`,
    method: 'delete'
  })
}

export function updateMysqlData(data) {
  return request({
    url: '/scaffold/addressBook/update',
    method: 'put',
    data
  })
}
