package com.oneape.octopus.mapper.serve;

import com.oneape.octopus.domain.serve.ServeVersionDO;
import com.oneape.octopus.mapper.serve.provider.ServeVersionSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 14:47.
 * Modify:
 */
public interface ServeVersionMapper {
    /**
     * Add a version to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ServeVersionSqlProvider.class, method = "insert")
    int insert(ServeVersionDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ServeVersionSqlProvider.class, method = "updateById")
    int update(ServeVersionDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ServeVersionSqlProvider.class, method = "deleteById")
    int delete(ServeVersionDO model);

    /**
     * Finding by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ServeVersionSqlProvider.class, method = "findById")
    ServeVersionDO findById(@Param("id") Long id);

}
