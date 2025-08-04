package ru.yandex.practicum.blog.config;

import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

@Configuration
public class DataSourceConfiguration {


    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    @Bean
    public DataSource dataSource(
            // Настройки соединения возьмём из Environment
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * EventListener который по очереди выгружает все скрипты при старте приложения.
     * Важно, скрипты должны быть в алфавитном порядке для успешной проливки
     */
    @EventListener
    public void populate(ContextRefreshedEvent event) throws IOException {
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        Resource[] resources = resourcePatternResolver.getResources("classpath:sql/**/*.sql");

        Arrays.sort(resources, Comparator.comparing(Resource::getFilename));

        for (Resource resource : resources) {
            populator.addScript(resource);
        }
        populator.execute(dataSource);
    }
}