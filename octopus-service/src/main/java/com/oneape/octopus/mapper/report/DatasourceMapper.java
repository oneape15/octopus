package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.DatasourceSqlProvider;
import com.oneape.octopus.model.DO.report.DatasourceDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 数据源Mapper
 */
@Mapper
public interface DatasourceMapper {

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = DatasourceSqlProvider.class, method = "insert")
    int insert(DatasourceDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = DatasourceSqlProvider.class, method = "updateById")
    int update(DatasourceDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = DatasourceSqlProvider.class, method = "deleteById")
    int delete(DatasourceDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = DatasourceSqlProvider.class, method = "findById")
    DatasourceDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = DatasourceSqlProvider.class, method = "list")
    List<DatasourceDO> list(@Param("model") DatasourceDO model);
}
