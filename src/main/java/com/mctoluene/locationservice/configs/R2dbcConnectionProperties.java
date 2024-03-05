package com.mctoluene.locationservice.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("spring.r2dbc")
@Configuration
@Data
public class R2dbcConnectionProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private int maxSize;
}
