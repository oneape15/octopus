package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.PeekSqlProvider;
import com.oneape.octopus.model.domain.peekdata.PeekDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PeekMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = PeekSqlProvider.class, method = "insert")
    int insert(PeekDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = PeekSqlProvider.class, method = "updateById")
    int update(PeekDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = PeekSqlProvider.class, method = "deleteById")
    int delete(PeekDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = PeekSqlProvider.class, method = "findById")
    PeekDO findById(@Param("id") Long id);


    @SelectProvider(type = PeekSqlProvider.class, method = "listOrLink")
    List<PeekDO> listOrLink(@Param("model") PeekDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = PeekSqlProvider.class, method = "list")
    List<PeekDO> list(@Param("model") PeekDO model);

    /**
     * 取数增加一次
     *
     * @param peekId Long
     * @return int
     */
    @UpdateProvider(type = PeekSqlProvider.class, method = "incPeekTime")
    int incPeekTime(@Param("peekId") Long peekId);
}
