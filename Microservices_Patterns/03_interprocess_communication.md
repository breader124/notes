# Chapter 3 - Interprocess communication in a microservice architecture

## Overview of interprocess communication

### Interaction styles

One-to-one interactions:
- request/response - service client makes a request to a service and waits for a response; this communication style generally results in thight coupling
- asynchronous request/response - service client send a request to a service, which replies asynchronously, waiting doesn't make sense, because it's known that service won't answer for a long time
- one way notification - service client sends a request to a service, but no reply is expected

One-to-many interactions:
- publish/subscribe - client publishes a notification message, which is consumed by zero or more interested services
- publish/async response - client publishes a request message and then waits for a certain amount of time for response from interested services

### Designing APIs

**API-first design** - try to avoid creating frontend and backend in silos doing some up-front design work instead to agree upon some API contract before even start of the development. It significantly decreases the chance of heavy conflicts between two sides and need to do additional development to enable communication.

### Evolving APIs

It is good practice to utilize semantic versioning specifying version number in the forat of MAJOR.MINOR.PATCH, they change when:
- MAJOR - you make an incompatible change to the API
- MINOR - you make backward-compatible ehancements to the API
- PATCH - you make a backward-compatbile bug fix

Versioning doesn't only apply to HTTP based communication where it may exist e.g. in the URL, but it might also be used in the published event.

**Robustness principle** - "Be conservative in what you do, be liberal in what you accept from others."

### Message formats

There are two types of message formats:
- text-based - their biggest advantage is that they are human readable and are self-describing (in case of JSON and XML), on the other hand, they are really verbose and they assume text-parsing which is expensive operation
- binary-based - they force API-first approach, they are smaller and easier to parse what makes communicating with them faster, on ther other hand, they are not in human readable format, what makes working with them more difficult after all

## Remote procedure invocation pattern

**Remote procedure invocation** - A client invokes a service using a synchronous, remote procedure invocation-based protocol, such as REST.

### REST

Key concept in REST is a **resource**. It most commonly represents a single business object or a collection of business objects. We manipulate resources using HTTP specific verbs, such as GET/POST/PUT/DELETE/HEAD/PATCH etc.

Richardson's REST maturity model:
- Level 0: invocation of other services without properly using HTTP verbs, requests are sent to inproperly designed endpoints that returns some arbitrarty responses
- Level 1: support for the idea of resources
- Level 2: support for not only resources, but also for HTTP verbs used in proper way
- Level 3: support for HATEOAS (Hypertext as the engine of application state)

Benefits of REST:
- simple and familiar
- easy to test from browser or Postman
- directly supports request/response style communication
- HTTP is firewall friendly
- it doesn't require an intermediate broker, what simplifies system's architecture

Drawbacks of REST:
- only supports request/response communication style
- reduced availability due to the fact, that both client and server must run at the same time while exchanging data
- clients must implement *service discovery mechanism*
- fetching multiple resources in a single request is challenging
- it's sometimes challenging to map multiple update operations to HTTP verbs

### gRPC

gRPC is a binary message-based protocol solely based on HTTP/2. It utilizes Google's Protocol Buffers, which makes it possible to easily achieve backward-compatibility.

Benefits of gRPC:
- it's straightforward to design an API that has a rich set of update operations
- it has an efficient, compact IPC mechanism, especially when exchanging large messages
- bidirectional streaming enables both RPI and messaging style of communication
- it enables interoperability between clients and services written in a wide range of languages

Drawbacks of gRPC:
- it takes more work for JS clients to consume gRPC based API than REST/JSON based APIs
- older firewalls might not support HTTP/2

### Circuit breaker pattern

**Circuit breaker pattern** - an RPI proxy that immediately rejects invocations for a timeout period after the number of consecutive failures exceeds a specific threshold.

Developing robust RPI proxies is a combination of following mechanisms:
- network timeouts
- limiting the number of outstanding requests from a client to a service
- circuit breaker pattern 

Additionally, there's need to remember to always handle partial failure while collecting response from multiple services. On failing services is not necessarily a reason to fail entire request, instead of doing that, it might be possible to return some default or cached value.

### Using service discovery

Service discovery idea is solely based on existence of component called service registry. There are 2 ways of interacting with it:
- services may directly ask for address and port of the service they want to call
- deployment infrastructure may handle service discovery e.g. by introducing load balancers before instances of particular service

**Self registration** - A service instance registers itself with the service registry.
**Client-side discovery** - A service client retrieves the list of available service instances from the service registry and load balances across them.

One big advantage of using client-side discovery is that discovery mechanism is independent of deployment infrastructure. Part of the services might be deployed on Kubernetes, while rest might be deployed on AWS's EC2. On the other hand, maintaining service registry on our own is a distraction and generally better idea is to use solution supported by platform.

Approach based on deployment infrastructure assumes that component like *registrar* works and is managed by platform. Newly started serices are observed by it and whenever it spots a difference, it updates service registry. From the other side, whenever service wants to call other service (using its DNS name), this call is redirected to platform router, which queries service registry and then request is routed to valid service.

**3rd party registration** - Service instances are automatically registered with the service registry by third party.
**Server-side discovery** - A client makes a request to a router, which is responsible for service discovery.

Biggest advantage of server-side discovery is that services don't have any discovery code inside, on the other hand, it is solution working properly only if all of the services run in the environment of the same provider. After all, it's not a big problem and it's recommended solution.

## Asynchronous messaging pattern

**Messaging** - A client invokes a service using asynchronous messaging.

### Overview of messaging

**Message** consists of a headers and a body, there are 3 types of messages:
- document - generic message that contains only data, the receiver decides how to interpret it
- command - message that is equivalent of an RPC request, it specifies the operation to invoke and its parameters
- event - message indicating that something notable has occurred in the sender, most commonly it is a domain event representing change of a domain model's state

**Channel** can be of two types:
- point-to-point - there is exactly one consumer reading from the channel, it's often used to send commands
- publish-subscribe - message is delivered to all listening subscribers, this type of channel is ofter used in case of events

How to implements:
- async request/response - client sends command with *id* and *return address* as headers to request channel, service processes it and responds to reply channel pointed by *return address* with a *correlationId* header equal to received *id*
- one-way notifications - just send a message, nothing more
- publish-subscribe - messaging has a built-in support for this type of communication, service publishes messages to the channel attached to it and interested subscribers listens to mentioned channel
- publish/async responses - async request/response + publish-subscribe, there are 2 channels, one similar to described earlier and a response channel, correlation of requests/responses is done by correalating id/correlationId

Brokerless messaging:
- advantages:
	- lighter network traffic and better latency
	- eliminates possibility of the message broker being a performance bottleneck
	- features less operational complexity, because there's no message broker to maintain
- drawbacks:
	- services need to know about each other
	- if offers reduced availability (same as in case of sync request/response)
	- implementing mechanisms as guaranteed delivery is more challenging

Broker-based messaging:
- advantages:
	- loose coupling - client doesn't need to know about subscribers
	- message buffering - receiver doesn't need to available at a time of sending the message
	- flexible communication - all the interaction styles described earlier are supported 
	- explicit interprocess communication - there's no false sense of security based on the feeling that invoking local and remote service is very similar in case of brokerless approach to messaging
- drawbacks:
	- potential performance bootleneck - mitigated by very high performance of modern brokers
	- potential SPOF - mitigated by HA design of modern brokers
	- additional operational complexity

Key factors when choosing broker:
- supported programming languages
- supported messaging standards (e.g. JMS, AMQP, STOMP etc.)
- ordering
- delivery guarantees
- persistence
- durability
- scalability
- latency
- compteing consumers

### Competing receivers and message ordering

It is a common problem in messaging systems to ensure that messages related to particular resources are processed in order. Without that, e.g. when message are consumed by different instances out of order, some strange results might arise. To solve that problem, idea of *sharded* channels has been introduced, it is based on 3 steps:
1. Standard channel consists of 2 or more shards behaving like a channel
2. Message is routed to the specific channel based on its key (or result of some computations taking key as an arguments)
3. Each shard it assigned to a single receiver. This is the way how messages published in some order will be consumed in exactly this order.

### Handling duplicate messages

Ideally broker preserving ordering should be used when redelivering the messages!

There are two ways to handle message duplicates:
- write idempotent message handlers - idempotent means that delivering the same message twice doesn't change the state after first computation
- track messages and discard duplicates - save processed messages id in the same transaction that executed business logic, whenever in the future message with the same id is discovered, it can be just discarded

### Transactional messaging

**Pattern: Transactional outbox** - Publish an event as part of a database transaction by saving it in an OUTBOX in the database.

By using transactional outbox pattern, it's possible to make sure that message will be published at least once, because it is saved in the database in the same local, ACID transaction, in which domain object has been persisted.

Publishing events saved in OUTBOX table can be accomplished in two ways:
- polling publisher - message relay polls outbox table with fixed frequency trying to notice changes, whenever it spots the mentioned changes, it publishes messages
- transactional log tailing - transaction log miner reads transaction log entries and converts each log entry corresponding to an inserted messages into a message and published it to the message broker, there are already solutions available in the market supporting this approach:
	- Debezium
	- LinkedIn Databus
	- DynamoDB streams
	- Eventuate Tram ( written by author of the book :D )

**Pattern: Polling publisher** - Publish messages by polling the outbox in the database.
**Pattern: Transaction log tailiing** - Publish changes made to the database by tailiing the transaction log.

## Using asyn messaging to improve availability

Availability of a system consisting of several service communicating with each other in sync way is a product of availability of each of those services. In order to ensure HA, it's better to design system to utilise messaging.

### Eliminating sync interations

1. Use async request/response: This option assumes usage of request and reply channels. Communication between services is organized around command and replies. It makes system extremely resilient, but on the other hand might not be the best option when first service exposes sync API e.g. to external world.
2. Replicate data: Subscribe to events necessary to compute response to a request without calling any other services. Drawback of this approach is that storing large amounts of redundant data might not be efficient.
3. Finish processing after returning response: Compute a response from resources holding by service, return it to the client and then finish processing async way publishing right commands/event to the channels. It sounds complex, but it is preferred approach in many cases, because it solves other problems as well.