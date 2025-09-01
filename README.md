# ChatBot Menu - Sistema de Atendimento por Opções

## Descrição
Sistema de chatbot baseado em menus e opções pré-determinadas, sem inteligência artificial. Ideal para atendimento automatizado com fluxos de decisão simples.

## Funcionalidades
- Navegação por menus hierárquicos
- Respostas pré-configuradas
- Histórico de conversas
- Interface via terminal (para testes)
- API REST para integração
- Interface web simples

## Como Executar

### 1. Modo Terminal (para testes rápidos)
```bash
# Compile o projeto
./gradlew build

# Execute no modo terminal
./gradlew bootRun --args='--terminal'
```

### 2. Modo Web/API
```bash
# Execute o servidor
./gradlew bootRun

# Acesse:
# - Interface Web: http://localhost:8080
# - API REST: http://localhost:8080/rest/v1/chat/health
# - Console H2: http://localhost:8080/h2-console
```

## Estrutura do Projeto

```
src/main/java/chatbot/
├── domain/model/          # Entidades de domínio
│   ├── MenuNode.java      # Nós do menu
│   ├── MenuOption.java    # Opções de menu
│   ├── MenuType.java      # Tipos de menu
│   ├── UserSession.java   # Sessão do usuário
│   └── UserChoice.java    # Escolhas do usuário
├── application/service/   # Lógica de negócio
│   └── MenuNavigationService.java
├── presentation/controller/ # API REST
│   └── ChatController.java
└── infrastructure/        # Infraestrutura
    ├── terminal/         # Interface terminal
    └── config/          # Configurações
```

## Estrutura de Menus

O sistema possui os seguintes menus configurados:

1. **Bem-vindo** (Menu Principal)
   - Informações sobre produtos
   - Suporte técnico
   - Falar com atendente
   - Sair

2. **Produtos**
   - Eletrônicos
   - Roupas
   - Voltar

3. **Suporte**
   - Problemas com pedido
   - Dúvidas sobre produto
   - Voltar

## API REST

### Endpoints

#### Iniciar Conversação
```http
POST /rest/v1/chat/start
```

#### Obter Menu Atual
```http
GET /rest/v1/chat/menu/{sessionId}
```

#### Processar Escolha
```http
POST /rest/v1/chat/choose?sessionId=xxx&optionId=1
```

#### Resetar Sessão
```http
POST /rest/v1/chat/reset/{sessionId}
```

#### Histórico da Sessão
```http
GET /rest/v1/chat/history/{sessionId}
```

### Exemplo de Uso

```bash
# Iniciar conversação
curl -X POST http://localhost:8080/rest/v1/chat/start

# Processar escolha (assumindo sessionId retornado)
curl -X POST "http://localhost:8080/rest/v1/chat/choose?sessionId=abc123&optionId=1"
```

## Configuração de Menus

Os menus são configurados em `src/main/resources/menu-config.json`. Para adicionar novos menus:

1. Adicione um novo objeto no array `menu_nodes`
2. Especifique o tipo (MAIN_MENU, SUB_MENU, INFORMATION, etc.)
3. Defina as opções com seus respectivos próximos nós

## Desenvolvimento

### Pré-requisitos
- Java 17 ou superior
- Gradle

### Executar Testes
```bash
./gradlew test
```

### Executar Aplicação em Desenvolvimento
```bash
./gradlew bootRun
```

## Tecnologias Utilizadas
- **Backend**: Spring Boot 3.5.4
- **Banco de Dados**: H2 (em memória)
- **Frontend**: HTML5, JavaScript (interface simples)
- **Build**: Gradle

## Próximos Passos

1. Adicionar persistência em banco real (PostgreSQL)
2. Implementar admin dashboard para configuração de menus
3. Adicionar suporte para múltiplos idiomas
4. Integrar com WhatsApp/Telegram
5. Adicionar analytics de uso
6. Implementar A/B testing de fluxos

## Contribuição

1. Faça fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request
