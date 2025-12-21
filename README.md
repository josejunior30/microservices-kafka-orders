# 🍽️ Pedido | Pagamento | Nota Fiscal — Microsserviços com Spring Boot + Kafka

Sistema didático de e‑commerce com **3 microsserviços** (Pedido, Pagamento e NotaFiscal) que simula um fluxo real de pedidos, usando comunicação assíncrona via Kafka.

Quando um pagamento é confirmado, um evento é publicado e os outros serviços reagem: o pedido muda para **PAGO** e a nota fiscal é gerada em XML + PDF (DANFE).

---

## 🎯 Objetivos do projeto

Este projeto demonstra:

- Microsserviços com Spring Boot 3.5.8 e Java 21  
- Comunicação assíncrona com **Apache Kafka** (tópico `pagamento-confirmado`)  
- Padrão **event‑driven** e **idempotência** no serviço de Nota Fiscal  
- Geração de **XML de nota fiscal** e **DANFE em PDF** (com Apache PDFBox e ZXing para QR Code)  
- Persistência com **H2 Database** e Spring Data JPA  
- Testes unitários e de integração com **JUnit 5 + Mockito**  
- Documentação da API com **Swagger/OpenAPI**

---

## 🧱 Arquitetura (Event-Driven)

- **Tópico Kafka:** `pagamento-confirmado`  
- **Evento:** `PagamentoConfirmadoEvent(pedidoId, status, valor)`

### Fluxo de eventos

1. **Pagamento** confirma o pagamento via API e publica um evento no Kafka.  
2. **Pedido** consome o evento e atualiza o status do pedido para `PAGO`.  
3. **NotaFiscal** consome o evento, verifica se já gerou nota para aquele `pedidoId` (idempotência) e gera:
   - XML da nota fiscal em disco  
   - DANFE (PDF) em disco

---

## 🧰 Stack

- **Java 21** • **Spring Boot 3.5.8**
- Spring Web • Spring Data JPA • Validation
- **Spring Kafka**
- **H2 Database** (em memória)
- **Apache PDFBox** (geração de PDF) • **ZXing** (QR Code)
- **JUnit 5 + Mockito** (testes unitários e testes web com MockMvc)
- **Swagger/OpenAPI (springdoc)**

---

## 📁 Estrutura do repositório

```text
.
├── pedido/       → Microsserviço de pedidos (Spring Boot)
├── pagamento/    → Microsserviço de pagamento (Spring Boot)
├── notafiscal/   → Microsserviço de nota fiscal (Spring Boot)
├── infra/        → Docker Compose (Kafka, Kafka UI)
└── README.md     → Este arquivo

```
---

## 📚 Swagger (OpenAPI)

Acesse a documentação da API de cada serviço:

- **Pedido:** `http://localhost:8080/swagger-ui.html`  
- **Pagamento:** `http://localhost:8081/swagger-ui.html`  
- **NotaFiscal:** `http://localhost:8082/swagger-ui.html`

---

## 📚 Kafka UI

Acesse a interface gráfica do Kafka:

- **Kafka UI:** `http://localhost:8085`

---

# 🚀 Como iniciar o projeto

## ✅ Rodar tudo com Docker (recomendado)

### Pré-requisitos

- Docker Desktop (Windows/Mac/Linux)  
- Docker Compose (já incluso no Docker Desktop)

### 1) Subir toda a infra + microsserviços

Na raiz do repositório:
```bash
cd infra
docker compose up -d --build
```
### 2) Acessar os serviços

- Swagger dos serviços:
  - Pedido: `http://localhost:8080/swagger-ui.html`
  - Pagamento: `http://localhost:8081/swagger-ui.html`
  - NotaFiscal: `http://localhost:8082/swagger-ui.html`
- Kafka UI: `http://localhost:8085`

---

## 🔧 Rodar local (sem Docker)

### Pré-requisitos

- **Java 21**
- **Maven**
- Kafka rodando localmente (pode ser subido via Docker)

### 1) Subir apenas Kafka + Kafka UI (via Docker)

Na raiz do repositório:

```bash
cd infra
docker compose up -d kafka kafka-ui
```

### 2) Rodar cada microsserviço localmente

Em cada projeto (`pedido`, `pagamento`, `notafiscal`), execute:
```bash
  mvn spring-boot:run
```
Os serviços ficarão disponíveis nas portas:

- Pedido: `8080`
- Pagamento: `8081`
- NotaFiscal: `8082`

---

## 🧪 Testes

O projeto inclui testes unitários e de integração com:

- JUnit 5
- Mockito
- Spring Boot Test / MockMvc

Para rodar os testes em cada microsserviço:
  - mvn test
---

## 🧪 Fluxo de uso (exemplo)

1. Confirme um pagamento via API do serviço **Pagamento**:
```bash
PATCH /pagamentos/1/confirmar
Content-Type: application/json
```
> Não é necessário enviar corpo (`JSON`) nessa chamada. O serviço já sabe o `pedidoId` pela URL e o valor do pagamento pelo pedido associado.

2. O serviço publica um evento `PagamentoConfirmadoEvent` no tópico `pagamento-confirmado`.  
3. O serviço **Pedido** consome o evento e atualiza o pedido com `id=1` para status `PAGO`.  
4. O serviço **NotaFiscal** consome o evento, verifica se já gerou nota para `pedidoId=1` e gera:
   - XML da nota em `notas/1.xml`
   - DANFE (PDF) em `notas/1.pdf`

---

## 🚧 Próximos passos (melhorias planejadas)

- Trocar H2 por PostgreSQL em produção  
- Adicionar autenticação JWT entre microsserviços  
- Deploy na aws
- Observabilidade com Spring actuator
- Incluir CI/CD básico (GitHub Actions)

---

## 📎 Observações

- Este é um projeto **didático**, ideal para demonstrar microsserviços, Kafka e geração de documentos em Java/Spring Boot.  
- Os arquivos XML e PDF da nota fiscal são salvos em disco (pasta `notas/`), mas em produção usaria um storage (S3, etc.).
