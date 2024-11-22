package com.store.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.store.cloud.api.auth.constant.SysTypeEnum;
import com.store.cloud.api.auth.dto.AuthAccountDTO;
import com.store.cloud.api.auth.feign.AccountFeignClient;
import com.store.cloud.api.auth.vo.AuthAccountVO;
import com.store.cloud.api.leaf.feign.SegmentFeignClient;
import com.store.cloud.api.user.vo.UserApiVO;
import com.store.cloud.common.cache.constant.UserCacheNames;
import com.store.cloud.common.database.dto.PageDTO;
import com.store.cloud.common.database.util.PageUtil;
import com.store.cloud.common.database.vo.PageVO;
import com.store.cloud.common.exception.StorecloudException;
import com.store.cloud.common.response.ResponseEnum;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.common.util.IpHelper;
import com.store.cloud.user.dto.UserRegisterDTO;
import com.store.cloud.user.model.User;
import com.store.cloud.user.mapper.UserMapper;
import com.store.cloud.user.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import com.store.cloud.common.util.BeanUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户表
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Override
    public PageVO<UserApiVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userMapper.list());
    }

    @Override
    @Cacheable(cacheNames = UserCacheNames.USER_INFO, key = "#userId")
    public UserApiVO getByUserId(Long userId) {
        return userMapper.getByUserId(userId);
    }

    @Override
    @CacheEvict(cacheNames = UserCacheNames.USER_INFO, key = "#user.userId")
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public List<UserApiVO> getUserByUserIds(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return userMapper.getUserByUserIds(userIds);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public Long save(UserRegisterDTO param) {
        this.checkRegisterInfo(param);

        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(User.DISTRIBUTED_ID_KEY);
        if (!segmentIdResponse.isSuccess()) {
            throw new StorecloudException(ResponseEnum.EXCEPTION);
        }
        Long userId = segmentIdResponse.getData();

        param.setUserId(userId);

        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setCreateIp(IpHelper.getIpAddr());
        authAccountDTO.setPassword(param.getPassword());
        authAccountDTO.setIsAdmin(0);
        authAccountDTO.setSysType(SysTypeEnum.ORDINARY.value());
        authAccountDTO.setUsername(param.getUserName());
        authAccountDTO.setStatus(1);
        authAccountDTO.setUserId(userId);

        // 保存统一账户信息
        ServerResponseEntity<Long> serverResponse = accountFeignClient.save(authAccountDTO);
        // 抛异常回滚
        if (!serverResponse.isSuccess()) {
            throw new StorecloudException(serverResponse.getMsg());
        }

        User user = new User();
        user.setUserId(userId);
        user.setPic(param.getImg());
        user.setNickName(param.getNickName());
        user.setStatus(1);
        // 这里保存之后才有用户id
        userMapper.save(user);

        return serverResponse.getData();
    }

    private void checkRegisterInfo(UserRegisterDTO userRegisterDTO) {
        ServerResponseEntity<AuthAccountVO> responseEntity = accountFeignClient.getByUsernameAndSysType(userRegisterDTO.getUserName(), SysTypeEnum.ORDINARY);
        if (!responseEntity.isSuccess()) {
            throw new StorecloudException(responseEntity.getMsg());
        }
        if (Objects.nonNull(responseEntity.getData())) {
            throw new StorecloudException("用户名已存在");
        }
    }

}
