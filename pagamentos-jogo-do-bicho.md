# 💰 Tabelas de Pagamento e Métodos – Jogo do Bicho

Este documento consolida as **cotações (multiplicadores de prêmio)** e os **métodos de cálculo** para todas as modalidades atualmente disponíveis no sistema.

> As cotações abaixo são referências médias praticadas em bancas físicas do RJ/SP. Podem variar conforme casa lotérica, aplicativo ou acordo regional.

## 1. Grupo (Animal)
| Variação | Descrição | Cotação | Método de Pagamento |
|----------|-----------|---------|---------------------|
| Cabeça | Acerta o grupo no 1.º prêmio | **18 ×** | valor_aposta × 18 |
| Cercado (1.º-5.º) | Grupo sai em qualquer dos cinco primeiros | **3,6 ×** | valor_aposta × 3,6 |
| Posição fixa (ex.: 3.º) | Grupo sai em prêmio exato | **12-18 ×** | valor_aposta × cotação_específica |

## 2. Duque de Grupo
| Variação | Cotação | Método |
|----------|---------|--------|
| Duque 1.º-5.º | **18-19 ×** | valor_aposta × cotação |
| Duque Combinado | idem, porém banca gera todas as combinações $C(n,2)$ | total = valor_unitário × nCombos; cada combinação paga isolada |

## 3. Terno de Grupo
| Variação | Cotação | Método |
|----------|---------|--------|
| Terno 1.º-5.º | **130-150 ×** | valor_aposta × cotação |
| TG(3) 1.º-3.º | **~1 000 ×** | valor_aposta × 1 000 |
| Terno Combinado | banca gera $C(n,3)$ | total = valor_unitário × nCombos |

## 4. Quadra de Grupo
| Variação | Cotação | Método |
|----------|---------|--------|
| Quadra 1.º-4.º/1.º-5.º | **~1 000 ×** | valor_aposta × 1 000 |
| Quadra Combinada | $C(n,4)$ combinações | total = valor_unitário × nCombos |

## 5. Quina de Grupo
| Variação | Cotação | Método |
|----------|---------|--------|
| Quina (5 bichos) | **5 000 ×** | valor_aposta × 5 000 |
| Quina (>5 bichos) | prêmio divide-se entre combinações | cada combinação vencedora paga 5 000 × (valor_unitário) |

## 6. Centena
| Variação | Cotação |
|----------|---------|
| Seca na Cabeça | **600 ×** |
| Cercada 1.º-5.º | **120 ×** |
| Posicional / Invertida | **90-150 ×** |

## 7. Milhar
| Variação | Cotação |
|----------|---------|
| Seca (1.º) | **4 000 ×** |
| Cercada 1.º-5.º | **800 ×** |
| Combinada (até 24 inversões) | 4 000 ÷ nPermutações |

### Fórmulas Gerais
* **valor_aposta**: quantia em R$
* **nCombos**: número de combinações geradas (para apostas combinadas)
* Pagamento bruto = multiplicador × valor_aposta.
* Casas podem aplicar taxas sobre o pagamento ou arredondamentos.

---

ℹ️ Este arquivo foi gerado para referência rápida de desenvolvimento e pode ser exibido ao usuário final em seções de ajuda ou regulamento.