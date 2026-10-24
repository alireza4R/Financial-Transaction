# Account and Transaction Management

A REST API built with Java and Spring Boot for managing customer accounts, processing financial transactions, and retrieving account statements.

## Features

- Create accounts with unique 14-digit account numbers and zero initial balances.
- Retrieve account details, balances, and account numbers by customer identifier.
- Update account information and record field changes, timestamps, and operator usernames.
- Submit deposits, withdrawals, and transfers with tracking numbers.
- Check transaction status, execution time, and failure reasons.
- Calculate transfer fees using a configurable rate, minimum, and maximum.
- Retrieve successful transactions using optional filters and database pagination.
- Authenticate API requests with HTTP Basic and test endpoints through Swagger UI.


## Architecture and Processing

The application uses a layered structure with separate account, transaction, security, and exception-handling components.

Transaction submission and execution are separate:

1. The API stores a request with `PENDING` status and returns its tracking number.
2. A scheduled worker selects a pending request from the database.
3. The corresponding deposit, withdrawal, or transfer handler locks the required accounts, validates the operation, and executes it.
4. The request is updated to `SUCCESSFUL` or, when execution validation fails, `FAILURE`.

Successful requests include a transaction number and completion time. Failed validation includes a failure reason. The request tracking number and successful transaction number are separate identifiers.

Handlers share a common workflow through the Template Method pattern. Pending requests are selected using `FOR UPDATE SKIP LOCKED`, and account updates use database locks to coordinate multiple application instances.

Balance updates, request completion, and transfer fee records share one database transaction. Each transfer fee is stored as a separate transaction linked to its parent transfer.

## Technology

Java 21, Spring Boot, Spring Data JPA, Spring Security, Oracle, Flyway, Maven, and springdoc-openapi.

The development environment runs Oracle in Docker with persistent volume storage. The application can also connect to another accessible Oracle instance. The worker runs inside the Spring Boot application.

## Account Statements

`GET /transaction/report` requires only `accountNumber`.

Optional filters include:

- `counterpartyAccount`
- `transactionType`
- `direction`: `IN`, `OUT`, or `ALL`
- `minAmount` and `maxAmount`
- `from` and `to`

Results contain successful transactions, ordered newest first. Page numbering starts at `1`; the default page size is `20`, with a maximum of `100`.

Dates use ISO-8601, for example `2026-10-01T00:00:00Z`. The start of the date range is inclusive, and the end is exclusive.

## Setup and Configuration

### Requirements

- JDK 21 or later.
- A running Oracle database. The development environment uses Oracle AI Database 26ai Free.
- A database user with `CREATE SESSION`, `CREATE TABLE`, and `CREATE SEQUENCE` privileges and sufficient tablespace quota.

The repository includes Maven Wrapper; a separate Maven installation is unnecessary.

### Database Connection

Configuration is located in:

```text
src/main/resources/application.properties
```

Set the following environment variables in your shell or IDE run configuration:

| Variable | Description | Default |
|---|---|---|
| `DB_URL` | Oracle JDBC connection URL | `jdbc:oracle:thin:@localhost:1521/FREEPDB1` |
| `DB_USERNAME` | Database username | `bank_app` |
| `DB_PASSWORD` | Database password | Required; no default |

### Application Settings

```properties
app.transfer-fee.rate=0.001
app.transfer-fee.minimum=500
app.transfer-fee.maximum=10000
app.transfer-fee.bank-account-number=00000000000001

app.transactions.worker-delay-ms=500
```

- `rate`: Fee multiplier.
- `minimum` and `maximum`: Fee limits in tomans.
- `bank-account-number`: Account receiving transfer fees.
- `worker-delay-ms`: Delay between the completion of one worker execution and the next, in milliseconds. Each execution processes at most one request.

Operators are configured in the same file. The current local testing credentials are:

```properties
app.security.operators[0].username=ali
app.security.operators[0].password-hash={noop}demo
```

`{noop}` identifies a plain-text test password. To use BCrypt, configure the value as `{bcrypt}<hash>`. Additional operators can be added using indexes `[1]`, `[2]`, and so on.

### Database Migrations

Flyway scripts are located in:

```text
src/main/resources/db/migration/
├── V1__create_sequences.sql
├── V2__create_tables.sql
├── V3__create_bank_account.sql
└── V4__create_indexes.sql
```

They create sequences, the `accounts`, `account_history`, and `transaction_requests` tables, the initial bank account, and indexes.

The database user must already exist. On startup, Flyway applies pending migrations before Hibernate validates the schema. Do not execute these scripts manually.

The initial bank account number is `00000000000001`. If the configured bank account changes, the replacement account must exist and be active.

Add a new migration for subsequent schema changes instead of editing an already-applied migration.

### Running the Application

Once Oracle is ready and environment variables are configured, run from the project root.

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Alternatively, run `AlirezaApplication` from your IDE.

The default port is `8080`:

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [OpenAPI JSON](http://localhost:8080/v3/api-docs)

Use Swagger's **Authorize** button with the application operator credentials. The current local defaults are username `ali` and password `demo`. These are separate from the database credentials.

To try a transaction, create an account, submit a request through `POST /transaction/request`, and check its result through `GET /transaction/{id}` using the returned tracking number. Receiving a tracking number confirms submission; check the status to determine the execution result.
