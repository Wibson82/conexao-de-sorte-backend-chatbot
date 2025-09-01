package chatbot.domain.jogoBicho.service;

import chatbot.domain.jogoBicho.entities.*;
import chatbot.domain.jogoBicho.repository.*;
import chatbot.infrastructure.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BetStorageService {

    @Autowired
    private ApostaRepository apostaRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CentenaRepository centenaRepository;

    @Autowired
    private MilharRepository milharRepository;

    @Autowired
    private DuqueDezenaRepository duqueDezenaRepository;

    @Autowired
    private TernoDezenaRepository ternoDezenaRepository;

    @Autowired
    private TernoGrupoRepository ternoGrupoRepository;

    @Autowired
    private QuadraGrupoRepository quadraGrupoRepository;

    @Autowired
    private QuinaGrupoRepository quinaGrupoRepository;

    @Autowired
    private DuqueGrupoRepository duqueGrupoRepository;

    @Autowired
    private PasseRepository passeRepository;

    @Autowired
    private QuinaDezenaRepository quinaDezenaRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Transactional
    public Aposta storeBet(Map<String, Object> betData) {
        try {
            // Validate required data
            validateBetData(betData);
            
            // Create new bet entity
            Aposta aposta = new Aposta();
            
            // Set basic bet information
            aposta.setApostaValida(true);
            aposta.setUsuario("terminal_user");
            aposta.setCliente(1);
            aposta.setCodigoUnico(generateUniqueCode());
            aposta.setDataCriacao(LocalDateTime.now());
            aposta.setDataAposta(LocalDate.now());
            aposta.setDataEfetuada(LocalDate.now());
            aposta.setHoraApostaEfetuada(LocalTime.now());
            aposta.setTerminal(1);
            aposta.setTipoPagamento(1);
            aposta.setParceiro(false);
            aposta.setCredito(0);
            
            // Set bet details from session data
            aposta.setHorario(mapHorarioToInt((String) betData.get("horario")));
            aposta.setValorDaAposta((BigDecimal) betData.get("valor"));
            
            // Save the bet first to get ID
            aposta = apostaRepository.save(aposta);
            
            // Handle different bet types
            String tipoAposta = (String) betData.getOrDefault("tipoAposta", "GRUPO");
            
            switch (tipoAposta) {
                    case "GRUPO":
                        saveGrupoBet(aposta, betData);
                        break;
                    case "CENTENA":
                        saveCentenaBet(aposta, betData);
                        break;
                    case "MILHAR":
                        saveMilharBet(aposta, betData);
                        break;
                    case "DUQUE_DEZENA":
                        saveDuqueDezenaBet(aposta, betData);
                        break;
                    case "TERNO_DEZENA":
                        saveTernoDezenaBet(aposta, betData);
                        break;
                    case "TERNO_GRUPO":
                        saveTernoGrupoBet(aposta, betData);
                        break;
                    case "DUQUE_GRUPO":
                        saveDuqueGrupoBet(aposta, betData);
                        break;
                    case "QUADRA_GRUPO":
                        saveQuadraGrupoBet(aposta, betData);
                        break;
                    case "QUINA_GRUPO":
                        saveQuinaGrupoBet(aposta, betData);
                        break;
                    case "QUINA_DEZENA":
                        saveQuinaDezenaBet(aposta, betData);
                        break;
                    case "PASSE":
                        savePasseBet(aposta, betData);
                        break;
                    default:
                        saveGrupoBet(aposta, betData);
                        break;
                }
            
            return aposta;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao armazenar aposta: " + e.getMessage(), e);
        }
    }

    private void saveGrupoBet(Aposta aposta, Map<String, Object> betData) {
        Integer grupoNum = (Integer) betData.get("grupo");
        Grupo grupo = new Grupo();
        grupo.setAposta(aposta);
        grupo.setGrupo(String.format("%02d", grupoNum));
        grupo.setOpcao(grupoNum);
        grupo.setDataCriacao(LocalDateTime.now());
        grupo.setValor((BigDecimal) betData.get("valor"));
        grupoRepository.save(grupo);
    }

    private void saveCentenaBet(Aposta aposta, Map<String, Object> betData) {
        Centena centena = new Centena();
        centena.setAposta(aposta);
        centena.setCentena((String) betData.get("centena"));
        centena.setOpcao(Integer.parseInt((String) betData.get("centena")));
        centena.setDataCriacao(LocalDateTime.now());
        centena.setValor((BigDecimal) betData.get("valor"));
        centena.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        centenaRepository.save(centena);
    }

    private void saveMilharBet(Aposta aposta, Map<String, Object> betData) {
        Milhar milhar = new Milhar();
        milhar.setAposta(aposta);
        milhar.setMilhar((String) betData.get("milhar"));
        milhar.setOpcao(Integer.parseInt((String) betData.get("milhar")));
        milhar.setDataCriacao(LocalDateTime.now());
        milhar.setValor((BigDecimal) betData.get("valor"));
        milhar.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        milharRepository.save(milhar);
    }

    private void saveDuqueDezenaBet(Aposta aposta, Map<String, Object> betData) {
        DuqueDezena duqueDezena = new DuqueDezena();
        duqueDezena.setAposta(aposta);
        duqueDezena.setDezenaUm((String) betData.get("dezena1"));
        duqueDezena.setDezenaDois((String) betData.get("dezena2"));
        duqueDezena.setOpcao(1);
        duqueDezena.setDataCriacao(LocalDateTime.now());
        duqueDezena.setValor((BigDecimal) betData.get("valor"));
        duqueDezena.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        duqueDezenaRepository.save(duqueDezena);
    }

    private void saveTernoDezenaBet(Aposta aposta, Map<String, Object> betData) {
        TernoDezena ternoDezena = new TernoDezena();
        ternoDezena.setAposta(aposta);
        ternoDezena.setDezenaUm((String) betData.get("dezena1"));
        ternoDezena.setDezenaDois((String) betData.get("dezena2"));
        ternoDezena.setDezenaTres((String) betData.get("dezena3"));
        ternoDezena.setOpcao(1);
        ternoDezena.setDataCriacao(LocalDateTime.now());
        ternoDezena.setValor((BigDecimal) betData.get("valor"));
        ternoDezena.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        ternoDezenaRepository.save(ternoDezena);
    }

    private void saveTernoGrupoBet(Aposta aposta, Map<String, Object> betData) {
        TernoGrupo ternoGrupo = new TernoGrupo();
        ternoGrupo.setAposta(aposta);
        ternoGrupo.setGrupoUm((String) betData.get("grupo1"));
        ternoGrupo.setGrupoDois((String) betData.get("grupo2"));
        ternoGrupo.setGrupoTres((String) betData.get("grupo3"));
        ternoGrupo.setOpcao(1);
        ternoGrupo.setDataCriacao(LocalDateTime.now());
        ternoGrupo.setValor((BigDecimal) betData.get("valor"));
        ternoGrupo.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        ternoGrupoRepository.save(ternoGrupo);
    }

    private void saveQuadraGrupoBet(Aposta aposta, Map<String, Object> betData) {
        QuinaGrupo quadraGrupo = new QuinaGrupo();
        quadraGrupo.setAposta(aposta);
        quadraGrupo.setGrupoUm((String) betData.get("grupo1"));
        quadraGrupo.setGrupoDois((String) betData.get("grupo2"));
        quadraGrupo.setGrupoTres((String) betData.get("grupo3"));
        quadraGrupo.setGrupoQuatro((String) betData.get("grupo4"));
        quadraGrupo.setOpcao(1);
        quadraGrupo.setDataCriacao(LocalDateTime.now());
        quadraGrupo.setValor((BigDecimal) betData.get("valor"));
        quadraGrupo.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        quadraGrupoRepository.save(quadraGrupo);
    }

    private void saveDuqueGrupoBet(Aposta aposta, Map<String, Object> betData) {
        QuinaGrupo duqueGrupo = new QuinaGrupo();
        duqueGrupo.setAposta(aposta);
        duqueGrupo.setGrupoUm((String) betData.get("grupo1"));
        duqueGrupo.setGrupoDois((String) betData.get("grupo2"));
        duqueGrupo.setOpcao(1);
        duqueGrupo.setDataCriacao(LocalDateTime.now());
        duqueGrupo.setValor((BigDecimal) betData.get("valor"));
        duqueGrupo.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        duqueGrupoRepository.save(duqueGrupo);
    }

    private void saveQuinaGrupoBet(Aposta aposta, Map<String, Object> betData) {
        QuinaGrupo quinaGrupo = new QuinaGrupo();
        quinaGrupo.setAposta(aposta);
        quinaGrupo.setGrupoUm((String) betData.get("grupo1"));
        quinaGrupo.setGrupoDois((String) betData.get("grupo2"));
        quinaGrupo.setGrupoTres((String) betData.get("grupo3"));
        quinaGrupo.setGrupoQuatro((String) betData.get("grupo4"));
        quinaGrupo.setGrupoCinco((String) betData.get("grupo5"));
        quinaGrupo.setOpcao(1);
        quinaGrupo.setDataCriacao(LocalDateTime.now());
        quinaGrupo.setValor((BigDecimal) betData.get("valor"));
        quinaGrupo.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        quinaGrupoRepository.save(quinaGrupo);
    }

    private void saveQuinaDezenaBet(Aposta aposta, Map<String, Object> betData) {
        QuinaDezena quinaDezena = new QuinaDezena();
        quinaDezena.setAposta(aposta);
        quinaDezena.setDezenaUm((String) betData.get("dezena1"));
        quinaDezena.setDezenaDois((String) betData.get("dezena2"));
        quinaDezena.setDezenaTres((String) betData.get("dezena3"));
        quinaDezena.setDezenaQuatro((String) betData.get("dezena4"));
        quinaDezena.setDezenaCinco((String) betData.get("dezena5"));
        quinaDezena.setOpcao(1);
        quinaDezena.setDataCriacao(LocalDateTime.now());
        quinaDezena.setValor((BigDecimal) betData.get("valor"));
        quinaDezena.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        quinaDezenaRepository.save(quinaDezena);
    }

    private void savePasseBet(Aposta aposta, Map<String, Object> betData) {
        Passe passe = new Passe();
        passe.setAposta(aposta);
        passe.setGrupoUm((String) betData.get("grupo1"));
        passe.setGrupoDois((String) betData.get("grupo2"));
        passe.setGrupoTres((String) betData.get("grupo3"));
        passe.setGrupoQuatro((String) betData.get("grupo4"));
        passe.setGrupoCinco((String) betData.get("grupo5"));
        passe.setOpcao(1);
        passe.setDataCriacao(LocalDateTime.now());
        passe.setValor((BigDecimal) betData.get("valor"));
        passe.setCombinado(betData.containsKey("combinado") ? (Integer) betData.get("combinado") : 0);
        passeRepository.save(passe);
    }

    private void validateBetData(Map<String, Object> betData) {
        if (betData == null) {
            throw new IllegalArgumentException("Dados da aposta não podem ser nulos");
        }
        
        if (!betData.containsKey("grupo") || betData.get("grupo") == null) {
            throw new IllegalArgumentException("Grupo da aposta é obrigatório");
        }
        
        if (!betData.containsKey("horario") || betData.get("horario") == null) {
            throw new IllegalArgumentException("Horário da aposta é obrigatório");
        }
        
        if (!betData.containsKey("valor") || betData.get("valor") == null) {
            throw new IllegalArgumentException("Valor da aposta é obrigatório");
        }
        
        BigDecimal valor = (BigDecimal) betData.get("valor");
        if (valor.compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Valor mínimo da aposta é R$ 1,00");
        }
    }

    private String generateUniqueCode() {
        String rawCode = "BET" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return encryptionService.encrypt(rawCode);
    }

    private Integer mapHorarioToInt(String horario) {
        if (horario == null) return 1;
        
        if (horario.contains("14:00")) return 1;
        if (horario.contains("18:00")) return 2;
        if (horario.contains("21:00")) return 3;
        
        return 1;
    }

    public Aposta findBetByCode(String encryptedCode) {
        try {
            // Try to decrypt the provided code
            String decryptedCode = encryptedCode;
            if (encryptionService.isEncrypted(encryptedCode)) {
                decryptedCode = encryptionService.decrypt(encryptedCode);
            }
            
            // Search for the bet with the decrypted code
            return apostaRepository.findByCodigoUnico(decryptedCode).orElse(null);
        } catch (Exception e) {
            // If decryption fails, search with original code
            return apostaRepository.findByCodigoUnico(encryptedCode).orElse(null);
        }
    }

    public boolean isCodeValid(String encryptedCode) {
        try {
            String decryptedCode = encryptedCode;
            if (encryptionService.isEncrypted(encryptedCode)) {
                decryptedCode = encryptionService.decrypt(encryptedCode);
            }
            return apostaRepository.findByCodigoUnico(decryptedCode).isPresent();
        } catch (Exception e) {
            return apostaRepository.findByCodigoUnico(encryptedCode).isPresent();
        }
    }

    public String generateBetReceipt(Aposta aposta, String tipoAposta) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("====================================\n");
        receipt.append("        COMPROVANTE DE APOSTA        \n");
        receipt.append("====================================\n");
        receipt.append(String.format("Código: %s\n", aposta.getCodigoUnico()));
        receipt.append(String.format("Data: %s\n", aposta.getDataAposta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        receipt.append(String.format("Tipo: %s\n", tipoAposta));
        receipt.append(String.format("Valor: R$ %.2f\n", aposta.getValorDaAposta()));
        receipt.append(String.format("Horário: %s\n", mapHorarioToString(aposta.getHorario())));
        receipt.append("------------------------------------\n");

        switch (tipoAposta) {
            case "GRUPO":
                List<Grupo> grupos = grupoRepository.findByApostaId(aposta.getId());
                for (Grupo grupo : grupos) {
                    receipt.append(String.format("Grupo: %s\n", grupo.getGrupo()));
                }
                break;
            case "CENTENA":
                List<Centena> centenas = centenaRepository.findByApostaId(aposta.getId());
                for (Centena centena : centenas) {
                    receipt.append(String.format("Centena: %s\n", centena.getCentena()));
                }
                break;
            case "MILHAR":
                List<Milhar> milhares = milharRepository.findByApostaId(aposta.getId());
                for (Milhar milhar : milhares) {
                    receipt.append(String.format("Milhar: %s\n", milhar.getMilhar()));
                }
                break;
            case "DUQUE_DEZENA":
                List<DuqueDezena> duquesDezena = duqueDezenaRepository.findByApostaId(aposta.getId());
                for (DuqueDezena duque : duquesDezena) {
                    receipt.append(String.format("Dezenas: %s - %s\n", duque.getDezenaUm(), duque.getDezenaDois()));
                }
                break;
            case "TERNO_DEZENA":
                List<TernoDezena> ternosDezena = ternoDezenaRepository.findByApostaId(aposta.getId());
                for (TernoDezena terno : ternosDezena) {
                    receipt.append(String.format("Dezenas: %s - %s - %s\n", 
                        terno.getDezenaUm(), terno.getDezenaDois(), terno.getDezenaTres()));
                }
                break;
            case "TERNO_GRUPO":
                List<TernoGrupo> ternosGrupo = ternoGrupoRepository.findByApostaId(aposta.getId());
                for (TernoGrupo terno : ternosGrupo) {
                    receipt.append(String.format("Grupos: %s - %s - %s\n", 
                        terno.getGrupoUm(), terno.getGrupoDois(), terno.getGrupoTres()));
                }
                break;
            case "DUQUE_GRUPO":
                List<QuinaGrupo> duquesGrupo = duqueGrupoRepository.findByApostaId(aposta.getId());
                for (QuinaGrupo duque : duquesGrupo) {
                    receipt.append(String.format("Grupos: %s - %s\n", 
                        duque.getGrupoUm(), duque.getGrupoDois()));
                }
                break;
            case "QUADRA_GRUPO":
                List<QuinaGrupo> quadrasGrupo = quadraGrupoRepository.findByApostaId(aposta.getId());
                for (QuinaGrupo quadra : quadrasGrupo) {
                    receipt.append(String.format("Grupos: %s - %s - %s - %s\n", 
                        quadra.getGrupoUm(), quadra.getGrupoDois(), quadra.getGrupoTres(), quadra.getGrupoQuatro()));
                }
                break;
            case "QUINA_GRUPO":
                List<QuinaGrupo> quinasGrupo = quinaGrupoRepository.findByApostaId(aposta.getId());
                for (QuinaGrupo quina : quinasGrupo) {
                    receipt.append(String.format("Grupos: %s - %s - %s - %s - %s\n", 
                        quina.getGrupoUm(), quina.getGrupoDois(), quina.getGrupoTres(), quina.getGrupoQuatro(), quina.getGrupoCinco()));
                }
                break;
            case "QUINA_DEZENA":
                List<QuinaDezena> quinasDezena = quinaDezenaRepository.findByApostaId(aposta.getId());
                for (QuinaDezena quina : quinasDezena) {
                    receipt.append(String.format("Dezenas: %s - %s - %s - %s - %s\n", 
                        quina.getDezenaUm(), quina.getDezenaDois(), quina.getDezenaTres(), quina.getDezenaQuatro(), quina.getDezenaCinco()));
                }
                break;
            case "PASSE":
                List<Passe> passes = passeRepository.findByApostaId(aposta.getId());
                for (Passe passe : passes) {
                    receipt.append(String.format("Grupos: %s - %s - %s - %s - %s\n", 
                        passe.getGrupoUm(), passe.getGrupoDois(), passe.getGrupoTres(), passe.getGrupoQuatro(), passe.getGrupoCinco()));
                }
                break;
        }

        receipt.append("====================================\n");
        receipt.append("   Guarde este comprovante!         \n");
        receipt.append("====================================\n");
        
        return receipt.toString();
    }

    private String mapHorarioToString(Integer horario) {
        switch (horario) {
            case 1: return "09:00";
            case 2: return "BS-11:00";
            case 3: return "RIO-11:00";
            case 4: return "14:00";
            case 5: return "16:00";
            case 6: return "18:00";
            case 7: return "19:00";
            case 8: return "21:00";
            default: return "N/A";
        }
    }

    public java.util.List<Aposta> getAllBets() {
        return apostaRepository.findAll();
    }

    public java.util.List<Aposta> getBetsByDate(LocalDate date) {
        return apostaRepository.findByDataAposta(date);
    }

    public long getTotalBetsCount() {
        return apostaRepository.count();
    }
}