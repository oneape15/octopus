package com.oneape.octopus.mapper;

import com.oneape.octopus.mapper.provider.UserSqlProvider;
import com.oneape.octopus.model.DO.UserDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = UserSqlProvider.class, method = "insert")
    int insert(UserDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = UserSqlProvider.class, method = "updateById")
    int update(UserDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = UserSqlProvider.class, method = "deleteById")
    int delete(UserDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = UserSqlProvider.class, method = "findById")
    UserDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = UserSqlProvider.class, method = "list")
    List<UserDO> list(@Param("model") UserDO model);

    /**
     * 根据登录名查询用户信息
     *
     * @param username String
     * @return UserDO
     */
    @Select({
            "SELECT * FROM " + UserSqlProvider.TABLE_NAME + " WHERE archive = 0 AND username= #{username}"
    })
    UserDO getByUsername(@Param("username") String username);
}
