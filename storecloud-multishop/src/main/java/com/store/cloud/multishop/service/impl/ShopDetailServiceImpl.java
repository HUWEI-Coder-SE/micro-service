package com.store.cloud.multishop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.store.cloud.api.auth.bo.UserInfoInTokenBO;
import com.store.cloud.api.auth.constant.SysTypeEnum;
import com.store.cloud.api.auth.dto.AuthAccountDTO;
import com.store.cloud.api.auth.feign.AccountFeignClient;
import com.store.cloud.api.auth.vo.AuthAccountVO;
import com.store.cloud.api.feign.SearchSpuFeignClient;
import com.store.cloud.api.multishop.bo.EsShopDetailBO;
import com.store.cloud.api.vo.search.SpuSearchVO;
import com.store.cloud.common.cache.constant.CacheNames;
import com.store.cloud.common.constant.Constant;
import com.store.cloud.common.constant.UserAdminType;
import com.store.cloud.common.constant.StatusEnum;
import com.store.cloud.common.database.dto.PageDTO;
import com.store.cloud.common.database.util.PageUtil;
import com.store.cloud.common.database.vo.PageVO;
import com.store.cloud.common.exception.StorecloudException;
import com.store.cloud.common.response.ResponseEnum;
import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.common.security.AuthUserContext;
import com.store.cloud.common.util.IpHelper;
import com.store.cloud.common.util.PrincipalUtil;
import com.store.cloud.multishop.constant.ShopStatus;
import com.store.cloud.multishop.constant.ShopType;
import com.store.cloud.multishop.dto.ShopDetailDTO;
import com.store.cloud.multishop.dto.UpdateShopPasswordDTO;
import com.store.cloud.multishop.mapper.ShopDetailMapper;
import com.store.cloud.multishop.model.ShopDetail;
import com.store.cloud.multishop.model.ShopUser;
import com.store.cloud.multishop.service.ShopDetailService;
import com.store.cloud.api.multishop.vo.ShopDetailVO;
import com.store.cloud.multishop.service.ShopUserService;
import com.store.cloud.multishop.vo.ShopDetailAppVO;
import com.store.cloud.multishop.vo.ShopUserVO;
import io.seata.spring.annotation.GlobalTransactional;
import com.store.cloud.common.util.BeanUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 店铺详情
 *
 * @author FrozenWatermelon
 * @date 2020-11-23 16:24:29
 */
@Service
public class ShopDetailServiceImpl implements ShopDetailService {

    @Autowired
    private ShopDetailMapper shopDetailMapper;
    @Autowired
    private SearchSpuFeignClient searchSpuFeignClient;
    @Autowired
    private ShopUserService shopUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public PageVO<ShopDetailVO> page(PageDTO pageDTO, ShopDetailDTO shopDetailDTO) {
        return PageUtil.doPage(pageDTO, () -> shopDetailMapper.list(shopDetailDTO));
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SHOP_DETAIL_ID_KEY, key = "#shopId")
    public ShopDetailVO getByShopId(Long shopId) {
        ServerResponseEntity<AuthAccountVO> accountRes = accountFeignClient.getMerchantInfoByTenantId(shopId);
        if (!accountRes.isSuccess()) {
            throw new StorecloudException("商家信息获取失败");
        }
        AuthAccountVO authAccountVO = accountRes.getData();
        ShopDetailVO shopDetailVO = shopDetailMapper.getByShopId(shopId);
        if (Objects.nonNull(authAccountVO)) {
            shopDetailVO.setUsername(authAccountVO.getUsername());
        }
        return shopDetailVO;
    }

    @Override
    public void save(ShopDetail shopDetail) {
        shopDetailMapper.save(shopDetail);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SHOP_DETAIL_ID_KEY, key = "#shopDetail.shopId")
    public void update(ShopDetail shopDetail) {
        shopDetailMapper.update(shopDetail);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SHOP_DETAIL_ID_KEY, key = "#shopId")
    public void deleteById(Long shopId) {
        shopDetailMapper.deleteById(shopId);
    }

    @Override
    public List<ShopDetail> listByShopIds(List<Long> shopIds) {
        if (CollUtil.isEmpty(shopIds)) {
            return new ArrayList<>();
        }
        return shopDetailMapper.listByShopIds(shopIds);
    }

    @Override
    public PageVO<ShopDetailAppVO> shopSearchPage(PageDTO pageDTO, ShopDetailDTO shopDetailDTO) {
        PageVO<ShopDetailAppVO> page = PageUtil.doPage(pageDTO, () -> shopDetailMapper.shopSearchList(shopDetailDTO));
        Set<Long> spuIdSet = page.getList().stream().map(ShopDetailAppVO::getShopId).collect(Collectors.toSet());
        ServerResponseEntity<List<SpuSearchVO>> spuResponse = searchSpuFeignClient.limitSizeListByShopIds(new ArrayList<>(spuIdSet), Constant.SPU_SIZE_FIVE);
        if (!Objects.equals(spuResponse.getCode(), ResponseEnum.OK.value())) {
            throw new StorecloudException(spuResponse.getMsg());
        } else if (CollectionUtil.isEmpty(spuResponse.getData())) {
            return page;
        }
        List<SpuSearchVO> data = spuResponse.getData();
        Map<Long, List<SpuSearchVO>> shopMap = data.stream().collect(Collectors.groupingBy(SpuSearchVO::getShopId));
        for (ShopDetailAppVO shopDetail : page.getList()) {
            shopDetail.setSpuList(shopMap.get(shopDetail.getShopId()));
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public void applyShop(ShopDetailDTO shopDetailDTO) {
        checkShopInfo(shopDetailDTO);
        ShopDetail newShopDetail = BeanUtil.map(shopDetailDTO, ShopDetail.class);
        // 申请开店
        newShopDetail.setShopStatus(ShopStatus.OPEN.value());
        newShopDetail.setType(ShopType.STOP.value());
        shopDetailMapper.save(newShopDetail);
        shopDetailDTO.setShopId(newShopDetail.getShopId());
        // 创建账号
        createShopAccount(shopDetailDTO, StatusEnum.ENABLE);
    }

    @Override
    public void changeSpuStatus(Long shopId, Integer shopStatus) {
        shopDetailMapper.changeSpuStatus(shopId, shopStatus);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SHOP_DETAIL_ID_KEY, key = "#shopId")
    public void removeCacheByShopId(Long shopId) {

    }

    @Override
    public EsShopDetailBO shopExtensionData(Long shopId) {
        return shopDetailMapper.shopExtensionData(shopId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void createShop(ShopDetailDTO shopDetailDTO) {
        checkShopInfo(shopDetailDTO);
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (Objects.nonNull(userInfoInTokenBO.getTenantId())) {
            throw new StorecloudException("该用户已经创建过店铺");
        }
        // 保存店铺
        ShopDetail shopDetail = BeanUtil.map(shopDetailDTO, ShopDetail.class);
        shopDetail.setShopStatus(ShopStatus.OPEN.value());
        shopDetailMapper.save(shopDetail);
        // 保存商家账号
        // 保存到shopUser
        ShopUser shopUser = new ShopUser();
        shopUser.setShopId(shopDetail.getShopId());
        shopUser.setHasAccount(1);
        shopUser.setNickName(shopDetailDTO.getShopName());
        shopUserService.save(shopUser, null);
        // 保存到authAccount
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setTenantId(shopDetail.getShopId());
        authAccountDTO.setUsername(shopDetailDTO.getUsername());
        authAccountDTO.setPassword(shopDetailDTO.getPassword());
        authAccountDTO.setCreateIp(IpHelper.getIpAddr());
        authAccountDTO.setStatus(StatusEnum.ENABLE.value());
        authAccountDTO.setSysType(SysTypeEnum.MULTISHOP.value());
        authAccountDTO.setIsAdmin(UserAdminType.ADMIN.value());
        authAccountDTO.setUserId(shopUser.getShopUserId());
        accountFeignClient.save(authAccountDTO);

        userInfoInTokenBO.setTenantId(shopDetail.getShopId());
        ServerResponseEntity<Void> updateTenantIdRes = accountFeignClient.updateUserInfoByUserIdAndSysType(userInfoInTokenBO, AuthUserContext.get().getUserId(), SysTypeEnum.ORDINARY.value());
        if (!Objects.equals(updateTenantIdRes.getCode(), ResponseEnum.OK.value())) {
            throw new StorecloudException(updateTenantIdRes.getMsg());
        }
    }

    @Override
    public List<ShopDetailVO> getShopDetailByShopIdAndShopName(List<Long> shopIds, String shopName) {
        return shopDetailMapper.getShopDetailByShopIdAndShopName(shopIds,shopName);
    }

    @Override
    public Boolean checkShopName(String shopName) {
        int count = shopDetailMapper.countShopName(shopName, null);
        return count <= 0;
    }

    @Override
    public void resetShopPassword(UpdateShopPasswordDTO updateShopPasswordDTO) {
        if (!Objects.equals(updateShopPasswordDTO.getPassword(), updateShopPasswordDTO.getConfirmPsw())) {
            throw new StorecloudException("密码不一致");
        }
        ShopUserVO shopAdminUser = shopUserService.getShopAdminUser(updateShopPasswordDTO.getShopId());
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setPassword(updateShopPasswordDTO.getPassword());
        authAccountDTO.setUserId(shopAdminUser.getShopUserId());
        authAccountDTO.setSysType(SysTypeEnum.MULTISHOP.value());
        accountFeignClient.updateShopPassword(authAccountDTO);
    }

    /**
     * 检验店铺信息是否正确
     * @param shopDetailDTO
     */
    private void checkShopInfo(ShopDetailDTO shopDetailDTO) {
        // 店铺名称
        if (StrUtil.isNotBlank(shopDetailDTO.getShopName())) {
            shopDetailDTO.setShopName(shopDetailDTO.getShopName().trim());
        }
        if(shopDetailMapper.countShopName(shopDetailDTO.getShopName(), null) > 0) {
            throw new StorecloudException("店铺名称已存在");
        }

        String username = shopDetailDTO.getUsername();
        // 用户名
        if (!PrincipalUtil.isUserName(username)) {
            throw new StorecloudException("用户名格式不正确");
        }

        ServerResponseEntity<AuthAccountVO> accountResponse = accountFeignClient.getByUsernameAndSysType(username, SysTypeEnum.MULTISHOP);
        if (!Objects.equals(accountResponse.getCode(), ResponseEnum.OK.value())) {
            throw new StorecloudException(accountResponse.getMsg());
        }

        AuthAccountVO authAccountVO = accountResponse.getData();
        if (Objects.nonNull(authAccountVO)) {
            throw new StorecloudException("用户账号已存在");
        }
    }

    /**
     * 创建店铺初始账号
     * @param shopDetailDTO
     * @param statusEnum
     */
    public void createShopAccount(ShopDetailDTO shopDetailDTO, StatusEnum statusEnum) {
        ShopUser shopUser = new ShopUser();
        shopUser.setShopId(shopDetailDTO.getShopId());
        shopUser.setHasAccount(1);
        shopUser.setNickName(shopDetailDTO.getUsername());
        shopUserService.save(shopUser, null);

        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setUsername(shopDetailDTO.getUsername());
        String password = passwordEncoder.encode(shopDetailDTO.getPassword().trim());
        authAccountDTO.setPassword(password);
        authAccountDTO.setStatus(statusEnum.value());
        authAccountDTO.setSysType(SysTypeEnum.MULTISHOP.value());
        authAccountDTO.setCreateIp(IpHelper.getIpAddr());
        authAccountDTO.setTenantId(shopDetailDTO.getShopId());
        authAccountDTO.setUserId(shopUser.getShopUserId());
        authAccountDTO.setIsAdmin(UserAdminType.ADMIN.value());
        ServerResponseEntity<Long> save = accountFeignClient.save(authAccountDTO);
        if (!Objects.equals(save.getCode(), ResponseEnum.OK.value())) {
            throw new StorecloudException(save.getMsg());
        }
    }
}
