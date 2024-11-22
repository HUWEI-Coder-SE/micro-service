package com.store.cloud.api.feign;

import com.store.cloud.api.vo.EsPageVO;
import com.store.cloud.api.vo.search.EsOrderVO;
import com.store.cloud.common.dto.OrderSearchDTO;
import com.store.cloud.common.feign.FeignInsideAuthConfig;
import com.store.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 订单搜索
 * @author FrozenWatermelon
 * @date 2021/02/05
 */
@FeignClient(value = "storecloud-search",contextId = "searchOrder")
public interface SearchOrderFeignClient {


    /**
     * 订单搜索
     * @param orderSearch 订单搜索参数
     * @return 订单列表
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchOrder/getOrderPage")
    ServerResponseEntity<EsPageVO<EsOrderVO>> getOrderPage(@RequestBody OrderSearchDTO orderSearch);

}
