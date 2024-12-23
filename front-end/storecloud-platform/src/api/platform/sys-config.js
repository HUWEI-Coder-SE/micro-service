import request from '@/utils/request'

export function get (key) {
  return request({
    url: '/storecloud_platform/sys_config/info/' + key,
    method: 'get',
    params: ''
  })
}
export function save (data) {
  return request({
    url: '/storecloud_platform/sys_config/save/',
    method: 'post',
    data
  })
}
