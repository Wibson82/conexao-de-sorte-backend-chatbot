package chatbot.application.service;

import chatbot.domain.model.MenuNode;
import chatbot.domain.model.MenuOption;
import chatbot.domain.model.MenuType;
import chatbot.domain.model.UserSession;
import chatbot.domain.model.UserChoice;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuNavigationService {
    private final Map<String, MenuNode> menuNodes;
    private final Map<String, UserSession> sessions;
    
    public MenuNavigationService() {
        this.menuNodes = new HashMap<>();
        this.sessions = new HashMap<>();
        initializeMenuNodes();
    }
    
    private void initializeMenuNodes() {
        // Menu Principal
        MenuNode welcomeNode = new MenuNode("welcome", "Bem-vindo ao ChatBot de Atendimento!", "Escolha uma opção para continuar:", MenuType.MAIN_MENU);
        welcomeNode.setOptions(Arrays.asList(
            new MenuOption("1", "Informações sobre produtos", "show_products", "products", "Ver informações sobre nossos produtos", "produtos"),
            new MenuOption("2", "Suporte técnico", "show_support", "support", "Obter ajuda técnica", "suporte"),
            new MenuOption("3", "Falar com atendente", "transfer_human", null, "Conectar com um atendente humano", "atendente"),
            new MenuOption("4", "Sair", "exit", null, "Encerrar atendimento", "sair")
        ));
        menuNodes.put("welcome", welcomeNode);
        
        // Menu de Produtos
        MenuNode productsNode = new MenuNode("products", "Nossos Produtos", "Selecione uma categoria:", MenuType.SUB_MENU);
        productsNode.setOptions(Arrays.asList(
            new MenuOption("1", "Eletrônicos", "show_electronics", "electronics", "Produtos eletrônicos", "eletronicos"),
            new MenuOption("2", "Roupas", "show_clothes", "clothes", "Vestuário e acessórios", "roupas"),
            new MenuOption("3", "Casa e Jardim", "show_home", "home", "Produtos para casa", "casa"),
            new MenuOption("0", "Voltar", "back", "welcome", "Voltar ao menu principal", "voltar")
        ));
        menuNodes.put("products", productsNode);
        
        // Menu de Suporte
        MenuNode supportNode = new MenuNode("support", "Suporte Técnico", "Como podemos ajudar?", MenuType.SUB_MENU);
        supportNode.setOptions(Arrays.asList(
            new MenuOption("1", "Problemas com pedido", "order_issues", "order_help", "Ajuda com pedidos", "pedido"),
            new MenuOption("2", "Dúvidas sobre produto", "product_questions", "product_help", "Informações sobre produtos", "produto"),
            new MenuOption("3", "Problemas técnicos", "tech_issues", "tech_help", "Suporte técnico", "tecnico"),
            new MenuOption("0", "Voltar", "back", "welcome", "Voltar ao menu principal", "voltar")
        ));
        menuNodes.put("support", supportNode);
        
        // Informações específicas
        MenuNode electronicsNode = new MenuNode("electronics", "Eletrônicos", "Temos uma grande variedade de produtos eletrônicos com as melhores marcas do mercado.", MenuType.INFORMATION);
        electronicsNode.setOptions(Arrays.asList(
            new MenuOption("0", "Voltar", "back", "products", "Voltar aos produtos", "voltar")
        ));
        menuNodes.put("electronics", electronicsNode);
        
        MenuNode clothesNode = new MenuNode("clothes", "Roupas", "Confira nossa coleção de roupas e acessórios para todas as idades.", MenuType.INFORMATION);
        clothesNode.setOptions(Arrays.asList(
            new MenuOption("0", "Voltar", "back", "products", "Voltar aos produtos", "voltar")
        ));
        menuNodes.put("clothes", clothesNode);
        
        MenuNode homeNode = new MenuNode("home", "Casa e Jardim", "Produtos para deixar sua casa ainda mais bonita e funcional.", MenuType.INFORMATION);
        homeNode.setOptions(Arrays.asList(
            new MenuOption("0", "Voltar", "back", "products", "Voltar aos produtos", "voltar")
        ));
        menuNodes.put("home", homeNode);
        
        MenuNode orderHelpNode = new MenuNode("order_help", "Ajuda com Pedidos", "Para problemas com pedidos, entre em contato pelo telefone (11) 1234-5678 ou email: pedidos@empresa.com", MenuType.INFORMATION);
        orderHelpNode.setOptions(Arrays.asList(
            new MenuOption("0", "Voltar", "back", "support", "Voltar ao suporte", "voltar")
        ));
        menuNodes.put("order_help", orderHelpNode);
        
        MenuNode productHelpNode = new MenuNode("product_help", "Informações sobre Produtos", "Para dúvidas sobre produtos, consulte nossa FAQ no site ou fale com nossos especialistas pelo chat.", MenuType.INFORMATION);
        productHelpNode.setOptions(Arrays.asList(
            new MenuOption("0", "Voltar", "back", "support", "Voltar ao suporte", "voltar")
        ));
        menuNodes.put("product_help", productHelpNode);
        
        MenuNode techHelpNode = new MenuNode("tech_help", "Suporte Técnico", "Para problemas técnicos, acesse nossa central de ajuda online ou abra um chamado pelo sistema.", MenuType.INFORMATION);
        techHelpNode.setOptions(Arrays.asList(
            new MenuOption("0", "Voltar", "back", "support", "Voltar ao suporte", "voltar")
        ));
        menuNodes.put("tech_help", techHelpNode);
    }
    
    public String createSession() {
        String sessionId = UUID.randomUUID().toString();
        UserSession session = new UserSession();
        session.setSessionId(sessionId);
        session.setCurrentNodeId("welcome");
        sessions.put(sessionId, session);
        return sessionId;
    }
    
    public MenuNode getCurrentMenu(String sessionId) {
        UserSession session = sessions.get(sessionId);
        if (session == null) {
            return null;
        }
        return menuNodes.get(session.getCurrentNodeId());
    }
    
    public MenuNode processChoice(String sessionId, String optionId) {
        UserSession session = sessions.get(sessionId);
        if (session == null) {
            return null;
        }
        
        MenuNode currentNode = menuNodes.get(session.getCurrentNodeId());
        if (currentNode == null) {
            return null;
        }
        
        // Find the selected option
        MenuOption selectedOption = null;
        for (MenuOption option : currentNode.getOptions()) {
            if (option.getId().equals(optionId) || 
                (option.getKeyword() != null && option.getKeyword().equalsIgnoreCase(optionId))) {
                selectedOption = option;
                break;
            }
        }
        
        if (selectedOption == null) {
            return null;
        }
        
        // Record the choice
        UserChoice choice = new UserChoice(optionId, selectedOption.getText(), selectedOption.getAction());
        session.addChoice(choice);
        
        // Handle special actions
        if ("exit".equals(selectedOption.getAction()) || "transfer_human".equals(selectedOption.getAction())) {
            // These actions don't navigate to another menu
            return currentNode;
        }
        
        // Navigate to next node if specified
        if (selectedOption.getNextNodeId() != null) {
            session.setCurrentNodeId(selectedOption.getNextNodeId());
            return menuNodes.get(selectedOption.getNextNodeId());
        }
        
        return currentNode;
    }
    
    public void resetSession(String sessionId) {
        UserSession session = sessions.get(sessionId);
        if (session != null) {
            session.setCurrentNodeId("welcome");
            session.getChoiceHistory().clear();
        }
    }
    
    public List<UserChoice> getSessionHistory(String sessionId) {
        UserSession session = sessions.get(sessionId);
        if (session == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(session.getChoiceHistory());
    }
    
    public boolean isValidSession(String sessionId) {
        return sessions.containsKey(sessionId);
    }
    
    public MenuNode getNode(String nodeId) {
        return menuNodes.get(nodeId);
    }
}