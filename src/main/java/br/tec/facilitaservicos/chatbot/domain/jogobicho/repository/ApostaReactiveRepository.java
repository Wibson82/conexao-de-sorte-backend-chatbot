package br.tec.facilitaservicos.chatbot.domain.jogobicho.repository;

import br.tec.facilitaservicos.chatbot.domain.jogobicho.entities.ApostaR2dbc;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ============================================================================
 * 🎰 REPOSITÓRIO REATIVO DE APOSTAS - JOGO DO BICHO
 * ============================================================================
 * 
 * Repositório reativo para operações com apostas:
 * - Consultas otimizadas para alta performance
 * - Operações não-bloqueantes com Mono/Flux
 * - Queries específicas para regras de negócio
 * - Integração completa com R2DBC
 * 
 * @author Sistema de Migração R2DBC
 * @version 1.0
 * @since 2024
 */
public interface ApostaReactiveRepository extends ReactiveCrudRepository<ApostaR2dbc, Long> {

    /**
     * Busca aposta por código único
     */
    Mono<ApostaR2dbc> findByCodigoUnico(String codigoUnico);

    /**
     * Busca apostas por data de aposta
     */
    Flux<ApostaR2dbc> findByDataAposta(LocalDate dataAposta);

    /**
     * Busca apostas válidas de um usuário
     */
    @Query("SELECT * FROM tb_apostas WHERE usuario = :usuario AND aposta_valida = true ORDER BY data_criacao DESC")
    Flux<ApostaR2dbc> findApostasValidasByUsuario(@Param("usuario") String usuario);

    /**
     * Busca apostas por usuário em um período
     */
    @Query("SELECT * FROM tb_apostas WHERE usuario = :usuario AND data_aposta BETWEEN :dataInicio AND :dataFim ORDER BY data_aposta DESC")
    Flux<ApostaR2dbc> findByUsuarioAndDataApostaBetween(
        @Param("usuario") String usuario,
        @Param("dataInicio") LocalDate dataInicio, 
        @Param("dataFim") LocalDate dataFim
    );

    /**
     * Calcula total apostado por usuário em uma data
     */
    @Query("SELECT COALESCE(SUM(valor_da_aposta), 0) FROM tb_apostas WHERE usuario = :usuario AND data_aposta = :dataAposta AND aposta_valida = true")
    Mono<BigDecimal> calcularTotalApostadoPorUsuarioNaData(
        @Param("usuario") String usuario,
        @Param("dataAposta") LocalDate dataAposta
    );

    /**
     * Conta apostas válidas por horário
     */
    @Query("SELECT COUNT(*) FROM tb_apostas WHERE horario = :horario AND data_aposta = :dataAposta AND aposta_valida = true")
    Mono<Long> contarApostasValidasPorHorario(
        @Param("horario") Integer horario,
        @Param("dataAposta") LocalDate dataAposta
    );

    /**
     * Busca apostas pendentes de validação
     */
    @Query("SELECT * FROM tb_apostas WHERE aposta_valida IS NULL OR aposta_valida = false ORDER BY data_criacao ASC LIMIT :limite")
    Flux<ApostaR2dbc> findApostasPendentesValidacao(@Param("limite") Integer limite);

    /**
     * Busca apostas por número do terminal
     */
    Flux<ApostaR2dbc> findByTerminal(Integer terminal);

    /**
     * Busca apostas por tipo de pagamento
     */
    Flux<ApostaR2dbc> findByTipoPagamento(Integer tipoPagamento);

    /**
     * Verifica se existe aposta com código único
     */
    Mono<Boolean> existsByCodigoUnico(String codigoUnico);

    /**
     * Busca apostas de parceiros
     */
    @Query("SELECT * FROM tb_apostas WHERE parceiro = true AND data_aposta = :dataAposta ORDER BY valor_da_aposta DESC")
    Flux<ApostaR2dbc> findApostasParceiros(@Param("dataAposta") LocalDate dataAposta);

    /**
     * Busca top apostadores por valor
     */
    @Query("""
        SELECT usuario, SUM(valor_da_aposta) as total_apostado 
        FROM tb_apostas 
        WHERE data_aposta BETWEEN :dataInicio AND :dataFim 
        AND aposta_valida = true 
        GROUP BY usuario 
        ORDER BY total_apostado DESC 
        LIMIT :limite
    """)
    Flux<Object[]> findTopApostadoresPorPeriodo(
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim,
        @Param("limite") Integer limite
    );

    /**
     * Remove apostas antigas (para limpeza de dados)
     */
    @Query("DELETE FROM tb_apostas WHERE data_aposta < :dataLimite AND aposta_valida = false")
    Mono<Long> removeApostasInvalidasAntigas(@Param("dataLimite") LocalDate dataLimite);

    /**
     * Busca estatísticas de apostas por período
     */
    @Query("""
        SELECT 
            COUNT(*) as total_apostas,
            SUM(valor_da_aposta) as valor_total,
            AVG(valor_da_aposta) as valor_medio,
            MIN(valor_da_aposta) as menor_aposta,
            MAX(valor_da_aposta) as maior_aposta
        FROM tb_apostas 
        WHERE data_aposta BETWEEN :dataInicio AND :dataFim 
        AND aposta_valida = true
    """)
    Mono<Object[]> obterEstatisticasPorPeriodo(
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );

    /**
     * Busca apostas suspeitas (mesmo valor, mesmo horário, mesmo usuário)
     */
    @Query("""
        SELECT a1.* FROM tb_apostas a1
        INNER JOIN (
            SELECT usuario, valor_da_aposta, horario, data_aposta, COUNT(*) as quantidade
            FROM tb_apostas 
            WHERE data_aposta = :dataAposta
            GROUP BY usuario, valor_da_aposta, horario, data_aposta
            HAVING COUNT(*) > :limiteRepeticoes
        ) a2 ON a1.usuario = a2.usuario 
        AND a1.valor_da_aposta = a2.valor_da_aposta 
        AND a1.horario = a2.horario
        AND a1.data_aposta = a2.data_aposta
        ORDER BY a1.data_criacao DESC
    """)
    Flux<ApostaR2dbc> findApostasSuspeitasPorRepeticao(
        @Param("dataAposta") LocalDate dataAposta,
        @Param("limiteRepeticoes") Integer limiteRepeticoes
    );
}