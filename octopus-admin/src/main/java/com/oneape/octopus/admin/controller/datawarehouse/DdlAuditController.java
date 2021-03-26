package com.oneape.octopus.admin.controller.datawarehouse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.controller.datawarehouse.form.DdlAuditInfoForm;
import com.oneape.octopus.commons.dto.ApiResult;
import com.oneape.octopus.admin.service.warehouse.DdlAuditService;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.domain.warehouse.DdlAuditInfoDO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * datasource DDL opt manage
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-25 15:49.
 * Modify:
 */
public class DdlAuditController {

    @Resource
    private DdlAuditService ddlAuditService;

    /**
     * Paging query ddl information.
     */
    @PostMapping(value = "/list")
    public ApiResult<PageInfo<DdlAuditInfoDO>> list(@RequestBody @Validated DdlAuditInfoForm form) {
        PageHelper.startPage(form.getCurrent(), form.getPageSize());
        List<DdlAuditInfoDO> vos = ddlAuditService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * Save ddl information.
     */
    @PostMapping("/save")
    public ApiResult<String> doSaveDatasource(@RequestBody @Validated(value = DdlAuditInfoForm.SaveCheck.class) DdlAuditInfoForm form) {
        int status = ddlAuditService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("ddl.save.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ddl.save.fail"));
    }

    /**
     * Deleted ddl information.
     */
    @PostMapping("/del/{id}")
    public ApiResult<String> doDelDs(@PathVariable(name = "id") Long id) {
        int status = ddlAuditService.deleteById(id);
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("ddl.del.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ddl.del.fail"));
    }

    /**
     * Execute DDL statements.
     */
    @PostMapping("/exec/{id}")
    public ApiResult<String> doExecDdl(@PathVariable(name = "id") Long id) {
        int status = ddlAuditService.exec(id);
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("ddl.exec.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ddl.exec.fail"));
    }

    /**
     * Review DDL statements.
     */
    @PostMapping("/audit")
    public ApiResult<String> doAuditDdl(@RequestBody @Validated(value = DdlAuditInfoForm.AuditCheck.class) DdlAuditInfoForm form) {
        int status = ddlAuditService.audit(form.toDO());
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("ddl.audit.success"));
        }

        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ddl.audit.fail"));
    }

}
