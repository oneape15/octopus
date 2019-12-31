package com.oneape.octopus.mapper;

import com.oneape.octopus.mapper.provider.UserSqlProvider;
import com.oneape.octopus.model.DO.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
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
