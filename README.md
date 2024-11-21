
## 目录结构

```
mall4cloud
├─mall4cloud-api -- 内网接口
│  ├─mall4cloud-api-auth  -- 授权对内接口
│  ├─mall4cloud-api-biz  -- biz对内接口
│  ├─mall4cloud-api-leaf  -- 美团分布式id生成接口
│  ├─mall4cloud-api-multishop  -- 店铺对内接口
│  ├─mall4cloud-api-order  -- 订单对内接口
│  ├─mall4cloud-api-platform  -- 平台对内接口
│  ├─mall4cloud-api-product  -- 商品对内接口
│  ├─mall4cloud-api-rbac  -- 用户角色权限对内接口
│  ├─mall4cloud-api-search  -- 搜索对内接口
│  └─mall4cloud-api-user  -- 用户对内接口
├─mall4cloud-auth  -- 授权校验模块
├─mall4cloud-biz  -- mall4cloud 业务代码。如图片上传/短信等
├─mall4cloud-common -- 一些公共的方法
│  ├─mall4cloud-common-cache  -- 缓存相关公共代码
│  ├─mall4cloud-common-core  -- 公共模块核心（公共中的公共代码）
│  ├─mall4cloud-common-database  -- 数据库连接相关公共代码
│  ├─mall4cloud-common-order  -- 订单相关公共代码
│  ├─mall4cloud-common-product  -- 商品相关公共代码
│  ├─mall4cloud-common-rocketmq  -- rocketmq相关公共代码
│  └─mall4cloud-common-security  -- 安全相关公共代码
├─mall4cloud-gateway  -- 网关
├─mall4cloud-leaf  -- 基于美团leaf的生成id服务
├─mall4cloud-multishop  -- 商家端
├─mall4cloud-order  -- 订单服务
├─mall4cloud-payment  -- 支付服务
├─mall4cloud-platform  -- 平台端
├─mall4cloud-product  -- 商品服务
├─mall4cloud-rbac  -- 用户角色权限模块
├─mall4cloud-search  -- 搜索模块
└─mall4cloud-user  -- 用户服务
```

## 商城部署后 API 地址

| 服务                                                 | 地址                    |
| ---------------------------------------------------- |-----------------------|
| mall4cloud-gatway 网关服务                           | http://127.0.0.1:8000 |
| mall4cloud-auth  授权校验服务                        | http://127.0.0.1:9101 |
| mall4cloud-biz     业务代码服务（如图片上传/短信等） | http://127.0.0.1:9000 |
| mall4cloud-leaf   基于美团leaf的生成id服务           | http://127.0.0.1:9100 |
| mall4cloud-multishop 商家服务                        | http://127.0.0.1:9103 |
| mall4cloud-order         订单服务                    | http://127.0.0.1:9106 |
| mall4cloud-payment   支付服务                        | http://127.0.0.1:9113 |
| mall4cloud-product    商品服务                       | http://127.0.0.1:9114 |
| mall4cloud-rbac          用户角色服务                | http://127.0.0.1:9102 |
| mall4cloud-search      搜索服务                      | http://127.0.0.1:9108 |
| mall4cloud-user          用户服务                    | http://127.0.0.1:9105 |

