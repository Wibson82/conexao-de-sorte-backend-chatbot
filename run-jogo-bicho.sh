#!/bin/bash

echo "🎰 INICIANDO SISTEMA DE APOSTAS - JOGO DO BICHO 🎰"
echo "=================================================="

# Compilar o projeto
./gradlew build

if [ $? -ne 0 ]; then
    echo "❌ Erro na compilação do projeto"
    exit 1
fi

# Executar o sistema de jogo do bicho
./gradlew runJogoBicho --console=plain

echo "✅ Sistema encerrado"