package com.cloud.provider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@SuppressWarnings("all")
public class DataSourceConfig {

    @Autowired
    private Environment env;
    private static final String PREFIX_01 = "spring.datasource.test01.";
    private static final String PREFIX_02 = "spring.datasource.test02.";
    private static final String PREFIX_03 = "spring.datasource.test03.";

    @Bean(name = "dataSource01")
    public DataSource dataSource01() throws Exception {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName("test01");
        ds.setPoolSize(5);
        ds.setXaProperties(build(PREFIX_01));
        return ds;
    }

    @Bean(name = "dataSource02")
    public DataSource dataSource02() throws Exception {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName("test02");
        ds.setPoolSize(5);
        ds.setXaProperties(build(PREFIX_02));
        return ds;
    }

    @Bean(name = "dataSource03")
    public DataSource dataSource03() throws Exception {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName("test03");
        ds.setPoolSize(5);
        ds.setXaProperties(build(PREFIX_03));
        return ds;
    }

    private Properties build(String prefix) {
        Properties prop = new Properties();
        prop.put("url", env.getProperty(prefix + "url"));
        prop.put("username", env.getProperty(prefix + "username"));
        prop.put("password", env.getProperty(prefix + "password"));
        prop.put("driverClassName", env.getProperty(prefix + "driverClassName"));
        return prop;
    }
}
