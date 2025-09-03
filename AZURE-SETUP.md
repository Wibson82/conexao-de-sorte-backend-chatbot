# Configuração do Pipeline CI/CD - Azure

Este documento explica como configurar o pipeline CI/CD para deploy no Azure seguindo as melhores práticas.

## 📋 Pré-requisitos

### 1. Recursos Azure Necessários
- **Azure Subscription** ativa
- **Azure Key Vault** para gerenciar secrets
- **Azure Container Registry (ACR)** ou usar GitHub Container Registry
- **Azure Container Instances (ACI)** para deploy dos containers
- **Resource Group** para organizar recursos

### 2. Configuração do Azure App Registration

1. Acesse o [Portal Azure](https://portal.azure.com)
2. Vá para **Azure Active Directory** > **App registrations**
3. Clique em **New registration**:
   - **Name**: `chatbot-microservice-ci-cd`
   - **Supported account types**: Single tenant
   - **Redirect URI**: Deixe em branco
4. Anote o **Application (client) ID**
5. Anote o **Directory (tenant) ID**

### 3. Configuração de Certificados (OIDC)

1. No App Registration criado, vá para **Certificates & secrets**
2. Clique na aba **Federated credentials**
3. Clique em **Add credential**:
   - **Federated credential scenario**: GitHub Actions deploying Azure resources
   - **Organization**: Seu username/organização do GitHub
   - **Repository**: Nome do seu repositório
   - **Entity type**: Branch
   - **GitHub branch name**: main
   - **Name**: `chatbot-main-branch`

### 4. Configuração de Permissões

1. Vá para **Subscriptions** > Sua subscription
2. Clique em **Access control (IAM)**
3. Clique em **Add** > **Add role assignment**:
   - **Role**: Contributor
   - **Assign access to**: User, group, or service principal
   - **Select**: Seu App Registration

## 🔑 Secrets do GitHub

Configure os seguintes secrets no seu repositório GitHub:

### Repository Secrets
```
AZURE_CLIENT_ID          = "application-id-do-app-registration"
AZURE_TENANT_ID          = "tenant-id-do-azure-ad"
AZURE_SUBSCRIPTION_ID    = "id-da-sua-subscription"
AZURE_KEYVAULT_NAME      = "nome-do-seu-keyvault"
```

### Secrets no Azure Key Vault
Configure os seguintes secrets no Azure Key Vault:

```
# Database Configuration
conexao-de-sorte-database-url             = "r2dbc:postgresql://servidor:5432/chatbot_db"
conexao-de-sorte-database-jdbc-url        = "jdbc:postgresql://servidor:5432/chatbot_db"
conexao-de-sorte-database-r2dbc-url       = "r2dbc:postgresql://servidor:5432/chatbot_db"
conexao-de-sorte-database-username        = "usuario_do_banco"
conexao-de-sorte-database-password        = "senha_do_banco"

# Redis Configuration
conexao-de-sorte-redis-host               = "endereco_do_redis"
conexao-de-sorte-redis-password           = "senha_do_redis"
conexao-de-sorte-redis-port               = "6379"
conexao-de-sorte-redis-database           = "0"

# JWT Configuration
conexao-de-sorte-jwt-secret               = "sua_chave_jwt_secreta"
conexao-de-sorte-jwt-issuer               = "conexao-de-sorte"
conexao-de-sorte-jwt-privateKey           = "chave_privada_jwt"
conexao-de-sorte-jwt-publicKey            = "chave_publica_jwt"
conexao-de-sorte-jwt-signing-key          = "chave_assinatura_jwt"
conexao-de-sorte-jwt-verification-key     = "chave_verificacao_jwt"
conexao-de-sorte-jwt-jwks-uri             = "uri_do_jwks"
conexao-de-sorte-jwt-key-id               = "id_da_chave_jwt"

# Encryption Configuration
conexao-de-sorte-encryption-master-key    = "chave_mestre_criptografia"
conexao-de-sorte-encryption-master-password = "senha_mestre_criptografia"
conexao-de-sorte-encryption-backup-key    = "chave_backup_criptografia"

# CORS Configuration
conexao-de-sorte-cors-allowed-origins     = "http://localhost:3000,https://app.conexao-de-sorte.com"
conexao-de-sorte-cors-allow-credentials   = "true"

# SSL Configuration
conexao-de-sorte-ssl-enabled              = "true"
conexao-de-sorte-ssl-keystore-path        = "/path/to/keystore.p12"
conexao-de-sorte-ssl-keystore-password    = "senha_do_keystore"
```

## 🏗️ Estrutura do Pipeline

### Jobs Executados:

1. **test**: Executa testes unitários e de integração
2. **security-scan**: Análise de vulnerabilidades com Trivy
3. **build-and-push**: Build da aplicação e push da imagem Docker
4. **deploy-azure**: Deploy no Azure Container Instances
5. **notify**: Notificação do resultado do deploy

### Características:

- ✅ **OIDC Authentication**: Autenticação sem senhas usando certificados
- ✅ **Azure Key Vault**: Gestão segura de secrets
- ✅ **Container Signing**: Assinatura de imagens com Cosign
- ✅ **Multi-platform**: Build para AMD64 e ARM64
- ✅ **Security Scanning**: Análise de vulnerabilidades
- ✅ **Health Checks**: Verificação automática após deploy

## 🛠️ Como Criar o Azure Key Vault

```bash
# Criar resource group
az group create --name rg-conexao-de-sorte --location "Brazil South"

# Criar Key Vault
az keyvault create \
  --name kv-conexao-sorte-001 \
  --resource-group rg-conexao-de-sorte \
  --location "Brazil South" \
  --enable-rbac-authorization

# Dar permissão ao App Registration para acessar o Key Vault
az role assignment create \
  --role "Key Vault Secrets User" \
  --assignee "application-id-do-app-registration" \
  --scope "/subscriptions/sua-subscription-id/resourceGroups/rg-conexao-de-sorte/providers/Microsoft.KeyVault/vaults/kv-conexao-sorte-001"

# Adicionar secrets
# Database secrets
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-database-url --value "r2dbc:postgresql://servidor:5432/chatbot_db"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-database-jdbc-url --value "jdbc:postgresql://servidor:5432/chatbot_db"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-database-r2dbc-url --value "r2dbc:postgresql://servidor:5432/chatbot_db"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-database-username --value "usuario_do_banco"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-database-password --value "senha_do_banco"

# Redis secrets
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-redis-host --value "endereco_do_redis"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-redis-password --value "senha_do_redis"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-redis-port --value "6379"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-redis-database --value "0"

# JWT secrets
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-jwt-secret --value "sua_chave_jwt_secreta"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-jwt-issuer --value "conexao-de-sorte"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-jwt-privateKey --value "chave_privada_jwt"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-jwt-publicKey --value "chave_publica_jwt"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-jwt-signing-key --value "chave_assinatura_jwt"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-jwt-verification-key --value "chave_verificacao_jwt"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-jwt-jwks-uri --value "uri_do_jwks"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-jwt-key-id --value "id_da_chave_jwt"

# Encryption secrets
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-encryption-master-key --value "chave_mestre_criptografia"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-encryption-master-password --value "senha_mestre_criptografia"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-encryption-backup-key --value "chave_backup_criptografia"

# CORS secrets
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-cors-allowed-origins --value "http://localhost:3000,https://app.conexao-de-sorte.com"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-cors-allow-credentials --value "true"

# SSL secrets
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-ssl-enabled --value "true"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-ssl-keystore-path --value "/path/to/keystore.p12"
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-ssl-keystore-password --value "senha_do_keystore"
```

## 🔄 Diferenças vs Pipeline Anterior

### Pipeline Anterior (Hostinger)
- ❌ Self-hosted runners
- ❌ SSH com senhas em texto simples
- ❌ Sem gestão centralizada de secrets
- ❌ Deploy manual via SSH

### Pipeline Atual (Azure)
- ✅ GitHub-hosted runners
- ✅ OIDC authentication (sem senhas)
- ✅ Azure Key Vault para secrets
- ✅ Deploy automatizado via Azure CLI
- ✅ Container Instances com DNS automático
- ✅ Health checks integrados

## 📝 Próximos Passos

1. Configure os recursos Azure necessários
2. Atualize os secrets do GitHub
3. Teste o pipeline em uma branch de desenvolvimento
4. Monitore os logs de deploy no GitHub Actions
5. Verifique a aplicação deployada no Azure

## 🆘 Troubleshooting

### Erro de Autenticação OIDC
- Verifique se o App Registration está configurado corretamente
- Confirme que as federated credentials estão corretas
- Verifique as permissões na subscription

### Erro no Key Vault
- Verifique se o App Registration tem permissões no Key Vault
- Confirme que todos os secrets estão configurados
- Verifique o nome do Key Vault nos secrets do GitHub

### Erro no Deploy
- Verifique se a subscription está ativa
- Confirme que o resource group existe
- Verifique os logs detalhados no GitHub Actions
