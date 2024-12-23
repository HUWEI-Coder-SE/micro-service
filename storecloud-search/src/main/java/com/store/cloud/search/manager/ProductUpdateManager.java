package com.store.cloud.search.manager;

import com.store.cloud.api.product.bo.EsProductBO;
import com.store.cloud.common.exception.StorecloudException;
import com.store.cloud.common.util.Json;
import com.store.cloud.search.constant.EsIndexEnum;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/16
 */
@Component
public class ProductUpdateManager {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 批量更新es中的商品信息
     * @param spuIds spuId列表
     * @param esProductBO 更新的数据
     */
    public void esUpdateSpuBySpuIds(List<Long> spuIds, EsProductBO esProductBO) {
        String source = Json.toJsonString(esProductBO);
        try {
            BulkRequest request = new BulkRequest();
            // 准备更新的数据
            for (Long spuId : spuIds) {
                request.add(new UpdateRequest(EsIndexEnum.PRODUCT.value(), String.valueOf(spuId)).doc(source, XContentType.JSON));
            }
            //更新
            BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                throw new StorecloudException(bulkResponse.buildFailureMessage());
            }
        } catch (Exception e) {
            throw new StorecloudException(e.getMessage());
        }
    }
}
