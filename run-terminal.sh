#!/bin/bash

echo "=== ChatBot Menu - Terminal Mode ==="
echo "Iniciando aplicação..."
echo

# Verificar se o gradle wrapper existe
if [ ! -f "./gradlew" ]; then
    echo "Gradle wrapper não encontrado. Execute: gradle wrapper"
    exit 1
fi

# Tornar o gradlew executável
chmod +x ./gradlew

# Executar o chatbot no modo terminal
echo "Para sair do chatbot, digite 'sair' ou 'exit'"
echo "Para voltar ao menu inicial, digite 'voltar' ou 'back'"
echo "---"
echo

./gradlew bootRun --args='--terminal'