package com.oneape.octopus.admin.controller.gateway;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.controller.gateway.form.RouteForm;
import com.oneape.octopus.admin.model.vo.ApiResult;
import com.oneape.octopus.admin.service.gateway.GatewayRouteService;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.domain.gateway.GatewayRouteDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Gateway route Management.
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-07 15:02.
 * Modify:
 */
@Slf4j
@RestController
@RequestMapping("/route")
public class GatewayRouteController {

    @Resource
    private GatewayRouteService gatewayRouteService;

    /**
     * Paging query serve information.
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<GatewayRouteDO>> doList(@RequestBody @Validated RouteForm form) {
        PageHelper.startPage(form.getCurrent(), form.getPageSize());
        List<GatewayRouteDO> dos = gatewayRouteService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(dos));
    }

    /**
     * Save route information.
     */
    @PostMapping("/save")
    public ApiResult<String> doSaveRoute(@RequestBody @Validated RouteForm form) {
        int status = gatewayRouteService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("route.save.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("route.save.fail"));
    }

    /**
     * Deleted route information.
     */
    @PostMapping("/del/{id}")
    public ApiResult<String> doDelServe(@PathVariable(name = "id") Long id) {
        int status = gatewayRouteService.deleteById(id);
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("route.del.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("route.del.fail"));
    }
}
