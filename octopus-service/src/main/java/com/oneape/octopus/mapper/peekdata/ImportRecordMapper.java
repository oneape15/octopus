package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.ImportRecordSqlProvider;
import com.oneape.octopus.model.DO.peekdata.ImportRecordDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ImportRecordMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ImportRecordSqlProvider.class, method = "insert")
    int insert(ImportRecordDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ImportRecordSqlProvider.class, method = "updateById")
    int update(ImportRecordDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ImportRecordSqlProvider.class, method = "deleteById")
    int delete(ImportRecordDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ImportRecordSqlProvider.class, method = "findById")
    ImportRecordDO findById(@Param("id") Long id);


    @SelectProvider(type = ImportRecordSqlProvider.class, method = "listOrLink")
    List<ImportRecordDO> listOrLink(@Param("model") ImportRecordDO model);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ImportRecordSqlProvider.class, method = "list")
    List<ImportRecordDO> list(@Param("model") ImportRecordDO model);
}
