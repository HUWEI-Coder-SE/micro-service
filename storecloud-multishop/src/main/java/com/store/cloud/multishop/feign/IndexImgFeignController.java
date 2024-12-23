package com.store.cloud.multishop.feign;

import com.store.cloud.api.multishop.feign.IndexImgFeignClient;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.multishop.service.IndexImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lth
 * @Date 2021/7/8 11:12
 */
@RestController
public class IndexImgFeignController implements IndexImgFeignClient {

    @Autowired
    private IndexImgService indexImgService;

    @Override
    public ServerResponseEntity<Void> deleteBySpuId(Long spuId, Long shopId) {
        indexImgService.deleteBySpuId(spuId, shopId);
        return ServerResponseEntity.success();
    }
}
