package com.store.cloud.platform.controller;

import com.store.cloud.common.response.ServerResponseEntity;
import com.store.cloud.platform.model.SysConfig;
import com.store.cloud.platform.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * @author chenlin
 * @DateTime: 2020/3/19 8:29
 * @description: 支付配置、文件上传配置、短信配置、快递配置、小程序配置、公众号配置
 */
@RestController
@RequestMapping("/sys_config")
public class SysConfigController {


    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 获取保存支付宝支付配置信息
     */
    @GetMapping("/info/{key}")
    public ServerResponseEntity<String> info(@PathVariable("key")String key){
        return ServerResponseEntity.success(sysConfigService.getValue(key));
    }

    /**
     * 保存配置
     */
    @PostMapping("/save")
    public ServerResponseEntity<Void> save(@RequestBody @Valid SysConfig sysConfig){
        sysConfigService.saveOrUpdateSysConfig(sysConfig);
        return ServerResponseEntity.success();
    }




}
