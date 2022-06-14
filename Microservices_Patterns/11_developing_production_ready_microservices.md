# Chapter 11 - Developing production-ready microservices

## Developing secure services

There are 4 aspects of security that there's need to take care of:
- authentication - verification of identity of being trying to access resources
- authorization - verification if user/application is allowed to perform requested operation
- auditing - tracking operations in order to enforce compliace, help customer service or detect security issues
- secure interprocess communication - in a perfect world, all services should communicate over TLS

### Implementing security in microservices architecture

There are 2 obstacles preventing us from implementing same approach to authentication like in case of monolithic application in distributed world:
- in-memory security context - assuming that request might be handled by any instance of the microservice, it might be the case that it won't be exactly the same service as the one storing security context
- centralized session - like in case of security context, in-memory centralized sessions don't make sense in distributed world

Additionally, to not force all the service to handle authentication itself, good approach is to do it in API gateway. API gateway in such architecture is responsible for making sure, that request it forwards is authenticated and can enter the system. Additionally, security token is also attached to forwarded request to let services know who's calling them and make it possible to implement authorization (if such approach for implementing authorization has been chosen). Putting authorization there has an advantage of simplifying implementation of allowing access to particular objects, not entire operations exposed via API. That'd be very hard to implement in API gateway and most probably in the future would cause API gateway to be updated in a lockstep with services.

**Pattern: Access token** - The API gateway passes a token containing information about the user, such as their identity and their roles, to the services that it invokes.

There are 2 types of token:
- opaque - typically UUID, recipient needs to make additional call to fetch user info, that reduces availability and performance
- transparent - contains info about user, most popular form is JWT formed of claims; JWT consists of header, payload and signature, downside of JWT is that there's no way to revoke it, action is performed by service after checking signature and expiration data, so the only way to limit the damage that might be caused by malicious third party is to make it JWT short-lived (set near expiration date)

### OAuth 2.0

Key concepts of OAuth 2.0:
- Authorization Server - provides an API for authenticating users and obtaining an access token and a refresh token
- Access Token - a token that grants access to a Resource Server, format of this token is implementation dependent, but most commonly used one is JWT
- Refresh Token - a long-lived and recovable token that a Client uses to obtain a new AccessToken
- Resource Server - a service that uses an access token to authorize access, in microservice architecture, services are resource servers
- Client - a client that wants to access a Resource Server, in microservices architecture, API gateway is the OAuth 2.0 client

In the book Password Grant flow is utilised, however in Auth0 docs it's advised to rather use Authorization Code flow for regular web clients.

## Designing configurable services

It's not a good idea to hardcode information changing from env to env in the services' code nor configuration for different profiles (in case of Spring). Better solution is to use externalized configuration approach and utilise Hashicorp Vault or AWS Parameter Store for that purpose.

**Pattern: Externalized configuration** - Supply configuration property values, such as database credentials and network location, to a service at runtime.

There are 2 approaches to externalized config:
- push model - deployment infra passes the configuration properties to the service instance using e.g. system env variables, downside of this approach might be that it's most probably impossible to change configuration of a service without restarting it
- pull model - service instance reads its configuration properties from a configuration server, recommended approach because of numerous reasons, etc. the fact that this approach assumes that all configuration is centralized and can be managed easier, one downside might be that config server is another piece of infrastructure that needs to be maintained 

## Designing observable services

### Using the Health check API pattern

**Pattern: Health check API** - A service exposes a health check API endpoint, such as GET /health, which returns the health of the service.

It's important to check the health of each external component being used by the service. If that's e.g. database, then simple query might be executed to check if connection is still established and queries might be executed. Nice off-the-shelf implementation of /health endpoint is provided by Spring Boot Actuator and its /actuator/health endpoint.

### Applying the Log aggregation pattern

**Pattern: Log aggregation pattern** - Aggregate the logs of all services in a centralized database that supports searching and alerting.

In the old days it was a common pattern to store generated logs in one file present in the container's filesystem. As we stepped into the days of serverless computing, lambdas etc., good idea would be to just log to *stdout* and then leave the decision about what to do with logs up to the infrastructure.

### Using the Distributed tracing pattern

**Pattern: Distributed tracing** - Assign each request a unique ID and record how it flows through the system from one service to the next in a centralized server that provides visualization and analysis.

Distributed tracing is based on traces consiting of spans. Trace represents an external request and consists of one or more spans. A span represents an operation, and its key attributes are an operation name, start timestamp and end time. Each trace has its traceId assigned, this traceId might be used to search for all the logs related to this trace stored on log server mentioned earlier.

### Applying the Application metrics pattern

**Pattern: Application metrics** - Services report metrics to a central server that provides aggregation, visualization and alerting.

Metrics are sampled periodically and have following 3 properties:
- Name
- Value
- Timestamp

Developer's role in collecting metrics boils down to instrumenting the service to collect metrics and then exposing them to metrics server. Both can be achieved using e.g. Micrometer Metrics library in case of Spring Boot apps. Metrics are important, because observing them might make it possible to react to production issues even before users realize that something is not working properly. Key role in so fast reating belongs to automatic alerting offered by many metrics servers.

### Using the Exception tracking pattern

**Pattern: Exception tracking** - Services report exceptions to a central service that de-duplicates exceptions, generates alerts, and manages the resolution of exceptions.

### Applying the Audit logging pattern

**Pattern: Audit logging** - Record user actions in a database in order to help customer support, ensure compliance, and detect suspicious behaviour.

There are 3 optins to implement Audit logging pattern:
- add audit logging code to the business logic - might be hard to maintain in the future and is error-prone
- use aspect-oriented programming - much more handy than writing auditing logic manually, but there are restrictions regarding access to more detailed information like object which being modified
- use event sourcing - very good solution for logging actions that changes state, on the other hand, there's no option to audit log queries this way, if this is required, there's need to do it different way, e.g. utilising aspect-oriented programming

## Developing services using the Microservice chassis pattern

**Pattern: Microservice chassis** - Build services on a framework or collection of frameworks that handle cross-cutting concerns, such as exception tracking, logging, health checks, externalized configuration, and distributed tracing.

Additionally, it's good idea to prepare one template organizing microservice chassis using which development can be started almost in no time.

### From microservice chassis to service mesh

**Pattern: Service mesh** - Route all network traffic in and out of services through a networking layer that implements various concerns, including circuit breakers, distributed tracing, service discovery, load balancing, and rule-based traffic routing.

Service mesh is very promising technology and that's very likely that most of the responsibilities of a microservice chassis will migrate to mentioned mesh. This way it'd be even easier for developers to start writing business logic in almost no-time and maintain as little code as possible.
