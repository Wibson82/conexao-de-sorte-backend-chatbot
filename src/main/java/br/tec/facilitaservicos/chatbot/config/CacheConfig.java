package br.tec.facilitaservicos.chatbot.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração de cache Redis otimizada para microserviço de chatbot.
 * TTLs ajustados para dados de chatbot:
 * - Sessões: 30 minutos (sessões de usuário)
 * - Respostas: 1 hora (respostas pré-definidas)
 * - Contextos: 15 minutos (contexto da conversa)
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.application.name:conexao-de-sorte-chatbot}")
    private String applicationName;

    // Cache names específicos do domínio do chatbot
    public static final String SESSOES_CHAT_CACHE = "chatbot:sessoes";
    public static final String RESPOSTAS_PRE_DEFINIDAS_CACHE = "chatbot:respostas";
    public static final String CONTEXTOS_CONVERSA_CACHE = "chatbot:contextos";
    public static final String USUARIOS_CHAT_CACHE = "chatbot:usuarios";

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, MeterRegistry meterRegistry) {
        // Configuração base com serialização otimizada
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // TTL padrão para chatbot
                .computePrefixWith(cacheName -> applicationName + ":" + cacheName + ":")
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // TTLs diferenciados por criticidade e padrão de acesso
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Sessões de chat - TTL moderado
        cacheConfigurations.put(SESSOES_CHAT_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // Respostas pré-definidas - TTL mais longo
        cacheConfigurations.put(RESPOSTAS_PRE_DEFINIDAS_CACHE, defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // Contextos de conversa - TTL mais curto
        cacheConfigurations.put(CONTEXTOS_CONVERSA_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(15)));

        // Usuários do chat - TTL moderado
        cacheConfigurations.put(USUARIOS_CHAT_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(45)));

        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();

        // Métricas de cache são automaticamente registradas pelo Spring Boot Actuator
        return cacheManager;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Serialização otimizada para performance
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        
        return template;
    }
}
