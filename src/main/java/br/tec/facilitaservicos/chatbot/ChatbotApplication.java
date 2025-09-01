package br.tec.facilitaservicos.chatbot;

import br.tec.facilitaservicos.chatbot.infrastructure.terminal.JogoBichoTerminalService;
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
		
		// Verificar se deve executar modo terminal jogo do bicho
		if (args.length > 0 && "--jogo-bicho".equals(args[0])) {
			System.out.println("🎰 INICIANDO SISTEMA DE APOSTAS - JOGO DO BICHO 🎰");
			System.out.println("🔒 Modo seguro ativado - JWT necessário para operações críticas");
			JogoBichoTerminalService terminalService = context.getBean(JogoBichoTerminalService.class);
			terminalService.start();
			System.exit(0);
		} else {
			System.out.println("🌐 SERVIDOR WEB REATIVO INICIADO - API REST DISPONÍVEL 🌐");
			System.out.println("Acesso: http://localhost:8089");
			System.out.println("Documentação: http://localhost:8089/swagger-ui.html");
			System.out.println("Métricas: http://localhost:8089/actuator/prometheus");
			System.out.println("Para modo terminal: java -jar app.jar --jogo-bicho");
			System.out.println("🔐 Sistema seguro - Autenticação JWT obrigatória");
		}
	}
}