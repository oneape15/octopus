package com.oneape.octopus.datasource;

import com.alibaba.fastjson.JSON;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.datasource.data.Value;
import com.oneape.octopus.datasource.schema.FieldInfo;
import com.oneape.octopus.datasource.schema.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void queryTest() {
        String rawSql = "SELECT   " +
                " TABLE_SCHEMA schema_name,   " +
                " TABLE_NAME table_name,   " +
                " COLUMN_NAME column_name,   " +
                " COLUMN_DEFAULT default_val, " +
                " IS_NULLABLE,  " +
                " DATA_TYPE data_type,   " +
                " CHARACTER_MAXIMUM_LENGTH max_len,   " +
                " COLUMN_COMMENT " +
                "FROM   " +
                " information_schema.`COLUMNS`    " +
                "WHERE   " +
                " TABLE_SCHEMA = ? " +
                " AND TABLE_NAME = ?  " +
                " AND  1 = ? " +
                "ORDER BY   " +
                " TABLE_SCHEMA,   " +
                " TABLE_NAME,   " +
                " ORDINAL_POSITION   ";
        ExecParam execParam = new ExecParam();
        execParam.setNeedTotalSize(true);
        execParam.setRawSql(rawSql);
        List<Value> values = new ArrayList<>();
        values.add(new Value("octopus", DataType.VARCHAR));
        values.add(new Value("r_datasource", DataType.VARCHAR));
        values.add(new Value(1, DataType.INTEGER));
        execParam.setParams(values);
        execParam.setPageIndex(1);
        execParam.setPageSize(5);

        Result result = queryFactory.execSql(dsi, execParam);

        log.info("查询结果： {}", JSON.toJSONString(result));
    }
}
