package com.oneape.octopus.datasource;

import com.alibaba.fastjson.JSON;
import com.oneape.octopus.datasource.schema.FieldInfo;
import com.oneape.octopus.datasource.schema.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Slf4j
public class QueryFactoryTest {

    private QueryFactory queryFactory;
    private DatasourceInfo dsi;
    private String schema;

    @Before
    public void init() {
        DatasourceFactory datasourceFactory = new DefaultDatasourceFactory();
        schema = "octopus";
        dsi = new DatasourceInfo();
        dsi.setUsername("root");
        dsi.setPassword("20061253");
        dsi.setUrl("jdbc:mysql://localhost:3306/octopus");
        dsi.setDatasourceType(DatasourceTypeHelper.MySQL);

        queryFactory = new DefaultQueryFactory(datasourceFactory);
    }

    @Test
    public void allDatabaseTest() {
        List<String> databaseNames = queryFactory.allDatabase(dsi);
        Assert.assertNotNull("数据库列表为空", databaseNames);
        log.info("数据库如下： {}", JSON.toJSONString(databaseNames));
    }

    @Test
    public void allTablesTest() {
        List<TableInfo> tableInfos = queryFactory.allTables(dsi);

        Assert.assertNotNull(tableInfos);
        Assert.assertTrue(tableInfos.size() > 0);
        log.info("表信息如下： {}", JSON.toJSONString(tableInfos));
    }

    @Test
    public void tablesTest() {
        List<TableInfo> tableInfos = queryFactory.allTables(dsi, schema);

        Assert.assertNotNull(tableInfos);
        Assert.assertTrue(tableInfos.size() > 0);
        log.info("表信息如下： {}", JSON.toJSONString(tableInfos));
    }

    @Test
    public void allFieldsTest() {
        List<FieldInfo> fields = queryFactory.allFields(dsi);
        Assert.assertNotNull(fields);
        log.info("数据库字段信息如下：{}", JSON.toJSONString(fields));
    }

    @Test
    public void tableFieldsTest() {
        List<FieldInfo> fields = queryFactory.fieldOfTable(dsi, schema, "r_datasource");
        Assert.assertNotNull(fields);
        log.info("表：{}字段信息如下：{}", "r_resource", JSON.toJSONString(fields));
    }
}
