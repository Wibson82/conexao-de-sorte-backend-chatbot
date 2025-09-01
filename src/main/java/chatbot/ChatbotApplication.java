package chatbot;

import chatbot.infrastructure.terminal.JogoBichoTerminalService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ChatbotApplication {

	public static void main(String[] args) {
		// Sempre iniciar o Spring Boot para disponibilizar a API REST
		ConfigurableApplicationContext context = SpringApplication.run(ChatbotApplication.class, args);
		
		// Verificar se deve executar modo terminal jogo do bicho
		if (args.length > 0 && "--jogo-bicho".equals(args[0])) {
			System.out.println("🎰 INICIANDO SISTEMA DE APOSTAS - JOGO DO BICHO 🎰");
			JogoBichoTerminalService terminalService = context.getBean(JogoBichoTerminalService.class);
			terminalService.start();
			System.exit(0);
		} else {
			System.out.println("🌐 SERVIDOR WEB INICIADO - API REST DISPONÍVEL 🌐");
			System.out.println("Acesse: http://localhost:8080");
			System.out.println("Para modo terminal: java -jar app.jar --jogo-bicho");
		}
	}
}
