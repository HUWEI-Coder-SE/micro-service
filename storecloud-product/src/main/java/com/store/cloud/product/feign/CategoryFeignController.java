package com.store.cloud.product.feign;

import com.store.cloud.api.product.feign.CategoryFeignClient;
import com.store.cloud.api.product.vo.CategoryVO;
import com.store.cloud.common.constant.Constant;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lhd
 * @date 2020/12/23
 */
@RestController
public class CategoryFeignController implements CategoryFeignClient {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ServerResponseEntity<List<CategoryVO>> listByOneLevel() {
        return ServerResponseEntity.success(categoryService.listByShopIdAndParenId(Constant.PLATFORM_SHOP_ID, Constant.CATEGORY_ID));
    }

    @Override
    public ServerResponseEntity<List<Long>> listCategoryId(Long categoryId) {
        CategoryVO category = categoryService.getById(categoryId);
        List<Long> categoryIds = categoryService.listCategoryId(category.getShopId(), category.getParentId());
        return ServerResponseEntity.success(categoryIds);
    }
}
