package vn.hhh.phong_tro.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();

        // Chạy repair để sửa metadata
//        flyway.repair();

        // Sau đó mới chạy migrate
        flyway.migrate();

        return flyway;
    }
}