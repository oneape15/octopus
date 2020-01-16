package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportSqlProvider;
import com.oneape.octopus.model.DO.report.ReportDO;
import com.oneape.octopus.model.VO.ReportVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 报表Mapper
 */
@Mapper
public interface ReportMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ReportSqlProvider.class, method = "insert")
    int insert(ReportDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportSqlProvider.class, method = "updateById")
    int update(ReportDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportSqlProvider.class, method = "deleteById")
    int delete(ReportDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportSqlProvider.class, method = "findById")
    ReportDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportSqlProvider.class, method = "list")
    List<ReportDO> list(@Param("model") ReportDO model);

    /**
     * 获取简单的报表信息
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportSqlProvider.class, method = "findSimpleReport")
    List<ReportVO> findSimpleReport(@Param("model") ReportDO model);
}
