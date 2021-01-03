package com.oneape.octopus.mapper.serve;

import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.serve.provider.ServeGroupSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ServeGroupSqlProvider.class, method = "list")
    List<ServeGroupDO> list(@Param("model") ServeGroupDO model);

    /**
     * Finding by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ServeGroupSqlProvider.class, method = "findById")
    ServeGroupDO findById(@Param("id") Long id);

    /**
     * change the parent id.
     *
     * @param id       Long
     * @param parentId Long
     */
    @Update({
            "UPDATE " + ServeGroupSqlProvider.TABLE_NAME +
                    " SET parent_id = #{parentId} " +
                    " WHERE id = #{id}"
    })
    int changeParentId(@Param("id") Long id, @Param("parentId") Long parentId);

    /**
     * @param name     String
     * @param filterId Long
     * @return int
     */
    @SelectProvider(type = ServeGroupSqlProvider.class, method = "checkHasTheSameName")
    int checkHasTheSameName(@Param("name") String name, @Param("filterId") Long filterId);

    /**
     * @param model       ServeGroupDO
     * @param orderFields List
     * @return List
     */
    @SelectProvider(type = ServeGroupSqlProvider.class, method = "listWithOrder")
    List<ServeGroupDO> listWithOrder(@Param("model") ServeGroupDO model,
                                     @Param("orderFields") List<String> orderFields);

    @Select({
            "SELECT id, parent_id FROM " + ServeGroupSqlProvider.TABLE_NAME + " WHERE " + BaseSqlProvider.FIELD_ARCHIVE + " = 0 "
    })
    List<ServeGroupDO> listAllOfIdAndParentId();
}
