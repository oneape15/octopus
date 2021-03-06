package com.oneape.octopus.mapper.serve;

import com.oneape.octopus.domain.serve.ServeInfoDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.serve.provider.ServeInfoSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * The report Mapper.
 */
@Mapper
public interface ServeInfoMapper {

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
     * Finding by primary key.
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
    @SelectProvider(type = ServeInfoSqlProvider.class, method = "listWithOutTextField")
    List<ServeInfoDO> list(@Param("model") ServeInfoDO model);

    /**
     * Finding the same name.
     *
     * @param name     String
     * @param filterId Long
     * @return int
     */
    @SelectProvider(type = ServeInfoSqlProvider.class, method = "hasSameName")
    int hasSameName(@Param("name") String name, @Param("filterId") Long filterId);

    /**
     * Whether the serveId Id is valid
     *
     * @param serveId Long
     * @return int
     */
    @Select({
            "SELECT COUNT(0) FROM " + ServeInfoSqlProvider.TABLE_NAME +
                    " WHERE " + BaseSqlProvider.FIELD_ARCHIVE + " = 0 AND id = #{serveId}"
    })
    int checkServeId(@Param("serveId") Long serveId);

    /**
     * Count the number of archived serves
     *
     * @param serveType String
     */
    @SelectProvider(type = ServeInfoSqlProvider.class, method = "countArchiveServe")
    int countArchiveServe(@Param("serveType") String serveType);

    /**
     * Count the number of personal serves
     *
     * @param serveType String
     * @param userId    Long
     */
    @SelectProvider(type = ServeInfoSqlProvider.class, method = "countPersonalServe")
    int countPersonalServe(@Param("serveType") String serveType, @Param("userId") Long userId);

    /**
     * Change the owner id of the serve.
     *
     * @param serveId Long
     * @param ownerId Long
     */
    @Update(
            "UPDATE " + ServeInfoSqlProvider.TABLE_NAME +
                    " SET owner_id = #{ownerId} " +
                    " WHERE id = #{serveId}"
    )
    int changeServeOwner(@Param("serveId") Long serveId, @Param("ownerId") Long ownerId);
}
