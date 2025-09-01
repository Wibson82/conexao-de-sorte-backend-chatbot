package chatbot.domain.jogoBicho.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_quina_dezenas")
public class QuinaDezena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dezena_um")
    private String dezenaUm;
    
    @Column(name = "dezena_dois")
    private String dezenaDois;
    
    @Column(name = "dezena_tres")
    private String dezenaTres;
    
    @Column(name = "dezena_quatro")
    private String dezenaQuatro;
    
    @Column(name = "dezena_cinco")
    private String dezenaCinco;
    
    @Column(name = "dezena_seis")
    private String dezenaSeis;
    
    @Column(name = "dezena_sete")
    private String dezenaSete;
    
    @Column(name = "dezena_oito")
    private String dezenaOito;
    
    @Column(name = "dezena_nove")
    private String dezenaNove;
    
    @Column(name = "dezena_dez")
    private String dezenaDez;
    
    @Column(name = "combinado", nullable = false)
    private Integer combinado;
    
    @Column(name = "opcao", nullable = false)
    private Integer opcao;
    
    @Column(name = "valor", precision = 38, scale = 2, nullable = false)
    private BigDecimal valor;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @ManyToOne
    @JoinColumn(name = "id_aposta_fk")
    private Aposta aposta;
    
    @Column(name = "data_ultima_modificacao")
    private LocalDateTime dataUltimaModificacao;
    
    @Column(name = "modificado_por")
    private String modificadoPor;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDezenaUm() { return dezenaUm; }
    public void setDezenaUm(String dezenaUm) { this.dezenaUm = dezenaUm; }
    
    public String getDezenaDois() { return dezenaDois; }
    public void setDezenaDois(String dezenaDois) { this.dezenaDois = dezenaDois; }
    
    public String getDezenaTres() { return dezenaTres; }
    public void setDezenaTres(String dezenaTres) { this.dezenaTres = dezenaTres; }
    
    public String getDezenaQuatro() { return dezenaQuatro; }
    public void setDezenaQuatro(String dezenaQuatro) { this.dezenaQuatro = dezenaQuatro; }
    
    public String getDezenaCinco() { return dezenaCinco; }
    public void setDezenaCinco(String dezenaCinco) { this.dezenaCinco = dezenaCinco; }
    
    public String getDezenaSeis() { return dezenaSeis; }
    public void setDezenaSeis(String dezenaSeis) { this.dezenaSeis = dezenaSeis; }
    
    public String getDezenaSete() { return dezenaSete; }
    public void setDezenaSete(String dezenaSete) { this.dezenaSete = dezenaSete; }
    
    public String getDezenaOito() { return dezenaOito; }
    public void setDezenaOito(String dezenaOito) { this.dezenaOito = dezenaOito; }
    
    public String getDezenaNove() { return dezenaNove; }
    public void setDezenaNove(String dezenaNove) { this.dezenaNove = dezenaNove; }
    
    public String getDezenaDez() { return dezenaDez; }
    public void setDezenaDez(String dezenaDez) { this.dezenaDez = dezenaDez; }
    
    public Integer getCombinado() { return combinado; }
    public void setCombinado(Integer combinado) { this.combinado = combinado; }
    
    public Integer getOpcao() { return opcao; }
    public void setOpcao(Integer opcao) { this.opcao = opcao; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public Aposta getAposta() { return aposta; }
    public void setAposta(Aposta aposta) { this.aposta = aposta; }
    
    public LocalDateTime getDataUltimaModificacao() { return dataUltimaModificacao; }
    public void setDataUltimaModificacao(LocalDateTime dataUltimaModificacao) { this.dataUltimaModificacao = dataUltimaModificacao; }
    
    public String getModificadoPor() { return modificadoPor; }
    public void setModificadoPor(String modificadoPor) { this.modificadoPor = modificadoPor; }
}