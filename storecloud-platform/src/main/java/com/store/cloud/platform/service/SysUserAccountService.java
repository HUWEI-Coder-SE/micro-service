package com.store.cloud.platform.service;

import com.store.cloud.api.auth.vo.AuthAccountVO;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.platform.dto.ChangeAccountDTO;

/**
 * @author lhd
 * @date 2020/12/22
 */
public interface SysUserAccountService {

	/**
	 * 添加账户
	 * @param changeAccountDTO 账户信息
	 * @return void
	 */
	ServerResponseEntity<Void> save(ChangeAccountDTO changeAccountDTO);

	/**
	 * 更新账户
	 * @param changeAccountDTO 账户信息
	 * @return
	 */
	ServerResponseEntity<Void> update(ChangeAccountDTO changeAccountDTO);

	/**
	 * 根据用户id和系统类型获取用户信息
	 * @param userId 用户id
	 * @param sysType 系统类型
	 * @return void
	 */
	ServerResponseEntity<AuthAccountVO> getByUserIdAndSysType(Long userId, Integer sysType);
}
