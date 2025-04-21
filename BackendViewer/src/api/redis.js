import request from '@/utils/request'

export function getRedisList(params) {
  return request({
    url: '/scaffold/redis/all',
    method: 'get',
    params
  })
}

export function addRedisData(data) {
  return request({
    url: '/scaffold/redis/set',
    method: 'post',
    data
  })
}

export function delRedisData(key) {
  return request({
    url: `/scaffold/redis/delete/${key}`,
    method: 'delete'
  })
}
