#!/bin/bash

echo "🎰 INICIANDO SISTEMA DE APOSTAS - JOGO DO BICHO 🎰"
echo "=================================================="

# Compilar o projeto
./mvnw clean compile

if [ $? -ne 0 ]; then
    echo "❌ Erro na compilação do projeto"
    exit 1
fi

# Executar o sistema de jogo do bicho
./mvnw spring-boot:run -Dspring.profiles.active=local

echo "✅ Sistema encerrado"