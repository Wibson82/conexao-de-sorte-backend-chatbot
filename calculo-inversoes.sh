#!/usr/bin/env bash
# =====================================================================
# Script de Cálculo de Combinações, Inversões e Multiconjuntos (com repetição)
# para Jogo do Bicho (Terno/Quina de Grupo, Terno/Quina de Dezena, etc)
# Agora inclui geração de todas as quinas possíveis de grupo (com repetição)
# =====================================================================

fatorial() {
  local n=$1
  local r=1
  for ((i=2; i<=n; i++)); do
    r=$((r*i))
  done
  echo $r
}

combinacao_simples() {
  local n=$1
  local k=$2
  if ((k > n)); then echo 0; return; fi
  local fn=$(fatorial $n)
  local fk=$(fatorial $k)
  local fnk=$(fatorial $((n-k)))
  echo $((fn / (fk * fnk)))
}

combinacao_repeticao() {
  # Combinação com repetição: (n+k-1)! / [k! * (n-1)!]
  local n=$1
  local k=$2
  local num=$(fatorial $((n+k-1)))
  local denom=$(( $(fatorial $k) * $(fatorial $((n-1))) ))
  echo $((num / denom))
}

inversoes() {
  local n=$1
  echo $(fatorial $n)
}

# Gera todas as quinas possíveis com repetição, ordenadas e numeradas
gerar_todas_quinas_grupo() {
  local total=0
  for a in {1..25}; do
    for b in $(seq $a 25); do
      for c in $(seq $b 25); do
        for d in $(seq $c 25); do
          for e in $(seq $d 25); do
            total=$((total+1))
            printf "%07d: %02d %02d %02d %02d %02d\n" "$total" "$a" "$b" "$c" "$d" "$e"
          done
        done
      done
    done
  done
  echo -e "\nTotal de quinas geradas (com repetição): $total"
}

exibir_combinacoes_grupos() {
  local modalidade="$1"
  local universo="$2" # 25 grupos ou 100 dezenas
  local min="$3"
  local max="$4"
  for ((qtd=$min; qtd<=$max; qtd++)); do
    echo -e "\n=> $modalidade com $qtd opções escolhidas ($universo possíveis)"
    echo "- Combinação simples (sem repetição): $(combinacao_simples $universo $qtd)"
    echo "- Combinação com repetição:           $(combinacao_repeticao $universo $qtd)"
    echo "- Permutações possíveis (inversões):  $(inversoes $qtd)"
  done
}

main() {
  echo -e "\n== Calculadora de Combinações & Inversões - Jogo do Bicho (com repetição) ==\n"
  echo "Escolha: terno-grupo, quina-grupo, terno-dezena, quina-dezena, todas-quinas-grupo"
  read -p "Informe a modalidade: " modalidade
  modalidade=$(echo "$modalidade" | tr '[:upper:]' '[:lower:]' | tr '_' '-')
  case "$modalidade" in
    terno-grupo)
      exibir_combinacoes_grupos "Terno de Grupo" 25 3 10
      ;;
    quina-grupo)
      exibir_combinacoes_grupos "Quina de Grupo" 25 5 10
      ;;
    terno-dezena)
      exibir_combinacoes_grupos "Terno de Dezena" 100 3 10
      ;;
    quina-dezena)
      exibir_combinacoes_grupos "Quina de Dezena" 100 5 10
      ;;
    todas-quinas-grupo)
      echo -e "\nGerando todas as quinas possíveis de grupo (com repetição, ordenadas):\n"
      gerar_todas_quinas_grupo
      ;;
    *)
      echo "Modalidade não reconhecida. Opções: terno-grupo, quina-grupo, terno-dezena, quina-dezena, todas-quinas-grupo."
      ;;
  esac

  echo -e "\nExplicação:"
  echo "- Combinação simples: todas as possibilidades SEM repetição (ex: [1,2,3,4,5], não pode [1,1,2,3,4])"
  echo "- Combinação com repetição: todas as possibilidades, PERMITINDO repetir grupos/dezenas (ex: [1,1,2,3,4])"
  echo "- Permutações: quantidade de maneiras de ordenar as opções escolhidas"
}

main
