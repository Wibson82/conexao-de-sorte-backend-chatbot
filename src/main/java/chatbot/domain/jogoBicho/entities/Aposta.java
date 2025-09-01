package chatbot.domain.jogoBicho.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "tb_apostas")
public class Aposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "aposta_valida")
    private Boolean apostaValida;
    
    @Column(name = "usuario")
    private String usuario;
    
    @Column(name = "cliente")
    private Integer cliente;
    
    @Column(name = "codigo_unico", unique = true)
    private String codigoUnico;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_aposta")
    private LocalDate dataAposta;
    
    @Column(name = "data_efetuada")
    private LocalDate dataEfetuada;
    
    @Column(name = "hora_aposta_efetuada")
    private LocalTime horaApostaEfetuada;
    
    @Column(name = "horario")
    private Integer horario;
    
    @Column(name = "numero_pule")
    private String numeroPule;
    
    @Column(name = "terminal")
    private Integer terminal;
    
    @Column(name = "valor_da_aposta", precision = 38, scale = 2)
    private BigDecimal valorDaAposta;
    
    @Column(name = "numero_conta")
    private String numeroConta;
    
    @Column(name = "parceiro")
    private Boolean parceiro;
    
    @Column(name = "tipo_pagamento")
    private Integer tipoPagamento;
    
    @Column(name = "credito")
    private Integer credito;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<Grupo> grupos;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<Dezena> dezenas;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<Centena> centenas;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<Milhar> milhares;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<DuqueDezena> duqueDezenas;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<TernoDezena> ternoDezenas;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<TernoGrupo> ternoGrupos;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<QuinaDezena> quinaDezenas;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<QuinaGrupo> quinaGrupos;
    
    @OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL)
    private List<Passe> passes;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Boolean getApostaValida() { return apostaValida; }
    public void setApostaValida(Boolean apostaValida) { this.apostaValida = apostaValida; }
    
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public Integer getCliente() { return cliente; }
    public void setCliente(Integer cliente) { this.cliente = cliente; }
    
    public String getCodigoUnico() { return codigoUnico; }
    public void setCodigoUnico(String codigoUnico) { this.codigoUnico = codigoUnico; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDate getDataAposta() { return dataAposta; }
    public void setDataAposta(LocalDate dataAposta) { this.dataAposta = dataAposta; }
    
    public LocalDate getDataEfetuada() { return dataEfetuada; }
    public void setDataEfetuada(LocalDate dataEfetuada) { this.dataEfetuada = dataEfetuada; }
    
    public LocalTime getHoraApostaEfetuada() { return horaApostaEfetuada; }
    public void setHoraApostaEfetuada(LocalTime horaApostaEfetuada) { this.horaApostaEfetuada = horaApostaEfetuada; }
    
    public Integer getHorario() { return horario; }
    public void setHorario(Integer horario) { this.horario = horario; }
    
    public String getNumeroPule() { return numeroPule; }
    public void setNumeroPule(String numeroPule) { this.numeroPule = numeroPule; }
    
    public Integer getTerminal() { return terminal; }
    public void setTerminal(Integer terminal) { this.terminal = terminal; }
    
    public BigDecimal getValorDaAposta() { return valorDaAposta; }
    public void setValorDaAposta(BigDecimal valorDaAposta) { this.valorDaAposta = valorDaAposta; }
    
    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }
    
    public Boolean getParceiro() { return parceiro; }
    public void setParceiro(Boolean parceiro) { this.parceiro = parceiro; }
    
    public Integer getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(Integer tipoPagamento) { this.tipoPagamento = tipoPagamento; }
    
    public Integer getCredito() { return credito; }
    public void setCredito(Integer credito) { this.credito = credito; }
    
    public List<Grupo> getGrupos() { return grupos; }
    public void setGrupos(List<Grupo> grupos) { this.grupos = grupos; }
    
    public List<Dezena> getDezenas() { return dezenas; }
    public void setDezenas(List<Dezena> dezenas) { this.dezenas = dezenas; }
    
    public List<Centena> getCentenas() { return centenas; }
    public void setCentenas(List<Centena> centenas) { this.centenas = centenas; }
    
    public List<Milhar> getMilhares() { return milhares; }
    public void setMilhares(List<Milhar> milhares) { this.milhares = milhares; }
    
    public List<DuqueDezena> getDuqueDezenas() { return duqueDezenas; }
    public void setDuqueDezenas(List<DuqueDezena> duqueDezenas) { this.duqueDezenas = duqueDezenas; }
    
    public List<TernoDezena> getTernoDezenas() { return ternoDezenas; }
    public void setTernoDezenas(List<TernoDezena> ternoDezenas) { this.ternoDezenas = ternoDezenas; }
    
    public List<TernoGrupo> getTernoGrupos() { return ternoGrupos; }
    public void setTernoGrupos(List<TernoGrupo> ternoGrupos) { this.ternoGrupos = ternoGrupos; }
    
    public List<QuinaDezena> getQuinaDezenas() { return quinaDezenas; }
    public void setQuinaDezenas(List<QuinaDezena> quinaDezenas) { this.quinaDezenas = quinaDezenas; }
    
    public List<QuinaGrupo> getQuinaGrupos() { return quinaGrupos; }
    public void setQuinaGrupos(List<QuinaGrupo> quinaGrupos) { this.quinaGrupos = quinaGrupos; }
    
    public List<Passe> getPasses() { return passes; }
    public void setPasses(List<Passe> passes) { this.passes = passes; }
}