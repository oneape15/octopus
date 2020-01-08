package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.UserSessionSqlProvider;
import com.oneape.octopus.model.DO.system.UserSessionDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserSessionMapper {

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = UserSessionSqlProvider.class, method = "insert")
    int insert(UserSessionDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = UserSessionSqlProvider.class, method = "updateById")
    int update(UserSessionDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = UserSessionSqlProvider.class, method = "deleteById")
    int delete(UserSessionDO model);

    @Select({
            "SELECT * FROM " + UserSessionSqlProvider.TABLE_NAME + " WHERE token = #{token}"
    })
    UserSessionDO findByToken(@Param("token") String token);
}
