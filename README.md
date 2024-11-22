
## 目录结构

```
storecloud
├─storecloud-api -- 内网接口
│  ├─storecloud-api-auth  -- 授权对内接口
│  ├─storecloud-api-biz  -- biz对内接口
│  ├─storecloud-api-leaf  -- 美团分布式id生成接口
│  ├─storecloud-api-multishop  -- 店铺对内接口
│  ├─storecloud-api-order  -- 订单对内接口
│  ├─storecloud-api-platform  -- 平台对内接口
│  ├─storecloud-api-product  -- 商品对内接口
│  ├─storecloud-api-rbac  -- 用户角色权限对内接口
│  ├─storecloud-api-search  -- 搜索对内接口
│  └─storecloud-api-user  -- 用户对内接口
├─storecloud-auth  -- 授权校验模块
├─storecloud-biz  -- storecloud 业务代码。如图片上传/短信等
├─storecloud-common -- 一些公共的方法
│  ├─storecloud-common-cache  -- 缓存相关公共代码
│  ├─storecloud-common-core  -- 公共模块核心（公共中的公共代码）
│  ├─storecloud-common-database  -- 数据库连接相关公共代码
│  ├─storecloud-common-order  -- 订单相关公共代码
│  ├─storecloud-common-product  -- 商品相关公共代码
│  ├─storecloud-common-rocketmq  -- rocketmq相关公共代码
│  └─storecloud-common-security  -- 安全相关公共代码
├─storecloud-gateway  -- 网关
├─storecloud-leaf  -- 基于美团leaf的生成id服务
├─storecloud-multishop  -- 商家端
├─storecloud-order  -- 订单服务
├─storecloud-payment  -- 支付服务
├─storecloud-platform  -- 平台端
├─storecloud-product  -- 商品服务
├─storecloud-rbac  -- 用户角色权限模块
├─storecloud-search  -- 搜索模块
└─storecloud-user  -- 用户服务
```

## 商城部署后 API 地址

| 服务                                                 | 地址                    |
| ---------------------------------------------------- |-----------------------|
| storecloud-gatway 网关服务                           | http://127.0.0.1:8000 |
| storecloud-auth  授权校验服务                        | http://127.0.0.1:9101 |
| storecloud-biz     业务代码服务（如图片上传/短信等） | http://127.0.0.1:9000 |
| storecloud-leaf   基于美团leaf的生成id服务           | http://127.0.0.1:9100 |
| storecloud-multishop 商家服务                        | http://127.0.0.1:9103 |
| storecloud-order         订单服务                    | http://127.0.0.1:9106 |
| storecloud-payment   支付服务                        | http://127.0.0.1:9113 |
| storecloud-product    商品服务                       | http://127.0.0.1:9114 |
| storecloud-rbac          用户角色服务                | http://127.0.0.1:9102 |
| storecloud-search      搜索服务                      | http://127.0.0.1:9108 |
| storecloud-user          用户服务                    | http://127.0.0.1:9105 |

