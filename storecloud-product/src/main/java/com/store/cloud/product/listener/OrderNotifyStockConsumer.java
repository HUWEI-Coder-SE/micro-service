package com.store.cloud.product.listener;

import com.store.cloud.common.order.bo.PayNotifyBO;
import com.store.cloud.common.rocketmq.config.RocketMqConstant;
import com.store.cloud.product.service.SkuStockLockService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 解锁库存的监听
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_NOTIFY_STOCK_TOPIC,consumerGroup = RocketMqConstant.ORDER_NOTIFY_STOCK_TOPIC)
public class OrderNotifyStockConsumer implements RocketMQListener<PayNotifyBO> {

    @Autowired
    private SkuStockLockService skuStockLockService;

    /**
     * 订单支付成功锁定库存
     */
    @Override
    public void onMessage(PayNotifyBO message) {
        skuStockLockService.markerStockUse(message.getOrderIds());
    }
}
