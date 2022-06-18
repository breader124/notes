# Chapter 13 - Refactoring to microservices

## Overview of refactoring to microservices

Business problems caused by monolithic architecture:
- slow delivery - it's harder to compete with competitors because we cannot deliver so often as they do
- buggy software releases - bugs on production often causes that our clients finally choose services offered by competitors
- poor scalability - some parts of an application needs more resources than other parts, but keeping them in the same executable means that there's no way to scale accordingly to demands

While rewriting application from monolithic to microservice architecture, **avoid big bang way of doing it**.
> The only thing a Big Bang rewrite guarantees is a Big Bang ~ Martin Fowler

**Pattern: Strangler application** - Modernize an application by incrementally developing a new (stangler) application around the legacy application.
Strangler pattern took its name from the strangler vines growing around trees in rain forests in order to reach the sunlight. During the process, the tree often dies, because it's either killed by the vine or it dies of old age, leaving a tree-shaped vine.

Good practices while refactoring monolith to microservices:
- demonstrate value early and often
- minimize changes to the monolith
- don't invest in expensive and fancy infrastructure at the beginning, only must-have component to start is deployment pipeline taking care of automated testing

## Strategies for refactoring a monolith to microservices

There are 3 main strategies for strangling the monolith:
- Implement new features as services
- Separate the presentation tier and backend
- Break up the monolith by extracting functionality into services

### Implement new features as services

It stops monolith from growing and is a good argument showing value of microservices to the business. Additionally, it doesn't cause monolithic application to become even more unmaintanable and complex. Just obey Law of Holes saying "if you find yourself in a hole, stop digging".

Most probably changes in 2 components will be need to be able to implement new features as services:
- in API gateway to route new feature requests to new services and rest of them to the monolith
- integration glue code to keep communication between monolith and new services, and sync data

### Separate presentation tier from the backend

Most commonly monolithic application consists of 3 layers:
- presentation layer
- business layer
- data access layer

It's considered good practice to firstly detach presentation layer from rest of the monolith and create separate app from it. This way we'll force backend monolith to expose API and enable presentation monolithg developers to work faster. Mentioned API exposed by backend monolith can be later used by newly create microservices.

### Extract business capabilities into services

To extract one functionality to separate application, there's need to perform vertical slice through the monolith consisting of:
- inbound adapters that implement API endpoints
- domain logic
- outbound adapters such as DB access logic
- the monolith's database schema

There are 2 main difficulties that most probably will be met when extracting the service:
- splitting the domain model - solution'd be to replace object reference with primary key reference, that's the method used in case of DDD aggregates
- refactoring the database - to minimize the scope of changes needed to introduce, it's good practice to replicate the data between old monolith's DB and new service's DB

Technique to avoid a lot of changes in monolith's code after splitting domain model is to use slightly modified data replication (as in case of refactoring DB). Let's assume that we split God class A to A' and B, and then B is moved to the new service. Then:
1. we route all state changing requests to the new service, which becomes source of truth and handles state changing operations
2. we change migrated fields of A class to be read-only in A'
3. we look for places, when previously changed read-only fields are being set
4. we change each set operation invoked on read-only field to be the call to the new service
5. to keep the state consistent between both services and feed read-only fields with correct data, we enable data replication between new DB and old DB

## Designing how the service and the monolith collaborate

**Pattern: Anti-corruption layer** - A software layer that translates between two different domain models in order to prevent concepts from one model polluting another.

This chapter contained a lot of code and examples based on the context provided by the auther. To explain all the techniques, I'd need to copy paste this context here, so I won't do it. But before approaching migration from monolith to microservice-based application, it's strongly advised to revisit this chapter and remind all the useful techniques and their implementations.
