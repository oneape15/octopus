package com.oneape.octopus.mapper.serve;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.serve.provider.ServeInfoSqlProvider;
import com.oneape.octopus.model.DO.serve.ServeInfoDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * The report Mapper.
 */
@Mapper
public interface ServeInfoMapper {

    @Select({
            "SELECT COUNT(0) FROM " + ServeInfoSqlProvider.TABLE_NAME +
                    " WHERE " + BaseSqlProvider.FIELD_ARCHIVE + " = 0 AND id = #{reportId}"
    })
    int checkReportId(@Param("reportId") Long reportId);

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ServeInfoSqlProvider.class, method = "insert")
    int insert(ServeInfoDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ServeInfoSqlProvider.class, method = "updateById")
    int update(ServeInfoDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ServeInfoSqlProvider.class, method = "deleteById")
    int delete(ServeInfoDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ServeInfoSqlProvider.class, method = "findById")
    ServeInfoDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ServeInfoSqlProvider.class, method = "list")
    List<ServeInfoDO> list(@Param("model") ServeInfoDO model);
}
