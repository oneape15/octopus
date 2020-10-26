package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.ImportRecordSqlProvider;
import com.oneape.octopus.model.domain.peekdata.ImportRecordDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ImportRecordMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ImportRecordSqlProvider.class, method = "insert")
    int insert(ImportRecordDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ImportRecordSqlProvider.class, method = "updateById")
    int update(ImportRecordDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ImportRecordSqlProvider.class, method = "deleteById")
    int delete(ImportRecordDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ImportRecordSqlProvider.class, method = "findById")
    ImportRecordDO findById(@Param("id") Long id);


    @SelectProvider(type = ImportRecordSqlProvider.class, method = "listOrLink")
    List<ImportRecordDO> listOrLink(@Param("model") ImportRecordDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ImportRecordSqlProvider.class, method = "list")
    List<ImportRecordDO> list(@Param("model") ImportRecordDO model);
}
