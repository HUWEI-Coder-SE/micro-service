package com.store.cloud.multishop.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.store.cloud.api.auth.bo.UserInfoInTokenBO;
import com.store.cloud.api.auth.feign.AccountFeignClient;
import com.store.cloud.api.leaf.feign.SegmentFeignClient;
import com.store.cloud.api.rbac.dto.UserRoleDTO;
import com.store.cloud.api.rbac.feign.UserRoleFeignClient;
import com.store.cloud.common.database.dto.PageDTO;
import com.store.cloud.common.database.util.PageUtil;
import com.store.cloud.common.database.vo.PageVO;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.common.security.AuthUserContext;
import com.store.cloud.multishop.mapper.ShopUserMapper;
import com.store.cloud.multishop.model.ShopUser;
import com.store.cloud.multishop.service.ShopUserService;
import com.store.cloud.multishop.vo.ShopUserVO;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/09/03
 */
@Service
public class ShopUserServiceImpl implements ShopUserService {

	@Resource
	private ShopUserMapper shopUserMapper;
	@Autowired
	private AccountFeignClient accountFeignClient;
	@Autowired
	private UserRoleFeignClient userRoleFeignClient;
	@Autowired
	private SegmentFeignClient segmentFeignClient;

	@Override
	public PageVO<ShopUserVO> pageByShopId(PageDTO pageDTO, Long shopId, String nickName) {
		return PageUtil.doPage(pageDTO, () -> shopUserMapper.listByShopId(shopId, nickName));
	}

	@Override
	public ShopUserVO getByUserId(Long userId) {
		ShopUserVO shopUser = shopUserMapper.getByUserId(userId);
		ServerResponseEntity<List<Long>> roleIds = userRoleFeignClient.getRoleIds(shopUser.getShopUserId());
		shopUser.setRoleIds(roleIds.getData());
		return shopUser;
	}

    @Override
	@Transactional(rollbackFor = Exception.class)
    public void save(ShopUser shopUser, List<Long> roleIds) {
		shopUserMapper.save(shopUser);
		if (CollUtil.isEmpty(roleIds)) {
			return;
		}
		UserRoleDTO userRoleDTO = new UserRoleDTO();
		userRoleDTO.setRoleIds(roleIds);
		userRoleDTO.setUserId(shopUser.getShopUserId());
		userRoleFeignClient.saveByUserIdAndSysType(userRoleDTO);
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	public void update(ShopUser shopUser, List<Long> roleIds) {
		UserRoleDTO userRoleDTO = new UserRoleDTO();
		userRoleDTO.setRoleIds(roleIds);
		userRoleDTO.setUserId(shopUser.getShopUserId());
		shopUserMapper.update(shopUser);
		userRoleFeignClient.updateByUserIdAndSysType(userRoleDTO);
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	public void deleteById(Long shopUserId) {
		UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
		accountFeignClient.deleteByUserIdAndSysType(shopUserId);
		userRoleFeignClient.deleteByUserIdAndSysType(shopUserId);
		shopUserMapper.deleteById(shopUserId);
	}

    @Override
    public Long getUserIdByShopId(Long shopId) {
        return shopUserMapper.getUserIdByShopId(shopId);
    }

	@Override
	public ShopUserVO getShopAdminUser(Long shopId) {
		return shopUserMapper.getShopAdminUser(shopId);
	}

}
