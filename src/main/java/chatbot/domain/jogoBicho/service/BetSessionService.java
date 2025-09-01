package chatbot.domain.jogoBicho.service;

import chatbot.domain.jogoBicho.entities.Aposta;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço responsável por gerenciar múltiplas apostas em uma sessão de usuário.
 * Implementa o padrão Single Responsibility Principle (SRP) do SOLID.
 */
@Service
public class BetSessionService {
    
    private final BetStorageService betStorageService;
    private final List<Map<String, Object>> pendingBets;
    private final List<Aposta> confirmedBets;
    
    public BetSessionService(BetStorageService betStorageService) {
        this.betStorageService = betStorageService;
        this.pendingBets = new ArrayList<>();
        this.confirmedBets = new ArrayList<>();
    }
    
    /**
     * Adiciona uma aposta à sessão atual sem persistir no banco.
     * A aposta fica armazenada temporariamente até ser finalizada.
     */
    public void addBetToSession(Map<String, Object> betData) {
        if (betData == null) {
            throw new IllegalArgumentException("Dados da aposta não podem ser nulos");
        }
        
        // Cria uma cópia dos dados para evitar modificações externas
        Map<String, Object> betCopy = new HashMap<>(betData);
        pendingBets.add(betCopy);
    }
    
    /**
     * Remove uma aposta específica da sessão.
     */
    public boolean removeBetFromSession(int index) {
        if (index >= 0 && index < pendingBets.size()) {
            pendingBets.remove(index);
            return true;
        }
        return false;
    }
    
    /**
     * Retorna todas as apostas pendentes na sessão.
     */
    public List<Map<String, Object>> getPendingBets() {
        return new ArrayList<>(pendingBets);
    }
    
    /**
     * Retorna o número de apostas pendentes.
     */
    public int getPendingBetsCount() {
        return pendingBets.size();
    }
    
    /**
     * Calcula o valor total das apostas pendentes.
     */
    public BigDecimal getTotalPendingValue() {
        return pendingBets.stream()
                .map(bet -> {
                    Object valorObj = bet.get("valor");
                    if (valorObj instanceof BigDecimal valor) {
                        return valor;
                    } else if (valorObj instanceof String valorStr && !valorStr.isBlank()) {
                        try {
                            return new BigDecimal(valorStr.replace(",", "."));
                        } catch (NumberFormatException e) {
                            return BigDecimal.ZERO;
                        }
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Finaliza todas as apostas pendentes, persistindo-as no banco de dados.
     * Retorna a lista de apostas confirmadas.
     */
    public List<Aposta> finalizePendingBets() {
        List<Aposta> newConfirmedBets = new ArrayList<>();
        
        for (Map<String, Object> betData : pendingBets) {
            try {
                Aposta aposta = betStorageService.storeBet(betData);
                newConfirmedBets.add(aposta);
                confirmedBets.add(aposta);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao finalizar aposta: " + e.getMessage(), e);
            }
        }
        
        // Limpa as apostas pendentes após finalização
        pendingBets.clear();
        
        return newConfirmedBets;
    }
    
    /**
     * Retorna todas as apostas confirmadas na sessão atual.
     */
    public List<Aposta> getConfirmedBets() {
        return new ArrayList<>(confirmedBets);
    }
    
    /**
     * Limpa todas as apostas da sessão (pendentes e confirmadas).
     */
    public void clearSession() {
        pendingBets.clear();
        confirmedBets.clear();
    }
    
    /**
     * Verifica se existem apostas pendentes na sessão.
     */
    public boolean hasPendingBets() {
        return !pendingBets.isEmpty();
    }
    
    /**
     * Verifica se existem apostas confirmadas na sessão.
     */
    public boolean hasConfirmedBets() {
        return !confirmedBets.isEmpty();
    }
    
    /**
     * Gera um resumo das apostas pendentes para exibição.
     */
    public String generatePendingBetsSummary() {
        if (pendingBets.isEmpty()) {
            return "Nenhuma aposta pendente na sessão.";
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("\n📋 APOSTAS PENDENTES NA SESSÃO\n");
        summary.append("=".repeat(50)).append("\n");
        
        for (int i = 0; i < pendingBets.size(); i++) {
            Map<String, Object> bet = pendingBets.get(i);
            summary.append(String.format("%d. ", i + 1));
            
            Integer grupo = (Integer) bet.get("grupo");
            String horario = (String) bet.get("horario");
            BigDecimal valor = null;
            Object valorObj = bet.get("valor");
            if (valorObj instanceof BigDecimal) {
                valor = (BigDecimal) valorObj;
            } else if (valorObj instanceof String valorStr && !valorStr.isBlank()) {
                try {
                    valor = new BigDecimal(valorStr.replace(",", "."));
                } catch (NumberFormatException ignored) {
                    // Valor inválido; será tratado como nulo
                }
            }
            String tipoAposta = (String) bet.getOrDefault("tipo_aposta", bet.getOrDefault("tipoAposta", "GRUPO"));
            
            switch (tipoAposta) {
                case "GRUPO" -> {
                    if (grupo != null) summary.append(String.format("🐦 Grupo %02d - %s | ", grupo, getGrupoName(grupo)));
                }
                case "CENTENA" -> {
                    String centena = (String) bet.get("centena");
                    summary.append(String.format("🔢 Centena: %s | ", centena));
                }
                case "MILHAR" -> {
                    String milhar = (String) bet.get("milhar");
                    summary.append(String.format("🔢 Milhar: %s | ", milhar));
                }
                case "DUQUE_GRUPO" -> {
                    @SuppressWarnings("unchecked")
                    List<Integer> gruposDuque = (List<Integer>) bet.get("grupos");
                    if (gruposDuque != null && gruposDuque.size() == 2) {
                        summary.append(String.format("🎲 Duque: %02d & %02d | ", gruposDuque.get(0), gruposDuque.get(1)));
                    }
                }
                case "TERNO_GRUPO" -> {
                    @SuppressWarnings("unchecked")
                    List<Integer> gruposTerno = (List<Integer>) bet.get("grupos");
                    if (gruposTerno != null && gruposTerno.size() == 3) {
                        summary.append(String.format("🎲 Terno: %02d, %02d & %02d | ", gruposTerno.get(0), gruposTerno.get(1), gruposTerno.get(2)));
                    }
                }
                case "QUADRA_GRUPO" -> {
                    @SuppressWarnings("unchecked")
                    List<Integer> gruposQuadra = (List<Integer>) bet.get("grupos");
                    if (gruposQuadra != null && gruposQuadra.size() == 4) {
                        String lista = gruposQuadra.stream()
                                .map(g -> String.format("%02d", g))
                                .collect(Collectors.joining(", "));
                        summary.append(String.format("🎲 Quadra: %s | ", lista));
                    }
                }
                case "QUINA_GRUPO" -> {
                    @SuppressWarnings("unchecked")
                    List<Integer> gruposQuina = (List<Integer>) bet.get("grupos");
                    if (gruposQuina != null && gruposQuina.size() >= 5) {
                        String lista = gruposQuina.stream()
                                .map(g -> String.format("%02d", g))
                                .collect(Collectors.joining(", "));
                        summary.append(String.format("🎲 Quina: %s | ", lista));
                    }
                }
            }
            if (horario != null) summary.append(String.format("⏰ %s | ", horario));
            if (valor != null) summary.append(String.format("💰 R$ %.2f", valor));
            
            summary.append("\n");
        }
        
        summary.append("\n💰 Valor Total: R$ ").append(String.format("%.2f", getTotalPendingValue()));
        summary.append("\n");
        
        return summary.toString();
    }
    
    /**
     * Método auxiliar para obter o nome do grupo.
     */
    private String getGrupoName(int grupoNum) {
        String[] grupos = {
            "Avestruz", "Águia", "Burro", "Borboleta", "Cachorro",
            "Cabra", "Carneiro", "Camelo", "Cobra", "Coelho",
            "Cavalo", "Elefante", "Galo", "Gato", "Jacaré",
            "Leão", "Macaco", "Porco", "Pavão", "Peru",
            "Touro", "Tigre", "Urso", "Veado", "Vaca"
        };
        
        if (grupoNum >= 1 && grupoNum <= 25) {
            return grupos[grupoNum - 1];
        }
        return "Grupo Inválido";
    }
}