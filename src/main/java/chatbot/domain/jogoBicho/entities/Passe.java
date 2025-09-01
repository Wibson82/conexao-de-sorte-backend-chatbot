package chatbot.domain.jogoBicho.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_passes")
public class Passe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "grupo_um")
    private String grupoUm;
    
    @Column(name = "grupo_dois")
    private String grupoDois;
    
    @Column(name = "grupo_tres")
    private String grupoTres;
    
    @Column(name = "grupo_quatro")
    private String grupoQuatro;
    
    @Column(name = "grupo_cinco")
    private String grupoCinco;
    
    @Column(name = "grupo_seis")
    private String grupoSeis;
    
    @Column(name = "grupo_sete")
    private String grupoSete;
    
    @Column(name = "grupo_oito")
    private String grupoOito;
    
    @Column(name = "grupo_nove")
    private String grupoNove;
    
    @Column(name = "grupo_dez")
    private String grupoDez;
    
    @Column(name = "opcao", nullable = false)
    private Integer opcao;
    
    @Column(name = "valor", precision = 38, scale = 2)
    private BigDecimal valor;
    
    @Column(name = "combinado")
    private Integer combinado;
    
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
    
    public String getGrupoUm() { return grupoUm; }
    public void setGrupoUm(String grupoUm) { this.grupoUm = grupoUm; }
    
    public String getGrupoDois() { return grupoDois; }
    public void setGrupoDois(String grupoDois) { this.grupoDois = grupoDois; }
    
    public String getGrupoTres() { return grupoTres; }
    public void setGrupoTres(String grupoTres) { this.grupoTres = grupoTres; }
    
    public String getGrupoQuatro() { return grupoQuatro; }
    public void setGrupoQuatro(String grupoQuatro) { this.grupoQuatro = grupoQuatro; }
    
    public String getGrupoCinco() { return grupoCinco; }
    public void setGrupoCinco(String grupoCinco) { this.grupoCinco = grupoCinco; }
    
    public String getGrupoSeis() { return grupoSeis; }
    public void setGrupoSeis(String grupoSeis) { this.grupoSeis = grupoSeis; }
    
    public String getGrupoSete() { return grupoSete; }
    public void setGrupoSete(String grupoSete) { this.grupoSete = grupoSete; }
    
    public String getGrupoOito() { return grupoOito; }
    public void setGrupoOito(String grupoOito) { this.grupoOito = grupoOito; }
    
    public String getGrupoNove() { return grupoNove; }
    public void setGrupoNove(String grupoNove) { this.grupoNove = grupoNove; }
    
    public String getGrupoDez() { return grupoDez; }
    public void setGrupoDez(String grupoDez) { this.grupoDez = grupoDez; }
    
    public Integer getOpcao() { return opcao; }
    public void setOpcao(Integer opcao) { this.opcao = opcao; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public Integer getCombinado() { return combinado; }
    public void setCombinado(Integer combinado) { this.combinado = combinado; }
    
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