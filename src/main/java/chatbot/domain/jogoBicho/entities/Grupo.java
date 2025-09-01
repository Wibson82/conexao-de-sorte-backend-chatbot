package chatbot.domain.jogoBicho.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_grupos")
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "grupo", nullable = false)
    private String grupo;
    
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
    
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    
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