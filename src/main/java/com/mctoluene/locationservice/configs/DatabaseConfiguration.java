package com.mctoluene.locationservice.configs;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories
@RequiredArgsConstructor
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {

    private final R2dbcConnectionProperties r2dbcConnectionProperties;

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {

        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "pool")
                .option(PROTOCOL, "postgresql")
                .option(HOST, r2dbcConnectionProperties.getHost())
                .option(USER, r2dbcConnectionProperties.getUsername())
                .option(PASSWORD, r2dbcConnectionProperties.getPassword())
                .option(DATABASE, r2dbcConnectionProperties.getDatabase())
                .option(MAX_SIZE, r2dbcConnectionProperties.getMaxSize())
                .option(PORT, r2dbcConnectionProperties.getPort())
                .build());
    }
}
