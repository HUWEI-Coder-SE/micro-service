package com.store.cloud.auth.feign;

import cn.hutool.core.util.StrUtil;
import com.store.cloud.api.auth.bo.UserInfoInTokenBO;
import com.store.cloud.api.auth.constant.SysTypeEnum;
import com.store.cloud.api.auth.dto.AuthAccountDTO;
import com.store.cloud.api.auth.feign.AccountFeignClient;
import com.store.cloud.api.auth.vo.AuthAccountVO;
import com.store.cloud.api.leaf.feign.SegmentFeignClient;
import com.store.cloud.auth.manager.TokenStore;
import com.store.cloud.auth.mapper.AuthAccountMapper;
import com.store.cloud.auth.model.AuthAccount;
import com.store.cloud.common.exception.StorecloudException;
import com.store.cloud.common.response.ResponseEnum;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.common.security.AuthUserContext;
import com.store.cloud.common.security.bo.AuthAccountInVerifyBO;
import com.store.cloud.common.security.constant.InputUserNameEnum;
import com.store.cloud.api.auth.vo.TokenInfoVO;
import com.store.cloud.common.util.PrincipalUtil;
import com.store.cloud.common.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/9/22
 */
@RestController
public class AccountFeignController implements AccountFeignClient {

    @Autowired
    private AuthAccountMapper authAccountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Long> save(AuthAccountDTO authAccountDTO) {
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId("storecloud-auth-account");
        if (!segmentIdResponse.isSuccess()) {
            throw new StorecloudException(ResponseEnum.EXCEPTION);
        }

        ServerResponseEntity<AuthAccount> verify = verify(authAccountDTO);
        if (!verify.isSuccess()) {
            return ServerResponseEntity.transform(verify);
        }
        AuthAccount data = verify.getData();
        data.setUid(segmentIdResponse.getData());
        authAccountMapper.save(data);

        return ServerResponseEntity.success(data.getUid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> update(AuthAccountDTO authAccountDTO) {
        ServerResponseEntity<AuthAccount> verify = verify(authAccountDTO);
        if (!verify.isSuccess()) {
            return ServerResponseEntity.transform(verify);
        }
        authAccountMapper.updateAccountInfo(verify.getData());
        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> updateAuthAccountStatus(AuthAccountDTO authAccountDTO) {
        if (Objects.isNull(authAccountDTO.getStatus())) {
            throw new StorecloudException(ResponseEnum.EXCEPTION);
        }
        AuthAccount authAccount = BeanUtil.map(authAccountDTO, AuthAccount.class);
        authAccountMapper.updateAccountInfo(authAccount);
        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> deleteByUserIdAndSysType(Long userId) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        authAccountMapper.deleteByUserIdAndSysType(userId, userInfoInTokenBO.getSysType());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getByUserIdAndSysType(Long userId,Integer sysType) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        AuthAccount authAccount = authAccountMapper.getByUserIdAndType(userId, userInfoInTokenBO.getSysType());
        return ServerResponseEntity.success(BeanUtil.map(authAccount, AuthAccountVO.class));
    }

    @Override
    public ServerResponseEntity<TokenInfoVO> storeTokenAndGetVo(UserInfoInTokenBO userInfoInTokenBO) {
        return ServerResponseEntity.success(tokenStore.storeAndGetVo(userInfoInTokenBO));
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getByUsernameAndSysType(String username, SysTypeEnum sysType) {
        return ServerResponseEntity.success(authAccountMapper.getByUsernameAndSysType(username, sysType.value()));
    }

    private ServerResponseEntity<AuthAccount> verify(AuthAccountDTO authAccountDTO) {

        // 用户名
        if (!PrincipalUtil.isUserName(authAccountDTO.getUsername())) {
            return ServerResponseEntity.showFailMsg("用户名格式不正确");
        }

        AuthAccountInVerifyBO userNameBo = authAccountMapper.getAuthAccountInVerifyByInputUserName(InputUserNameEnum.USERNAME.value(), authAccountDTO.getUsername(), authAccountDTO.getSysType());
        if (userNameBo != null && !Objects.equals(userNameBo.getUserId(), authAccountDTO.getUserId())) {
            return ServerResponseEntity.showFailMsg("用户名已存在，请更换用户名再次尝试");
        }

        AuthAccount authAccount = BeanUtil.map(authAccountDTO, AuthAccount.class);

        if (StrUtil.isNotBlank(authAccount.getPassword())) {
            authAccount.setPassword(passwordEncoder.encode(authAccount.getPassword()));
        }

        return ServerResponseEntity.success(authAccount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> updateUserInfoByUserIdAndSysType(UserInfoInTokenBO userInfoInTokenBO, Long userId, Integer sysType) {
        AuthAccount byUserIdAndType = authAccountMapper.getByUserIdAndType(userId, sysType);
        userInfoInTokenBO.setUid(byUserIdAndType.getUid());
        tokenStore.updateUserInfoByUidAndAppId(byUserIdAndType.getUid(), sysType.toString(), userInfoInTokenBO);
        AuthAccount authAccount = BeanUtil.map(userInfoInTokenBO, AuthAccount.class);
        int res = authAccountMapper.updateUserInfoByUserId(authAccount, userId, sysType);
        if (res != 1) {
            throw new StorecloudException("用户信息错误，更新失败");
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getMerchantInfoByTenantId(Long tenantId) {
        AuthAccountVO authAccountVO = authAccountMapper.getMerchantInfoByTenantId(tenantId);
        return ServerResponseEntity.success(authAccountVO);
    }

    @Override
    public ServerResponseEntity<Void> updateShopPassword(AuthAccountDTO authAccountDTO) {
        if (StrUtil.isNotBlank(authAccountDTO.getPassword())) {
            authAccountDTO.setPassword(passwordEncoder.encode(authAccountDTO.getPassword()));
        }
        authAccountMapper.updatePassword(authAccountDTO.getUserId(), authAccountDTO.getSysType(), authAccountDTO.getPassword());
        return ServerResponseEntity.success();
    }
}
