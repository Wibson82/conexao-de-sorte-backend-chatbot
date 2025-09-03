#!/bin/bash

echo "=== ChatBot Menu - Terminal Mode ==="
echo "Iniciando aplicação..."
echo

# Verificar se o maven wrapper existe
if [ ! -f "./mvnw" ]; then
    echo "Maven wrapper não encontrado. Execute: mvn wrapper:wrapper"
    exit 1
fi

# Tornar o mvnw executável
chmod +x ./mvnw

# Executar o chatbot no modo terminal
echo "Para sair do chatbot, digite 'sair' ou 'exit'"
echo "Para voltar ao menu inicial, digite 'voltar' ou 'back'"
echo "---"
echo

./mvnw spring-boot:run -Dspring.profiles.active=local