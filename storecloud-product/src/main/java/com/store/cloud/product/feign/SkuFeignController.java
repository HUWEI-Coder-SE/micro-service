package com.store.cloud.product.feign;

import com.store.cloud.api.product.feign.SkuFeignClient;
import com.store.cloud.api.product.vo.SkuVO;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.product.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
@RestController
public class SkuFeignController implements SkuFeignClient {

    @Autowired
    private SkuService skuService;


    @Override
    public ServerResponseEntity<SkuVO> getById(Long skuId) {
        return ServerResponseEntity.success(skuService.getSkuBySkuId(skuId));
    }
}
