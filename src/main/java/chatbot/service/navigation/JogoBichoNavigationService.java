package chatbot.service.navigation;

import chatbot.domain.model.MenuNode;
import chatbot.domain.model.MenuOption;
import chatbot.domain.model.MenuType;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class JogoBichoNavigationService {
    private final Map<String, MenuNode> menuNodes;
    private final Map<String, Object> sessionData;
    
    public JogoBichoNavigationService() {
        this.menuNodes = new HashMap<>();
        this.sessionData = new HashMap<>();
        initializeMenuNodes();
    }
    
    private void initializeMenuNodes() {
        // Menu Principal
        MenuNode welcomeNode = new MenuNode("welcome", "🎰 SISTEMA DE APOSTAS - JOGO DO BICHO 🎰", "Selecione uma opção para continuar:", MenuType.MAIN_MENU);
        welcomeNode.setDescription("Selecione uma opção para continuar:");
        welcomeNode.setOptions(Arrays.asList(
            new MenuOption("1", "🎯 Fazer Aposta", "bet", "select_date", "Selecione para fazer uma aposta", "aposta"),
            new MenuOption("2", "📊 Ver Resultados", "view_results", "results", "Veja os últimos resultados", "resultados"),
            new MenuOption("3", "📋 Ver Tabela de Grupos", "view_groups", "groups", "Consulte a tabela de grupos", "grupos"),
            new MenuOption("4", "📖 Regras do Jogo", "rules", "rules", "Leia as regras de cada tipo de aposta", "regras"),
            new MenuOption("5", "💰 Ver Pagamentos", "check_payment", "payment", "Confira a tabela de pagamentos", "pagamentos"),
            new MenuOption("6", "📝 Ver Minhas Apostas", "view_bets", "view_bets", "Visualize suas apostas realizadas", "apostas"),
            new MenuOption("0", "❌ Sair", "exit", "exit", "Sair do sistema", "sair")
        ));
        menuNodes.put("welcome", welcomeNode);
        
        // Selecionar Data da Aposta
        MenuNode selectDateNode = new MenuNode("select_date", "📅 SELECIONAR DATA", "Para qual data deseja fazer a aposta?", MenuType.QUESTION);
        selectDateNode.setDescription("Para qual data deseja fazer a aposta?\n(Não é possível apostar em datas passadas ou mais de 3 dias no futuro)");
        selectDateNode.setOptions(Arrays.asList(
            new MenuOption("1", "Hoje", "select_time", "select_time", "Apostar para hoje", "hoje"),
            new MenuOption("2", "Amanhã", "select_time", "select_time", "Apostar para amanhã", "amanha"),
            new MenuOption("3", "Depois de amanhã", "select_time", "select_time", "Apostar para depois de amanhã", "depois_amanha"),
            new MenuOption("4", "Em 3 dias", "select_time", "select_time", "Apostar para daqui a 3 dias", "3_dias"),
            new MenuOption("0", "⬅️ Voltar", "welcome", "welcome", "Voltar ao menu principal", "voltar")
        ));
        menuNodes.put("select_date", selectDateNode);
        
        // Selecionar Horário da Aposta
        MenuNode selectTimeNode = new MenuNode("select_time", "⏰ SELECIONAR HORÁRIO", "Escolha o horário do sorteio:", MenuType.QUESTION);
        selectTimeNode.setDescription("Escolha o horário do sorteio:");
        selectTimeNode.setOptions(Arrays.asList(
            new MenuOption("1", "09:00 - PTM (Paraíba)", "select_time_action", "bet_menu", "Sorteio das 09h", "09h"),
            new MenuOption("2", "11:00 - BS (Bahia)", "select_time_action", "bet_menu", "Sorteio das 11h", "11h_bs"),
            new MenuOption("3", "11:00 - RIO (Rio de Janeiro)", "select_time_action", "bet_menu", "Sorteio das 11h", "11h_rio"),
            new MenuOption("4", "14:00 - PTM (Paraíba)", "select_time_action", "bet_menu", "Sorteio das 14h", "14h"),
            new MenuOption("5", "16:00 - PT (Paraíba)", "select_time_action", "bet_menu", "Sorteio das 16h", "16h"),
            new MenuOption("6", "18:00 - PTN (Paraíba)", "select_time_action", "bet_menu", "Sorteio das 18h", "18h"),
            new MenuOption("7", "19:00 - PTM (Paraíba)", "select_time_action", "bet_menu", "Sorteio das 19h", "19h"),
            new MenuOption("8", "21:00 - PTN (Paraíba)", "select_time_action", "bet_menu", "Sorteio das 21h", "21h"),
            new MenuOption("0", "⬅️ Voltar", "select_date", "select_date", "Voltar à seleção de data", "voltar")
        ));
        menuNodes.put("select_time", selectTimeNode);
        
        // Menu de Apostas
        MenuNode betMenuNode = new MenuNode("bet_menu", "🎯 TIPOS DE APOSTA", "Escolha o tipo de aposta:", MenuType.SUB_MENU);
        betMenuNode.setDescription("Escolha o tipo de aposta:");
        betMenuNode.setOptions(Arrays.asList(
            new MenuOption("1", "🐦 Grupo", "select_grupo", "select_grupo", "Aposte em um grupo específico", "grupo"),
            new MenuOption("2", "🔢 Centena", "select_centena", "select_centena", "Aposte em uma centena", "centena"),
            new MenuOption("3", "🔢 Milhar", "select_milhar", "select_milhar", "Aposte em uma milhar", "milhar"),
            new MenuOption("4", "🎲 Duque de Grupo", "select_duque_grupo", "select_duque_grupo", "Escolha 2 grupos", "duque"),
            new MenuOption("5", "🎲 Terno de Grupo", "select_terno_grupo", "select_terno_grupo", "Escolha 3 grupos", "terno"),
            new MenuOption("6", "🎲 Quadra de Grupo", "select_quadra_grupo", "select_quadra_grupo", "Escolha 4 grupos", "quadra"),
            new MenuOption("7", "🎲 Quina de Grupo", "select_quina_grupo", "select_quina_grupo", "Escolha 5-10 grupos", "quina"),
            new MenuOption("8", "🎲 Passe Direto", "select_passe_direto", "select_passe_direto", "2 grupos – ordem exata 1.º→2.º", "passe_direto"),
            new MenuOption("9", "🎲 Passe Ida-e-Volta", "select_passe_ida_volta", "select_passe_ida_volta", "2 grupos – ordem livre (1º/2º)", "passe_ida_volta"),
            new MenuOption("10", "🎲 Passe ao Quinto", "select_passe_ao_quinto", "select_passe_ao_quinto", "2 grupos – concorrem 1.º-5.º", "passe_quinto"),
            new MenuOption("11", "🔢 Dezena", "select_dezena", "select_dezena", "Aposte em uma dezena", "dezena"),
            new MenuOption("12", "🔢 Duque de Dezena", "select_duque_dezena", "select_duque_dezena", "Escolha 2 dezenas", "duque_dz"),
            new MenuOption("13", "🔢 Terno de Dezena", "select_terno_dezena", "select_terno_dezena", "Escolha 3 dezenas", "terno_dz"),
            new MenuOption("0", "⬅️ Voltar", "select_time", "select_time", "Voltar à seleção de horário", "voltar")
        ));
        menuNodes.put("bet_menu", betMenuNode);
        
        // Selecionar Grupo
        MenuNode selectGrupoNode = new MenuNode("select_grupo", "🐦 SELECIONAR GRUPO", "Escolha um grupo (1-25):", MenuType.QUESTION);
        selectGrupoNode.setDescription("Escolha um grupo (1-25):");
        selectGrupoNode.setOptions(createGrupoOptions());
        menuNodes.put("select_grupo", selectGrupoNode);
        
        // Selecionar Horário (nó antigo - será removido gradualmente)
        MenuNode selectHorarioNode = new MenuNode("select_horario", "⏰ SELECIONAR HORÁRIO", "Escolha o horário do sorteio:", MenuType.QUESTION);
        selectHorarioNode.setDescription("Escolha o horário do sorteio:");
        selectHorarioNode.setOptions(Arrays.asList(
            new MenuOption("1", "09:00 - PTM", "enter_value", "enter_value", "Sorteio das 09h", "09h"),
            new MenuOption("2", "11:00 - BS", "enter_value", "enter_value", "Sorteio das 11h", "11h_bs"),
            new MenuOption("3", "11:00 - RIO", "enter_value", "enter_value", "Sorteio das 11h", "11h_rio"),
            new MenuOption("4", "14:00 - PTM", "enter_value", "enter_value", "Sorteio das 14h", "14h"),
            new MenuOption("5", "16:00 - PT", "enter_value", "enter_value", "Sorteio das 16h", "16h"),
            new MenuOption("6", "18:00 - PTN", "enter_value", "enter_value", "Sorteio das 18h", "18h"),
            new MenuOption("7", "19:00 - PTM", "enter_value", "enter_value", "Sorteio das 19h", "19h"),
            new MenuOption("8", "21:00 - PTN", "enter_value", "enter_value", "Sorteio das 21h", "21h"),
            new MenuOption("0", "⬅️ Voltar", "bet_menu", "bet_menu", "Voltar ao menu de apostas", "voltar")
        ));
        menuNodes.put("select_horario", selectHorarioNode);
        
        // Inserir Valor
        MenuNode enterValueNode = new MenuNode("enter_value", "💰 VALOR DA APOSTA", "Digite o valor que deseja apostar:", MenuType.QUESTION);
        enterValueNode.setDescription("Digite o valor que deseja apostar:");

        // Selecionar Centena - Menu de Opções
        MenuNode selectCentenaNode = new MenuNode("select_centena", "🔢 MODALIDADES DA CENTENA", "Escolha o tipo de aposta em centena:", MenuType.SUB_MENU);
        selectCentenaNode.setDescription("Escolha o tipo de aposta em centena:");
        selectCentenaNode.setOptions(Arrays.asList(
            new MenuOption("1", "🎯 Centena no Primeiro Prêmio (Cabeça)", "centena_cabeca", "centena_cabeca", "Acerto exclusivo do 1º prêmio", "cabeca"),
            new MenuOption("2", "🎯 Centena Cercada pelos Cinco", "centena_cercada_5", "centena_cercada_5", "Válida do 1º ao 5º prêmio", "cercada_5"),
            new MenuOption("3", "🎯 Centena Cercada pelos Sete", "centena_cercada_7", "centena_cercada_7", "Válida do 1º ao 7º prêmio", "cercada_7"),
            new MenuOption("4", "🎯 Centena Cercada no MR", "centena_cercada_mr", "centena_cercada_mr", "Válida no 6º e 7º prêmio", "cercada_mr"),
            new MenuOption("5", "🎯 Centena em Prêmio Específico", "centena_premio_especifico", "centena_premio_especifico", "Só vence no prêmio indicado", "especifico"),
            new MenuOption("6", "🔄 Centena Invertida", "centena_invertida", "centena_invertida", "Qualquer permutação dos 3 dígitos", "invertida"),
            new MenuOption("0", "⬅️ Voltar", "bet_menu", "bet_menu", "Voltar ao menu de apostas", "voltar")
        ));
        menuNodes.put("select_centena", selectCentenaNode);
        
        // Centena no Primeiro Prêmio (Cabeça)
        MenuNode centenaCabecaNode = new MenuNode("centena_cabeca", "🎯 CENTENA NO PRIMEIRO PRÊMIO", "Digite a centena (000-999):", MenuType.QUESTION);
        centenaCabecaNode.setDescription("Digite a centena (3 dígitos):");
        centenaCabecaNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "select_centena", "select_centena", "Voltar às modalidades da centena", "voltar")
        ));
        menuNodes.put("centena_cabeca", centenaCabecaNode);
        
        // Centena Cercada pelos Cinco
        MenuNode centenaCercada5Node = new MenuNode("centena_cercada_5", "🎯 CENTENA CERCADA PELOS CINCO", "Digite a centena (000-999):", MenuType.QUESTION);
        centenaCercada5Node.setDescription("Digite a centena (3 dígitos) - válida do 1º ao 5º prêmio:");
        centenaCercada5Node.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "select_centena", "select_centena", "Voltar às modalidades da centena", "voltar")
        ));
        menuNodes.put("centena_cercada_5", centenaCercada5Node);
        
        // Centena Cercada pelos Sete
        MenuNode centenaCercada7Node = new MenuNode("centena_cercada_7", "🎯 CENTENA CERCADA PELOS SETE", "Digite a centena (000-999):", MenuType.QUESTION);
        centenaCercada7Node.setDescription("Digite a centena (3 dígitos) - válida do 1º ao 7º prêmio:");
        centenaCercada7Node.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "select_centena", "select_centena", "Voltar às modalidades da centena", "voltar")
        ));
        menuNodes.put("centena_cercada_7", centenaCercada7Node);
        
        // Centena Cercada no MR
        MenuNode centenaCercadaMRNode = new MenuNode("centena_cercada_mr", "🎯 CENTENA CERCADA NO MR", "Digite a centena (000-999):", MenuType.QUESTION);
        centenaCercadaMRNode.setDescription("Digite a centena (3 dígitos) - válida no 6º e 7º prêmio:");
        centenaCercadaMRNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "select_centena", "select_centena", "Voltar às modalidades da centena", "voltar")
        ));
        menuNodes.put("centena_cercada_mr", centenaCercadaMRNode);
        
        // Centena em Prêmio Específico
        MenuNode centenaEspecificoNode = new MenuNode("centena_premio_especifico", "🎯 CENTENA EM PRÊMIO ESPECÍFICO", "Escolha o prêmio específico:", MenuType.QUESTION);
        centenaEspecificoNode.setDescription("Em qual prêmio específico deseja apostar?");
        centenaEspecificoNode.setOptions(Arrays.asList(
            new MenuOption("1", "1º Prêmio", "centena_especifico_numero", "centena_especifico_numero", "Apostar no 1º prêmio", "1"),
            new MenuOption("2", "2º Prêmio", "centena_especifico_numero", "centena_especifico_numero", "Apostar no 2º prêmio", "2"),
            new MenuOption("3", "3º Prêmio", "centena_especifico_numero", "centena_especifico_numero", "Apostar no 3º prêmio", "3"),
            new MenuOption("4", "4º Prêmio", "centena_especifico_numero", "centena_especifico_numero", "Apostar no 4º prêmio", "4"),
            new MenuOption("5", "5º Prêmio", "centena_especifico_numero", "centena_especifico_numero", "Apostar no 5º prêmio", "5"),
            new MenuOption("6", "6º Prêmio", "centena_especifico_numero", "centena_especifico_numero", "Apostar no 6º prêmio", "6"),
            new MenuOption("7", "7º Prêmio", "centena_especifico_numero", "centena_especifico_numero", "Apostar no 7º prêmio", "7"),
            new MenuOption("0", "⬅️ Voltar", "select_centena", "select_centena", "Voltar às modalidades da centena", "voltar")
        ));
        menuNodes.put("centena_premio_especifico", centenaEspecificoNode);
        
        // Centena Específico - Digite o Número
        MenuNode centenaEspecificoNumeroNode = new MenuNode("centena_especifico_numero", "🎯 DIGITE A CENTENA", "Digite a centena (000-999):", MenuType.QUESTION);
        centenaEspecificoNumeroNode.setDescription("Digite a centena (3 dígitos):");
        centenaEspecificoNumeroNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "centena_premio_especifico", "centena_premio_especifico", "Voltar à seleção de prêmio", "voltar")
        ));
        menuNodes.put("centena_especifico_numero", centenaEspecificoNumeroNode);
        
        // Centena Invertida
        MenuNode centenaInvertidaNode = new MenuNode("centena_invertida", "🔄 CENTENA INVERTIDA", "Digite a centena (000-999):", MenuType.QUESTION);
        centenaInvertidaNode.setDescription("Digite a centena (3 dígitos) - qualquer permutação vale:");
        centenaInvertidaNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "select_centena", "select_centena", "Voltar às modalidades da centena", "voltar")
        ));
        menuNodes.put("centena_invertida", centenaInvertidaNode);

        // Selecionar Milhar - Menu de Opções
        MenuNode selectMilharNode = new MenuNode("select_milhar", "🔢 MODALIDADES DA MILHAR", "Escolha o tipo de aposta em milhar:", MenuType.SUB_MENU);
        selectMilharNode.setDescription("Escolha o tipo de aposta em milhar:");
        selectMilharNode.setOptions(Arrays.asList(
            new MenuOption("1", "🎯 Milhar no Primeiro Prêmio (Cabeça)", "milhar_cabeca", "milhar_cabeca", "Acerto exclusivo do 1º prêmio", "cabeca"),
            new MenuOption("2", "🎯 Milhar Cercada pelos Cinco", "milhar_cercada_5", "milhar_cercada_5", "Válida do 1º ao 5º prêmio", "cercada_5"),
            new MenuOption("3", "🎯 Milhar em Prêmio Específico", "milhar_premio_especifico", "milhar_premio_especifico", "Só vence no prêmio indicado", "especifico"),
            new MenuOption("4", "🎯 Milhar + Centena no Primeiro", "milhar_centena_primeiro", "milhar_centena_primeiro", "Paga integral milhar ou parcial centena", "milhar_centena"),
            new MenuOption("5", "🎯 Milhar + Centena Cercadas pelos Cinco", "milhar_centena_5", "milhar_centena_5", "Milhar e centena do 1º ao 5º", "milhar_centena_5"),
            new MenuOption("6", "🎯 Milhar + Centena Cercadas pelos Sete", "milhar_centena_7", "milhar_centena_7", "5 milhares + 7 centenas", "milhar_centena_7"),
            new MenuOption("7", "🔄 Milhar Invertida", "milhar_invertida", "milhar_invertida", "Qualquer permutação dos 4 dígitos", "invertida"),
            new MenuOption("8", "🔄 Milhar Invertida & Combinada", "milhar_invertida_combinada", "milhar_invertida_combinada", "Mais de 4 dígitos com inversões", "invertida_combinada"),
            new MenuOption("0", "⬅️ Voltar", "bet_menu", "bet_menu", "Voltar ao menu de apostas", "voltar")
        ));
        menuNodes.put("select_milhar", selectMilharNode);
        
        // Milhar no Primeiro Prêmio (Cabeça)
        MenuNode milharCabecaNode = new MenuNode("milhar_cabeca", "🎯 MILHAR NO PRIMEIRO PRÊMIO", "Digite a milhar (0000-9999):", MenuType.QUESTION);
        milharCabecaNode.setDescription("Digite a milhar (4 dígitos):");
        milharCabecaNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "select_milhar", "select_milhar", "Voltar às modalidades da milhar", "voltar")
        ));
        menuNodes.put("milhar_cabeca", milharCabecaNode);
        
        // Milhar Cercada pelos Cinco
        MenuNode milharCercada5Node = new MenuNode("milhar_cercada_5", "🎯 MILHAR CERCADA PELOS CINCO", "Digite a milhar (0000-9999):", MenuType.QUESTION);
        milharCercada5Node.setDescription("Digite a milhar (4 dígitos) - válida do 1º ao 5º prêmio:");
        milharCercada5Node.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "select_milhar", "select_milhar", "Voltar às modalidades da milhar", "voltar")
        ));
        menuNodes.put("milhar_cercada_5", milharCercada5Node);
        
        // Milhar em Prêmio Específico
        MenuNode milharEspecificoNode = new MenuNode("milhar_premio_especifico", "🎯 MILHAR EM PRÊMIO ESPECÍFICO", "Escolha o prêmio específico:", MenuType.QUESTION);
        milharEspecificoNode.setDescription("Em qual prêmio específico deseja apostar?");
        milharEspecificoNode.setOptions(Arrays.asList(
            new MenuOption("1", "1º Prêmio", "milhar_especifico_numero", "milhar_especifico_numero", "Apostar no 1º prêmio", "1"),
            new MenuOption("2", "2º Prêmio", "milhar_especifico_numero", "milhar_especifico_numero", "Apostar no 2º prêmio", "2"),
            new MenuOption("3", "3º Prêmio", "milhar_especifico_numero", "milhar_especifico_numero", "Apostar no 3º prêmio", "3"),
            new MenuOption("4", "4º Prêmio", "milhar_especifico_numero", "milhar_especifico_numero", "Apostar no 4º prêmio", "4"),
            new MenuOption("5", "5º Prêmio", "milhar_especifico_numero", "milhar_especifico_numero", "Apostar no 5º prêmio", "5"),
            new MenuOption("0", "⬅️ Voltar", "select_milhar", "select_milhar", "Voltar às modalidades da milhar", "voltar")
        ));
        menuNodes.put("milhar_premio_especifico", milharEspecificoNode);
        
        // Milhar Específico - Digite o Número
        MenuNode milharEspecificoNumeroNode = new MenuNode("milhar_especifico_numero", "🎯 DIGITE A MILHAR", "Digite a milhar (0000-9999):", MenuType.QUESTION);
        milharEspecificoNumeroNode.setDescription("Digite a milhar (4 dígitos):");
        milharEspecificoNumeroNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "milhar_premio_especifico", "milhar_premio_especifico", "Voltar à seleção de prêmio", "voltar")
        ));
        menuNodes.put("milhar_especifico_numero", milharEspecificoNumeroNode);
        
        // Milhar Invertida
        MenuNode milharInvertidaNode = new MenuNode("milhar_invertida", "🔄 MILHAR INVERTIDA", "Digite a milhar (0000-9999):", MenuType.QUESTION);
        milharInvertidaNode.setDescription("Digite a milhar (4 dígitos) - qualquer permutação vale:");
        milharInvertidaNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "select_milhar", "select_milhar", "Voltar às modalidades da milhar", "voltar")
        ));
        menuNodes.put("milhar_invertida", milharInvertidaNode);
        
        // Milhar Invertida & Combinada
        MenuNode milharInvertidaCombinadaNode = new MenuNode("milhar_invertida_combinada", "🔄 MILHAR INVERTIDA & COMBINADA", "Digite os números (máximo 10 caracteres):", MenuType.QUESTION);
        milharInvertidaCombinadaNode.setDescription("Digite mais de 4 dígitos (ex: 12345) - a banca gera todas as milhares possíveis:");
        milharInvertidaCombinadaNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "select_milhar", "select_milhar", "Voltar às modalidades da milhar", "voltar")
        ));
        menuNodes.put("milhar_invertida_combinada", milharInvertidaCombinadaNode);

        // Duque de Grupo (placeholder)
        MenuNode selectDuqueGrupoNode = new MenuNode("select_duque_grupo", "🎲 DUQUE DE GRUPO", "Selecione dois grupos (em desenvolvimento):", MenuType.INFORMATION);
        selectDuqueGrupoNode.setDescription("Funcionalidade em desenvolvimento.");
        selectDuqueGrupoNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "bet_menu", "bet_menu", "Voltar ao menu de apostas", "voltar")
        ));
        menuNodes.put("select_duque_grupo", selectDuqueGrupoNode);

        // Terno de Grupo (placeholder)
        MenuNode selectTernoGrupoNode = new MenuNode("select_terno_grupo", "🎲 TERNO DE GRUPO", "Selecione três grupos (em desenvolvimento):", MenuType.INFORMATION);
        selectTernoGrupoNode.setDescription("Funcionalidade em desenvolvimento.");
        selectTernoGrupoNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "bet_menu", "bet_menu", "Voltar ao menu de apostas", "voltar")
        ));
        menuNodes.put("select_terno_grupo", selectTernoGrupoNode);

        // Quadra de Grupo (placeholder)
        MenuNode selectQuadraGrupoNode = new MenuNode("select_quadra_grupo", "🎲 QUADRA DE GRUPO", "Selecione quatro grupos (em desenvolvimento):", MenuType.INFORMATION);
        selectQuadraGrupoNode.setDescription("Funcionalidade em desenvolvimento.");
        selectQuadraGrupoNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "bet_menu", "bet_menu", "Voltar ao menu de apostas", "voltar")
        ));
        menuNodes.put("select_quadra_grupo", selectQuadraGrupoNode);

        // Quina de Grupo - seleção de grupos
        MenuNode selectQuinaGrupoNode = new MenuNode("select_quina_grupo", "🎲 QUINA DE GRUPO", getGroupsTable() + "\n\nDigite o número do grupo desejado (1-25).", MenuType.INFORMATION);
        selectQuinaGrupoNode.setDescription("Selecione os grupos para a sua Quina:");
        selectQuinaGrupoNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "bet_menu", "bet_menu", "Voltar ao menu de apostas", "voltar")
        ));
        menuNodes.put("select_quina_grupo", selectQuinaGrupoNode);

        // >>> NOVO: quantidade para Quina de Grupo <<<
        MenuNode selectQuinaGrupoCountNode = new MenuNode("select_quina_grupo_count", "🎲 QUINA DE GRUPO – QUANTIDADE", "Quantos grupos deseja escolher? (5-10):", MenuType.QUESTION);
        selectQuinaGrupoCountNode.setDescription("Informe a quantidade de grupos (5 a 10):");
        selectQuinaGrupoCountNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "bet_menu", "bet_menu", "Voltar ao menu de apostas", "voltar")
        ));
        menuNodes.put("select_quina_grupo_count", selectQuinaGrupoCountNode);
        enterValueNode.setOptions(Arrays.asList(
            new MenuOption("1", "R$ 1,00", "confirm_bet", "confirm_bet", "Apostar R$ 1,00", "1"),
            new MenuOption("2", "R$ 2,00", "confirm_bet", "confirm_bet", "Apostar R$ 2,00", "2"),
            new MenuOption("3", "R$ 5,00", "confirm_bet", "confirm_bet", "Apostar R$ 5,00", "5"),
            new MenuOption("4", "R$ 10,00", "confirm_bet", "confirm_bet", "Apostar R$ 10,00", "10"),
            new MenuOption("5", "Outro valor", "confirm_bet", "confirm_bet", "Digitar outro valor", "outro"),
            new MenuOption("0", "⬅️ Voltar", "select_horario", "select_horario", "Voltar à seleção de horário", "voltar")
        ));
        menuNodes.put("enter_value", enterValueNode);
        
        // Confirmar Aposta
        MenuNode confirmBetNode = new MenuNode("confirm_bet", "✅ CONFIRMAR APOSTA", "Confirme os detalhes da sua aposta:", MenuType.QUESTION);
        confirmBetNode.setDescription("Confirme os detalhes da sua aposta:");
        confirmBetNode.setOptions(Arrays.asList(
            new MenuOption("1", "✅ Confirmar", "finalize_bet", "welcome", "Confirmar aposta", "confirmar"),
            new MenuOption("2", "❌ Cancelar", "bet_menu", "bet_menu", "Cancelar aposta", "cancelar")
        ));
        menuNodes.put("confirm_bet", confirmBetNode);
        
        // Resultados
        MenuNode resultsNode = new MenuNode("results", "📊 ÚLTIMOS RESULTADOS", "Resultados dos últimos sorteios:", MenuType.INFORMATION);
        resultsNode.setDescription("Resultados dos últimos sorteios:");
        resultsNode.setOptions(Arrays.asList(
            new MenuOption("1", "📅 Ver resultados de hoje", "today_results", "today_results", "Resultados do dia", "hoje"),
            new MenuOption("2", "📊 Ver resultados da semana", "week_results", "week_results", "Resultados da semana", "semana"),
            new MenuOption("0", "⬅️ Voltar", "welcome", "welcome", "Voltar ao menu principal", "voltar")
        ));
        menuNodes.put("results", resultsNode);
        
        // Tabela de Grupos
        MenuNode groupsNode = new MenuNode("groups", "📋 TABELA DE GRUPOS", "Grupos do Jogo do Bicho:", MenuType.INFORMATION);
        groupsNode.setDescription("Grupos do Jogo do Bicho:");
        groupsNode.setOptions(Arrays.asList(
            new MenuOption("1", "📖 Ver tabela completa", "show_groups", "show_groups", "Ver todos os grupos", "completa"),
            new MenuOption("2", "🔍 Buscar grupo por número", "search_group", "search_group", "Buscar grupo específico", "buscar"),
            new MenuOption("0", "⬅️ Voltar", "welcome", "welcome", "Voltar ao menu principal", "voltar")
        ));
        menuNodes.put("groups", groupsNode);
        
        // Regras
        MenuNode rulesNode = new MenuNode("rules", "📖 REGRAS DO JOGO DO BICHO", "Conheça as regras de cada tipo de aposta:", MenuType.INFORMATION);
        rulesNode.setDescription("Conheça as regras de cada tipo de aposta:");
        rulesNode.setOptions(Arrays.asList(
            new MenuOption("1", "🐦 Regras - Grupo", "rules_grupo", "rules_grupo", "Regras para apostas em grupo", "grupo"),
            new MenuOption("2", "🔢 Regras - Centena", "rules_centena", "rules_centena", "Regras para apostas em centena", "centena"),
            new MenuOption("3", "🔢 Regras - Milhar", "rules_milhar", "rules_milhar", "Regras para apostas em milhar", "milhar"),
            new MenuOption("4", "🎲 Regras - Múltiplos", "rules_multiplos", "rules_multiplos", "Regras para apostas múltiplas", "multiplos"),
            new MenuOption("0", "⬅️ Voltar", "welcome", "welcome", "Voltar ao menu principal", "voltar")
        ));
        menuNodes.put("rules", rulesNode);
        
        // Pagamentos
        MenuNode paymentNode = new MenuNode("payment", "💰 TABELA DE PAGAMENTOS", "Confira os valores de pagamento:", MenuType.INFORMATION);
        paymentNode.setDescription("Confira os valores de pagamento:");
        paymentNode.setOptions(Arrays.asList(
            new MenuOption("1", "📊 Ver tabela completa", "show_payments", "show_payments", "Ver tabela completa de pagamentos", "completa"),
            new MenuOption("2", "💡 Dicas de aposta", "bet_tips", "bet_tips", "Dicas para melhores apostas", "dicas"),
            new MenuOption("0", "⬅️ Voltar", "welcome", "welcome", "Voltar ao menu principal", "voltar")
        ));
        menuNodes.put("payment", paymentNode);

        // Visualizar Apostas
        MenuNode viewBetsNode = new MenuNode("view_bets", "📝 MINHAS APOSTAS", "Visualize suas apostas realizadas:", MenuType.INFORMATION);
        viewBetsNode.setDescription("Visualize suas apostas realizadas:");
        viewBetsNode.setOptions(Arrays.asList(
            new MenuOption("1", "📅 Ver apostas de hoje", "today_bets", "today_bets", "Apostas realizadas hoje", "hoje"),
            new MenuOption("2", "📊 Ver todas as apostas", "all_bets", "all_bets", "Todas as suas apostas", "todas"),
            new MenuOption("3", "🔍 Buscar por código", "search_bet", "search_bet", "Buscar aposta por código", "buscar"),
            new MenuOption("0", "⬅️ Voltar", "welcome", "welcome", "Voltar ao menu principal", "voltar")
        ));
        menuNodes.put("view_bets", viewBetsNode);
        
        // Additional nodes for functionality
        addInformationNodes();
    }
    
    private void addInformationNodes() {
        // Show groups
        MenuNode showGroupsNode = new MenuNode("show_groups", "📋 TABELA COMPLETA DE GRUPOS", getGroupsTable(), MenuType.INFORMATION);
        showGroupsNode.setDescription(getGroupsTable());
        showGroupsNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "groups", "groups", "Voltar", "voltar")
        ));
        menuNodes.put("show_groups", showGroupsNode);
        
        // Show payments
        MenuNode showPaymentsNode = new MenuNode("show_payments", "📊 TABELA DE PAGAMENTOS", getPaymentsTable(), MenuType.INFORMATION);
        showPaymentsNode.setDescription(getPaymentsTable());
        showPaymentsNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "payment", "payment", "Voltar", "voltar")
        ));
        menuNodes.put("show_payments", showPaymentsNode);
        
        // Rules grupo
        MenuNode rulesGrupoNode = new MenuNode("rules_grupo", "🐦 REGRAS - GRUPO", getGrupoRules(), MenuType.INFORMATION);
        rulesGrupoNode.setDescription(getGrupoRules());
        rulesGrupoNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "rules", "rules", "Voltar", "voltar")
        ));
        menuNodes.put("rules_grupo", rulesGrupoNode);
        
        // Today bets
        MenuNode todayBetsNode = new MenuNode("today_bets", "📅 APOSTAS DE HOJE", "Carregando apostas de hoje...", MenuType.INFORMATION);
        todayBetsNode.setDescription("Apostas realizadas hoje");
        todayBetsNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "view_bets", "view_bets", "Voltar", "voltar")
        ));
        menuNodes.put("today_bets", todayBetsNode);
        
        // All bets
        MenuNode allBetsNode = new MenuNode("all_bets", "📊 TODAS AS APOSTAS", "Carregando todas as apostas...", MenuType.INFORMATION);
        allBetsNode.setDescription("Todas as suas apostas realizadas");
        allBetsNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "view_bets", "view_bets", "Voltar", "voltar")
        ));
        menuNodes.put("all_bets", allBetsNode);
        
        // Search bet
        MenuNode searchBetNode = new MenuNode("search_bet", "🔍 BUSCAR APOSTA", "Digite o código da aposta:", MenuType.QUESTION);
        searchBetNode.setDescription("Buscar aposta por código");
        searchBetNode.setOptions(Arrays.asList(
            new MenuOption("0", "⬅️ Voltar", "view_bets", "view_bets", "Voltar", "voltar")
        ));
        menuNodes.put("search_bet", searchBetNode);
    }
    
    private List<MenuOption> createGrupoOptions() {
        List<MenuOption> options = new ArrayList<>();
        String[] grupos = {
            "Avestruz", "Águia", "Burro", "Borboleta", "Cachorro",
            "Cabra", "Carneiro", "Camelo", "Cobra", "Coelho",
            "Cavalo", "Elefante", "Galo", "Gato", "Jacaré",
            "Leão", "Macaco", "Porco", "Pavão", "Peru",
            "Touro", "Tigre", "Urso", "Veado", "Vaca"
        };
        
        for (int i = 1; i <= 25; i++) {
            options.add(new MenuOption(
                String.valueOf(i),
                String.format("%02d - %s", i, grupos[i-1]),
                "select_horario",
                "select_horario",
                String.format("Grupo %02d - %s", i, grupos[i-1]),
                String.valueOf(i)
            ));
        }
        
        options.add(new MenuOption("0", "⬅️ Voltar", "bet_menu", "bet_menu", "Voltar ao menu de apostas", "voltar"));
        return options;
    }
    
    private String getGroupsTable() {
        return "\n📋 TABELA DE GRUPOS DO JOGO DO BICHO\n" +
               "=====================================\n\n" +
               "01 - Avestruz    14 - Gato\n" +
               "02 - Águia       15 - Jacaré\n" +
               "03 - Burro       16 - Leão\n" +
               "04 - Borboleta   17 - Macaco\n" +
               "05 - Cachorro    18 - Porco\n" +
               "06 - Cabra       19 - Pavão\n" +
               "07 - Carneiro    20 - Peru\n" +
               "08 - Camelo      21 - Touro\n" +
               "09 - Cobra       22 - Tigre\n" +
               "10 - Coelho      23 - Urso\n" +
               "11 - Cavalo      24 - Veado\n" +
               "12 - Elefante    25 - Vaca\n" +
               "13 - Galo\n\n" +
               "Cada grupo corresponde a 4 dezenas no jogo.\n";
    }
    
    private String getPaymentsTable() {
        return "\n💰 TABELA DE PAGAMENTOS\n" +
               "======================\n\n" +
               "🐦 GRUPO...............: 18x\n" +
               "🔢 CENTENA.............: 600x\n" +
               "🔢 MILHAR..............: 4000x\n" +
               "🎲 DUQUE DE GRUPO.....: 18x\n" +
               "🎲 TERNO DE GRUPO.....: 130x\n" +
               "🎲 QUADRA DE GRUPO...: 500x\n" +
               "🎲 QUINA DE GRUPO....: 20000x\n\n" +
               "Os pagamentos são feitos conforme o valor apostado.\n";
    }
    
    private String getGrupoRules() {
        return "\n🐦 REGRAS - APOSTA EM GRUPO\n" +
               "===========================\n\n" +
               "• Escolha um grupo de 1 a 25\n" +
               "• Cada grupo representa 4 dezenas\n" +
               "• Se o grupo escolhido sair no sorteio, você ganha\n" +
               "• Pagamento: 18x o valor apostado\n\n" +
               "Exemplo: Apostou R$ 10 no grupo 5 (Cão) e saiu Cão = R$ 180,00\n";
    }
    
    public MenuNode getNode(String nodeId) {
        return menuNodes.get(nodeId);
    }
    
    public Map<String, Object> getSessionData() {
        return sessionData;
    }
    
    public void setSessionData(String key, Object value) {
        sessionData.put(key, value);
    }
    
    public void clearSessionData() {
        sessionData.clear();
    }
    
    public boolean isValidValor(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ONE) >= 0;
    }
    
    public String getGrupoName(int grupoNum) {
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