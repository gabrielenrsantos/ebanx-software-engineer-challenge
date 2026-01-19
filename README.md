# EBANX Software Engineer Take-Home Challenge

This project implements a simple HTTP API to handle account balances and financial events (deposit, withdraw, transfer), following the specifications provided in the EBANX take-home challenge.

The focus of this implementation is simplicity, correctness, and adherence to the API contract, with clear separation of concerns and automated tests validating all endpoints.

## Tech Stack

- Java 21
- Spring Boot 3.5.9
- Maven 3.9.9
- JUnit 5 + MockMvc

## Architecture

The project follows a layered architecture:

- Controller layer: HTTP request/response handling
- Service layer: business logic and orchestration
- Domain layer: core business rules and state
- DTOs: API response models

The service layer is agnostic of HTTP, while controllers are responsible for mapping domain results to HTTP responses.

## Running the Application

Prerequisites

- Java 21
- Maven 3

### Run locally

```
mvn spring-boot:run
```

### The application will start on:

```
http://localhost:8080
```

## API Endpoints

### Reset state

Resets all accounts and balances.

```
POST /reset
```

#### Status code

```
200
```

### Get balance

Returns the balance for an account.

```
GET /balance?account_id={id}
```

### Non-existing account

#### Status code

```
404
```

#### Response

```
0
```

### Existing account

#### Status code

```
200
```

#### Response

```
5.0
```

### Create account / Deposit

Creates an account if it does not exist, or deposits into an existing one.

```
POST /event
```

#### Request

```
{
  "type": "DEPOSIT",
  "destination": "100",
  "amount": 10
}
```

#### Status code

```
201
```

#### Response

```
{
  "destination": {
    "id": "100",
    "balance": 10.0
  }
}
```

### Withdraw

Withdraws amount from an existing account.

```
POST /event
```

#### Request

```
{
  "type": "WITHDRAW",
  "origin": "100",
  "amount": 5
}
```

#### Status code

```
201
```

#### Response

```
{
  "origin": {
    "id": "100",
    "balance": 5.0
  }
}
```

### Non-existing account

#### Status code

```
404
```

#### Response

```
0
```

### Transfer

Transfers amount from one account to another.

```
POST /event
```

### Request

```
{
  "type": "TRANSFER",
  "origin": "100",
  "destination": "300",
  "amount": 15
}
```

#### Status code

```
201
```

### Response

```
{
  "origin": {
    "id": "100",
    "balance": 0.0
  },
  "destination": {
    "id": "300",
    "balance": 15.0
  }
}
```

### Non-existing origin account

#### Status code

```
404
```

#### Response

```
0
```

## Tests

All endpoints are covered by integration tests using JUnit 5 + MockMvc, validating:

- HTTP status codes
- Response bodies
- State transitions between requests

### To run tests:

```
mvn test
```

## Deployment

The application can be easily deployed as a standalone Spring Boot service.

For this challenge, the focus was kept on API correctness and test coverage.

## Design Decisions

- Controllers are responsible for HTTP concerns only
- Service layer does not depend on HTTP abstractions
- State is kept in-memory, as specified by the challenge
