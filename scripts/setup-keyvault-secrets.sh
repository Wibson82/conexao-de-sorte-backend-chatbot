#!/bin/bash

# Script para configurar todos os secrets no Azure Key Vault
# Uso: ./setup-keyvault-secrets.sh <nome-do-keyvault>

set -euo pipefail

KEYVAULT_NAME="${1:-kv-conexao-sorte-001}"

echo "🔐 Configurando secrets no Azure Key Vault: $KEYVAULT_NAME"
echo ""

# Verificar se o usuário está logado no Azure
if ! az account show >/dev/null 2>&1; then
    echo "❌ Erro: Você precisa estar logado no Azure CLI"
    echo "Execute: az login"
    exit 1
fi

# Verificar se o Key Vault existe
if ! az keyvault show --name "$KEYVAULT_NAME" >/dev/null 2>&1; then
    echo "❌ Erro: Key Vault '$KEYVAULT_NAME' não encontrado"
    echo "Execute primeiro: az keyvault create --name $KEYVAULT_NAME --resource-group rg-conexao-de-sorte --location 'Brazil South' --enable-rbac-authorization"
    exit 1
fi

echo "✅ Key Vault encontrado: $KEYVAULT_NAME"
echo ""

# Função para adicionar secret com verificação
add_secret() {
    local name="$1"
    local value="$2"
    local description="$3"
    
    echo "📝 Adicionando secret: $name ($description)"
    if az keyvault secret set --vault-name "$KEYVAULT_NAME" --name "$name" --value "$value" >/dev/null 2>&1; then
        echo "   ✅ Sucesso"
    else
        echo "   ❌ Erro ao adicionar $name"
        return 1
    fi
}

echo "🗄️ Configurando secrets do banco de dados..."
add_secret "conexao-de-sorte-database-url" "r2dbc:postgresql://localhost:5432/conexao_sorte" "URL R2DBC do banco"
add_secret "conexao-de-sorte-database-jdbc-url" "jdbc:postgresql://localhost:5432/conexao_sorte" "URL JDBC do banco"
add_secret "conexao-de-sorte-database-r2dbc-url" "r2dbc:postgresql://localhost:5432/conexao_sorte" "URL R2DBC alternativa"
add_secret "conexao-de-sorte-database-username" "conexao_user" "Usuário do banco"
add_secret "conexao-de-sorte-database-password" "CHANGE_ME_DB_PASSWORD" "Senha do banco"

echo ""
echo "🔴 Configurando secrets do Redis..."
add_secret "conexao-de-sorte-redis-host" "localhost" "Host do Redis"
add_secret "conexao-de-sorte-redis-password" "CHANGE_ME_REDIS_PASSWORD" "Senha do Redis"
add_secret "conexao-de-sorte-redis-port" "6379" "Porta do Redis"
add_secret "conexao-de-sorte-redis-database" "0" "Database do Redis"

echo ""
echo "🔑 Configurando secrets do JWT..."
add_secret "conexao-de-sorte-jwt-secret" "CHANGE_ME_JWT_SECRET_SUPER_SECURE_KEY_HERE" "Chave secreta JWT"
add_secret "conexao-de-sorte-jwt-issuer" "conexao-de-sorte" "Emissor do JWT"
add_secret "conexao-de-sorte-jwt-privateKey" "CHANGE_ME_JWT_PRIVATE_KEY" "Chave privada JWT"
add_secret "conexao-de-sorte-jwt-publicKey" "CHANGE_ME_JWT_PUBLIC_KEY" "Chave pública JWT"
add_secret "conexao-de-sorte-jwt-signing-key" "CHANGE_ME_JWT_SIGNING_KEY" "Chave de assinatura JWT"
add_secret "conexao-de-sorte-jwt-verification-key" "CHANGE_ME_JWT_VERIFICATION_KEY" "Chave de verificação JWT"
add_secret "conexao-de-sorte-jwt-jwks-uri" "https://api.conexao-de-sorte.com/.well-known/jwks.json" "URI do JWKS"
add_secret "conexao-de-sorte-jwt-key-id" "conexao-sorte-key-001" "ID da chave JWT"

echo ""
echo "🔐 Configurando secrets de criptografia..."
add_secret "conexao-de-sorte-encryption-master-key" "CHANGE_ME_MASTER_ENCRYPTION_KEY" "Chave mestre de criptografia"
add_secret "conexao-de-sorte-encryption-master-password" "CHANGE_ME_MASTER_PASSWORD" "Senha mestre de criptografia"
add_secret "conexao-de-sorte-encryption-backup-key" "CHANGE_ME_BACKUP_ENCRYPTION_KEY" "Chave de backup"

echo ""
echo "🌐 Configurando secrets do CORS..."
add_secret "conexao-de-sorte-cors-allowed-origins" "http://localhost:3000,https://app.conexao-de-sorte.com" "Origens permitidas CORS"
add_secret "conexao-de-sorte-cors-allow-credentials" "true" "Permitir credenciais CORS"

echo ""
echo "🔒 Configurando secrets do SSL..."
add_secret "conexao-de-sorte-ssl-enabled" "false" "SSL habilitado"
add_secret "conexao-de-sorte-ssl-keystore-path" "/app/ssl/keystore.p12" "Caminho do keystore SSL"
add_secret "conexao-de-sorte-ssl-keystore-password" "CHANGE_ME_KEYSTORE_PASSWORD" "Senha do keystore SSL"

echo ""
echo "🎉 Configuração concluída!"
echo ""
echo "⚠️  IMPORTANTE: Atualize os valores dos secrets que contêm 'CHANGE_ME' com os valores reais:"
echo "   - Senhas do banco de dados e Redis"
echo "   - Chaves JWT (gere chaves RSA reais)"
echo "   - Chaves de criptografia (gere valores seguros)"
echo "   - Senha do keystore SSL"
echo ""
echo "📋 Para listar todos os secrets criados:"
echo "   az keyvault secret list --vault-name $KEYVAULT_NAME --query '[].name' -o tsv"
echo ""
echo "🔍 Para visualizar um secret específico:"
echo "   az keyvault secret show --vault-name $KEYVAULT_NAME --name <nome-do-secret> --query 'value' -o tsv"
