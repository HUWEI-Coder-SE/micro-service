package com.store.cloud.product.feign;

import com.store.cloud.api.product.dto.SkuStockLockDTO;
import com.store.cloud.api.product.feign.SkuStockLockFeignClient;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.product.service.*;
import com.store.cloud.product.service.SkuStockLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
@RestController
public class SkuStockLockFeignController implements SkuStockLockFeignClient {


    @Autowired
    private SkuStockLockService skuStockLockService;

    @Override
    public ServerResponseEntity<Void> lock(List<SkuStockLockDTO> skuStockLocksParam) {
        return skuStockLockService.lock(skuStockLocksParam);
    }
}
