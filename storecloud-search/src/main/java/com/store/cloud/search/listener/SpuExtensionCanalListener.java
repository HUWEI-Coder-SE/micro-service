package com.store.cloud.search.listener;


import cn.throwx.canal.gule.model.CanalBinLogResult;
import cn.throwx.canal.gule.support.processor.BaseCanalBinlogEventProcessor;
import com.store.cloud.api.product.bo.EsProductBO;
import com.store.cloud.common.cache.constant.CacheNames;
import com.store.cloud.common.cache.util.CacheManagerUtil;
import com.store.cloud.common.exception.StorecloudException;
import com.store.cloud.common.util.Json;
import com.store.cloud.search.bo.SpuExtensionBO;
import com.store.cloud.search.constant.EsIndexEnum;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author FrozenWatermelon
 * @date 2020/11/13
 */
@Component
public class SpuExtensionCanalListener extends BaseCanalBinlogEventProcessor<SpuExtensionBO> {

    private static final Logger log = LoggerFactory.getLogger(SpuExtensionCanalListener.class);

    @Autowired
    private CacheManagerUtil cacheManagerUtil;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 插入商品，此时插入es
     */
    @Override
    protected void processInsertInternal(CanalBinLogResult<SpuExtensionBO> result) {
        System.out.println();
    }

    /**
     * 更新商品，删除商品索引，再重新构建一个
     */
    @Override
    protected void processUpdateInternal(CanalBinLogResult<SpuExtensionBO> result) {
        // 更新之后的数据
        SpuExtensionBO afterData = result.getAfterData();

        // 清除缓存
        cacheManagerUtil.evictCache(CacheNames.SPU_EXTENSION_KEY, afterData.getSpuId().toString());

        UpdateRequest request = new UpdateRequest(EsIndexEnum.PRODUCT.value(), String.valueOf(afterData.getSpuId()));

        EsProductBO esProductBO = new EsProductBO();
        // 可售库存
        esProductBO.setSpuId(afterData.getSpuId());
        esProductBO.setStock(afterData.getStock());
        esProductBO.setHasStock(afterData.getStock() != 0);
        esProductBO.setSaleNum(afterData.getSaleNum());

        request.doc(Json.toJsonString(esProductBO), XContentType.JSON);
        try {
            UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
            log.info(updateResponse.toString());
        } catch (IOException e) {
            log.error(e.toString());
            throw new StorecloudException("更新es信息异常",e);
        }
    }
}
