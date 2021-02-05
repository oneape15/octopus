package com.oneape.octopus.admin.config;

import com.github.pagehelper.PageHelper;
import com.oneape.octopus.admin.interceptor.MyBatisFillValueInterceptor;
import com.oneape.octopus.admin.interceptor.MyBatisLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@org.springframework.context.annotation.Configuration
@MapperScan(basePackages = "com.oneape.octopus.mapper.**")
public class MyBatisConfig {

    @Resource
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        log.info("init sqlSessionFactory...");

        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        // setting the session plugins.
        sqlSessionFactory.setPlugins(
                new MyBatisLogInterceptor(),
                new MyBatisFillValueInterceptor()
        );

        Configuration configuration = new Configuration();
        // Use JDBC's getGeneratedKeys to get the database auto-augment primary key value.
        configuration.setUseGeneratedKeys(true);
        // Replace column names with column aliases, which default to: true.
        configuration.setUseColumnLabel(true);
        // Enable hump naming conversion: Table{create_time} -> Entity{createTime}
        configuration.setMapUnderscoreToCamelCase(true);

        sqlSessionFactory.setConfiguration(configuration);

        return sqlSessionFactory.getObject();
    }

    @Bean
    public PageHelper pageHelper() {
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("dialect", "mysql");
        properties.setProperty("returnPageInfo", "false");
        properties.setProperty("supportMethodsArguments", "false");
        properties.setProperty("pageSizeZero", "true");
        properties.setProperty("reasonable", "false");
        properties.setProperty("defaultCount", "true");

        PageHelper pageHelper = new PageHelper();
        pageHelper.setProperties(properties);

        return pageHelper;
    }

}
