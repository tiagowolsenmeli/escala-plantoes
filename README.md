# escala-plantoes
Escala de Plantões

## Decisões Técnicas

### ADR-001 — Clean Architecture por feature package

**Decisão:** O projeto é organizado em pacotes por *feature* (`professional/`, `plantao/`), cada um com sub-camadas `controller`, `domain`, `infrastructure`, `service` e `usecase`.

**Justificativa:** Isola mudanças por domínio, facilita a adição de novas features sem tocar em código existente, e mantém dependências claras (controller → usecase → service → repository, nunca ao contrário).

**Consequências:** Controllers não podem injetar repositórios diretamente; use cases não devem usar DTOs; adicionar uma feature exige criar a estrutura completa de pacotes mesmo que inicialmente pequena.

---

### ADR-002 — DTOs como Java Records

**Decisão:** Todos os DTOs de request e response são Java Records, colocados em `controller/dto/`.

**Justificativa:** Records são imutáveis por padrão, eliminam boilerplate (getters, equals, hashCode, toString) e tornam explícito que DTOs são objetos de transferência sem comportamento.

**Consequências:** DTOs não podem ter lógica de negócio; a conversão entidade→DTO é feita por um factory estático `from(Entity)` no próprio record.

---

### ADR-003 — Estrutura de camadas interna por feature

**Decisão:** Cada feature é dividida internamente nas camadas `controller`, `domain`, `infrastructure`, `service` e `usecase`.

**Justificativa:** Organização voltada para a evolução futura do sistema — cada camada tem uma responsabilidade bem definida, o que facilita localizar onde realizar alterações em melhorias futuras.

**Consequências:** A divisão clara entre camadas permite testes de unidade mais fáceis devido à melhor modularização, e reforça os princípios SOLID ao manter cada classe com uma única responsabilidade e dependências sempre apontando para dentro (domain não depende de nada externo).

---

### ADR-004 — Subpastas por feature dentro de cada camada (Screaming Architecture)

**Decisão:** Cada camada (`controller`, `usecase`, `service`, etc.) possui subpastas nomeadas pela feature: `escala/`, `plantao/`, `professional/`.

**Justificativa:** Aplica o conceito de *Screaming Architecture* — a estrutura de pastas "grita" o que o sistema faz. Qualquer pessoa, seja desenvolvedor ou alguém de negócio, consegue identificar as features do sistema apenas olhando para os diretórios, sem precisar ler código.

**Consequências:** Facilita a leitura e navegação por novos desenvolvedores; localizar onde adicionar ou alterar uma feature é intuitivo. Em contrapartida, adicionar uma feature exige criar subpastas em todas as camadas envolvidas.

---

### ADR-005 — Exceções específicas por estado de negócio

**Decisão:** Cada estado de erro relevante possui sua própria classe de exceção (ex: `DuplicatePlantaoException`, `PlantaoNotFoundException`, `CargaHorariaExceededException`), mapeadas no `GlobalExceptionHandler` para o HTTP status adequado.

**Justificativa:** Exceções genéricas como `IllegalArgumentException` não comunicam intenção. Exceções nomeadas tornam o código mais legível — ao ler um use case, fica imediatamente claro o que pode dar errado e por quê. O mapeamento centralizado no `GlobalExceptionHandler` garante respostas HTTP consistentes sem poluir os use cases com detalhes de HTTP.

**Consequências:** Cada novo estado de erro exige a criação de uma classe de exceção e seu registro no `GlobalExceptionHandler`; em contrapartida, o código dos use cases fica limpo e expressivo, e a API retorna status HTTP semânticos (404, 409, 422) em vez de sempre retornar 500.

---

### ADR-006 — Separação entre UseCase e Service

**Decisão:** Use cases (`usecase/`) concentram as regras de negócio específicas de cada operação; services (`service/`) são camadas finas que delegam ao repositório e podem ser reutilizadas por múltiplos use cases.

**Justificativa:** Centralizar regras de negócio no use case garante que cada operação tenha um único ponto de entrada com toda a lógica relevante. O service, por não ter regras específicas, pode ser injetado em diferentes use cases sem duplicação — por exemplo, `ProfessionalService.findById()` é usado tanto por `RegisterPlantaoUseCase` quanto por qualquer outro use case que precise buscar um profissional.

**Consequências:** Services não devem conter lógica de negócio nem usar DTOs — apenas delegam ao repositório e retornam entidades. Use cases não acessam repositórios diretamente. Essa separação facilita testar regras de negócio de forma isolada, mockando apenas o service.

---

### ADR-007 — Identidade visual baseada na marca SPDATA

**Decisão:** As cores, logo e estilo visual do frontend seguem a identidade da SPDATA — empresa para a qual o sistema foi desenvolvido.

**Justificativa:** Alinhar o produto à marca do cliente transmite profissionalismo e consistência, reduzindo a distância visual entre o sistema entregue e os demais produtos que a SPDATA já utiliza internamente.

**Consequências:** Padronização.

---

### ADR-008 — Remoção de plantões adicionados na sessão atual

**Decisão:** O frontend permite remover plantões cadastrados durante a sessão atual, caso o usuário cometa um erro na adição.

**Justificativa:** Erros de cadastro são comuns em fluxos manuais. Permitir a remoção imediata evita que o usuário precise acionar suporte ou intervenção técnica para corrigir um lançamento errado.

**Consequências:** O endpoint `DELETE /api/plantoes/{id}` é exposto e acessível diretamente pela interface; a operação é irreversível.
