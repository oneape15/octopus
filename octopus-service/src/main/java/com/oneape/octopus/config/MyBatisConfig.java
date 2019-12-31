package com.oneape.octopus.config;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configurable
@MapperScan(basePackages = "com.oneape.octopus.mapper.**")
public class MyBatisConfig {

    public static final String MYBATIS_CONFIG = "classpath:META-INF/mybatis/mybatis-config.xml";

    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        log.info("init sqlSessionFactory...");

        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactory.setConfigLocation(resolver.getResource(MYBATIS_CONFIG));
        return sqlSessionFactory.getObject();
    }

    @Bean
    public PageHelper pageHelper() {
        Properties properties = new Properties();
        //offset作为PageNum使用
        properties.setProperty("offsetAsPageNum", "true");
        //RowBounds方式是否做count查询
        properties.setProperty("rowBoundsWithCount", "true");
        //分页合理化，true开启，如果分页参数不合理会自动修正。默认false不启用
        properties.setProperty("reasonable", "true");
        //配置mysql数据库的方言
        properties.setProperty("dialect", "mysql");
        properties.setProperty("returnPageInfo", "false");
        //是否支持接口参数来传递分页参数，默认false
        properties.setProperty("supportMethodsArguments", "false");
        //当设置为true的时候，如果pagesize设置为0（或RowBounds的limit=0），就不执行分页
        properties.setProperty("pageSizeZero", "true");
        properties.setProperty("reasonable", "false");
        //defaultCount，这是一个全局生效的参数，多数据源时也是统一的行为, 默认为true
        properties.setProperty("defaultCount", "true");

        PageHelper pageHelper = new PageHelper();
        pageHelper.setProperties(properties);

        return pageHelper;
    }

}
