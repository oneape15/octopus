package com.oneape.octopus.mapper;

import com.oneape.octopus.mapper.provider.RoleSqlProvider;
import com.oneape.octopus.model.DO.RoleDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = RoleSqlProvider.class, method = "insert")
    int insert(RoleDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = RoleSqlProvider.class, method = "updateById")
    int update(RoleDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = RoleSqlProvider.class, method = "deleteById")
    int delete(RoleDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = RoleSqlProvider.class, method = "findById")
    RoleDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = RoleSqlProvider.class, method = "list")
    List<RoleDO> list(@Param("model") RoleDO model);
}
