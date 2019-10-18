package com.cloud.provider.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import static constants.Constants.*;
@Configuration
@MapperScan(basePackages = MAPPERSCAN_BASEPACKAGES_01,
        sqlSessionTemplateRef = "sqlSessionTemplateRef01")
public class MybatisConfig01 {

    @Autowired
    @Qualifier(value = "dataSource01")
    DataSource dataSource01;

    @Bean(name = "sqlSessionFactory01")
    @Primary
    public SqlSessionFactory sqlSessionFactory01() throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(dataSource01);
        fb.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATIONS_01));
        return fb.getObject();
    }

    @Bean(name = "sqlSessionTemplateRef01")
    @Primary
    public SqlSessionTemplate sqlSessionTemplateSecondary(@Qualifier("sqlSessionFactory01") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
