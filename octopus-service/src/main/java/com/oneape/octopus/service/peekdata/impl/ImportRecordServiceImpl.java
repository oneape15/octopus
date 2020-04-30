package com.oneape.octopus.service.peekdata.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.mapper.peekdata.ImportRecordMapper;
import com.oneape.octopus.model.DO.peekdata.ImportRecordDO;
import com.oneape.octopus.model.VO.ImportRecordVO;
import com.oneape.octopus.service.peekdata.ImportRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImportRecordServiceImpl implements ImportRecordService {
    @Resource
    private ImportRecordMapper importRecordMapper;

    /**
     * 上传数据
     *
     * @param importRecord ImportRecordDO
     * @param file         MultipartFile
     * @return int 0 - 失败； 1 - 成功；
     */
    @Override
    public int uploadData(ImportRecordDO importRecord, MultipartFile file) {
        Preconditions.checkNotNull(file, "上传文件为空");
        return 0;
    }

    /**
     * 逻辑删除指定id的数据
     *
     * @param importRecord ImportRecordDO
     */
    @Override
    public void archiveData(ImportRecordDO importRecord) {

    }

    /**
     * 预览导入数据
     *
     * @param importRecord ImportRecordDO
     * @return Result
     */
    @Override
    public Result previewData(ImportRecordDO importRecord) {
        return null;
    }

    /**
     * 根据条件查询
     *
     * @param importRecord ImportRecordDO
     * @return List
     */
    @Override
    public List<ImportRecordVO> list(ImportRecordDO importRecord) {
        if (importRecord == null) {
            importRecord = new ImportRecordDO();
        }
        List<ImportRecordDO> list = importRecordMapper.list(importRecord);
        List<ImportRecordVO> ret = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(ir -> ret.add(ImportRecordVO.ofDO(ir)));
        }

        return ret;
    }

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(ImportRecordDO model) {
        return 0;
    }

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(ImportRecordDO model) {
        return 0;
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(ImportRecordDO model) {
        return 0;
    }
}
