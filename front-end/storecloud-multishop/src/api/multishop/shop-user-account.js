import request from '@/utils/request'

export function get (shopUserId) {
  return request({
    url: '/storecloud_multishop/shop_user/account',
    method: 'get',
    params: {
      shopUserId
    }
  })
}

export function save (data) {
  return request({
    url: '/storecloud_multishop/shop_user/account',
    method: 'post',
    data
  })
}

export function update (data) {
  return request({
    url: '/storecloud_multishop/shop_user/account',
    method: 'put',
    data
  })
}
