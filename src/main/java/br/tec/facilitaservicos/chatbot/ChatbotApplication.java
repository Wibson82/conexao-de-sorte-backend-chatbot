package br.tec.facilitaservicos.chatbot;

// JogoBichoTerminalService removido - funcionalidade migrada para R2DBC
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * ============================================================================
 * 🎰 CHATBOT JOGO DO BICHO - SISTEMA DE APOSTAS REATIVO
 * ============================================================================
 * 
 * Sistema completo de apostas para Jogo do Bicho com:
 * - API REST reativa (WebFlux + R2DBC)
 * - Interface terminal interativa
 * - Segurança JWT com authorities granulares
 * - Gestão de apostas em tempo real
 * - Integração com sistema de resultados
 * 
 * @author Sistema de Migração R2DBC
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
public class ChatbotApplication {

	public static void main(String[] args) {
		// Sempre iniciar o Spring Boot para disponibilizar a API REST reativa
		ConfigurableApplicationContext context = SpringApplication.run(ChatbotApplication.class, args);
		
		// Modo terminal removido - usando apenas API REST reativa
		System.out.println("🌐 SERVIDOR WEB REATIVO INICIADO - API REST DISPONÍVEL 🌐");
		System.out.println("Acesso: http://localhost:8089");
		System.out.println("🔒 Autenticação JWT necessária para operações de apostas");
		System.out.println("Documentação: http://localhost:8089/swagger-ui.html");
		System.out.println("Métricas: http://localhost:8089/actuator/prometheus");
	}
}