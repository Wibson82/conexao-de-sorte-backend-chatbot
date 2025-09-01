#!/usr/bin/env bash
# =====================================================================
# Script de Cálculo de Combinações e Inversões - Jogo do Bicho & Loterias
# Autor: Conexão de Sorte (OpenAI ChatGPT)
# Data: $(date '+%d/%m/%Y')
# ---------------------------------------------------------------------
# Calcula combinações e inversões para TODAS as modalidades do jogo do bicho
# e principais loterias (Mega-Sena, Quina, etc), respeitando os limites e
# regras das apostas (até 10 dígitos, grupos ou dezenas por modalidade).
# =====================================================================

fatorial() {
  local n=$1
  local r=1
  for ((i=2; i<=n; i++)); do
    r=$((r*i))
  done
  echo $r
}

combinacoes() {
  local n=$1
  local k=$2
  if ((k > n)); then echo 0; return; fi
  local fn=$(fatorial $n)
  local fk=$(fatorial $k)
  local fnk=$(fatorial $((n-k)))
  echo $((fn / (fk * fnk)))
}

inversoes() {
  local n=$1
  echo $(fatorial $n)
}

is_valid_range() {
  local val=$1
  local min=$2
  local max=$3
  [[ $val -ge $min && $val -le $max ]]
}

opcoes_cercamento() {
  local modalidade=$1
  case "$modalidade" in
    milhar)
      echo "Cercamentos: pelos cinco, pelos dez, pelos doze";;
    centena|grupo|dezena)
      echo "Cercamentos: pelos cinco, pelos sete, no MR (6º-7º prêmio)";;
    duque-dezena|terno-dezena|duque-grupo|terno-grupo|quina-grupo|passe-direto|passe-ida-volta|passe-quinto)
      echo "Cercamentos: ver EMD e especiais por modalidade";;
    *)
      echo "Cercamentos específicos não cadastrados. Consulte as regras."
      ;;
  esac
}

main() {
  echo -e "\n== Calculadora de Combinações & Inversões - Jogo do Bicho & Loterias ==\n"
  echo "Modalidades: milhar, centena, grupo, duque-grupo, passe-direto, passe-ida-volta, passe-quinto, terno-grupo, quadra-grupo, quina-grupo, dezena, duque-dezena, terno-dezena, mega-sena, quina, quadra, etc."
  read -p "Informe a modalidade: " modalidade
  modalidade=$(echo "$modalidade" | tr '[:upper:]' '[:lower:]' | tr '_' '-')
  case "$modalidade" in
    milhar)
      read -p "Quantidade de dígitos (4-10): " qtd
      if ! is_valid_range $qtd 4 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de combinações possíveis: $((10 ** $qtd))"
      echo "Inversões de cada milhar (permutações): $(inversoes $qtd)"
      opcoes_cercamento milhar
      ;;
    centena)
      read -p "Quantidade de dígitos (3-10): " qtd
      if ! is_valid_range $qtd 3 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de combinações possíveis: $((10 ** $qtd))"
      echo "Inversões de cada centena (permutações): $(inversoes $qtd)"
      opcoes_cercamento centena
      ;;
    grupo)
      read -p "Quantidade de grupos (1-10): " qtd
      if ! is_valid_range $qtd 1 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de combinações: $(combinacoes 25 $qtd)"
      echo "Total de inversões possíveis: $(inversoes $qtd)"
      opcoes_cercamento grupo
      ;;
    duque-grupo)
      read -p "Quantidade de grupos (2-10): " qtd
      if ! is_valid_range $qtd 2 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de duques possíveis: $(combinacoes 25 $qtd)"
      echo "Inversões (permutações): $(inversoes $qtd)"
      opcoes_cercamento duque-grupo
      ;;
    passe-direto|passe-ida-volta|passe-quinto)
      read -p "Quantidade de grupos (2-10): " qtd
      if ! is_valid_range $qtd 2 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de combinações: $(combinacoes 25 $qtd)"
      echo "Inversões (permutações): $(inversoes $qtd)"
      opcoes_cercamento $modalidade
      ;;
    terno-grupo)
      read -p "Quantidade de grupos (3-10): " qtd
      if ! is_valid_range $qtd 3 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de ternos possíveis: $(combinacoes 25 $qtd)"
      echo "Inversões (permutações): $(inversoes $qtd)"
      opcoes_cercamento terno-grupo
      ;;
    quadra-grupo)
      read -p "Quantidade de grupos (4-10): " qtd
      if ! is_valid_range $qtd 4 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de quadras possíveis: $(combinacoes 25 $qtd)"
      echo "Inversões (permutações): $(inversoes $qtd)"
      ;;
    quina-grupo)
      read -p "Quantidade de grupos (5-10): " qtd
      if ! is_valid_range $qtd 5 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de quinas possíveis: $(combinacoes 25 $qtd)"
      echo "Inversões (permutações): $(inversoes $qtd)"
      opcoes_cercamento quina-grupo
      ;;
    dezena)
      read -p "Quantidade de dezenas (1-10): " qtd
      if ! is_valid_range $qtd 1 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de combinações: $(combinacoes 100 $qtd)"
      echo "Inversões: $(inversoes $qtd)"
      opcoes_cercamento dezena
      ;;
    duque-dezena)
      read -p "Quantidade de dezenas (2-10): " qtd
      if ! is_valid_range $qtd 2 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de duques possíveis: $(combinacoes 100 $qtd)"
      echo "Inversões (permutações): $(inversoes $qtd)"
      opcoes_cercamento duque-dezena
      ;;
    terno-dezena)
      read -p "Quantidade de dezenas (3-10): " qtd
      if ! is_valid_range $qtd 3 10; then echo "Valor inválido"; exit 1; fi
      echo "Total de ternos possíveis: $(combinacoes 100 $qtd)"
      echo "Inversões (permutações): $(inversoes $qtd)"
      opcoes_cercamento terno-dezena
      ;;
    mega-sena)
      read -p "Quantidade de dezenas (6-15): " qtd
      if ! is_valid_range $qtd 6 15; then echo "Valor inválido"; exit 1; fi
      echo "Total de combinações: $(combinacoes 60 $qtd)"
      echo "Inversões: $(inversoes $qtd)"
      ;;
    quina)
      read -p "Quantidade de números (5-15): " qtd
      if ! is_valid_range $qtd 5 15; then echo "Valor inválido"; exit 1; fi
      echo "Total de combinações: $(combinacoes 80 $qtd)"
      echo "Inversões: $(inversoes $qtd)"
      ;;
    quadra)
      read -p "Quantidade de números (4-15): " qtd
      if ! is_valid_range $qtd 4 15; then echo "Valor inválido"; exit 1; fi
      echo "Total de combinações: $(combinacoes 80 $qtd)"
      echo "Inversões: $(inversoes $qtd)"
      ;;
    *)
      echo "Modalidade não reconhecida ou não implementada ainda."
      ;;
  esac
  echo -e "\n(Regras comuns: milhares e centenas até 10 dígitos; duques, passes, ternos e quinas até 10 grupos/dezenas; milhares, centenas, grupos e dezenas podem ser cercados pelos cinco; centenas, grupos e dezenas pelos sete; milhar pode ser cercada pelos dez ou doze; centenas, grupos e dezenas podem ser cercados no MR/6º-7º prêmio.)\n"
}

main
