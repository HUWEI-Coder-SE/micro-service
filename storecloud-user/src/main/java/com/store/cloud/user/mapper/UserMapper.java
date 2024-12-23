package com.store.cloud.user.mapper;

import com.store.cloud.api.user.vo.UserApiVO;
import com.store.cloud.user.model.User;
import com.store.cloud.user.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
public interface UserMapper {

	/**
	 * 获取用户表列表
	 *
	 * @return 用户表列表
	 */
	List<UserVO> list();

	/**
	 * 根据用户表id获取用户表
	 *
	 * @param userId 用户表id
	 * @return 用户表
	 */
	UserApiVO getByUserId(@Param("userId") Long userId);

	/**
	 * 保存用户表
	 *
	 * @param user 用户表
	 */
	void save(@Param("user") User user);

	/**
	 * 更新用户表
	 *
	 * @param user 用户表
	 */
	void update(@Param("user") User user);

	/**
	 * 根据用户id列表，获取用户信息
	 *
	 * @param userIds
	 * @return
	 */
	List<UserApiVO> getUserByUserIds(@Param("userIds") List<Long> userIds);
}
