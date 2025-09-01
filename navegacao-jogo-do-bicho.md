# Mapeamento de Navegação – Sistema Jogo do Bicho

> Este documento centraliza o fluxo de navegação de todos os `MenuNode` existentes no serviço `JogoBichoNavigationService`. **Sempre que um novo nó ou opção for criada/alterada/removida, este arquivo deve ser atualizado e os caminhos testados.**

---

## Lenda
- **ID**: identificador interno do nó (`nodeId`)
- **Título**: título exibido na tela
- **Tipo**: `MAIN_MENU`, `SUB_MENU`, `QUESTION`, `INFORMATION`
- **Opções**: lista de opções visíveis ao usuário. Cada opção é descrita como:
  - `codigo` – texto digitado pelo usuário
  - `label` – rótulo apresentado
  - `destino` – próximo `nodeId` quando selecionada

---

## Fluxo Detalhado

### 1. welcome (MAIN_MENU)
| código | label                     | destino |
|-------|---------------------------|---------|
| 1     | 🎯 Fazer Aposta            | bet_menu |
| 2     | 📊 Ver Resultados          | results |
| 3     | 📋 Ver Tabela de Grupos    | groups |
| 4     | 📖 Regras do Jogo          | rules |
| 5     | 💰 Ver Pagamentos          | payment |
| 6     | 📝 Ver Minhas Apostas      | view_bets |
| 0     | ❌ Sair                    | exit |

### 2. bet_menu (SUB_MENU)
| código | label                     | destino |
|-------|---------------------------|---------|
| 1 | 🐦 Grupo              | select_grupo |
| 2 | 🔢 Centena            | select_centena |
| 3 | 🔢 Milhar             | select_milhar |
| 4 | 🎲 Duque de Grupo     | select_duque_grupo |
| 5 | 🎲 Terno de Grupo     | select_terno_grupo |
| 6 | 🎲 Quadra de Grupo    | select_quadra_grupo |
| 7 | 🎲 Quina de Grupo     | select_quina_grupo |
| 0 | ⬅️ Voltar             | welcome |

### 3. select_grupo (QUESTION)
- **Descrição**: Escolha um grupo (1-25). Cada grupo leva a `select_horario`.
- **Opções**: `1-25` → select_horario, `0` → bet_menu

### 4. select_horario (QUESTION)
| código | label                | destino |
|--------|----------------------|---------|
| 1 | 09:00 – PTM | enter_value |
| 2 | 11:00 – BS  | enter_value |
| 3 | 11:00 – RIO | enter_value |
| 1 | 14:00 – PTM | enter_value |
| 2 | 16:00 – PT  | enter_value |
| 3 | 18:00 – PTN | enter_value |
| 1 | 19:00 – PTM | enter_value |
| 3 | 21:00 – PTN | enter_value |
| 0 | ⬅️ Voltar    | select_grupo |

> **Observação**: há códigos duplicados (1,2,3) reutilizados para horários diferentes. Isso causa conflitos e deve ser reavaliado.

### 5. enter_value (QUESTION)
| código | label   | destino |
|--------|---------|---------|
|1|R$ 1,00 | confirm_bet|
|2|R$ 2,00 | confirm_bet|
|3|R$ 5,00 | confirm_bet|
|4|R$ 10,00| confirm_bet|
|5|Outro   | confirm_bet|
|0|⬅️ Voltar| select_horario|

### 6. confirm_bet (QUESTION)
| código | label           | destino |
|--------|-----------------|---------|
|1|✅ Confirmar | welcome (via finalize_bet) |
|2|❌ Cancelar   | bet_menu |

### 7. results (INFORMATION)
| código | label                     | destino |
|-------|---------------------------|---------|
|1|📅 Ver resultados de hoje  | today_results |
|2|📊 Ver resultados da semana| week_results |
|0|⬅️ Voltar                 | welcome |

### 8. groups (INFORMATION)
| código | label                         | destino |
|-------|-------------------------------|---------|
|1|📖 Ver tabela completa       | show_groups |
|2|🔍 Buscar grupo por número    | search_group |
|0|⬅️ Voltar                    | welcome |

### 9. rules (INFORMATION)
| código | label                       | destino |
|-------|-----------------------------|---------|
|1|🐦 Regras – Grupo    | rules_grupo |
|2|🔢 Regras – Centena  | rules_centena |
|3|🔢 Regras – Milhar   | rules_milhar |
|4|🎲 Regras – Múltiplos| rules_multiplos |
|0|⬅️ Voltar            | welcome |

### 10. payment (INFORMATION)
| código | label                       | destino |
|-------|-----------------------------|---------|
|1|📊 Ver tabela completa | show_payments |
|2|💡 Dicas de aposta     | bet_tips |
|0|⬅️ Voltar            | welcome |

### 11. view_bets (INFORMATION)
| código | label                       | destino |
|-------|-----------------------------|---------|
|1|📅 Ver apostas de hoje | today_bets |
|2|📊 Ver todas as apostas| all_bets |
|3|🔍 Buscar por código    | search_bet |
|0|⬅️ Voltar            | welcome |

### 12. select_quina_grupo_count (QUESTION)
| código | label      | destino |
|--------|------------|---------|
|5|5 grupos | select_quina_grupo_count |
|6|6 grupos | select_quina_grupo_count |
|7|7 grupos | select_quina_grupo_count |
|8|8 grupos | select_quina_grupo_count |
|9|9 grupos | select_quina_grupo_count |
|10|10 grupos| select_quina_grupo_count |
|0|⬅️ Voltar| bet_menu |

### 13. select_quina_grupo (QUESTION)
- **Descrição**: Digite diretamente o número do grupo (1-25). Os grupos não são listados para evitar conflito com a lógica de captura dinâmica; o usuário deve utilizar os códigos numéricos.
- **Opções**: `1-25` → mantém em `select_quina_grupo` até completar a quantidade definida, `0` → bet_menu

---

## Pontos de Atenção / Bugs Encontrados
1. **Duplicidade de códigos no `select_horario`**: múltiplas opções usam os mesmos códigos `1`, `2`, `3`, gerando conflito na entrada do usuário.
2. **Placeholders não implementados**: `select_duque_grupo`, `select_terno_grupo`, `select_quadra_grupo`, `select_quina_grupo` ainda estão marcados como "em desenvolvimento".
3. **Fluxo de retorno**: após `finalize_bet`, retorno direto a `welcome`. Pode ser interessante levar o usuário a um recibo ou resumo antes.

---

## Checklist de Manutenção
- [ ] Resolver códigos duplicados em `select_horario`.
- [ ] Implementar telas de seleção para apostas múltiplas (duque, terno, quadra, quina).
- [ ] Adicionar validação de entrada numérica para grupos/horários.
- [ ] Atualizar este arquivo ao concluir cada item e retestar fluxos.

---

## Próximos Passos Sugeridos
1. **Refatorar `select_horario`** para códigos exclusivos (ex.: 1-8 sequenciais).
2. **Criar os nós reais** de seleção de grupos múltiplos reutilizando `createGrupoOptions()` com estados acumulados na sessão.
3. **Automatizar testes de navegação**: implementar testes unitários usando `menuNodes` para assegurar que todas as opções apontam para nós existentes.
4. **Internacionalização**: separar textos estáticos em arquivos de propriedades para futura tradução.
5. **Documentação viva**: gerar automaticamente esta tabela a partir do código usando reflexão, evitando divergências.

### Prompt aprimorado — Migração completa de **JPA → R2DBC**

> **Objetivo geral**
> Migrar **100 %** do projeto de **JPA** para **R2DBC** (programação reativa), eliminando qualquer resquício de código híbrido.

---

#### 1 ▪ Contexto & premissas

1. O projeto segue rigidamente os arquivos `CONTRIBUTING.md`, `commit-guidelines-pt-br.md`, `documentation-guidelines.md` e `SECURITY.md`.
2. Não utilizar **Lombok**; DTOs devem ser `record`; validação com `@Valid`; logs sem dados sensíveis.
3. A primeira fase da migração já foi executada; há entidades R2DBC novas e possivelmente restos de entidades JPA legadas.

---

#### 2 ▪ Metas específicas

1. **Verificar** se *todas* as entidades JPA migraram para R2DBC, mantendo exatamente **um** modelo por tabela.
2. **Remover** classes JPA equivalentes após cada conversão para evitar conflitos.
3. **Refatorar** repositórios para `ReactiveCrudRepository`/`R2dbcEntityTemplate` (conforme padrão do projeto).
4. **Compilar** o projeto; **corrigir erros** até `./mvnw verify` ou `./gradlew build` finalizar sem falhas.
5. **Garantir** testes de integração rodando em Testcontainers-MySQL (nunca H2).
6. Criar **plano de monitoramento** que permita auditar a migração e impedir regressões futuras.
7. **Commitar** e **pushar** todas as alterações ao branch principal seguindo *commit-lint* em português.

---

#### 3 ▪ Tarefas operacionais (executar na ordem)

| # | Ação                                                                                                                                                                                                                           | Resultado esperado               |
| - | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------------------------------- |
| 1 | **Auditar entidades**: listar `*.java` anotados com `@Entity`, `@Table` e `@DataR2dbc`.<br>Identificar duplicidades.                                                                                                           | Lista “*entidades-migracao.csv*” |
| 2 | Para cada entidade JPA sem equivalente R2DBC:<br>  a) Converter para modelo R2DBC com `@Table` e campos reativos.<br>  b) Atualizar quaisquer *mappers* / DTOs.                                                                | Entidade R2DBC criada            |
| 3 | Para cada entidade JPA *já migrada*:<br>  a) Confirmar renomeação/namespace correto para evitar colisão.<br>  b) **Excluir** arquivo JPA antigo.                                                                               | Nenhum JPA remanescente          |
| 4 | **Refatorar repositórios**:<br>substituir `JpaRepository` por `ReactiveCrudRepository` ou templates.                                                                                                                           | Repositórios 100 % reativos      |
| 5 | Ajustar **serviços** e **controladores** para `Flux`/`Mono`.                                                                                                                                                                   | Camada de serviço reativa        |
| 6 | **Compilar & testar**; corrigir erros até build verde.                                                                                                                                                                         | Build sem falhas                 |
| 7 | Criar **arquivo de monitoramento** `r2dbc-migration-checklist.md` contendo:<br>  • data, autor,<br>  • status de cada entidade/repositório,<br>  • métricas de cobertura de testes,<br>  • instruções para auditorias futuras. | Checklist rastreável             |
| 8 | `git add -A && git commit -m "feat(r2dbc): migração completa para R2DBC …"`                                                                                                                                                    | Commit atômico                   |
| 9 | `git push origin <branch>`                                                                                                                                                                                                     | Branch atualizado                |

---

#### 4 ▪ Critérios de aceitação

* **Zero** ocorrência de `javax.persistence` ou `jakarta.persistence` no código-fonte.
* Todos os testes passam com `Testcontainers-MySQL`.
* `r2dbc-migration-checklist.md` apresenta **100 %** das entidades migradas.
* Pipeline CI/CD conclui sem erros.

---

#### 5 ▪ Monitoramento contínuo

* Adicionar *job* de CI que execute `grep -R --line-number "jakarta\\.persistence"`; falhar se > 0 ocorrências.
* Incluir métrica de cobertura Jacoco > 80 %.
* Revisão de PRs deve exigir tag `r2dbc-only`.

---

#### 6 ▪ Entrega

* Repositório final **exclusivamente R2DBC e reativo**.
* Checklist de migração versionado.
* Commit(s) assinados e descritos em português claro.

> **IMPORTANTE:**
>
> * **Não gerar código simulado**; manipular diretamente os arquivos reais.
> * Mantenha a ordem sequencial; não prossiga para o passo seguinte sem o anterior verde.
> * Caso encontre obstáculos (ex.: funcionalidade impossível em R2DBC), documentar no checklist e propor solução antes de prosseguir.

---

Use este prompt como instrução única ao agente de automação.
