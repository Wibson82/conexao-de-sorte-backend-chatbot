package br.tec.facilitaservicos.chatbot.aplicacao.servico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
public class CacheWarmingService {

    private static final Logger log = LoggerFactory.getLogger(CacheWarmingService.class);

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    public CacheWarmingService(ReactiveRedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void warmUpCache() {
        log.info("🔥 Iniciando aquecimento do cache do chatbot...");

        // Exemplo de aquecimento: carregar respostas pré-definidas
        List<String> respostas = Arrays.asList(
                "Olá! Como posso ajudar hoje?",
                "Para apostar, digite 'apostar' e siga as instruções.",
                "Os resultados são atualizados diariamente às 14h e 21h.",
                "Qual bicho você gostaria de apostar?"
        );

        Mono<Boolean> warmingMono = redisTemplate.opsForList()
                .rightPushAll("chatbot:respostas:pre-definidas", respostas.toArray())
                .flatMap(count -> {
                    log.info("Aquecendo cache com {} respostas pré-definidas.", count);
                    return redisTemplate.expire("chatbot:respostas:pre-definidas", Duration.ofHours(1));
                });

        warmingMono
                .timeout(Duration.ofSeconds(10)) // Timeout para o processo de aquecimento
                .doOnError(e -> log.error("Erro durante o aquecimento do cache do chatbot: {}", e.getMessage(), e))
                .doOnSuccess(v -> log.info("✅ Aquecimento do cache do chatbot concluído."))
                .subscribe();
    }
}
