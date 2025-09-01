package chatbot.domain.jogoBicho.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tb_resultados")
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "data", nullable = false)
    private LocalDate data;
    
    @Column(name = "hora", nullable = false)
    private LocalTime hora;
    
    @Column(name = "grupo_um", nullable = false)
    private String grupoUm;
    
    @Column(name = "grupo_dois", nullable = false)
    private String grupoDois;
    
    @Column(name = "grupo_tres", nullable = false)
    private String grupoTres;
    
    @Column(name = "grupo_quatro", nullable = false)
    private String grupoQuatro;
    
    @Column(name = "grupo_cinco", nullable = false)
    private String grupoCinco;
    
    @Column(name = "premio_grupo", precision = 38, scale = 2)
    private BigDecimal premioGrupo;
    
    @Column(name = "premio_centena", precision = 38, scale = 2)
    private BigDecimal premioCentena;
    
    @Column(name = "premio_milhar", precision = 38, scale = 2)
    private BigDecimal premioMilhar;
    
    @Column(name = "premio_duque_dezena", precision = 38, scale = 2)
    private BigDecimal premioDuqueDezena;
    
    @Column(name = "premio_duque_grupo", precision = 38, scale = 2)
    private BigDecimal premioDuqueGrupo;
    
    @Column(name = "premio_terno_dezena", precision = 38, scale = 2)
    private BigDecimal premioTernoDezena;
    
    @Column(name = "premio_terno_grupo", precision = 38, scale = 2)
    private BigDecimal premioTernoGrupo;
    
    @Column(name = "premio_quadra_grupo", precision = 38, scale = 2)
    private BigDecimal premioQuadraGrupo;
    
    @Column(name = "premio_quina_grupo", precision = 38, scale = 2)
    private BigDecimal premioQuinaGrupo;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "data_ultima_modificacao")
    private LocalDateTime dataUltimaModificacao;
    
    @Column(name = "modificado_por")
    private String modificadoPor;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    
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
    
    public BigDecimal getPremioGrupo() { return premioGrupo; }
    public void setPremioGrupo(BigDecimal premioGrupo) { this.premioGrupo = premioGrupo; }
    
    public BigDecimal getPremioCentena() { return premioCentena; }
    public void setPremioCentena(BigDecimal premioCentena) { this.premioCentena = premioCentena; }
    
    public BigDecimal getPremioMilhar() { return premioMilhar; }
    public void setPremioMilhar(BigDecimal premioMilhar) { this.premioMilhar = premioMilhar; }
    
    public BigDecimal getPremioDuqueDezena() { return premioDuqueDezena; }
    public void setPremioDuqueDezena(BigDecimal premioDuqueDezena) { this.premioDuqueDezena = premioDuqueDezena; }
    
    public BigDecimal getPremioDuqueGrupo() { return premioDuqueGrupo; }
    public void setPremioDuqueGrupo(BigDecimal premioDuqueGrupo) { this.premioDuqueGrupo = premioDuqueGrupo; }
    
    public BigDecimal getPremioTernoDezena() { return premioTernoDezena; }
    public void setPremioTernoDezena(BigDecimal premioTernoDezena) { this.premioTernoDezena = premioTernoDezena; }
    
    public BigDecimal getPremioTernoGrupo() { return premioTernoGrupo; }
    public void setPremioTernoGrupo(BigDecimal premioTernoGrupo) { this.premioTernoGrupo = premioTernoGrupo; }
    
    public BigDecimal getPremioQuadraGrupo() { return premioQuadraGrupo; }
    public void setPremioQuadraGrupo(BigDecimal premioQuadraGrupo) { this.premioQuadraGrupo = premioQuadraGrupo; }
    
    public BigDecimal getPremioQuinaGrupo() { return premioQuinaGrupo; }
    public void setPremioQuinaGrupo(BigDecimal premioQuinaGrupo) { this.premioQuinaGrupo = premioQuinaGrupo; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public LocalDateTime getDataUltimaModificacao() { return dataUltimaModificacao; }
    public void setDataUltimaModificacao(LocalDateTime dataUltimaModificacao) { this.dataUltimaModificacao = dataUltimaModificacao; }
    
    public String getModificadoPor() { return modificadoPor; }
    public void setModificadoPor(String modificadoPor) { this.modificadoPor = modificadoPor; }
}