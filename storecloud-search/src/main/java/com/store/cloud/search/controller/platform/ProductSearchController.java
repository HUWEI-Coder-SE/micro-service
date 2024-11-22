package com.store.cloud.search.controller.platform;

import com.store.cloud.api.dto.EsPageDTO;
import com.store.cloud.api.dto.ProductSearchDTO;
import com.store.cloud.api.vo.EsPageVO;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.search.constant.SearchTypeEnum;
import com.store.cloud.search.manager.ProductSearchManager;
import com.store.cloud.search.vo.SpuAdminVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @Author lth
 * @Date 2021/6/21 13:48
 */
@RestController("platformSearchSpuController")
@RequestMapping("/p/search")
@Tag(name = "platform-spu列表接口")
public class ProductSearchController {

    @Autowired
    private ProductSearchManager productSearchManager;

    @GetMapping("/page")
    @Operation(summary = "商品管理信息列表（平台端）" , description = "商品管理信息列表（平台端）")
    public ServerResponseEntity<EsPageVO<SpuAdminVO>> adminPage(@Valid EsPageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        productSearchDTO.setSearchType(SearchTypeEnum.PLATFORM.value());
        EsPageVO<SpuAdminVO> searchPage =  productSearchManager.adminPage(pageDTO, productSearchDTO);
        return ServerResponseEntity.success(searchPage);
    }
}
