package com.store.cloud.multishop.service.impl;

import com.store.cloud.api.auth.dto.AuthAccountDTO;
import com.store.cloud.api.auth.bo.UserInfoInTokenBO;
import com.store.cloud.api.auth.feign.AccountFeignClient;
import com.store.cloud.api.auth.vo.AuthAccountVO;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.common.security.AuthUserContext;
import com.store.cloud.common.util.IpHelper;
import com.store.cloud.multishop.dto.ChangeAccountDTO;
import com.store.cloud.multishop.mapper.ShopUserMapper;
import com.store.cloud.multishop.model.ShopUser;
import com.store.cloud.multishop.service.ShopUserAccountService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

/**
 * @author FrozenWatermelon
 * @date 2020/09/03
 */
@Service
public class ShopUserAccountServiceImpl implements ShopUserAccountService {

	@Resource
	private ShopUserMapper shopUserMapper;
	@Autowired
	private AccountFeignClient accountFeignClient;

    @Override
	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> save(ChangeAccountDTO changeAccountDTO) {
		AuthAccountDTO authAccountDTO = getAuthAccountDTO(changeAccountDTO);
		authAccountDTO.setCreateIp(IpHelper.getIpAddr());
		authAccountDTO.setIsAdmin(0);
		// 保存
		ServerResponseEntity<Long> serverResponseEntity = accountFeignClient.save(authAccountDTO);
		if (!serverResponseEntity.isSuccess()) {
			return ServerResponseEntity.transform(serverResponseEntity);
		}
		ShopUser shopUser = new ShopUser();
		shopUser.setShopUserId(changeAccountDTO.getUserId());
		shopUser.setHasAccount(1);
		shopUser.setShopId(AuthUserContext.get().getTenantId());
		shopUserMapper.update(shopUser);
		return ServerResponseEntity.success();
    }

	@Override
	public ServerResponseEntity<Void> update(ChangeAccountDTO changeAccountDTO) {

		AuthAccountDTO authAccountDTO = getAuthAccountDTO(changeAccountDTO);
		// 更新，不涉及分布式事务
		ServerResponseEntity<Void> serverResponseEntity = accountFeignClient.update(authAccountDTO);
		if (!serverResponseEntity.isSuccess()) {
			return serverResponseEntity;
		}

		return ServerResponseEntity.success();
	}

	@Override
	public ServerResponseEntity<AuthAccountVO> getByUserIdAndSysType(Long userId, Integer sysType) {
		return accountFeignClient.getByUserIdAndSysType(userId,sysType);
	}

	private AuthAccountDTO getAuthAccountDTO(ChangeAccountDTO changeAccountDTO) {
		AuthAccountDTO authAccountDTO = new AuthAccountDTO();
		UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
		authAccountDTO.setPassword(changeAccountDTO.getPassword());
		authAccountDTO.setUsername(changeAccountDTO.getUsername());
		authAccountDTO.setStatus(changeAccountDTO.getStatus());
		authAccountDTO.setSysType(userInfoInTokenBO.getSysType());
		authAccountDTO.setTenantId(userInfoInTokenBO.getTenantId());
		authAccountDTO.setUserId(changeAccountDTO.getUserId());
		return authAccountDTO;
	}

}
