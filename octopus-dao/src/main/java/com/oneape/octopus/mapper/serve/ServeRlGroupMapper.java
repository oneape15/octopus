package com.oneape.octopus.mapper.serve;

import com.oneape.octopus.domain.serve.ServeRlGroupDO;
import com.oneape.octopus.dto.serve.ServeGroupSizeDTO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.serve.provider.ServeRlGroupSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 14:36.
 * Modify:
 */
@Mapper
public interface ServeRlGroupMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ServeRlGroupSqlProvider.class, method = "insert")
    int insert(ServeRlGroupDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ServeRlGroupSqlProvider.class, method = "updateById")
    int update(ServeRlGroupDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ServeRlGroupSqlProvider.class, method = "deleteById")
    int delete(ServeRlGroupDO model);

    /**
     * Gets the number of nodes associated under the grouping
     *
     * @param groupIds List
     * @return Map
     */
    @SelectProvider(type = ServeRlGroupSqlProvider.class, method = "getGroupLinkServeSize")
    List<ServeGroupSizeDTO> getGroupLinkServeSize(@Param("groupIds") List<Long> groupIds);

    /**
     * @param serveId Long
     * @param groupId Long
     * @return int
     */
    @Update({
            "UPDATE " + ServeRlGroupSqlProvider.TABLE_NAME +
                    " SET group_id = #{groupId} " +
                    " WHERE serve_id = #{serveId} AND " + BaseSqlProvider.FIELD_ARCHIVE + " = 0"
    })
    int updateGroupIdByServeId(@Param("serveId") Long serveId, @Param("groupId") Long groupId);

    /**
     * count the group only serve size.
     *
     * @param groupId Long
     * @return int
     */
    @Select({
            "SELECT COUNT(0) FROM " + ServeRlGroupSqlProvider.TABLE_NAME +
                    " WHERE group_id = #{groupId} AND " + BaseSqlProvider.FIELD_ARCHIVE + " = 0"
    })
    int countGroupLinkServeSize(@Param("groupId") Long groupId);
}

