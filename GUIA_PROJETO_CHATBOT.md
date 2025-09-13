# 🤖 Guia do Projeto: Chatbot
## Assistente Virtual Inteligente

> **🎯 Contexto**: Microserviço responsável pelo chatbot inteligente da plataforma, processamento de linguagem natural, integração com IA externa e atendimento automatizado aos usuários.

---

## 📋 INFORMAÇÕES DO PROJETO

### **Identificação:**
- **Nome**: conexao-de-sorte-backend-chatbot
- **Porta**: 8089
- **Rede Principal**: conexao-network-swarm
- **Database**: conexao_chatbot (MySQL 8.4)
- **Runner**: `[self-hosted, Linux, X64, conexao, conexao-de-sorte-backend-chatbot]`

### **Tecnologias Específicas:**
- Spring Boot 3.5.5 + Spring WebFlux (reativo)
- R2DBC MySQL (persistência reativa)
- OpenAI/Azure OpenAI integration
- Natural Language Processing (NLP)
- WebSocket (real-time chat)

---

## 🗄️ ESTRUTURA DO BANCO DE DADOS

### **Database**: `conexao_chatbot`

#### **Tabelas:**
1. **`conversas_bot`** - Conversas com o chatbot
2. **`mensagens_bot`** - Mensagens da conversa
3. **`intencoes_bot`** - Intenções de diálogo treinadas
4. **`conhecimento_base`** - Base de conhecimento do bot

#### **Estrutura das Tabelas:**
```sql
-- conversas_bot
id (String PK)
usuario_id (String)             -- Referência para autenticacao.usuarios.id
session_id (String)             -- ID da sessão
canal (String)                  -- WEB, MOBILE, WHATSAPP
status (String)                 -- ATIVA, FINALIZADA, TRANSFERIDA
idioma (String)                 -- pt-BR, en, es
contexto (JSON)                 -- Contexto da conversa
metadados (JSON)               -- Dados adicionais
iniciada_em (DateTime)
finalizada_em (DateTime)
atualizada_em (DateTime)

-- mensagens_bot
id (String PK)
conversa_id (String)           -- FK para conversas_bot
remetente (String)             -- USER, BOT, HUMAN_AGENT
conteudo (TEXT)
tipo_mensagem (String)         -- TEXT, IMAGE, QUICK_REPLY, CARD
intencao_detectada (String)    -- Intenção identificada pelo NLP
confianca_score (Decimal)      -- Score de confiança (0-1)
processamento_ms (Integer)     -- Tempo de processamento
resposta_gerada (TEXT)        -- Resposta gerada pela IA
feedback_usuario (String)      -- POSITIVO, NEGATIVO, NEUTRO
timestamp (DateTime)

-- intencoes_bot
id (String PK)
nome_intencao (String)         -- Nome da intenção
categoria (String)             -- SUPORTE, VENDAS, INFORMACAO
exemplos_treino (JSON)         -- Exemplos de frases para treino
respostas_padrao (JSON)        -- Respostas padrão para a intenção
ativa (Boolean)
confianca_minima (Decimal)     -- Score mínimo para trigger
acoes_integradas (JSON)        -- Ações a executar
criada_em (DateTime)
atualizada_em (DateTime)

-- conhecimento_base
id (String PK)
titulo (String)
categoria (String)             -- FAQ, TUTORIAL, POLITICA
conteudo (TEXT)
palavras_chave (JSON)          -- Keywords para busca
prioridade (Integer)           -- Prioridade na busca
ativo (Boolean)
visualizacoes (Integer)        -- Contador de acessos
criado_por (String)
criado_em (DateTime)
atualizado_em (DateTime)
```

#### **Relacionamentos Inter-Serviços:**
- conversas_bot.usuario_id → autenticacao.usuarios.id

### **Configuração R2DBC:**
```yaml
r2dbc:
  url: r2dbc:mysql://mysql-proxy:6033/conexao_chatbot
  pool:
    initial-size: 2
    max-size: 15
```

---

## 🔐 SECRETS ESPECÍFICOS

### **Azure Key Vault Secrets Utilizados:**
```yaml
# Database
conexao-de-sorte-database-r2dbc-url
conexao-de-sorte-database-username
conexao-de-sorte-database-password

# Redis Cache
conexao-de-sorte-redis-host
conexao-de-sorte-redis-password
conexao-de-sorte-redis-port

# JWT for service-to-service
conexao-de-sorte-jwt-secret
conexao-de-sorte-jwt-verification-key

# AI/ML Services
conexao-de-sorte-openai-api-key
conexao-de-sorte-azure-cognitive-key
conexao-de-sorte-azure-cognitive-endpoint

# External Integrations
conexao-de-sorte-whatsapp-api-key
conexao-de-sorte-telegram-bot-token
conexao-de-sorte-facebook-page-token

# NLP Configuration
conexao-de-sorte-nlp-model-path
conexao-de-sorte-sentiment-api-key
```

### **Cache Redis Específico:**
```yaml
redis:
  database: 11
  cache-names:
    - chatbot:sessions
    - chatbot:intencoes
    - chatbot:knowledge-base
    - chatbot:user-context
```

---

## 🌐 INTEGRAÇÃO DE REDE

### **Comunicação Entrada (Server):**
- **Frontend/Mobile** → Chatbot (interface de chat)
- **Gateway** → Chatbot (rotas /api/chatbot/*)
- **WhatsApp/Telegram** → Chatbot (webhooks externos)
- **Bate-papo** → Chatbot (escalação para humanos)

### **Comunicação Saída (Client):**
- Chatbot → **OpenAI/Azure OpenAI** (processamento NLP)
- Chatbot → **Autenticação** (dados do usuário)
- Chatbot → **Financeiro** (consultas de saldo/transações)
- Chatbot → **Resultados** (consulta resultados jogos)
- Chatbot → **Notificações** (trigger notificações)
- Chatbot → **Bate-papo** (transferir para humano)

### **Portas e Endpoints:**
```yaml
server.port: 8089

# Chat Interface
POST   /chatbot/message          # Enviar mensagem para bot
GET    /chatbot/conversations    # Listar conversas do usuário
GET    /chatbot/conversations/{id} # Detalhes da conversa
POST   /chatbot/conversations/{id}/close # Finalizar conversa

# WebSocket Real-time
/ws/chatbot                     # WebSocket para chat em tempo real

# External Webhooks  
POST   /chatbot/webhook/whatsapp # Webhook WhatsApp
POST   /chatbot/webhook/telegram # Webhook Telegram
POST   /chatbot/webhook/facebook # Webhook Facebook

# Admin & Training
POST   /chatbot/intencoes        # Criar intenção
GET    /chatbot/intencoes        # Listar intenções
PUT    /chatbot/intencoes/{id}   # Atualizar intenção
POST   /chatbot/knowledge        # Adicionar conhecimento
POST   /chatbot/treinar          # Retreinar modelo

# Analytics
GET    /chatbot/analytics        # Analytics do bot
GET    /chatbot/feedback         # Feedback dos usuários

GET    /actuator/health
```

---

## 🔗 DEPENDÊNCIAS CRÍTICAS

### **Serviços Dependentes (Upstream):**
1. **MySQL** (mysql-proxy:6033) - Persistência conversas
2. **Redis** (conexao-redis:6379) - Cache sessões  
3. **OpenAI/Azure OpenAI** - Processamento NLP
4. **Autenticação** (8081) - Dados usuários
5. **Azure Key Vault** - Secrets management

### **Serviços Consumidores (Downstream):**
- **Frontend/Mobile** - Interface de chat
- **Plataformas Externas** - WhatsApp, Telegram, Facebook
- **Bate-papo** - Escalação para humanos
- **Todos microserviços** - Consultas integradas

### **Ordem de Deploy:**
```
1. MySQL + Redis (storage)
2. Autenticação (user data)  
3. AI Services (OpenAI/Azure)
4. Chatbot (AI assistant)
5. Frontend (chat interface)
```

---

## 🚨 ESPECIFICIDADES DO CHATBOT

### **Intenções Principais:**
```yaml
intencoes:
  saudacao:
    exemplos: ["oi", "olá", "bom dia", "boa tarde"]
    acao: "apresentar_opcoes"
    
  consultar_saldo:
    exemplos: ["qual meu saldo", "saldo atual", "dinheiro disponível"]
    acao: "integrar_financeiro"
    
  resultados_jogos:
    exemplos: ["resultado do jogo", "números sorteados", "quem ganhou"]
    acao: "integrar_resultados"
    
  suporte_tecnico:
    exemplos: ["não consigo entrar", "erro no app", "problema técnico"]
    acao: "transferir_humano"
    
  falar_humano:
    exemplos: ["falar com atendente", "preciso de ajuda", "pessoa real"]
    acao: "escalacao_humano"
```

### **Processamento NLP:**
```yaml
nlp:
  provider: azure-openai
  model: gpt-4o-mini
  max-tokens: 150
  temperature: 0.3
  confidence-threshold: 0.7
  
sentiment-analysis:
  enabled: true
  provider: azure-cognitive
  languages: [pt-BR, en, es]
```

### **Multi-canal:**
```yaml
channels:
  web:
    websocket: true
    typing-indicator: true
    
  mobile:
    push-notifications: true
    offline-messages: true
    
  whatsapp:
    business-api: true
    templates: enabled
    
  telegram:
    inline-keyboard: true
    file-uploads: true
```

---

## 📊 MÉTRICAS ESPECÍFICAS

### **Custom Metrics:**
- `chatbot_mensagens_total{canal,tipo}` - Mensagens processadas
- `chatbot_intencoes_detectadas_total{intencao}` - Intenções identificadas
- `chatbot_confianca_score{intencao}` - Score de confiança médio
- `chatbot_conversas_ativas` - Conversas ativas simultâneas
- `chatbot_escalacao_humano_total` - Escalações para humanos
- `chatbot_resposta_tempo_ms` - Tempo de resposta
- `chatbot_satisfacao_usuario` - Score de satisfação

### **Alertas Configurados:**
- Response time > 3s
- Confidence score < 0.5 (>10% das mensagens)
- AI service connectivity failures > 0
- Human escalation rate > 30%
- User satisfaction < 3.0/5.0
- WebSocket disconnections > 10%

---

## 🔧 CONFIGURAÇÕES ESPECÍFICAS

### **Application Properties:**
```yaml
# Chatbot Configuration
chatbot:
  response-timeout: 10s
  max-conversation-length: 1000  # mensagens
  session-timeout: 1800s         # 30 minutos
  typing-simulation: true
  
# AI/NLP Configuration
ai:
  provider: azure-openai
  model: gpt-4o-mini
  max-tokens: 150
  temperature: 0.3
  system-prompt: |
    Você é um assistente virtual da plataforma Conexão de Sorte.
    Seja prestativo, educado e direto nas respostas.
    Para questões complexas, transfira para um atendente humano.
    
# Multi-channel Support
channels:
  web:
    enabled: true
    websocket: true
    
  whatsapp:
    enabled: false  # Configurar quando disponível
    webhook-url: /chatbot/webhook/whatsapp
    verify-token: ${conexao-de-sorte-whatsapp-verify-token}
    
# Knowledge Base
knowledge-base:
  auto-update: true
  similarity-threshold: 0.8
  max-results: 3
  
# Analytics
analytics:
  track-user-journey: true
  conversation-analysis: true
  feedback-collection: true
```

### **Training Data Structure:**
```yaml
training:
  intents:
    format: json
    validation: 80/20
    auto-retrain: weekly
    
  entities:
    custom-entities:
      - currency: ["real", "reais", "R$"]
      - games: ["lotofácil", "mega-sena", "quina"]
      - time-periods: ["hoje", "ontem", "semana passada"]
```

---

## 🧪 TESTES E VALIDAÇÕES

### **Health Checks:**
```bash
# Health principal
curl -f http://localhost:8089/actuator/health

# Database connectivity
curl -f http://localhost:8089/actuator/health/db

# Redis connectivity
curl -f http://localhost:8089/actuator/health/redis

# AI service connectivity
curl -f http://localhost:8089/chatbot/health/ai
```

### **Smoke Tests Pós-Deploy:**
```bash
# 1. Enviar mensagem para bot
curl -X POST http://localhost:8089/chatbot/message \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT" \
  -d '{
    "conteudo": "Olá, preciso de ajuda",
    "canal": "WEB"
  }'

# 2. Testar detecção de intenção  
curl -X POST http://localhost:8089/chatbot/message \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT" \
  -d '{
    "conteudo": "Qual é o meu saldo atual?",
    "canal": "WEB"
  }'

# 3. Testar WebSocket (via browser console)
# const ws = new WebSocket('ws://localhost:8089/ws/chatbot');
# ws.send(JSON.stringify({message: "teste"}));

# 4. Testar knowledge base
curl -X POST http://localhost:8089/chatbot/message \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT" \
  -d '{
    "conteudo": "Como jogar na lotofácil?",
    "canal": "WEB"
  }'
```

---

## ⚠️ TROUBLESHOOTING

### **Problema: Bot Não Responde**
```bash
# 1. Verificar conectividade AI
curl -f http://localhost:8089/chatbot/health/ai

# 2. Verificar logs processamento
docker service logs conexao-chatbot | grep "message processing"

# 3. Verificar sessão Redis
redis-cli -a $REDIS_PASS KEYS "chatbot:sessions:*"
```

### **Problema: Baixa Confiança NLP**
```bash
# 1. Verificar métricas de confiança
curl http://localhost:8089/actuator/metrics/chatbot.confianca.score

# 2. Analisar intenções detectadas
curl http://localhost:8089/chatbot/analytics | jq '.intencoes'

# 3. Retreinar modelo
curl -X POST http://localhost:8089/chatbot/treinar \
  -H "Authorization: Bearer $ADMIN_JWT"
```

### **Problema: Performance Lenta**
```bash
# 1. Tempo resposta AI
curl http://localhost:8089/actuator/metrics/chatbot.ai.response.time

# 2. Cache hit rate
curl http://localhost:8089/actuator/metrics/cache.gets

# 3. Connection pool database
curl http://localhost:8089/actuator/metrics/r2dbc.pool.connections
```

---

## 📋 CHECKLIST PRÉ-DEPLOY

### **Configuração:**
- [ ] Database `conexao_chatbot` criado
- [ ] Redis cache configurado (database 11)
- [ ] OpenAI/Azure OpenAI API keys configuradas
- [ ] Knowledge base populada
- [ ] Intenções básicas treinadas

### **Integração:**
- [ ] WebSocket endpoints funcionando
- [ ] Integração com Autenticação testada
- [ ] Integração com Financeiro testada
- [ ] Integração com Resultados testada
- [ ] Escalação para Bate-papo configurada

### **Multi-canal:**
- [ ] Interface Web funcionando
- [ ] Mobile app integration testada
- [ ] Webhooks externos configurados (se aplicável)
- [ ] Notifications integration ativa

---

## 🔄 DISASTER RECOVERY

### **Backup Crítico:**
1. **Database chatbot** (conversas e knowledge base)
2. **Trained models** (se local)
3. **Configuration files**
4. **Knowledge base content**

### **Recovery Procedure:**
1. Restore database chatbot
2. Restore knowledge base
3. Restart chatbot service
4. Verify AI service connectivity
5. Test WebSocket connections
6. Validate integrations
7. Test conversation flow

### **Estado Perdido Aceitável:**
- Sessões ativas Redis (usuários reconectam)
- Cache de intenções (é reconstruído)
- Métricas temporárias

---

## 💡 OPERATIONAL NOTES

### **AI Model Management:**
- Model versioning strategy
- A/B testing for responses
- Continuous learning from conversations
- Regular retraining schedule

### **Escalation Strategy:**
- Automatic human escalation triggers
- Priority queue for complex queries
- Sentiment-based escalation
- Business hours coverage

### **Monitoramento 24/7:**
- Conversation success rate
- User satisfaction scores
- AI response accuracy
- Channel availability
- Integration health

---

**📅 Última Atualização**: Setembro 2025  
**🏷️ Versão**: 1.0  
**🤖 Criticidade**: MÉDIA - Atendimento automatizado ao usuário