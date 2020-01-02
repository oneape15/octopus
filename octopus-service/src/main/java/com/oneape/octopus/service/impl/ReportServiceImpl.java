package com.oneape.octopus.service.impl;

import com.oneape.octopus.mapper.*;
import com.oneape.octopus.model.DO.ReportDO;
import com.oneape.octopus.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private ReportParamMapper reportParamMapper;
    @Resource
    private ReportColumnMapper reportColumnMapper;
    @Resource
    private ReportSqlMapper reportSqlMapper;
    @Resource
    private ReportSqlLogMapper reportSqlLogMapper;

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(ReportDO model) {
        return 0;
    }

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(ReportDO model) {
        return 0;
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(ReportDO model) {
        return 0;
    }
}
