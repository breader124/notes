# Channel 8 - External API patterns

## External API design issues

There are several drawbacks of an approach based on calling microservices directly by different clients:
- Fine-grained service APIs require client to make many requests what causes poor performance.
- Microservices are visible for clients thus architectural and API changes in the future will be more difficult or even impossible to implement.
- Services might use IPC mechanisms that aren't convenient or practical for client to use. Mobile and web clients work the best with HTTP and Websockets, they are most frequently used. On the other hand, protocols like gRPC or AMQP might not be possible to be implemented on frontend side or might be really cumbersome to implement, thus should be avoided.
- It's not sustainable solution for exposing API for third-party applications. It's not easy to change such API and for most of the cases it's a must to maintain it for a long period of time. It means that changes in the architecture also won't be easy if there's no encapsulation.

## The API gateway pattern

**Pattern: API gateway** - Implement a service that's the entry point into the microservices-based application from external API clients.

Responsibilities of API gateway:
- request routing
- API composition
- protocol translation (let's say from gRPC used internally to REST for external usage)
- providing each client with client-specific API
- edge functions (examples listed below) implementation:
	- authentication (sometimes it might be smart to implement upstream to API gateway service handling authentication)
	- authorization
	- rate limiting
	- caching
	- metrics collection
	- request logging

API gateway's architecture consists of 2 layers. One of them consists of modules per client type, e.g. mobile app module, web app module etc. Another one handles shared functionalities, such as implementation of edge functions. Way of handling requests by API gateway might differ based on complexity of the request. In case of simple ones, request is just routed to the destination service and its response is routed back to the requester. In case of complex ones, there's additional composition code joining responses of called services.

There's a problem regarding the ownership when using API gateway pattern. One solution might be to have one team responsible for the layer with implementation of shared functionalities and other teams responsible for modules intended for particular clients they own at the same time. Taking it one step further, API gateway team can develop common functionalities in form of a library and using this library, client teams can develop their own API gateways called *Backend for frontends*. This way each client has its own API gateway which is separately scalable, maintanable and has clear ownership model.

**Pattern: Backends for frontends** - Implement a separate API gateway for each type of client.

### Benefits and drawbacks of an API gateway

Benefits:
- it encapsulates internal structure of the application
- it reduces latency by limiting round-trips between clients and services
- it simplifies clients' code

Drawbacks:
- it might become development bottleneck
- it is another highly available component that must be developed and maintained

### API gateway design issues

There are following issues to consider during the process of designing API gateway:
- keeping performance and scalability on the greatest possible level - solution is to use reactive programming, to avoid callback hell good idea is to utilise declarative approach supported by some libraries like Reactor or RxJava
- writing maintainable code by using reactive programming abstractions
- handling partial failure - first of all, API gateway should be run behind load balancer sending request to another instance if one failed to respond, on the other hand, API gateways should also use patterns like circuit breaker and should somehow solve the cases when one of many services didn't respond correctly
- being the good citizen in the architecture - API gateway should respect all the decisions that have been made and implemented in case of other services, it's not an exception

## Implementing an API gateway

### Using an off-the-shelf API gateway product/service

There are following options to consider:
- AWS API Gateway - perfect for simple cases, AWS takes care of scaling and operations, doesn't support API composition so there'd be need to implement it in backend services, it also relies heavily on HTTP(S) and JSON, so protocol translation is impossible when using it
- AWS Application Load Balancer - it doesn't implement authentication or routing based on HTTP methods, can operate on HTTP/2 and Websockets as an addition to HTTP(S), but after all is a poor choice for an API gateway in comparison to the dedicated product described above
- Kong/Traefik - they can be installed on your own, so you need to take care of operations, they also don't support API composition, in order to achieve it, there's need to develop your own API gateway

### Developing your own API gateway

There are 2 problems to consider if decision's been made to start from scratch:
- designing mechanism for defining routing rules in order to minimize the complex coding
- correctly implementing HTTP proxying behaviour including headers handling

It's generally speaking better idea to consider using ready framework for building API gateway such as Netflix Zuul or Spring Cloud Gateway.
