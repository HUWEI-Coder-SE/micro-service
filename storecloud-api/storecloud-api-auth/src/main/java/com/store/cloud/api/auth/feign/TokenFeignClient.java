package com.store.cloud.api.auth.feign;

import com.store.cloud.api.auth.bo.UserInfoInTokenBO;
import com.store.cloud.common.constant.Auth;
import com.store.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author FrozenWatermelon
 * @date 2020/7/15
 */
@FeignClient(value = "storecloud-auth",contextId ="token")
public interface TokenFeignClient {

	/**
	 * 校验token并返回token保存的用户信息
	 * @param accessToken accessToken
	 * @return token保存的用户信息
	 */
	@GetMapping(value = Auth.CHECK_TOKEN_URI)
	ServerResponseEntity<UserInfoInTokenBO> checkToken(@RequestParam("accessToken") String accessToken);

}
