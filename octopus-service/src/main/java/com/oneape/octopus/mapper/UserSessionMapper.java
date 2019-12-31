package com.oneape.octopus.mapper;

import com.oneape.octopus.mapper.provider.UserSessionSqlProvider;
import com.oneape.octopus.model.DO.UserSessionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserSessionMapper extends BaseMapper<UserSessionDO> {

    @Select({
            "SELECT * FROM " + UserSessionSqlProvider.TABLE_NAME + " WHERE token = #{token}"
    })
    UserSessionDO findByToken(@Param("token") String token);
}
