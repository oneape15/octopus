package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.mapper.report.SqlLogMapper;
import com.oneape.octopus.model.DO.report.SqlLogDO;
import com.oneape.octopus.model.VO.SqlLogVO;
import com.oneape.octopus.service.SqlLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SqlLogServiceImpl implements SqlLogService {

    @Resource
    private SqlLogMapper sqlLogMapper;

    /**
     * 根据条件查询
     *
     * @param model ReportSqlDO
     * @return List
     */
    @Override
    public List<SqlLogVO> find(SqlLogDO model) {
        List<SqlLogVO> vos = new ArrayList<>();
        if (model == null) return vos;

        List<SqlLogDO> list = sqlLogMapper.list(model);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(l -> vos.add(SqlLogVO.ofDO(l)));
        }
        return vos;
    }

    /**
     * 根据id获取详情
     *
     * @param sqlLogId Long
     * @return SqlLogVO
     */
    @Override
    public SqlLogVO getById(Long sqlLogId) {
        SqlLogDO ldo = sqlLogMapper.findById(sqlLogId);
        return SqlLogVO.ofDO(ldo);
    }

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(SqlLogDO model) {
        return sqlLogMapper.insert(model);
    }

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(SqlLogDO model) {
        throw new BizException("SQL日志，不允许修改");
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(SqlLogDO model) {
        throw new BizException("SQL日志，不允许删除");
    }
}
