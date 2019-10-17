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
    private static final String URL = "url";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRIVER_CLASSNAME = "driverClassName";
    private static final String PREFIX_01 = "spring.datasource.test01.";
    private static final String PREFIX_02 = "spring.datasource.test02.";
    private static final String PREFIX_03 = "spring.datasource.test03.";
    private static final String RESOURCE_NAME_01 = "test01";
    private static final String RESOURCE_NAME_02 = "test02";
    private static final String RESOURCE_NAME_03 = "test03";
    private static final String XA_DATASOURCE_CLASSNAME = "com.alibaba.druid.pool.xa.DruidXADataSource";

    @Bean(name = "dataSource01")
    public DataSource dataSource01() throws Exception {
        return setAtomikosParams(RESOURCE_NAME_01, PREFIX_01);
    }

    @Bean(name = "dataSource02")
    public DataSource dataSource02() throws Exception {
        return setAtomikosParams(RESOURCE_NAME_02, PREFIX_02);
    }

    @Bean(name = "dataSource03")
    public DataSource dataSource03() throws Exception {
        return setAtomikosParams(RESOURCE_NAME_03, PREFIX_03);
    }

    public DataSource setAtomikosParams(String resourceName, String prefix) {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setXaDataSourceClassName(XA_DATASOURCE_CLASSNAME);
        ds.setUniqueResourceName(resourceName);
        ds.setPoolSize(5);
        ds.setXaProperties(build(prefix));
        return ds;
    }

    private Properties build(String prefix) {
        Properties prop = new Properties();
        prop.put(URL, env.getProperty(prefix + URL));
        prop.put(USERNAME, env.getProperty(prefix + USERNAME));
        prop.put(PASSWORD, env.getProperty(prefix + PASSWORD));
        prop.put(DRIVER_CLASSNAME, env.getProperty(prefix + DRIVER_CLASSNAME));
        return prop;
    }
}
