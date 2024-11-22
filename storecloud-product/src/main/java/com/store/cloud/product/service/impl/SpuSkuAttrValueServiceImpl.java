package com.store.cloud.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.store.cloud.product.mapper.SpuSkuAttrValueMapper;
import com.store.cloud.product.model.SpuSkuAttrValue;
import com.store.cloud.product.service.SpuSkuAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品sku销售属性关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Service
public class SpuSkuAttrValueServiceImpl implements SpuSkuAttrValueService {

    @Autowired
    private SpuSkuAttrValueMapper spuSkuAttrValueMapper;

    @Override
    public void save(SpuSkuAttrValue spuSkuAttrValue) {
        spuSkuAttrValueMapper.save(spuSkuAttrValue);
    }

    @Override
    public void updateBatch(List<SpuSkuAttrValue> spuSkuAttrValues) {
        if (CollUtil.isNotEmpty(spuSkuAttrValues)) {
            spuSkuAttrValueMapper.updateBatch(spuSkuAttrValues);
        }
    }

    @Override
    public void deleteById(Long spuSkuAttrId) {
        spuSkuAttrValueMapper.deleteById(spuSkuAttrId);
    }

    @Override
    public void saveBatch(List<SpuSkuAttrValue> spuSkuAttrValues) {
        if (CollUtil.isEmpty(spuSkuAttrValues)) {
            return;
        }
        spuSkuAttrValueMapper.saveBatch(spuSkuAttrValues);
    }

    @Override
    public void updateBySpuId(Long spuId) {
        spuSkuAttrValueMapper.updateBySpuId(spuId);
    }

    @Override
    public void changeStatusBySkuId(List<Long> skuIds, Integer status) {
        spuSkuAttrValueMapper.changeStatusBySkuId(skuIds, status);
    }
}
