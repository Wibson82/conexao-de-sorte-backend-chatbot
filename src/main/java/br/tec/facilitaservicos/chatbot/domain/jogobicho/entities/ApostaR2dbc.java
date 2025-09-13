package br.tec.facilitaservicos.chatbot.domain.jogobicho.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ============================================================================
 * 🎰 ENTIDADE APOSTA R2DBC - SISTEMA DE APOSTAS JOGO DO BICHO
 * ============================================================================
 * 
 * Entidade reativa para gestão de apostas do Jogo do Bicho:
 * - Suporte completo a apostas (grupos, dezenas, centenas, milhares)
 * - Auditoria completa de transações
 * - Integração com sistema de pagamentos
 * - Validações de negócio para apostas
 * 
 * @author Sistema de Migração R2DBC
 * @version 1.0
 * @since 2024
 */
@Schema(description = "Entidade que representa uma aposta no Jogo do Bicho.")
@Table("tb_apostas")
public class ApostaR2dbc {
    
    @Id
    @Schema(description = "Identificador único da aposta", example = "1")
    private Long id;
    
    @Schema(description = "Indica se a aposta é válida", example = "true")
    @Column("aposta_valida")
    private Boolean apostaValida;
    
    @Schema(description = "Nome do usuário que realizou a aposta", example = "usuario123")
    @Column("usuario")
    private String usuario;
    
    @Schema(description = "Identificador do cliente", example = "101")
    @Column("cliente")
    private Integer cliente;
    
    @Schema(description = "Código único da aposta", example = "ABC123XYZ")
    @Column("codigo_unico")
    private String codigoUnico;
    
    @Schema(description = "Data e hora de criação da aposta", example = "2025-01-01T10:00:00")
    @Column("data_criacao")
    private LocalDateTime dataCriacao;
    
    @Schema(description = "Data em que a aposta foi feita", example = "2025-01-01")
    @Column("data_aposta")
    private LocalDate dataAposta;
    
    @Schema(description = "Data em que a aposta foi efetivada", example = "2025-01-01")
    @Column("data_efetuada")
    private LocalDate dataEfetuada;
    
    @Schema(description = "Hora em que a aposta foi efetivada", example = "10:30:00")
    @Column("hora_aposta_efetuada")
    private LocalTime horaApostaEfetuada;
    
    @Schema(description = "Horário da aposta (ex: 1 para 10h, 2 para 12h)", example = "1")
    @Column("horario")
    private Integer horario;
    
    @Schema(description = "Número da pule da aposta", example = "PUL12345")
    @Column("numero_pule")
    private String numeroPule;
    
    @Schema(description = "Identificador do terminal onde a aposta foi registrada", example = "1")
    @Column("terminal")
    private Integer terminal;
    
    @Schema(description = "Valor da aposta", example = "10.50")
    @Column("valor_da_aposta")
    private BigDecimal valorDaAposta;
    
    @Schema(description = "Número da conta associada à aposta", example = "123456789")
    @Column("numero_conta")
    private String numeroConta;
    
    @Schema(description = "Indica se a aposta foi feita por um parceiro", example = "false")
    @Column("parceiro")
    private Boolean parceiro;
    
    @Schema(description = "Tipo de pagamento utilizado", example = "1")
    @Column("tipo_pagamento")
    private Integer tipoPagamento;
    
    @Schema(description = "Crédito utilizado na aposta", example = "0")
    @Column("credito")
    private Integer credito;

    // Construtores
    public ApostaR2dbc() {}

    // Construtor builder-style para facilitar criação
    public static ApostaR2dbc builder() {
        return new ApostaR2dbc();
    }

    public ApostaR2dbc comUsuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    public ApostaR2dbc comCliente(Integer cliente) {
        this.cliente = cliente;
        return this;
    }

    public ApostaR2dbc comCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
        return this;
    }

    public ApostaR2dbc comValor(BigDecimal valor) {
        this.valorDaAposta = valor;
        return this;
    }

    public ApostaR2dbc comDataAposta(LocalDate dataAposta) {
        this.dataAposta = dataAposta;
        return this;
    }

    public ApostaR2dbc comHorario(Integer horario) {
        this.horario = horario;
        return this;
    }

    public ApostaR2dbc valida() {
        this.apostaValida = true;
        this.dataCriacao = LocalDateTime.now();
        this.dataEfetuada = LocalDate.now();
        this.horaApostaEfetuada = LocalTime.now();
        return this;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getApostaValida() {
        return apostaValida;
    }

    public void setApostaValida(Boolean apostaValida) {
        this.apostaValida = apostaValida;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataAposta() {
        return dataAposta;
    }

    public void setDataAposta(LocalDate dataAposta) {
        this.dataAposta = dataAposta;
    }

    public LocalDate getDataEfetuada() {
        return dataEfetuada;
    }

    public void setDataEfetuada(LocalDate dataEfetuada) {
        this.dataEfetuada = dataEfetuada;
    }

    public LocalTime getHoraApostaEfetuada() {
        return horaApostaEfetuada;
    }

    public void setHoraApostaEfetuada(LocalTime horaApostaEfetuada) {
        this.horaApostaEfetuada = horaApostaEfetuada;
    }

    public Integer getHorario() {
        return horario;
    }

    public void setHorario(Integer horario) {
        this.horario = horario;
    }

    public String getNumeroPule() {
        return numeroPule;
    }

    public void setNumeroPule(String numeroPule) {
        this.numeroPule = numeroPule;
    }

    public Integer getTerminal() {
        return terminal;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public BigDecimal getValorDaAposta() {
        return valorDaAposta;
    }

    public void setValorDaAposta(BigDecimal valorDaAposta) {
        this.valorDaAposta = valorDaAposta;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Boolean getParceiro() {
        return parceiro;
    }

    public void setParceiro(Boolean parceiro) {
        this.parceiro = parceiro;
    }

    public Integer getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(Integer tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Integer getCredito() {
        return credito;
    }

    public void setCredito(Integer credito) {
        this.credito = credito;
    }

    // Métodos de negócio
    public boolean isValida() {
        return Boolean.TRUE.equals(apostaValida);
    }

    public boolean isVencedora(String resultadoOficial) {
        // Lógica para verificar se a aposta é vencedora será implementada
        // baseada no resultado oficial do sorteio
        return false; // TODO: Implementar lógica de verificação
    }

    public BigDecimal calcularPremio(BigDecimal multiplicador) {
        if (valorDaAposta == null || multiplicador == null) {
            return BigDecimal.ZERO;
        }
        return valorDaAposta.multiply(multiplicador);
    }

    @Override
    public String toString() {
        return "ApostaR2dbc{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", codigoUnico='" + codigoUnico + '\'' +
                ", valorDaAposta=" + valorDaAposta +
                ", apostaValida=" + apostaValida +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApostaR2dbc that = (ApostaR2dbc) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}