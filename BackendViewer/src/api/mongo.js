import request from '@/utils/request'

export function getMongoList(params) {
  return request({
    url: '/mongo/list',
    method: 'get',
    params
  })
}

export function addMongoData(data) {
  return request({
    url: '/mongo/add',
    method: 'post',
    data
  })
}

export function delMongoData(id) {
  return request({
    url: `/mongo/delete/${id}`,
    method: 'delete'
  })
}

export function updateMongoData(data) {
  return request({
    url: '/mongo/update',
    method: 'put',
    data
  })
}
