# 🍽️ Pedido | Pagamento | Nota Fiscal — Microsserviços com Spring Boot + Kafka

Sistema didático com **3 microserviços** simulando um fluxo real orientado a eventos:

1. **Pagamento** é confirmado via API  
2. Um **evento Kafka** é publicado no tópico `pagamento-confirmado`  
3. **Pedido** consome o evento e atualiza o status para **PAGO**  
4. **Nota Fiscal** consome o evento e gera **XML + DANFE (PDF)** em disco

---

## 🧱 Arquitetura (Event-Driven)

**Tópico Kafka:** `pagamento-confirmado`  
**Evento:** `PagamentoConfirmadoEvent(pedidoId, status, valor)`

- **pagamento** → confirma pagamento e **publica** evento Kafka
- **Pedido** → **consome** evento e atualiza o pedido para **PAGO**
- **NotaFiscal** → **consome** evento, garante **idempotência** por `pedidoId` e gera **XML + PDF**

---

## 🧰 Stack

- **Java 21** • **Spring Boot 3.5.8**
- Spring Web • Spring Data JPA • Validation
- **Spring Kafka**
- **H2 Database**
- **Apache PDFBox** (geração de PDF) • **ZXing** (QR Code)
- **JUnit 5 + Mockito** (testes unitários e testes web com MockMvc)
- **Swagger/OpenAPI (springdoc)**
---
## 📚 Swagger (OpenAPI)

- **Pedido**: http://localhost:8080/swagger-ui.html  
- **Pagamento**: http://localhost:8081/swagger-ui.html  
- **NotaFiscal**: http://localhost:8082/swagger-ui.html  


## 📦 Infra (Docker Compose)

O repositório inclui um `docker-compose.yml` com **2 brokers Kafka + Zookeeper + Kafka UI**.

```bash
cd infra
docker compose up -d

Kafka UI:http://localhost:8085



