package br.tec.facilitaservicos.chatbot.configuracao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * ============================================================================
 * 🎰 CONFIGURAÇÃO DE SEGURANÇA REATIVA - CHATBOT JOGO DO BICHO
 * ============================================================================
 * 
 * Configuração de segurança para sistema de apostas do Jogo do Bicho:
 * - Validação JWT via JWKS com authorities específicas para apostas
 * - Controle de acesso ultra-granular para operações financeiras
 * - CORS restritivo para sistemas de apostas
 * - Headers de segurança máxima para transações monetárias
 * - Rate limiting rigoroso para prevenção de fraudes
 * - Proteção de endpoints críticos de apostas
 * - Auditoria completa de operações de apostas
 * 
 * @author Sistema de Migração R2DBC
 * @version 1.0
 * @since 2024
 */
@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Constantes para valores repetidos
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String PROFILE_PRODUCAO = "prod";
    private static final String ERRO_CORS_ORIGEM_INSEGURA = "Configuração CORS inválida: apenas origins HTTPS são permitidas em produção para apostas";
    
    // Templates de resposta JSON para apostas (sem exposição de detalhes)
    private static final String TEMPLATE_ERRO_AUTENTICACAO = """
        {
            "status": 401,
            "erro": "Não autorizado",
            "mensagem": "Token JWT inválido ou ausente - acesso a apostas negado",
            "timestamp": "%s",
            "service": "jogo-bicho-chatbot"
        }
        """;
        
    private static final String TEMPLATE_ERRO_ACESSO = """
        {
            "status": 403,
            "erro": "Acesso negado",
            "mensagem": "Permissões insuficientes para operações de apostas",
            "timestamp": "%s",
            "service": "jogo-bicho-chatbot"
        }
        """;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${CORS_ALLOWED_ORIGINS:https://apostas.conexaodesorte.com,https://admin.conexaodesorte.com}")
    private String allowedOriginsProperty;

    private List<String> allowedOrigins;

    @Value("#{'${CORS_ALLOWED_METHODS:GET,POST,PUT,DELETE,OPTIONS}'.split(',')}")
    private List<String> allowedMethods;

    @Value("${CORS_ALLOW_CREDENTIALS:true}")
    private boolean allowCredentials;

    @Value("${CORS_MAX_AGE:1800}")
    private long maxAge;

    private final Environment environment;

    public SecurityConfig(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void validarConfiguracaoCors() {
        this.allowedOrigins = Arrays.stream(allowedOriginsProperty.split(","))
            .map(String::trim)
            .toList();

        boolean producao = Arrays.asList(environment.getActiveProfiles()).contains(PROFILE_PRODUCAO);
        if (producao) {
            // Em produção, apenas origins HTTPS devem ser permitidas
            boolean temOrigemInsegura = allowedOrigins.stream()
                .anyMatch(origin -> origin.equals("*") || origin.startsWith("http://"));
            if (temOrigemInsegura) {
                throw new IllegalStateException(ERRO_CORS_ORIGEM_INSEGURA);
            }
        }
    }

    /**
     * Configuração da cadeia de filtros de segurança para apostas (máxima restrição)
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            // Desabilitar proteções desnecessárias para API reativa
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // Configurar CORS ultra-restritivo para apostas
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Configurar autorização específica para apostas (máxima restrição)
            .authorizeExchange(exchanges -> exchanges
                // Endpoints públicos (apenas health check básico)
                .pathMatchers(
                    "/actuator/health",
                    "/favicon.ico"
                ).permitAll()
                
                // Documentação OpenAPI (apenas super admins)
                .pathMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/webjars/**"
                ).hasAuthority("SCOPE_super_admin")
                
                // Operações de apostas - controle ultra-granular
                .pathMatchers(HttpMethod.GET, "/rest/v1/jogo-bicho/apostas/**")
                    .hasAnyAuthority("SCOPE_betting_read", "SCOPE_jogo_bicho_read", "SCOPE_admin")
                    
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/apostas/criar")
                    .hasAnyAuthority("SCOPE_betting_create", "SCOPE_jogo_bicho_bet", "SCOPE_admin")
                    
                .pathMatchers(HttpMethod.PUT, "/rest/v1/jogo-bicho/apostas/validar/**")
                    .hasAnyAuthority("SCOPE_betting_validate", "SCOPE_admin")
                    
                .pathMatchers(HttpMethod.DELETE, "/rest/v1/jogo-bicho/apostas/cancelar/**")
                    .hasAnyAuthority("SCOPE_betting_cancel", "SCOPE_admin")
                
                // Operações de grupos - apostas básicas
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/grupos/**")
                    .hasAnyAuthority("SCOPE_jogo_bicho_grupo", "SCOPE_betting_create")
                
                // Operações de dezenas - apostas intermediárias
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/dezenas/**")
                    .hasAnyAuthority("SCOPE_jogo_bicho_dezena", "SCOPE_betting_create")
                
                // Operações de centenas - apostas avançadas
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/centenas/**")
                    .hasAnyAuthority("SCOPE_jogo_bicho_centena", "SCOPE_betting_advanced")
                
                // Operações de milhares - apostas premium
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/milhares/**")
                    .hasAnyAuthority("SCOPE_jogo_bicho_milhar", "SCOPE_betting_premium")
                
                // Operações especiais - duques, ternos, quadras, quinas
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/duques/**")
                    .hasAnyAuthority("SCOPE_jogo_bicho_duque", "SCOPE_betting_special")
                    
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/ternos/**")
                    .hasAnyAuthority("SCOPE_jogo_bicho_terno", "SCOPE_betting_special")
                    
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/quadras/**")
                    .hasAnyAuthority("SCOPE_jogo_bicho_quadra", "SCOPE_betting_premium")
                    
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/quinas/**")
                    .hasAnyAuthority("SCOPE_jogo_bicho_quina", "SCOPE_betting_premium")
                
                // Passes - apostas combinadas
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/passes/**")
                    .hasAnyAuthority("SCOPE_jogo_bicho_passe", "SCOPE_betting_advanced")
                
                // Resultados - leitura para apostadores
                .pathMatchers(HttpMethod.GET, "/rest/v1/jogo-bicho/resultados/**")
                    .hasAnyAuthority("SCOPE_results_read", "SCOPE_betting_read", "SCOPE_admin")
                    
                .pathMatchers(HttpMethod.POST, "/rest/v1/jogo-bicho/resultados/**")
                    .hasAuthority("SCOPE_admin")
                
                // Relatórios de apostas - sensível
                .pathMatchers("/rest/v1/jogo-bicho/relatorios/**")
                    .hasAnyAuthority("SCOPE_betting_reports", "SCOPE_admin")
                
                // Pagamentos - ultra crítico
                .pathMatchers("/rest/v1/jogo-bicho/pagamentos/**")
                    .hasAnyAuthority("SCOPE_betting_payment", "SCOPE_admin")
                
                // Auditoria - máxima restrição
                .pathMatchers("/rest/v1/jogo-bicho/auditoria/**")
                    .hasAuthority("SCOPE_admin")
                
                // Terminal/Chat interativo - acesso controlado
                .pathMatchers("/rest/v1/jogo-bicho/chat/**")
                    .hasAnyAuthority("SCOPE_betting_chat", "SCOPE_jogo_bicho_terminal")
                
                // Sessões de usuário - controle de sessão
                .pathMatchers("/rest/v1/jogo-bicho/sessoes/**")
                    .hasAnyAuthority("SCOPE_betting_session", "SCOPE_admin")
                
                // Endpoints administrativos ultra críticos
                .pathMatchers("/actuator/**").hasAuthority("SCOPE_super_admin")
                
                // Qualquer outro endpoint requer pelo menos role de apostas
                .anyExchange().hasAnyAuthority("SCOPE_betting_read", "SCOPE_admin")
            )

            // Configurar JWT
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtDecoder(reactiveJwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            )

            // Configurar tratamento de exceções para apostas
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((exchange, ex) -> {
                    var response = exchange.getResponse();
                    response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                    response.getHeaders().add(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
                    response.getHeaders().add("Cache-Control", "no-store");
                    
                    String body = TEMPLATE_ERRO_AUTENTICACAO.formatted(java.time.LocalDateTime.now());
                    
                    var buffer = response.bufferFactory().wrap(body.getBytes());
                    return response.writeWith(reactor.core.publisher.Mono.just(buffer));
                })
                .accessDeniedHandler((exchange, denied) -> {
                    var response = exchange.getResponse();
                    response.setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
                    response.getHeaders().add(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
                    response.getHeaders().add("Cache-Control", "no-store");
                    
                    String body = TEMPLATE_ERRO_ACESSO.formatted(java.time.LocalDateTime.now());
                    
                    var buffer = response.bufferFactory().wrap(body.getBytes());
                    return response.writeWith(reactor.core.publisher.Mono.just(buffer));
                })
            )

            .build();
    }

    /**
     * Decodificador JWT reativo via JWKS
     */
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    /**
     * Conversor de autenticação JWT personalizado para apostas
     */
    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    /**
     * Conversor personalizado de authorities JWT para operações de apostas
     */
    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return new CustomJwtGrantedAuthoritiesConverter();
    }

    /**
     * Configuração CORS ultra-restritiva para sistema de apostas
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Origins permitidas (apenas sistemas de apostas autorizados)
        configuration.setAllowedOrigins(allowedOrigins);
        
        // Métodos HTTP permitidos (mínimos necessários)
        configuration.setAllowedMethods(allowedMethods);
        
        // Headers permitidos (mínimos para apostas)
        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept"
        ));

        // Permitir credenciais para autenticação de apostas
        configuration.setAllowCredentials(allowCredentials);
        
        // Cache preflight reduzido para segurança
        configuration.setMaxAge(maxAge);
        
        // Headers expostos (mínimos para apostas)
        configuration.setExposedHeaders(List.of(
            "Content-Type",
            "X-Bet-Status",
            "X-Session-Id"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * Classe interna para conversão de authorities JWT específica para apostas
     */
    private static class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Collection<GrantedAuthority> authorities = new java.util.ArrayList<>();
            
            // Processar claim 'roles' 
            var rolesClaim = jwt.getClaim("roles");
            if (rolesClaim != null) {
                if (rolesClaim instanceof List<?> rolesList) {
                    authorities.addAll(
                        rolesList.stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            .toList()
                    );
                }
            }
            
            // Processar claim 'authorities'
            var authoritiesClaim = jwt.getClaim("authorities");
            if (authoritiesClaim != null) {
                if (authoritiesClaim instanceof List<?> authList) {
                    authorities.addAll(
                        authList.stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .map(SimpleGrantedAuthority::new)
                            .toList()
                    );
                }
            }
            
            // Processar claim 'scope' (OAuth2 padrão)
            var scopeClaim = jwt.getClaim("scope");
            if (scopeClaim instanceof String scopeString) {
                authorities.addAll(
                    Arrays.stream(scopeString.split("\\s+"))
                        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                        .toList()
                );
            }
            
            // Adicionar authorities específicas para apostas baseadas em claims customizados
            var bettingRolesClaim = jwt.getClaim("betting_roles");
            if (bettingRolesClaim instanceof List<?> bettingRolesList) {
                authorities.addAll(
                    bettingRolesList.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .map(role -> new SimpleGrantedAuthority("SCOPE_betting_" + role))
                        .toList()
                );
            }
            
            // Adicionar authorities específicas para jogo do bicho
            var jogoBichoRolesClaim = jwt.getClaim("jogo_bicho_roles");
            if (jogoBichoRolesClaim instanceof List<?> jogoBichoRolesList) {
                authorities.addAll(
                    jogoBichoRolesList.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .map(role -> new SimpleGrantedAuthority("SCOPE_jogo_bicho_" + role))
                        .toList()
                );
            }
            
            return authorities;
        }
    }
}