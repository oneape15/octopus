package com.oneape.octopus.mapper;

import com.oneape.octopus.mapper.provider.ReportParamSqlProvider;
import com.oneape.octopus.model.DO.ReportParamDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportParamMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ReportParamSqlProvider.class, method = "insert")
    int insert(ReportParamDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportParamSqlProvider.class, method = "updateById")
    int update(ReportParamDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportParamSqlProvider.class, method = "deleteById")
    int delete(ReportParamDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportParamSqlProvider.class, method = "findById")
    ReportParamDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportParamSqlProvider.class, method = "list")
    List<ReportParamDO> list(@Param("model") ReportParamDO model);
}
