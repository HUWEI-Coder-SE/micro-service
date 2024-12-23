package com.store.cloud.user.feign;

import com.store.cloud.api.user.feign.UserAddrFeignClient;
import com.store.cloud.common.order.vo.UserAddrVO;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.common.security.AuthUserContext;
import com.store.cloud.user.service.UserAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户地址feign连接
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@RestController
public class UserAddrFeignController implements UserAddrFeignClient {

    @Autowired
    private UserAddrService userAddrService;

    @Override
    public ServerResponseEntity<UserAddrVO> getUserAddrByAddrId(Long addrId) {
        return ServerResponseEntity.success(userAddrService.getUserAddrByUserId(addrId,AuthUserContext.get().getUserId()));
    }
}
