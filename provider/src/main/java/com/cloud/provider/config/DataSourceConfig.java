package com.cloud.provider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;
import static constants.Constants.*;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment env;


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
        ds.setPoolSize(POOL_SIZE);
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
