package com.oneape.octopus.mapper.serve;

import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.mapper.serve.provider.ServeGroupSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 14:47.
 * Modify:
 */
public interface ServeGroupMapper {
    /**
     * Add one group to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ServeGroupSqlProvider.class, method = "insert")
    int insert(ServeGroupDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ServeGroupSqlProvider.class, method = "updateById")
    int update(ServeGroupDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ServeGroupSqlProvider.class, method = "deleteById")
    int delete(ServeGroupDO model);

    /**
     * Finding by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ServeGroupSqlProvider.class, method = "findById")
    ServeGroupDO findById(@Param("id") Long id);

    @SelectProvider(type = ServeGroupSqlProvider.class, method = "checkHasTheSameName")
    int checkHasTheSameName(@Param("name") String name, @Param("filterId") Long filterId);
}
