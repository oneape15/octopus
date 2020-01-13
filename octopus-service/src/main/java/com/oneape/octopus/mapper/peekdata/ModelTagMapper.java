package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.ModelTagSqlProvider;
import com.oneape.octopus.model.DO.peekdata.ModelTagDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ModelTagMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ModelTagSqlProvider.class, method = "insert")
    int insert(ModelTagDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ModelTagSqlProvider.class, method = "updateById")
    int update(ModelTagDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ModelTagSqlProvider.class, method = "deleteById")
    int delete(ModelTagDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ModelTagSqlProvider.class, method = "findById")
    ModelTagDO findById(@Param("id") Long id);


    @SelectProvider(type = ModelTagSqlProvider.class, method = "listOrLink")
    List<ModelTagDO> listOrLink(@Param("model") ModelTagDO model);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ModelTagSqlProvider.class, method = "list")
    List<ModelTagDO> list(@Param("model") ModelTagDO model);
}
