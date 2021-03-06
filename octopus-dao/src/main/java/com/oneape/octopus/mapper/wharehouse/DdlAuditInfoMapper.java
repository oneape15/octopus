package com.oneape.octopus.mapper.wharehouse;

import com.oneape.octopus.domain.warehouse.DdlAuditInfoDO;
import com.oneape.octopus.mapper.wharehouse.provider.DdlAuditInfoSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-25 16:09.
 * Modify:
 */
@Mapper
public interface DdlAuditInfoMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = DdlAuditInfoSqlProvider.class, method = "insert")
    int insert(DdlAuditInfoDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = DdlAuditInfoSqlProvider.class, method = "updateById")
    int update(DdlAuditInfoDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = DdlAuditInfoSqlProvider.class, method = "deleteById")
    int delete(DdlAuditInfoDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = DdlAuditInfoSqlProvider.class, method = "findById")
    DdlAuditInfoDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = DdlAuditInfoSqlProvider.class, method = "list")
    List<DdlAuditInfoDO> list(@Param("model") DdlAuditInfoDO model);

    /**
     * Check if the same name exists
     *
     * @param name     String
     * @param filterId Long
     * @return int
     */
    @SelectProvider(type = DdlAuditInfoSqlProvider.class, method = "checkSameName")
    int checkSameName(@Param("name") String name, @Param("filterId") Long filterId);
}
