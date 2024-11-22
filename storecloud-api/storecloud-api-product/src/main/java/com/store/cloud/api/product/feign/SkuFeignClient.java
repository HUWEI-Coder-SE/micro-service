package com.store.cloud.api.product.feign;

import com.store.cloud.api.product.vo.SkuVO;
import com.store.cloud.common.feign.FeignInsideAuthConfig;
import com.store.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
@FeignClient(value = "storecloud-product",contextId = "sku")
public interface SkuFeignClient {

    /**
     * 通过skuId获取sku信息
     * @param skuId skuId
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/sku/getById")
    ServerResponseEntity<SkuVO> getById(@RequestParam("skuId") Long skuId);

}
