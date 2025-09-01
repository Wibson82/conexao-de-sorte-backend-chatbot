package chatbot.infrastructure.terminal;

import chatbot.service.navigation.JogoBichoNavigationService;
import chatbot.domain.model.MenuNode;
import chatbot.domain.model.MenuOption;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import chatbot.domain.jogoBicho.entities.Aposta;
import chatbot.domain.jogoBicho.service.BetStorageService;
import chatbot.domain.jogoBicho.service.BetSessionService;

import org.springframework.stereotype.Service;

@Service
public class JogoBichoTerminalService {
    private final JogoBichoNavigationService navigationService;
    private final Scanner scanner;
    private String currentNodeId = "welcome";
    private final BetStorageService betStorageService;
    private final BetSessionService betSessionService;

    public JogoBichoTerminalService(JogoBichoNavigationService navigationService,
            BetStorageService betStorageService,
            BetSessionService betSessionService) {
        this.navigationService = navigationService;
        this.betStorageService = betStorageService;
        this.betSessionService = betSessionService;
        this.scanner = new Scanner(System.in);
    }



    // === Validação de entradas numéricas ===
    private boolean isValidGrupoInput(String input) {
        return input.matches("\\d{2}") && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 25;
    }

    private boolean isValidDezenaInput(String input) {
        return input.matches("\\d{2}");
    }

    private boolean isValidCentenaInput(String input) {
        if (input.contains(",")) {
            return input.length() >= 5 && input.length() <= 10 && input.matches("[0-9,]+");
        }
        return input.matches("\\d{3}");
    }

    private boolean isValidMilharInput(String input) {
        if (input.contains(",")) {
            return input.length() >= 5 && input.length() <= 10 && input.matches("[0-9,]+");
        }
        return input.matches("\\d{4}");
    }

    public void start() {
        System.out.println("\n🎰 SISTEMA DE APOSTAS - JOGO DO BICHO 🎰");
        System.out.println("=".repeat(50));

        while (true) {
            try {
                displayCurrentMenu();

                if (!scanner.hasNextLine()) {
                    System.out.println("Entrada não disponível. Encerrando...");
                    break;
                }

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("sair")) {
                    handleExit();
                    break;
                }

                processUserChoice(input);

            } catch (Exception e) {
                System.out.println("❌ Erro: " + e.getMessage());
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private void displayCurrentMenu() {
        MenuNode currentNode = navigationService.getNode(currentNodeId);
        if (currentNode == null) {
            System.out.println("❌ Menu não encontrado. Voltando ao início...");
            currentNodeId = "welcome";
            currentNode = navigationService.getNode(currentNodeId);
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println(currentNode.getTitle());
        System.out.println("-".repeat(60));
        
        // Mensagem personalizada para milhar invertida combinada
        if (currentNodeId.equals("milhar_invertida_combinada")) {
            System.out.println("Digite a milhar (5 a 10 dígitos) - qualquer permutação vale:");
        } else {
            System.out.println(currentNode.getDescription());
        }
        System.out.println();

        for (MenuOption option : currentNode.getOptions()) {
            System.out.printf("%s. %s", option.getId(), option.getText());
            if (option.getKeyword() != null && !option.getKeyword().isEmpty()) {
                System.out.printf(" [%s]", option.getKeyword());
            }
            System.out.println();
        }

        System.out.println();
        System.out.print("Escolha uma opção: ");
    }

    private void processUserChoice(String input) {
        MenuNode currentNode = navigationService.getNode(currentNodeId);
        
        if (currentNode == null) {
            System.out.println("❌ Erro: Menu não encontrado para nodeId: " + currentNodeId);
            return;
        }

        // Process user input against menu options

        // Tenta processar como número ou palavra-chave
        for (MenuOption option : currentNode.getOptions()) {
            if (option.getId().equals(input) || 
                (option.getKeyword() != null && option.getKeyword().equalsIgnoreCase(input))) {
                
                // Option found, process the action
                
                // Se estamos no menu de valores e a opção leva à confirmação,
                // armazena o valor numérico escolhido na sessão antes de prosseguir.
                if (currentNodeId.equals("enter_value") && "confirm_bet".equals(option.getAction())) {
                    String keyword = option.getKeyword();
                    try {
                        BigDecimal predefinedValor = new BigDecimal(keyword);
                        navigationService.setSessionData("valor", predefinedValor);
                    } catch (NumberFormatException ignored) {
                        // Keyword pode não ser numérica (ex.: "outro"), ignora
                    }
                }
                handleMenuAction(option.getAction(), option.getNextNodeId());
                return;
            }
        }
        
        // No direct menu option match found, try specific node handlers

        // Processar seleção de data
        if (currentNodeId.equals("select_date")) {
            if (handleDateSelection(input)) {
                return;
            }
        }
        
        // Processar seleção de horário
        if (currentNodeId.equals("select_time")) {
            if (handleTimeSelection(input)) {
                return;
            }
        }

        // Processar escolhas específicas para grupos
        if (currentNodeId.equals("select_grupo")) {
            if (isValidGrupoInput(input)) {
                int grupoNum = Integer.parseInt(input);
                handleGrupoSelection(grupoNum);
                return;
            }
        }

        // Processar horários
        if (currentNodeId.equals("select_horario")) {
            try {
                int horario = Integer.parseInt(input);
                if (horario >= 1 && horario <= 3) {
                    handleHorarioSelection(horario);
                    return;
                }
            } catch (NumberFormatException e) {
                // Continuar para mensagem de erro
            }
        }

        // Processar centena - diferentes modalidades
        if (currentNodeId.equals("centena_cabeca") || currentNodeId.equals("centena_cercada_5") || 
            currentNodeId.equals("centena_cercada_7") || currentNodeId.equals("centena_cercada_mr") ||
            currentNodeId.equals("centena_invertida") || currentNodeId.equals("centena_especifico_numero")) {
            if (handleCentenaModalityInput(input)) {
                return;
            }
        }
        
        // Processar seleção de prêmio específico para centena
        if (currentNodeId.equals("centena_premio_especifico")) {
            if (handleCentenaPremioEspecificoSelection(input)) {
                return;
            }
        }

        // Processar milhar - diferentes modalidades
        if (currentNodeId.equals("milhar_cabeca") || currentNodeId.equals("milhar_cercada_5") || 
            currentNodeId.equals("milhar_invertida") || currentNodeId.equals("milhar_especifico_numero") ||
            currentNodeId.equals("milhar_invertida_combinada")) {
            if (handleMilharModalityInput(input)) {
                return;
            }
        }
        
        // Processar seleção de prêmio específico para milhar
        if (currentNodeId.equals("milhar_premio_especifico")) {
            if (handleMilharPremioEspecificoSelection(input)) {
                return;
            }
        }

        // Processar Duque de Grupo (dois grupos)
        if (currentNodeId.equals("select_duque_grupo")) {
            try {
                int grupoNum = Integer.parseInt(input);
                if (grupoNum >= 1 && grupoNum <= 25) {
                    @SuppressWarnings("unchecked")
                    List<Integer> grupos = (List<Integer>) navigationService.getSessionData().getOrDefault("grupos",
                            new ArrayList<Integer>());
                    if (!grupos.contains(grupoNum)) {
                        grupos.add(grupoNum);
                        navigationService.setSessionData("grupos", grupos);
                        System.out.printf("✅ Grupo %02d selecionado!\n", grupoNum);
                    } else {
                        System.out.println("⚠️  Grupo já selecionado, escolha outro.");
                    }
                    if (grupos.size() == 2) {
                        System.out.println("🎲 Duque completo! Agora escolha o horário:");
                        currentNodeId = "select_horario";
                    } else {
                        System.out.println("Escolha o segundo grupo (1-25): ");
                    }
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        // Processar Quina de Grupo - escolha quantidade (5-10)
        if (currentNodeId.equals("select_quina_grupo_count")) {
            try {
                int qtde = Integer.parseInt(input);
                if (qtde >= 5 && qtde <= 10) {
                    navigationService.setSessionData("quina_count", qtde);
                    navigationService.setSessionData("grupos", new ArrayList<Integer>());
                    System.out.println("🎲 Quina de Grupo iniciada! Selecione o primeiro grupo (1-25):");
                    currentNodeId = "select_quina_grupo";
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        // Processar Quina de Grupo (5-10 grupos)
        if (currentNodeId.equals("select_quina_grupo")) {
            try {
                int grupoNum = Integer.parseInt(input);
                if (grupoNum >= 1 && grupoNum <= 25) {
                    @SuppressWarnings("unchecked")
                    List<Integer> grupos = (List<Integer>) navigationService.getSessionData().getOrDefault("grupos",
                            new ArrayList<Integer>());
                    if (!grupos.contains(grupoNum)) {
                        grupos.add(grupoNum);
                        navigationService.setSessionData("grupos", grupos);
                        System.out.printf("✅ Grupo %02d selecionado!\n", grupoNum);
                    } else {
                        System.out.println("⚠️  Grupo já selecionado, escolha outro.");
                    }
                    int expected = (Integer) navigationService.getSessionData().getOrDefault("quina_count", 5);
                    if (grupos.size() == expected) {
                        System.out.println("🎲 Quina completa! Agora escolha o horário:");
                        currentNodeId = "select_horario";
                    } else {
                        System.out.printf("Escolha o próximo grupo (%d/%d restantes): \n", grupos.size() + 1, expected);
                    }
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        // Processar Terno de Grupo (três grupos)
        if (currentNodeId.equals("select_terno_grupo")) {
            try {
                int grupoNum = Integer.parseInt(input);
                if (grupoNum >= 1 && grupoNum <= 25) {
                    @SuppressWarnings("unchecked")
                    List<Integer> grupos = (List<Integer>) navigationService.getSessionData().getOrDefault("grupos",
                            new ArrayList<Integer>());
                    if (!grupos.contains(grupoNum)) {
                        grupos.add(grupoNum);
                        navigationService.setSessionData("grupos", grupos);
                        System.out.printf("✅ Grupo %02d selecionado!\n", grupoNum);
                    } else {
                        System.out.println("⚠️  Grupo já selecionado, escolha outro.");
                    }
                    if (grupos.size() == 3) {
                        System.out.println("🎲 Terno completo! Agora escolha o horário:");
                        currentNodeId = "select_horario";
                    } else {
                        System.out.println("Escolha o próximo grupo (1-25): ");
                    }
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        // Processar valores
        if (currentNodeId.equals("enter_value")) {
            try {
                BigDecimal valor = new BigDecimal(input);
                handleValorSelection(valor);
                return;
            } catch (NumberFormatException e) {
                // Continuar para mensagem de erro
            }
        }

        System.out.println("❌ Opção inválida. Por favor, escolha uma opção válida.");
    }

    private void handleMenuAction(String action, String nextNodeId) {
        switch (action) {
            case "bet":
            case "view_groups":
            case "rules":
            case "check_payment":
                currentNodeId = nextNodeId;
                break;

            case "view_results":
                handleViewResults();
                break;

            case "select_grupo":
                currentNodeId = nextNodeId;
                break;

            case "select_horario":
                currentNodeId = nextNodeId;
                break;

            case "enter_value":
                currentNodeId = nextNodeId;
                break;
                
            case "select_time":
                currentNodeId = nextNodeId;
                break;
                
            case "select_time_action":
                currentNodeId = nextNodeId;
                break;
                
            // Ações das modalidades da milhar
            case "milhar_cabeca":
            case "milhar_cercada_5":
            case "milhar_premio_especifico":
            case "milhar_invertida":
            case "milhar_invertida_combinada":
            case "milhar_especifico_numero":
                currentNodeId = nextNodeId;
                break;
                
            // Ações das modalidades da centena
            case "centena_cabeca":
            case "centena_cercada_5":
            case "centena_cercada_7":
            case "centena_cercada_mr":
            case "centena_premio_especifico":
            case "centena_invertida":
            case "centena_especifico_numero":
                currentNodeId = nextNodeId;
                break;

            case "select_duque_grupo":
                navigationService.clearSessionData();
                navigationService.setSessionData("tipo_aposta", "DUQUE_GRUPO");
                System.out.println("\n🎲 DUQUE DE GRUPO: Selecione o primeiro grupo (1-25):");
                currentNodeId = "select_duque_grupo";
                break;
            case "select_terno_grupo":
                navigationService.clearSessionData();
                navigationService.setSessionData("tipo_aposta", "TERNO_GRUPO");
                System.out.println("\n🎲 TERNO DE GRUPO: Selecione o primeiro grupo (1-25):");
                currentNodeId = "select_terno_grupo";
                break;
            case "select_quina_grupo":
                navigationService.clearSessionData();
                navigationService.setSessionData("tipo_aposta", "QUINA_GRUPO");
                System.out.println("\n🎲 QUINA DE GRUPO: Quantos grupos deseja escolher? (5-10):");
                currentNodeId = "select_quina_grupo_count";
                break;

            case "confirm_bet":
                handleConfirmBet();
                break;

            case "finalize_bet":
                handleFinalizeBets();
                currentNodeId = "welcome";
                break;

            case "today_bets":
                handleTodayBets();
                break;

            case "all_bets":
                handleAllBets();
                break;

            case "search_bet":
                handleSearchBet();
                break;

            case "view_bets":
                handleViewSessionBets();
                break;

            case "exit":
                handleExit();
                break;

            case "welcome":
                navigationService.clearSessionData();
                betSessionService.clearSession(); // Limpa apostas pendentes ao voltar ao menu principal
                currentNodeId = "welcome";
                break;

            default:
                currentNodeId = nextNodeId;
                break;
        }
    }

    private void handleGrupoSelection(int grupoNum) {
        navigationService.setSessionData("grupo", grupoNum);
        navigationService.setSessionData("tipo_aposta", "GRUPO");
        System.out.printf("✅ Grupo %02d - %s selecionado!\n",
                grupoNum, navigationService.getGrupoName(grupoNum));
        currentNodeId = "select_horario";
    }

    private void handleHorarioSelection(int horario) {
        String[] horarios = { "", "14:00 - PTM", "18:00 - PT", "21:00 - PTN" };
        navigationService.setSessionData("horario", horarios[horario]);
        currentNodeId = "enter_value";
    }

    // Novo: seleção de Centena
    private void handleCentenaSelection(String centena) {
        navigationService.setSessionData("centena", centena);
        navigationService.setSessionData("tipo_aposta", "CENTENA");
        System.out.printf("✅ Centena %s selecionada!\n", centena);
        currentNodeId = "select_horario";
    }

    // Novo: seleção de Milhar
    private void handleMilharSelection(String milhar) {
        navigationService.setSessionData("milhar", milhar);
        navigationService.setSessionData("tipo_aposta", "MILHAR");
        System.out.printf("✅ Milhar %s selecionada!\n", milhar);
        currentNodeId = "select_horario";
    }

    private void handleValorSelection(BigDecimal valor) {
        if (!navigationService.isValidValor(valor)) {
            System.out.println("❌ Valor mínimo para aposta é R$ 1,00");
            return;
        }

        navigationService.setSessionData("valor", valor);
        currentNodeId = "confirm_bet";
    }

    private void handleConfirmBet() {
        Map<String, Object> sessionData = navigationService.getSessionData();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ RESUMO DA APOSTA");
        System.out.println("-".repeat(60));

        Integer grupo = (Integer) sessionData.get("grupo");
        String horario = (String) sessionData.get("horario");
        Object valorObj = sessionData.get("valor");
        BigDecimal valor = null;
        if (valorObj instanceof BigDecimal v) {
            valor = v;
        } else if (valorObj instanceof String s && !s.isBlank()) {
            try {
                valor = new BigDecimal(s.replace(",", "."));
            } catch (NumberFormatException ignored) {
            }
        }
        String tipoAposta = (String) sessionData.getOrDefault("tipo_aposta", "GRUPO");
        @SuppressWarnings("unchecked")
        List<Integer> grupos = (List<Integer>) sessionData.get("grupos");

        switch (tipoAposta) {
            case "GRUPO" -> {
                if (grupo != null) {
                    System.out.printf("🐦 Grupo: %02d - %s\n", grupo, navigationService.getGrupoName(grupo));
                }
            }
            case "CENTENA" -> {
                String centena = (String) sessionData.get("centena");
                System.out.printf("🔢 Centena: %s\n", centena);
            }
            case "QUADRA_GRUPO" -> {
                if (grupos != null && grupos.size() == 4) {
                    System.out.print("🎲 Quadra: ");
                    for (int i = 0; i < grupos.size(); i++) {
                        System.out.printf("%02d%s", grupos.get(i),
                                (i == grupos.size() - 1 ? "\n" : i == grupos.size() - 2 ? " & " : ", "));
                    }
                }
            }
            case "QUINA_GRUPO" -> {
                if (grupos != null && grupos.size() >= 5) {
                    System.out.print("🎲 Quina: ");
                    for (int i = 0; i < grupos.size(); i++) {
                        System.out.printf("%02d%s", grupos.get(i),
                                (i == grupos.size() - 1 ? "\n" : i == grupos.size() - 2 ? " & " : ", "));
                    }
                }
            }
            case "MILHAR" -> {
                String milhar = (String) sessionData.get("milhar");
                System.out.printf("🔢 Milhar: %s\n", milhar);
            }
        }
        if (horario != null)
            System.out.printf("⏰ Horário: %s\n", horario);
        if (valor != null)
            System.out.printf("💰 Valor: R$ %.2f\n", valor);
        System.out.println();

        // Exibe opções ao usuário para salvar ou cancelar a aposta
        System.out.println("🎯 O QUE DESEJA FAZER?");
        System.out.println("-".repeat(40));
        System.out.println("1. 💾 Salvar aposta");
        System.out.println("2. ❌ Cancelar aposta");
        System.out.print("\nEscolha uma opção: ");

        if (scanner.hasNextLine()) {
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                handleSaveBet();
                return;
            } else if (choice.equals("2")) {
                System.out.println("❌ Aposta cancelada!");
                navigationService.clearSessionData();
            } else {
                System.out.println("❌ Opção inválida! Voltando ao menu principal...");
            }
        }

        currentNodeId = "welcome";
    }

    /**
     * Salva a aposta atual na sessão e oferece opções para continuar ou finalizar.
     */
    private void handleSaveBet() {
        Map<String, Object> sessionData = navigationService.getSessionData();

        try {
            // Garante compatibilidade de chave com serviço de persistência
            if (sessionData.containsKey("tipo_aposta") && !sessionData.containsKey("tipoAposta")) {
                sessionData.put("tipoAposta", sessionData.get("tipo_aposta"));
            }
            // Adiciona a aposta à sessão (não persiste ainda)
            betSessionService.addBetToSession(sessionData);

            // Exibe confirmação
            System.out.println("\n✅ APOSTA ADICIONADA À SESSÃO!");
            System.out.println("=".repeat(60));

            Integer grupo = (Integer) sessionData.get("grupo");
            @SuppressWarnings("unchecked")
            List<Integer> grupos = (List<Integer>) sessionData.get("grupos");
            String horario = (String) sessionData.get("horario");
            Object valorObjSess = sessionData.get("valor");
            BigDecimal valor = null;
            if (valorObjSess instanceof BigDecimal v) {
                valor = v;
            } else if (valorObjSess instanceof String s && !s.isBlank()) {
                try {
                    valor = new BigDecimal(s.replace(",", "."));
                } catch (NumberFormatException ignored) {
                }
            }

            String tipoAposta = (String) sessionData.getOrDefault("tipo_aposta", "GRUPO");
            switch (tipoAposta) {
                case "GRUPO" -> {
                    if (grupo != null)
                        System.out.printf("🐦 Grupo: %02d - %s\n", grupo, navigationService.getGrupoName(grupo));
                }
                case "DUQUE_GRUPO" -> {
                    if (grupos != null && grupos.size() == 2) {
                        System.out.printf("🎲 Duque: %02d & %02d\n", grupos.get(0), grupos.get(1));
                    }
                }
                case "TERNO_GRUPO" -> {
                    if (grupos != null && grupos.size() == 3) {
                        System.out.printf("🎲 Terno: %02d, %02d & %02d\n", grupos.get(0), grupos.get(1), grupos.get(2));
                    }
                }
                case "QUADRA_GRUPO" -> {
                    if (grupos != null && grupos.size() == 4) {
                        System.out.print("🎲 Quadra: ");
                        for (int i = 0; i < grupos.size(); i++) {
                            System.out.printf("%02d%s", grupos.get(i),
                                    (i == grupos.size() - 1 ? "\n" : i == grupos.size() - 2 ? " & " : ", "));
                        }
                    }
                }
                case "QUINA_GRUPO" -> {
                    if (grupos != null && grupos.size() >= 5) {
                        System.out.print("🎲 Quina: ");
                        for (int i = 0; i < grupos.size(); i++) {
                            System.out.printf("%02d%s", grupos.get(i),
                                    (i == grupos.size() - 1 ? "\n" : i == grupos.size() - 2 ? " & " : ", "));
                        }
                    }
                }
                case "CENTENA" -> {
                    String centena = (String) sessionData.get("centena");
                    System.out.printf("🔢 Centena: %s\n", centena);
                }
                case "MILHAR" -> {
                    String milhar = (String) sessionData.get("milhar");
                    System.out.printf("🔢 Milhar: %s\n", milhar);
                }
            }
            if (horario != null)
                System.out.printf("⏰ Horário: %s\n", horario);
            if (valor != null)
                System.out.printf("💰 Valor: R$ %.2f\n", valor);
            System.out.println();

            // Para Quadra ou Quina de Grupo, mapear grupos para chave grupoN
            if (("QUADRA_GRUPO".equals(tipoAposta) || "QUINA_GRUPO".equals(tipoAposta)) && grupos != null) {
                for (int i = 0; i < grupos.size(); i++) {
                    sessionData.put("grupo" + (i + 1), String.format("%02d", grupos.get(i)));
                }
            }
            // Exibe resumo das apostas na sessão
            System.out.println(betSessionService.generatePendingBetsSummary());

            // Oferece opções para o usuário
            System.out.println("\n🎯 O QUE DESEJA FAZER AGORA?");
            System.out.println("-".repeat(40));
            System.out.println("1. 🎰 Fazer outra aposta");
            System.out.println("2. 💾 Finalizar e salvar todas as apostas");
            System.out.println("3. 📋 Ver apostas da sessão");
            System.out.println("4. ❌ Cancelar todas as apostas");
            System.out.println("0. 🏠 Voltar ao menu principal");
            System.out.print("\nEscolha uma opção: ");

            if (scanner.hasNextLine()) {
                String choice = scanner.nextLine().trim();
                handlePostBetChoice(choice);
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao adicionar aposta à sessão: " + e.getMessage());
            System.out.println("Pressione Enter para continuar...");
            scanner.nextLine();
            betSessionService.clearSession(); // Limpa apostas pendentes em caso de erro
            currentNodeId = "welcome";
        } finally {
            // Limpa apenas os dados da aposta atual, mantém a sessão
            navigationService.clearSessionData();
        }
    }

    /**
     * Processa a escolha do usuário após salvar uma aposta.
     */
    private void handlePostBetChoice(String choice) {
        switch (choice) {
            case "1":
                currentNodeId = "bet_menu";
                break;
            case "2":
                handleFinalizeBets();
                break;
            case "3":
                handleViewSessionBets();
                break;
            case "4":
                handleCancelAllBets();
                break;
            case "0":
                betSessionService.clearSession(); // Limpa apostas pendentes ao voltar ao menu principal
                currentNodeId = "welcome";
                break;
            default:
                System.out.println("❌ Opção inválida!");
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
                betSessionService.clearSession(); // Limpa apostas pendentes em caso de erro
                currentNodeId = "welcome";
                break;
        }
    }

    /**
     * Finaliza todas as apostas da sessão, persistindo-as no banco.
     */
    private void handleFinalizeBets() {
        try {
            if (!betSessionService.hasPendingBets()) {
                System.out.println("\n❌ Não há apostas para finalizar!");
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
                currentNodeId = "welcome";
                return;
            }

            List<Aposta> confirmedBets = betSessionService.finalizePendingBets();

            System.out.println("\n🎉 TODAS AS APOSTAS FORAM REGISTRADAS COM SUCESSO!");
            System.out.println("=".repeat(60));
            System.out.printf("📊 Total de apostas: %d\n", confirmedBets.size());
            System.out.printf("💰 Valor total investido: R$ %.2f\n",
                    confirmedBets.stream()
                            .map(Aposta::getValorDaAposta)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
            System.out.println();

            System.out.println("📋 CÓDIGOS DAS APOSTAS:");
            System.out.println("-".repeat(30));
            for (int i = 0; i < confirmedBets.size(); i++) {
                Aposta aposta = confirmedBets.get(i);
                System.out.printf("%d. 🎫 %s\n", i + 1, aposta.getCodigoUnico());
            }

            System.out.println("\n✅ Todas as apostas foram salvas com segurança!");
            System.out.println("📅 Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("\nPressione Enter para voltar ao menu principal...");
            scanner.nextLine();

        } catch (Exception e) {
            System.err.println("❌ Erro ao finalizar apostas: " + e.getMessage());
            System.out.println("Pressione Enter para continuar...");
            scanner.nextLine();
        } finally {
            currentNodeId = "welcome";
        }
    }

    /**
     * Exibe as apostas pendentes na sessão atual.
     */
    private void handleViewSessionBets() {
        System.out.println(betSessionService.generatePendingBetsSummary());
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();

        // Volta para as opções pós-aposta
        System.out.println("\n🎯 O QUE DESEJA FAZER AGORA?");
        System.out.println("-".repeat(40));
        System.out.println("1. 🎰 Fazer outra aposta");
        System.out.println("2. 💾 Finalizar e salvar todas as apostas");
        System.out.println("4. ❌ Cancelar todas as apostas");
        System.out.println("0. 🏠 Voltar ao menu principal");
        System.out.print("\nEscolha uma opção: ");

        if (scanner.hasNextLine()) {
            String choice = scanner.nextLine().trim();
            handlePostBetChoice(choice);
        }
    }

    /**
     * Cancela todas as apostas da sessão atual.
     */
    private void handleCancelAllBets() {
        System.out.print("\n⚠️  Tem certeza que deseja cancelar todas as apostas da sessão? (S/N): ");

        if (scanner.hasNextLine()) {
            String confirm = scanner.nextLine().trim().toUpperCase();
            if (confirm.equals("S")) {
                betSessionService.clearSession();
                System.out.println("\n❌ Todas as apostas foram canceladas!");
                System.out.println("Pressione Enter para voltar ao menu principal...");
                scanner.nextLine();
                currentNodeId = "welcome";
            } else {
                System.out.println("\n✅ Cancelamento abortado!");
                handleViewSessionBets();
            }
        }
    }

    private void handleViewResults() {
        System.out.println("\n📊 RESULTADOS DOS ÚLTIMOS SORTEIOS");
        System.out.println("=".repeat(50));

        // Simular resultados
        String[] horarios = { "14:00 - PTM", "18:00 - PT", "21:00 - PTN" };
        String[] grupos = { "01 - Avestruz", "05 - Cão", "12 - Elefante", "08 - Camelo", "15 - Gato" };

        for (int i = 0; i < 3; i++) {
            System.out.printf("%s: %s\n", horarios[i], grupos[i]);
        }

        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    private void handleTodayBets() {
        System.out.println("\n📅 APOSTAS DE HOJE");
        System.out.println("=".repeat(60));

        try {
            var bets = betStorageService.getBetsByDate(LocalDate.now());

            if (bets.isEmpty()) {
                System.out.println("❌ Nenhuma aposta realizada hoje.");
            } else {
                System.out.printf("📊 Total de apostas hoje: %d\n\n", bets.size());

                for (Aposta aposta : bets) {
                    displayBetSummary(aposta);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar apostas de hoje: " + e.getMessage());
        }

        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    private void handleAllBets() {
        System.out.println("\n📊 TODAS AS APOSTAS");
        System.out.println("=".repeat(60));

        try {
            var bets = betStorageService.getAllBets();

            if (bets.isEmpty()) {
                System.out.println("❌ Nenhuma aposta realizada ainda.");
            } else {
                System.out.printf("📊 Total de apostas: %d\n\n", bets.size());

                for (Aposta aposta : bets) {
                    displayBetSummary(aposta);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar apostas: " + e.getMessage());
        }

        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    private void handleSearchBet() {
        System.out.println("\n🔍 BUSCAR APOSTA POR CÓDIGO");
        System.out.println("=".repeat(60));
        System.out.print("Digite o código da aposta: ");

        if (scanner.hasNextLine()) {
            String code = scanner.nextLine().trim();

            try {
                var aposta = betStorageService.findBetByCode(code);

                if (aposta != null) {
                    System.out.println("\n✅ APOSTA ENCONTRADA");
                    System.out.println("-".repeat(60));
                    displayBetSummary(aposta);
                } else {
                    System.out.println("❌ Aposta não encontrada com o código: " + code);
                }

            } catch (Exception e) {
                System.err.println("❌ Erro ao buscar aposta: " + e.getMessage());
            }

            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }
    
    // === Métodos para seleção de data e horário ===
    
    private boolean handleDateSelection(String input) {
        LocalDate today = LocalDate.now();
        LocalDate selectedDate = null;
        
        switch (input) {
            case "1":
                selectedDate = today;
                navigationService.setSessionData("selected_date", selectedDate);
                navigationService.setSessionData("date_option", "hoje");
                break;
            case "2":
                selectedDate = today.plusDays(1);
                navigationService.setSessionData("selected_date", selectedDate);
                navigationService.setSessionData("date_option", "amanha");
                break;
            case "3":
                selectedDate = today.plusDays(2);
                navigationService.setSessionData("selected_date", selectedDate);
                navigationService.setSessionData("date_option", "depois_amanha");
                break;
            case "4":
                selectedDate = today.plusDays(3);
                navigationService.setSessionData("selected_date", selectedDate);
                navigationService.setSessionData("date_option", "3_dias");
                break;
            case "0":
                betSessionService.clearSession(); // Limpa apostas pendentes ao voltar ao menu principal
                currentNodeId = "welcome";
                return true;
            default:
                System.out.println("❌ Opção inválida. Escolha entre 1-4 ou 0 para voltar.");
                return true;
        }
        
        if (selectedDate != null) {
            System.out.printf("✅ Data selecionada: %s\n", selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            currentNodeId = "select_time";
        }
        
        return true;
    }
    
    private boolean handleTimeSelection(String input) {
        LocalDate selectedDate = (LocalDate) navigationService.getSessionData().get("selected_date");
        LocalTime currentTime = LocalTime.now();
        LocalDate today = LocalDate.now();
        
        String selectedTime = null;
        String timeDescription = null;
        
        switch (input) {
            case "1":
                selectedTime = "09:00";
                timeDescription = "09:00 - PTM (Paraíba)";
                break;
            case "2":
                selectedTime = "11:00_BS";
                timeDescription = "11:00 - BS (Bahia)";
                break;
            case "3":
                selectedTime = "11:00_RIO";
                timeDescription = "11:00 - RIO (Rio de Janeiro)";
                break;
            case "4":
                selectedTime = "14:00";
                timeDescription = "14:00 - PTM (Paraíba)";
                break;
            case "5":
                selectedTime = "16:00";
                timeDescription = "16:00 - PT (Paraíba)";
                break;
            case "6":
                selectedTime = "18:00";
                timeDescription = "18:00 - PTN (Paraíba)";
                break;
            case "7":
                selectedTime = "19:00";
                timeDescription = "19:00 - PTM (Paraíba)";
                break;
            case "8":
                selectedTime = "21:00";
                timeDescription = "21:00 - PTN (Paraíba)";
                break;
            case "0":
                currentNodeId = "select_date";
                return true;
            default:
                System.out.println("❌ Opção inválida. Escolha entre 1-8 ou 0 para voltar.");
                return true;
        }
        
        // Validar se o horário ainda é válido para hoje
        if (selectedDate.equals(today)) {
            if (selectedTime.equals("09:00") && currentTime.isAfter(LocalTime.of(9, 0))) {
                System.out.println("❌ Horário das 09:00 não está mais disponível para hoje (já passou das 09:00).");
                return true;
            }
            if ((selectedTime.equals("11:00_BS") || selectedTime.equals("11:00_RIO")) && currentTime.isAfter(LocalTime.of(11, 0))) {
                System.out.println("❌ Horário das 11:00 não está mais disponível para hoje (já passou das 11:00).");
                return true;
            }
            if (selectedTime.equals("14:00") && currentTime.isAfter(LocalTime.of(14, 0))) {
                System.out.println("❌ Horário das 14:00 não está mais disponível para hoje (já passou das 14:00).");
                return true;
            }
            if (selectedTime.equals("16:00") && currentTime.isAfter(LocalTime.of(16, 0))) {
                System.out.println("❌ Horário das 16:00 não está mais disponível para hoje (já passou das 16:00).");
                return true;
            }
            if (selectedTime.equals("18:00") && currentTime.isAfter(LocalTime.of(18, 0))) {
                System.out.println("❌ Horário das 18:00 não está mais disponível para hoje (já passou das 18:00).");
                return true;
            }
            if (selectedTime.equals("19:00") && currentTime.isAfter(LocalTime.of(19, 0))) {
                System.out.println("❌ Horário das 19:00 não está mais disponível para hoje (já passou das 19:00).");
                return true;
            }
            if (selectedTime.equals("21:00") && currentTime.isAfter(LocalTime.of(21, 0))) {
                System.out.println("❌ Horário das 21:00 não está mais disponível para hoje (já passou das 21:00).");
                return true;
            }
        }
        
        if (selectedTime != null) {
            navigationService.setSessionData("selected_time", selectedTime);
            navigationService.setSessionData("time_description", timeDescription);
            System.out.printf("✅ Horário selecionado: %s\n", timeDescription);
            System.out.printf("📅 Data: %s\n", selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            currentNodeId = "bet_menu";
        }
        
        return true;
    }
    
    // === Métodos para modalidades da centena ===
    
    private boolean handleCentenaModalityInput(String input) {
        if (input.equals("0")) {
            // Voltar ao menu anterior
            if (currentNodeId.equals("centena_especifico_numero")) {
                currentNodeId = "centena_premio_especifico";
            } else {
                currentNodeId = "select_centena";
            }
            return true;
        }
        
        String modalidade = getCurrentCentenaModality();
        
        // Validar entrada baseada na modalidade
        if (!isValidCentenaInput(input)) {
            System.out.println("❌ Digite uma centena válida (3 dígitos).");
            return true;
        }
        
        // Armazenar dados da sessão
        navigationService.setSessionData("centena", input);
        navigationService.setSessionData("centena_modalidade", modalidade);
        navigationService.setSessionData("tipo_aposta", "CENTENA");
        
        // Armazenar prêmio específico se aplicável
        if (currentNodeId.equals("centena_especifico_numero")) {
            String premio = (String) navigationService.getSessionData().get("premio_especifico");
            navigationService.setSessionData("premio_especifico", premio);
        }
        
        System.out.printf("✅ Centena %s selecionada: %s\n", modalidade, input);
        currentNodeId = "enter_value";
        return true;
    }
    
    private boolean handleCentenaPremioEspecificoSelection(String input) {
        if (input.equals("0")) {
            currentNodeId = "select_centena";
            return true;
        }
        
        try {
            int premio = Integer.parseInt(input);
            if (premio >= 1 && premio <= 7) {
                navigationService.setSessionData("premio_especifico", input);
                System.out.printf("✅ Prêmio específico selecionado: %dº prêmio\n", premio);
                currentNodeId = "centena_especifico_numero";
                return true;
            }
        } catch (NumberFormatException e) {
            // Continuar para mensagem de erro
        }
        
        System.out.println("❌ Escolha um prêmio válido (1-7) ou 0 para voltar.");
        return true;
    }
    
    private String getCurrentCentenaModality() {
        return switch (currentNodeId) {
            case "centena_cabeca" -> "no primeiro prêmio (cabeça)";
            case "centena_cercada_5" -> "cercada pelos cinco";
            case "centena_cercada_7" -> "cercada pelos sete";
            case "centena_cercada_mr" -> "cercada no MR";
            case "centena_especifico_numero" -> "em prêmio específico";
            case "centena_invertida" -> "invertida";
            default -> "simples";
        };
    }
    
    // === Métodos para modalidades da milhar ===
    
    private boolean handleMilharModalityInput(String input) {
        if (input.equals("0")) {
            // Voltar ao menu anterior
            if (currentNodeId.equals("milhar_especifico_numero")) {
                currentNodeId = "milhar_premio_especifico";
            } else {
                currentNodeId = "select_milhar";
            }
            return true;
        }
        
        String modalidade = getCurrentMilharModality();
        
        // Validar entrada baseada na modalidade
        if (currentNodeId.equals("milhar_invertida_combinada")) {
            if (!isValidMilharCombinadaInput(input)) {
                System.out.println("❌ Digite entre 5 e 10 dígitos para milhar combinada.");
                return true;
            }
        } else {
            if (!isValidMilharInput(input)) {
                System.out.println("❌ Digite uma milhar válida (4 dígitos).");
                return true;
            }
        }
        
        // Armazenar dados da aposta
        navigationService.setSessionData("tipo_aposta", "MILHAR");
        navigationService.setSessionData("milhar_modalidade", modalidade);
        navigationService.setSessionData("milhar", input);
        
        // Se for prêmio específico, armazenar qual prêmio
        if (currentNodeId.equals("milhar_especifico_numero")) {
            String premio = (String) navigationService.getSessionData().get("premio_especifico");
            navigationService.setSessionData("premio_especifico", premio);
        }
        
        System.out.printf("✅ Milhar %s selecionada: %s\n", modalidade, input);
        currentNodeId = "enter_value";
        return true;
    }
    
    private boolean handleMilharPremioEspecificoSelection(String input) {
        if (input.equals("0")) {
            currentNodeId = "select_milhar";
            return true;
        }
        
        try {
            int premio = Integer.parseInt(input);
            if (premio >= 1 && premio <= 5) {
                navigationService.setSessionData("premio_especifico", input + "º");
                System.out.printf("✅ Prêmio selecionado: %sº\n", input);
                currentNodeId = "milhar_especifico_numero";
                return true;
            }
        } catch (NumberFormatException e) {
            // Continuar para mensagem de erro
        }
        
        System.out.println("❌ Escolha um prêmio válido (1-5) ou 0 para voltar.");
        return true;
    }
    
    private String getCurrentMilharModality() {
        switch (currentNodeId) {
            case "milhar_cabeca":
                return "Cabeça (1º Prêmio)";
            case "milhar_cercada_5":
                return "Cercada pelos Cinco";
            case "milhar_invertida":
                return "Invertida";
            case "milhar_especifico_numero":
                String premio = (String) navigationService.getSessionData().get("premio_especifico");
                return "Prêmio Específico (" + premio + ")";
            case "milhar_invertida_combinada":
                return "Invertida & Combinada";
            default:
                return "Simples";
        }
    }
    
    private boolean isValidMilharCombinadaInput(String input) {
        return input.matches("\\d{5,10}");
    }

    private void displayBetSummary(Aposta aposta) {
        System.out.println("🎫 Código: " + aposta.getCodigoUnico());
        System.out.println("📅 Data: " + aposta.getDataAposta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("⏰ Horário: " + aposta.getHorario() + "h");
        System.out.println("💰 Valor: R$ " + String.format("%.2f", aposta.getValorDaAposta()));

        // Determine bet type based on available data
        String betType = "Desconhecido";
        String betValue = "";

        if (aposta.getGrupos() != null && !aposta.getGrupos().isEmpty()) {
            betType = "Grupo";
            betValue = aposta.getGrupos().get(0).getGrupo();
        } else if (aposta.getCentenas() != null && !aposta.getCentenas().isEmpty()) {
            betType = "Centena";
            betValue = aposta.getCentenas().get(0).getCentena();
        } else if (aposta.getMilhares() != null && !aposta.getMilhares().isEmpty()) {
            betType = "Milhar";
            betValue = aposta.getMilhares().get(0).getMilhar();
        } else if (aposta.getTernoGrupos() != null && !aposta.getTernoGrupos().isEmpty()) {
            betType = "Terno de Grupo";
            var terno = aposta.getTernoGrupos().get(0);
            betValue = terno.getGrupoUm() + " x " + terno.getGrupoDois() + " x " + terno.getGrupoTres();
        }

        System.out.println("🎯 Tipo: " + betType + " " + betValue);
        System.out.println("✅ Status: " + (aposta.getApostaValida() ? "Registrada" : "Inválida"));
        System.out.println("-".repeat(60));
    }

    private void handleExit() {
        System.out.println("\n🙏 Obrigado por usar o Sistema de Apostas!");
        System.out.println("Volte sempre! 🎰");
        scanner.close();
    }
}