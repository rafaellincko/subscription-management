**Sistema de Assinaturas**

Este projeto é um sistema de gerenciamento de assinaturas, com cobrança automática de planos de usuários. Ele consiste em dois serviços principais:

subscription-service: gerencia assinaturas, verifica acesso e executa renovações automáticas.

payment-processor: Simula processamento de pagamentos de assinaturas via eventos do Kafka.

----------------------

**O projeto utiliza:**

PostgreSQL como banco de dados.

Redis para cache.

Kafka + Zookeeper para eventos de pagamento.

Spring Boot com perfil docker para configuração específica de container.

----------------------

**Pré-requisitos**

Docker 20+ e Docker Compose 2+

Java 17+ (opcional, para rodar localmente sem Docker)

----------------------

**Rodando com Docker Compose**

Na raiz do projeto, rode:
```
docker compose up --build
```

Os serviços ficarão disponíveis nos seguintes ports:

| Serviço |	Porta |
|-------|-------|
|subscription-service|	8080|
|payment-processor|	8081|
|Kafka (EXTERNAL)|	29092|
|Redis|	6379|
|PostgreSQL	|5432|

Para parar e remover os containers:

```
docker compose down
```


Para rodar em background:

```
docker compose up -d --build
```

----------------------

**Endpoints principais do Subscription Service**

- **Collections Postman:** Na raiz do repositório foram anexadas collections do postman para consumo dos endpoints.

**ADMIN:**


1. Criar assinatura como Admin
	```
	POST /admin/subscriptions
	```


	Exemplo JSON:
	```
	{
		"userId": "83e069bd-3cad-44ff-b0dc-ab8b20cf45a8",
		"plan": "PREMIUM",
		"startDate": "2026-01-24",
		"expirationDate": "2026-01-23",
		"status": "ATIVA",
		"failedRenewalAttempts": 0
	}
	```
	
2. Atualizar assinatura como Admin

	```
	PUT /admin/subscriptions/{userId}
	```

	Exemplo JSON:

	```
	{
		"plan": "PREMIUM",
		"startDate": "2026-01-24",
		"expirationDate": "2026-01-23",
		"status": "ATIVA",
		"failedRenewalAttempts": 3
	}
	```

**PÚBLICAS:**


1. Criar assinatura
	```
	POST /subscriptions
	```

	Exemplo JSON:
	```
	{
		"userId": "83e069bd-3cad-44ff-b0dc-ab8b20cf45a8",
		"plan": "PREMIUM"
	}
	```

2. Consultar assinatura por usuário
	```
	GET /subscriptions/{userId}
	```

3. Verificar permissão de acesso de usuário (assinatura cancelada mas ainda dentro do período de uso)
	```
	GET /subscriptions/{userId}/access
	```

4. Cancelar assinatura
	```
	DELETE /subscriptions/{userId}
	```

----------------------

**Scheduler de renovação automática**

O SubscriptionRenewalScheduler verifica assinaturas expiradas e envia eventos para o payment-processor via Kafka.

Cron atual: executa a cada minuto (0 * * * * *) para testes.

Em produção, ajustar para cron real (0 0 0 * * *) - toda meia noite.

---------------------

**Cache**

Cache configurado para consulta recorrentes que não fazem parte de processamentos críticos, como a consulta de assinaturas para renovação.

Quando uma assinatura é atualizada na base, o cache relacionado ao seu Id é limpo para atualização.

---------------------

**Observações**

O PostgreSQL inicializa com o script ./src/main/resources/schema.sql.

O Redis e Kafka possuem healthchecks para garantir que o subscription-service só rode quando estiverem prontos.

Máximo de tentativas de renovação de pagamento: 3. Após isso, a assinatura é suspensa.

Ao subir os containers é carregada uma massa de testes inicial com 10 assinaturas para consulta e observação (subscription-management\subscription-service\src\main\resources\data.sql)

O serviço payment-processor foi configurado para simular os pagamentos com uma taxa de 50% de chance de sucesso.

--------------------

**Idempotência e Consistência do Sistema**

O sistema de assinaturas garante que eventos de renovação e pagamentos não sejam processados múltiplas vezes de forma indevida, mantendo consistência no estado das assinaturas, mesmo em casos de falhas ou retrys. Isso é feito através de:

1. @Transactional no Spring Boot

Todos os casos de uso que modificam dados críticos (como RenewSubscriptionUseCase e HandlePaymentResultUseCase) são anotados com @Transactional.

Isso garante que todas as alterações no banco sejam atomicas: ou tudo é confirmado, ou nada é aplicado em caso de falha.

Evita que um evento de pagamento parcialmente processado deixe a assinatura em estado inconsistente.

2. Lock pessimista no banco

Antes de renovar uma assinatura, o repositório busca a assinatura usando lock pessimista (FOR UPDATE SKIP LOCKED) para evitar que dois eventos concorrentes tentem renovar a mesma assinatura ao mesmo tempo.

Isso garante que cada assinatura é processada por apenas uma thread/transação por vez, mantendo idempotência mesmo com múltiplos eventos Kafka chegando simultaneamente.

3. Estado intermediário da assinatura

Ao processar a renovação, a assinatura muda para o estado RENOVANDO.

Durante este estado intermediário:

Eventos duplicados de pagamento são ignorados.

A assinatura só volta para ATIVA se o pagamento for aprovado ou se não tiver atingido o máximo de tentativas permitidas, ficando apta para quando o scheduler rodar novamente no dia posterior. Caso atinja as 3 tentativas, é suspensa.

Isso permite identificar claramente se a assinatura já está em processamento e evita reprocessamento.

4. Validação antes da renovação

Antes de publicar um evento de renovação, o scheduler ou o use case valida se:

A assinatura está ativa.

A data de expiração indica que precisa ser renovada.

Essa validação garante que assinaturas já renovadas ou canceladas não sejam reprocessadas, reforçando a idempotência.

--------------------

**Cobertura de testes**

O projeto conta com testes unitários para garantir o comportamento correto das principais camadas da aplicação. Foram criados testes para os use cases, validando a lógica de renovação, tratamento de pagamentos e regras de negócio das assinaturas; para o scheduler, assegurando que a execução periódica de renovações funcione conforme esperado; e para o controller, garantindo que os endpoints REST retornem respostas corretas e tratem entradas inválidas de forma adequada. Os testes utilizam JUnit e Mockito, permitindo simular dependências externas, como repositórios e publicadores de eventos.

--------------------

**Próximos passos**

- Ampliar a cobertura de testes
