package com.store.cloud.rbac.service.impl;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import com.store.cloud.rbac.mapper.UserRoleMapper;
import com.store.cloud.rbac.service.UserRoleService;

/**
 * @author FrozenWatermelon
 * @date 2020/6/23
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

	@Resource
	private UserRoleMapper userRoleMapper;

}
