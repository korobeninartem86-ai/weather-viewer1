package com.weather.weatherviewer.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {
    @Bean
    public LocalSessionFactoryBean sessionFactoryBean(DataSource dataSource) {

        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.weather.weatherviewer.entity");

        Properties properties = new Properties();

        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        factoryBean.setHibernateProperties(properties);

        return factoryBean;
    }
    @Bean
    public SessionFactory sessionFactory(LocalSessionFactoryBean factoryBean){
        return factoryBean.getObject();
    }
}
