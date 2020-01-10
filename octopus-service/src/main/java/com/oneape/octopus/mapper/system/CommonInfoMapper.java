package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.CommonInfoSqlProvider;
import com.oneape.octopus.model.DO.system.CommonInfoDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommonInfoMapper {

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = CommonInfoSqlProvider.class, method = "insert")
    int insert(CommonInfoDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = CommonInfoSqlProvider.class, method = "updateById")
    int update(CommonInfoDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = CommonInfoSqlProvider.class, method = "deleteById")
    int delete(CommonInfoDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = CommonInfoSqlProvider.class, method = "findById")
    CommonInfoDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = CommonInfoSqlProvider.class, method = "list")
    List<CommonInfoDO> list(@Param("model") CommonInfoDO model);

    /**
     * 获取数量
     *
     * @param model T
     * @return int
     */
    @SelectProvider(type = CommonInfoSqlProvider.class, method = "size")
    int size(@Param("model") CommonInfoDO model);

    @Select({
            "SELECT DISTINCT classify" +
                    " FROM " + CommonInfoSqlProvider.TABLE_NAME +
                    " WHERE " + CommonInfoSqlProvider.FIELD_ARCHIVE + " = 0 " +
                    " ORDER BY classify "
    })
    List<String> getAllClassify();

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = CommonInfoSqlProvider.class, method = "listWithOrder")
    List<CommonInfoDO> listWithOrder(@Param("model") CommonInfoDO model, @Param("orderFields") List<String> orderFields);
}
