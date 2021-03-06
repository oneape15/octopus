package com.oneape.octopus.admin.service.peekdata;

import com.oneape.octopus.query.data.Result;
import com.oneape.octopus.domain.peekdata.ImportRecordDO;
import com.oneape.octopus.admin.model.vo.ImportRecordVO;
import com.oneape.octopus.admin.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImportRecordService extends BaseService<ImportRecordDO> {

    /**
     * 上传数据
     *
     * @param importRecord ImportRecordDO
     * @param file         MultipartFile
     * @return int 0 - 失败； 1 - 成功；
     */
    int uploadData(ImportRecordDO importRecord, MultipartFile file);

    /**
     * 逻辑删除指定id的数据
     *
     * @param importRecord ImportRecordDO
     */
    void archiveData(ImportRecordDO importRecord);

    /**
     * 预览导入数据
     *
     * @param importRecord ImportRecordDO
     * @return Result
     */
    Result previewData(ImportRecordDO importRecord);

    /**
     * 根据条件查询
     *
     * @param importRecord ImportRecordDO
     * @return List
     */
    List<ImportRecordVO> list(ImportRecordDO importRecord);
}
