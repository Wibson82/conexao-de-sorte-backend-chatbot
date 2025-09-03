# Scripts de Configuração

Este diretório contém scripts úteis para configurar o ambiente de desenvolvimento e produção.

## setup-keyvault-secrets.sh

Script automatizado para configurar todos os secrets necessários no Azure Key Vault.

### Uso

```bash
# Usar o nome padrão do Key Vault
./scripts/setup-keyvault-secrets.sh

# Ou especificar um nome customizado
./scripts/setup-keyvault-secrets.sh meu-keyvault-customizado
```

### Pré-requisitos

1. Azure CLI instalado e configurado
2. Login realizado: `az login`
3. Key Vault já criado no Azure
4. Permissões adequadas para escrever secrets no Key Vault

### O que o script faz

- ✅ Verifica se você está logado no Azure CLI
- ✅ Verifica se o Key Vault existe
- ✅ Cria todos os 25 secrets necessários para o projeto
- ✅ Organiza os secrets por categoria (Database, Redis, JWT, etc.)
- ✅ Fornece valores padrão seguros
- ✅ Lista quais valores precisam ser atualizados

### Secrets Criados

O script cria os seguintes grupos de secrets:

**Database (5 secrets)**
- conexao-de-sorte-database-url
- conexao-de-sorte-database-jdbc-url  
- conexao-de-sorte-database-r2dbc-url
- conexao-de-sorte-database-username
- conexao-de-sorte-database-password

**Redis (4 secrets)**
- conexao-de-sorte-redis-host
- conexao-de-sorte-redis-password
- conexao-de-sorte-redis-port
- conexao-de-sorte-redis-database

**JWT (8 secrets)**
- conexao-de-sorte-jwt-secret
- conexao-de-sorte-jwt-issuer
- conexao-de-sorte-jwt-privateKey
- conexao-de-sorte-jwt-publicKey
- conexao-de-sorte-jwt-signing-key
- conexao-de-sorte-jwt-verification-key
- conexao-de-sorte-jwt-jwks-uri
- conexao-de-sorte-jwt-key-id

**Encryption (3 secrets)**
- conexao-de-sorte-encryption-master-key
- conexao-de-sorte-encryption-master-password
- conexao-de-sorte-encryption-backup-key

**CORS (2 secrets)**
- conexao-de-sorte-cors-allowed-origins
- conexao-de-sorte-cors-allow-credentials

**SSL (3 secrets)**
- conexao-de-sorte-ssl-enabled
- conexao-de-sorte-ssl-keystore-path
- conexao-de-sorte-ssl-keystore-password

### Após a Execução

⚠️ **IMPORTANTE**: Atualize os valores que contêm `CHANGE_ME` com valores reais:

```bash
# Listar todos os secrets
az keyvault secret list --vault-name kv-conexao-sorte-001 --query '[].name' -o tsv

# Atualizar um secret específico
az keyvault secret set --vault-name kv-conexao-sorte-001 --name conexao-de-sorte-database-password --value "sua_senha_real"
```

### Troubleshooting

**Erro de permissão**:
```bash
az role assignment create \
  --role "Key Vault Secrets Officer" \
  --assignee $(az account show --query user.name -o tsv) \
  --scope "/subscriptions/$(az account show --query id -o tsv)/resourceGroups/rg-conexao-de-sorte/providers/Microsoft.KeyVault/vaults/kv-conexao-sorte-001"
```

**Key Vault não encontrado**:
```bash
az keyvault create \
  --name kv-conexao-sorte-001 \
  --resource-group rg-conexao-de-sorte \
  --location "Brazil South" \
  --enable-rbac-authorization
```
