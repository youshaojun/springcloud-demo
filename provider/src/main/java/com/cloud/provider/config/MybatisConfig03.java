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
@MapperScan(basePackages = MAPPERSCAN_BASEPACKAGES_03,
        sqlSessionTemplateRef = "sqlSessionTemplateRef03")
public class MybatisConfig03 {

    @Autowired
    @Qualifier(value = "dataSource03")
    DataSource dataSource03;

    @Bean(name = "sqlSessionFactory03")
    public SqlSessionFactory sqlSessionFactory03() throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(dataSource03);
        fb.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATIONS_01));
        return fb.getObject();
    }

    @Bean(name = "sqlSessionTemplateRef03")
    public SqlSessionTemplate sqlSessionTemplateSecondary(@Qualifier("sqlSessionFactory03") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
