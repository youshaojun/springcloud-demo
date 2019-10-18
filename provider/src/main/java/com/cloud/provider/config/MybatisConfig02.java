package com.cloud.provider.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

import static constants.Constants.*;

@Configuration
@MapperScan(basePackages = MAPPERSCAN_BASEPACKAGES_02,
        sqlSessionTemplateRef = "sqlSessionTemplateRef02")
public class MybatisConfig02 {

    @Autowired
    @Qualifier(value = "dataSource02")
    DataSource dataSource02;

    @Bean(name = "sqlSessionFactory02")
    public SqlSessionFactory sqlSessionFactory02() throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(dataSource02);
        fb.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATIONS_01));
        return fb.getObject();
    }

    @Bean(name = "sqlSessionTemplateRef02")
    public SqlSessionTemplate sqlSessionTemplateSecondary(@Qualifier("sqlSessionFactory02") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
