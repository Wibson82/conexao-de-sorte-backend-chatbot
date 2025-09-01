package chatbot.presentation.controller;

import chatbot.application.service.MenuNavigationService;
import chatbot.domain.model.MenuNode;
import chatbot.domain.model.UserChoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/v1/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    
    @Autowired
    private MenuNavigationService menuNavigationService;
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "ChatBot API");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startConversation() {
        String sessionId = menuNavigationService.createSession();
        MenuNode currentMenu = menuNavigationService.getCurrentMenu(sessionId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("currentMenu", currentMenu);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/menu/{sessionId}")
    public ResponseEntity<Map<String, Object>> getCurrentMenu(@PathVariable String sessionId) {
        if (!menuNavigationService.isValidSession(sessionId)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Sessão inválida ou expirada");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        MenuNode currentMenu = menuNavigationService.getCurrentMenu(sessionId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("currentMenu", currentMenu);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/choose")
    public ResponseEntity<Map<String, Object>> processChoice(
            @RequestParam String sessionId, 
            @RequestParam String optionId) {
        
        if (!menuNavigationService.isValidSession(sessionId)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Sessão inválida ou expirada");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        MenuNode nextMenu = menuNavigationService.processChoice(sessionId, optionId);
        
        Map<String, Object> response = new HashMap<>();
        if (nextMenu != null) {
            response.put("success", true);
            response.put("nextMenu", nextMenu);
            response.put("sessionId", sessionId);
        } else {
            response.put("success", false);
            response.put("error", "Opção inválida");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/reset/{sessionId}")
    public ResponseEntity<Map<String, Object>> resetSession(@PathVariable String sessionId) {
        if (!menuNavigationService.isValidSession(sessionId)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Sessão inválida ou expirada");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        menuNavigationService.resetSession(sessionId);
        MenuNode currentMenu = menuNavigationService.getCurrentMenu(sessionId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("currentMenu", currentMenu);
        response.put("message", "Sessão reiniciada com sucesso");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<Map<String, Object>> getSessionHistory(@PathVariable String sessionId) {
        if (!menuNavigationService.isValidSession(sessionId)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Sessão inválida ou expirada");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        List<UserChoice> history = menuNavigationService.getSessionHistory(sessionId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("history", history);
        response.put("sessionId", sessionId);
        
        return ResponseEntity.ok(response);
    }
}
