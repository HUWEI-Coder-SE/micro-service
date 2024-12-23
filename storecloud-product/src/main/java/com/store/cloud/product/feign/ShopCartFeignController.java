package com.store.cloud.product.feign;

import cn.hutool.core.collection.CollectionUtil;
import com.store.cloud.api.product.feign.ShopCartFeignClient;
import com.store.cloud.common.order.vo.ShopCartItemVO;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.common.security.AuthUserContext;
import com.store.cloud.product.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
@RestController
public class ShopCartFeignController implements ShopCartFeignClient {

    @Autowired
    private ShopCartService shopCartService;

    @Override
    public ServerResponseEntity<List<ShopCartItemVO>> getCheckedShopCartItems() {
        List<ShopCartItemVO> checkedShopCartItems = shopCartService.getCheckedShopCartItems();
        if (CollectionUtil.isNotEmpty(checkedShopCartItems)) {
            for (ShopCartItemVO shopCartItem : checkedShopCartItems) {
                shopCartItem.setTotalAmount(shopCartItem.getCount() * shopCartItem.getSkuPriceFee());
            }
        }
        return ServerResponseEntity.success(checkedShopCartItems);
    }

    @Override
    public ServerResponseEntity<Void> deleteItem(List<Long> shopCartItemIds) {
        Long userId = AuthUserContext.get().getUserId();
        shopCartService.deleteShopCartItemsByShopCartItemIds(userId,shopCartItemIds);
        return ServerResponseEntity.success();
    }
}
