# Project-Microservices

A Spring Boot–based microservices system built to explore production-grade distributed systems patterns, event-driven architecture, and AI/LLM integration in a backend context.

## Overview

This project simulates a real-world e-commerce/service platform composed of independently deployable microservices, communicating via REST and Kafka, with service discovery, API gateway routing, resilience patterns, and an AI-powered chatbot service.

## Architecture

- **Eureka Discovery Service** — service registry for dynamic service discovery
- **API Gateway** — single entry point for routing requests to downstream services, with load balancing
- **Auth Service** — authentication and authorization
- **Product Service** — product catalog and inventory management
- **Customer Service** — customer data and profile management
- **Notification Service** — event-driven notifications (Kafka consumer)
- **Chatbot / chatAI Service** — AI-powered conversational service using Spring AI, integrating OpenAI and local LLMs via Ollama

## Design Patterns & Practices

- **Saga Pattern** — for managing distributed transactions across services without a central coordinator
- **Circuit Breaker** — for fault tolerance and graceful degradation when a downstream service is unavailable
- **Load Balancing** — client-side load balancing across service instances via the API Gateway
- **Caching** — reducing redundant calls and improving response times for frequently accessed data
- **Event-Driven Architecture** — Kafka used for asynchronous, decoupled communication between services
- **Service Discovery** — Eureka for dynamic registration and lookup of service instances

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot, Spring Cloud (Eureka, Gateway)
- **Messaging:** Apache Kafka
- **Resilience:** Circuit Breaker (Resilience4j)
- **AI/LLM:** Spring AI, OpenAI API, Ollama (local LLM execution)
- **Database:** MySQL
- **API Style:** RESTful APIs, third-party API integrations

## Key Features

- Independently deployable microservices with clear domain boundaries
- Resilient inter-service communication with circuit breaker fallback handling
- Asynchronous event processing via Kafka producers/consumers
- Centralized routing and load balancing through the API Gateway
- AI-powered chatbot service supporting both cloud (OpenAI) and local (Ollama) LLM inference
- Caching layer to reduce latency on repeated queries

## Getting Started

### Prerequisites
- Java 17+
- Maven
- Docker (optional, for containerized services)
- Kafka (local or Dockerized)

### Running locally
```bash
# Clone the repository
git clone https://github.com/chetupatil/Project-Microservices.git
cd Project-Microservices

# Start Eureka Discovery Service first
cd Eureka-Discovery-service && mvn spring-boot:run

# Start API Gateway
cd ../api-gateway && mvn spring-boot:run

# Start individual services (Auth, Product, Customer, Notification, Chatbot)
cd ../Auth && mvn spring-boot:run
```

Each service can also be containerized and orchestrated via Docker Compose (see individual service folders for configuration).

## Project Structure

```
Project-Microservices/
├── Auth/                       # Authentication & authorization
├── Chatbot/ chatAI/            # AI-powered chatbot service (Spring AI + LLM)
├── Customer-service/           # Customer domain service
├── Eureka-Discovery-service/   # Service registry
├── Notification-service/       # Kafka-driven notification service
├── Product-service/            # Product catalog service
├── api-gateway/                # Central routing & load balancing
└── notify/                     # Notification support module
```

## Motivation

Built during a period of focused backend upskilling to gain hands-on experience with distributed systems patterns and generative AI integration in enterprise-style Java applications — beyond what's typically covered in day-to-day project work.

## Author

**Chetana Patil**
[LinkedIn](https://linkedin.com/in/chetana-patil-7b1207112/) · [GitHub](https://github.com/chetupatil)
