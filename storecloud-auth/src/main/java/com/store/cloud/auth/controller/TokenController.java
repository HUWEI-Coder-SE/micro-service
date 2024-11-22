package com.store.cloud.auth.controller;

import com.store.cloud.common.security.bo.TokenInfoBO;
import com.store.cloud.auth.dto.RefreshTokenDTO;
import com.store.cloud.auth.manager.TokenStore;
import com.store.cloud.api.auth.vo.TokenInfoVO;
import com.store.cloud.common.response.ServerResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.store.cloud.common.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author FrozenWatermelon
 * @date 2020/6/30
 */
@RestController
@Tag(name = "token")
public class TokenController {

	@Autowired
	private TokenStore tokenStore;


	@PostMapping("/ua/token/refresh")
	public ServerResponseEntity<TokenInfoVO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
		ServerResponseEntity<TokenInfoBO> tokenInfoServerResponseEntity = tokenStore
				.refreshToken(refreshTokenDTO.getRefreshToken());
		if (!tokenInfoServerResponseEntity.isSuccess()) {
			return ServerResponseEntity.transform(tokenInfoServerResponseEntity);
		}
		return ServerResponseEntity
				.success(BeanUtil.map(tokenInfoServerResponseEntity.getData(), TokenInfoVO.class));
	}

}
