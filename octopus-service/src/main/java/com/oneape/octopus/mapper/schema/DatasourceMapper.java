package com.oneape.octopus.mapper.schema;

import com.oneape.octopus.mapper.schema.provider.DatasourceSqlProvider;
import com.oneape.octopus.model.DO.schema.DatasourceDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 数据源Mapper
 */
@Mapper
public interface DatasourceMapper {

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = DatasourceSqlProvider.class, method = "insert")
    int insert(DatasourceDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = DatasourceSqlProvider.class, method = "updateById")
    int update(DatasourceDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = DatasourceSqlProvider.class, method = "deleteById")
    int delete(DatasourceDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = DatasourceSqlProvider.class, method = "findById")
    DatasourceDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = DatasourceSqlProvider.class, method = "list")
    List<DatasourceDO> list(@Param("model") DatasourceDO model);
}
