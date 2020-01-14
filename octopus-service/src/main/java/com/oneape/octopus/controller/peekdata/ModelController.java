package com.oneape.octopus.controller.peekdata;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.controller.peekdata.form.ModelForm;
import com.oneape.octopus.model.DO.peekdata.ModelDO;
import com.oneape.octopus.model.DO.peekdata.ModelMetaDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.ModelMetaVO;
import com.oneape.octopus.model.VO.ModelVO;
import com.oneape.octopus.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 模型管理
 */
@Slf4j
@RestController
@RequestMapping("/peek/model")
public class ModelController {
    @Resource
    private ModelService modelService;

    @PostMapping("/add")
    public ApiResult<String> doAddModel(@RequestBody @Validated(value = ModelForm.AddCheck.class) ModelForm form) {
        int status = modelService.addModelInfo(form.toDO(), form.getFields());
        return ApiResult.ofData(status > 0 ? "添加模型成功" : "添加模型失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditModel(@RequestBody @Validated(value = ModelForm.EditCheck.class) ModelForm form) {
        int status = modelService.editModelInfo(form.toDO(), form.getFields());
        return ApiResult.ofData(status > 0 ? "修改模型成功" : "修改模型失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelModel(@RequestBody @Validated(value = ModelForm.KeyCheck.class) ModelForm form) {
        int status = modelService.deleteById(form.toDO());
        return ApiResult.ofData(status > 0 ? "删除模型成功" : "删除模型失败");
    }

    /**
     * 根据模型Id获取信息
     */
    @PostMapping("/getById")
    public ApiResult<ModelVO> getByModelId(@RequestBody @Validated(value = ModelForm.KeyCheck.class) ModelForm form) {
        return ApiResult.ofData(modelService.getById(form.getModelId()));
    }

    /**
     * 获取指定模型的元素列表
     */
    @PostMapping("/listModelMetas")
    public ApiResult<List<ModelMetaVO>> listModelMeta(@RequestBody @Validated(value = ModelForm.KeyCheck.class) ModelForm form) {
        ModelMetaDO mmdo = new ModelMetaDO();
        mmdo.setDisplay(1); // 只返回显示的字段列表
        return ApiResult.ofData(modelService.listModelMeta(mmdo));
    }

    /**
     * 分页查询
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<ModelVO>> doList(@RequestBody @Validated ModelForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<ModelVO> vos = modelService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * 获取所有模型
     */
    @PostMapping("/getAllModels")
    public ApiResult<List<ModelVO>> getAllModels() {
        List<ModelVO> list = modelService.find(new ModelDO());
        return ApiResult.ofData(list);
    }

    /**
     * 获取指定模型的字段列表
     */
    @PostMapping("/columns")
    public ApiResult<List<ModelMetaVO>> getColumns(@RequestBody @Validated(value = ModelForm.ColumnCheck.class) ModelForm form) {
        Long modelId = form.getModelId();
        if (modelId == null) {
            modelId = -1L;
        }
        List<ModelMetaVO> metas = modelService.getTableColumns(modelId, form.getDatasourceId(), form.getTableName());
        return ApiResult.ofData(metas);
    }

    @PostMapping("/schemas")
    public ApiResult<List<ModelMetaVO>> getSchemas(@RequestBody @Validated(value = ModelForm.ColumnCheck.class) ModelForm form) {
        List<ModelMetaVO> metas = modelService.getTableColumns(null, form.getDatasourceId(), form.getTableName());
        return ApiResult.ofData(metas);
    }
}
