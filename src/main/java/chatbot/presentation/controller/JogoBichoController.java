package chatbot.presentation.controller;

import chatbot.service.navigation.JogoBichoNavigationService;
import chatbot.domain.jogoBicho.service.BetSessionService;
import chatbot.domain.jogoBicho.service.BetStorageService;
import chatbot.domain.model.MenuNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/v1/jogo-bicho")
@CrossOrigin(origins = "*")
public class JogoBichoController {
    
    @Autowired
    private JogoBichoNavigationService navigationService;
    
    @Autowired
    private BetSessionService betSessionService;
    
    @Autowired
    private BetStorageService betStorageService;
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "Jogo do Bicho API");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startSession() {
        String sessionId = java.util.UUID.randomUUID().toString();
        MenuNode welcomeMenu = navigationService.getNode("welcome");
        
        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("currentMenu", welcomeMenu);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/menu/{nodeId}")
    public ResponseEntity<Map<String, Object>> getMenu(@PathVariable String nodeId) {
        MenuNode menu = navigationService.getNode(nodeId);
        
        Map<String, Object> response = new HashMap<>();
        if (menu != null) {
            response.put("menu", menu);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("error", "Menu não encontrado");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/grupos")
    public ResponseEntity<Map<String, Object>> getGrupos() {
        Map<String, Object> response = new HashMap<>();
        
        // Tabela de grupos do Jogo do Bicho
        String[] grupos = {
            "01 - Avestruz", "02 - Águia", "03 - Burro", "04 - Borboleta", "05 - Cachorro",
            "06 - Cabra", "07 - Carneiro", "08 - Camelo", "09 - Cobra", "10 - Coelho",
            "11 - Cavalo", "12 - Elefante", "13 - Galo", "14 - Gato", "15 - Jacaré",
            "16 - Leão", "17 - Macaco", "18 - Porco", "19 - Pavão", "20 - Peru",
            "21 - Touro", "22 - Tigre", "23 - Urso", "24 - Veado", "25 - Vaca"
        };
        
        response.put("grupos", grupos);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/regras")
    public ResponseEntity<Map<String, Object>> getRegras() {
        Map<String, Object> response = new HashMap<>();
        
        Map<String, String> regras = new HashMap<>();
        regras.put("grupo", "Aposte em um dos 25 grupos. Cada grupo tem 4 números (ex: Avestruz = 01, 02, 03, 04)");
        regras.put("centena", "Aposte em uma centena de 000 a 999");
        regras.put("milhar", "Aposte em uma milhar de 0000 a 9999");
        regras.put("duque", "Escolha 2 grupos que devem sair nos primeiros prêmios");
        regras.put("terno", "Escolha 3 grupos que devem sair nos primeiros prêmios");
        regras.put("quadra", "Escolha 4 grupos que devem sair nos primeiros prêmios");
        regras.put("quina", "Escolha 5 grupos que devem sair nos primeiros prêmios");
        
        response.put("regras", regras);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/pagamentos")
    public ResponseEntity<Map<String, Object>> getPagamentos() {
        Map<String, Object> response = new HashMap<>();
        
        Map<String, String> pagamentos = new HashMap<>();
        pagamentos.put("grupo_cabeca", "18x o valor apostado");
        pagamentos.put("grupo_cercado", "3.6x o valor apostado");
        pagamentos.put("centena_seca", "600x o valor apostado");
        pagamentos.put("centena_cercada", "120x o valor apostado");
        pagamentos.put("milhar_seca", "4000x o valor apostado");
        pagamentos.put("milhar_cercada", "800x o valor apostado");
        pagamentos.put("duque", "18-19x o valor apostado");
        pagamentos.put("terno", "130-150x o valor apostado");
        pagamentos.put("quadra", "~1000x o valor apostado");
        pagamentos.put("quina", "5000x o valor apostado");
        
        response.put("pagamentos", pagamentos);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/aposta")
    public ResponseEntity<Map<String, Object>> criarAposta(@RequestBody Map<String, Object> apostaData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Aqui seria implementada a lógica de criação de aposta
            // Por enquanto, retornamos uma resposta simulada
            
            response.put("success", true);
            response.put("message", "Aposta registrada com sucesso");
            response.put("codigo", "JB" + System.currentTimeMillis());
            response.put("valor", apostaData.get("valor"));
            response.put("tipo", apostaData.get("tipo"));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Erro ao processar aposta: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/resultados")
    public ResponseEntity<Map<String, Object>> getResultados() {
        Map<String, Object> response = new HashMap<>();
        
        // Simulação de resultados - em um sistema real, viria do banco de dados
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("data", "04/08/2025");
        resultado.put("horario", "14:00");
        resultado.put("primeiro", "1234 - Grupo 09 (Cobra)");
        resultado.put("segundo", "5678 - Grupo 15 (Jacaré)");
        resultado.put("terceiro", "9012 - Grupo 03 (Burro)");
        resultado.put("quarto", "3456 - Grupo 21 (Touro)");
        resultado.put("quinto", "7890 - Grupo 07 (Carneiro)");
        
        response.put("resultado", resultado);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
}
