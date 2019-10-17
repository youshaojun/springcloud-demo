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

@Configuration
@MapperScan(basePackages = "com.cloud.provider.dao.dao01",
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
        fb.setTypeAliasesPackage("com.cloud.provider.entity");
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper01/*.xml"));
        return fb.getObject();
    }

    @Bean(name = "sqlSessionTemplateRef01")
    @Primary
    public SqlSessionTemplate sqlSessionTemplateSecondary(@Qualifier("sqlSessionFactory01") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
