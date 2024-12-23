package com.store.cloud.api.product.feign;

import com.store.cloud.api.product.dto.SkuStockLockDTO;
import com.store.cloud.common.feign.FeignInsideAuthConfig;
import com.store.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/22
 */
@FeignClient(value = "storecloud-product",contextId = "skuStockLock")
public interface SkuStockLockFeignClient {

    /**
     * 锁定库存
     * @param skuStockLocks 参数
     * @return 是否成功
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/skuStockLock/lock")
    ServerResponseEntity<Void> lock(@RequestBody List<SkuStockLockDTO> skuStockLocks);

}
