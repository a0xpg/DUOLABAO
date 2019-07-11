package com.hualong.duolabao.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = ClusterDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "clusterSqlSessionFactory")
public class ClusterDataSourceConfig {

    // 精确到 cluster 目录，以便跟其他数据源隔离  com.hlyf.gyl.dao.cluster
    static final String PACKAGE = "com.hualong.duolabao.dao.cluster";
    static final String MAPPER_LOCATION = "classpath:mapper/dlbmapper/*.xml";
    //SQL语句输出
    static final String SQL_PRINT = "classpath:mybatis-config.xml";

    @Value("${cluster.datasource.url}")
    private String url;

    @Value("${cluster.datasource.username}")
    private String user;

    @Value("${cluster.datasource.password}")
    private String password;

    @Value("${cluster.datasource.driver-class-name}")
    private String driverClass;



    @Value("${cluster.druid.initialSize}")
    private int initialSize;
    @Value("${cluster.druid.initialSize}")
    private int minIdle;
    @Value("${cluster.druid.maxActive}")
    private int maxActive;
    @Value("${cluster.druid.initialSize}")
    private int maxWait;
    @Value("${cluster.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${cluster.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${cluster.druid.validationQuery}")
    private String validationQuery;
    @Value("${cluster.druid.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${cluster.druid.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${cluster.druid.testOnReturn}")
    private boolean testOnReturn;
    @Value("${cluster.druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${cluster.druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${cluster.druid.filters}")
    private String filters;
    @Value("${cluster.druid.connectionProperties}")
    private String connectionProperties;


    @Bean(name = "clusterDataSource")
    public DataSource clusterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);


        //configuration
//        dataSource.setInitialSize(initialSize);
//        dataSource.setMinIdle(minIdle);
//        dataSource.setMaxActive(maxActive);
//        dataSource.setMaxWait(maxWait);
//        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//        dataSource.setValidationQuery(validationQuery);
//        dataSource.setTestWhileIdle(testWhileIdle);
//        dataSource.setTestOnBorrow(testOnBorrow);
//        dataSource.setTestOnReturn(testOnReturn);
//        //连接超时回收
//        dataSource.setRemoveAbandoned(true);
//        dataSource.setRemoveAbandonedTimeoutMillis(7200l);
//        dataSource.setLogAbandoned(true);
//
//        dataSource.setPoolPreparedStatements(poolPreparedStatements);
//        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
//        try {
//            dataSource.setFilters(filters);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        return dataSource;
    }

    @Bean(name = "clusterTransactionManager")
    public DataSourceTransactionManager clusterTransactionManager() {
        return new DataSourceTransactionManager(clusterDataSource());
    }

    @Bean(name = "clusterSqlSessionFactory")
    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("clusterDataSource") DataSource clusterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(clusterDataSource);
        sessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(ClusterDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}