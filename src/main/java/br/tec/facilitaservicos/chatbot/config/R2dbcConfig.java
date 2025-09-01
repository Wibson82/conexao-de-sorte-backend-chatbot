
package br.tec.facilitaservicos.chatbot.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.r2dbc.ConnectionPoolMetrics;
import java.util.Collections;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

import java.time.Duration;

@Configuration
@EnableR2dbcRepositories(basePackages = "br.tec.facilitaservicos.chatbot.repository")
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.url}")
    private String url;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    @Value("${spring.r2dbc.pool.initial-size:10}")
    private int initialSize;

    @Value("${spring.r2dbc.pool.max-size:20}")
    private int maxSize;

    @Value("${spring.r2dbc.pool.max-idle-time:30m}")
    private Duration maxIdleTime;

    @Value("${spring.r2dbc.pool.validation-query:SELECT 1}")
    private String validationQuery;

    @Override
    @Bean
    public ConnectionFactory connectionFactory(MeterRegistry meterRegistry) {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(url)
                .mutate()
                .option(ConnectionFactoryOptions.USER, username)
                .option(ConnectionFactoryOptions.PASSWORD, password)
                .build();

        ConnectionFactory connectionFactory = ConnectionFactories.get(options);

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder(connectionFactory)
                .initialSize(initialSize)
                .maxSize(maxSize)
                .maxIdleTime(maxIdleTime)
                .validationQuery(validationQuery)
                .build();

        ConnectionPool connectionPool = new ConnectionPool(poolConfiguration);
        new ConnectionPoolMetrics(connectionPool, "r2dbc.pool", Collections.emptyList()).bindTo(meterRegistry);
        return connectionPool;
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
