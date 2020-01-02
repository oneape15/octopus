package com.oneape.octopus.service;

import com.oneape.octopus.model.VO.DatasourceVO;

import java.util.List;

public interface DatasourceService {
    /**
     * 添加数据源
     *
     * @param datasource DatasourceVO
     * @return int  1- 成功; 0 - 失败
     */
    int addDatasource(DatasourceVO datasource);

    /**
     * 修改数据源
     *
     * @param datasource DatasourceVO
     * @return int  1- 成功; 0 - 失败
     */
    int editDatasource(DatasourceVO datasource);

    /**
     * 删除数据源
     *
     * @param datasource DatasourceVO
     * @return int  1- 成功; 0 - 失败
     */
    int delDatasource(DatasourceVO datasource);

    /**
     * 根据对象进行查询
     *
     * @param datasource DatasourceVO
     * @return int  1- 成功; 0 - 失败
     */
    List<DatasourceVO> find(DatasourceVO datasource);
}
