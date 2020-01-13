package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.ModelSqlProvider;
import com.oneape.octopus.model.DO.peekdata.ModelDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ModelMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ModelSqlProvider.class, method = "insert")
    int insert(ModelDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ModelSqlProvider.class, method = "updateById")
    int update(ModelDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ModelSqlProvider.class, method = "deleteById")
    int delete(ModelDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ModelSqlProvider.class, method = "findById")
    ModelDO findById(@Param("id") Long id);


    @SelectProvider(type = ModelSqlProvider.class, method = "listOrLink")
    List<ModelDO> listOrLink(@Param("model") ModelDO model);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ModelSqlProvider.class, method = "list")
    List<ModelDO> list(@Param("model") ModelDO model);
}
