# Chapter 2 - Decomposition strategies

**Software architecture** of a computing system is the set of structures needed to reason about the system, which comprise software elements, relations among them, and properties of both.

Even though definition is quite abstract, the most important part of it is that is implies existence of **decomposition**. Additionally, it's worth to notice here, that this definition doesn't say anything about satisfying functional requirements. In fact, they can be met with any type of architecture. Software architecture is, on the other hand, a key player when it comes to **quality of service**.

## 4+1 architecture view model

Architecture of a system can be described by 5 views:
- logical view - classes, packages and relations between them
- implementation view - output of a build system, in Java world it consists of WAR/JAR files
- process view - components at runtime == processes and their communication (IPC)
- deployment - how the processes are mapped to machines, this view consists of physical or virtial machines and their communication (networking)
- scenarios (+1 elem) - show how to elements in each particular view collaborate

## Overview of application architectural styles

### Layered architecture

This architecture type organizes software components into layers, each layer is dependent on exactly one below or just one of the ones below. It's easy and really well-known, but it has some serious drawbacks:
- only one presentation layer - it's hard to adjust app to be used in more than one way
- single persistence layer - hard to work with multiple DBs from one app
- business layer dependent on persistence layer - impossible to test without DB

### Hexagonal architecture

Hexagonal architecture is an alternative to layered architecture with business logic in its heart. Business logic communicates with external world through set of ports. Port can be either inbound of outbound and can be implemented by inboud or outbound adapter. Inbound port defines set of operations that can be invoked by external world's call. On the other hand, outbound port defines set of operations invoked by business logic whenever it wants to interact with e.g. database.

Most important benefit of hexagonal architecture is that business logic is not dependent on persistence, or even better, any other layer. It is placed in the heart of the service.

---

Difference between monolithic and microservice architecture is mostly visible on implementation and deployment view using words of 4+1 model.

**Monolithic architecture** - Structure the application as a single executable/deployable component.
**Microservice architecture** - Structure the application as a collection of loosely coupled, independently deployable services.

### Services

**Service** is a standalone, independently deployable software component that implements some useful functionality. In microservices architecture it's a very popular mistake to try to judge whether service is micro or not taking its size as an argument. Better way to do it is to look at the lead time, how many people are required to deliver and complexity of deployments. If it can be done by small team independently, then the service is truly *micro*.

## Defining application's microservice architecture

Three step, iterative approach for defining architecture could look like:
1. Identify system operations (system operation is an abstractor of a request that the app must handle)
2. Identify services
3. Define service API and collaborations

### Identifying the system operations

System operations can be defined as a result of many approaches. One of them is Event Storming, but other ones, such as two-step process also exists. Mentioned two-step process is based on, as expected, 2 steps. First of them is to actually create high-level domain model using nouns from user stories. Second one is to create operations using verbs from user stories and previously identified domain model.

### Identifying services

There are two main, separate ways we can do it
- decomposition by business capabilities
- decomposition by DDD subdomains

**Business capability** is something that a business does in order to generate value.

---

**Decompose by business capability** - Define services corresponding to business capabilities.

Business capability of a company are identified by analyzing the organization's purpose, structure, and business processes.

Process of translating business capabilities to services is subjective and is more like an art, rather than engineering. It is based mostly on experience and good-feelings. Anyway, big advantage of identifying services using business capabilities method is that it's unlikely that capabilities will change in the future. It's very likely that company will change the way of *how* it works, but rather it won't change the main aspect of how it earns money. After all, it's crucial to remember that all the architectural work is an iterative process.

---

**Decomponse by subdomain** - Define services corresponding to DDD subdomains.

Decomposition by DDD subdomain should give very similar results to decomposition by business capabilities. In fact, concept of subdomain and bounded context translates very well to the microservices world, because they both have very similar goals like enabling independent development and deployments.

### Decomposition guidelines

1. Single Responsibility Principle - "A class should have only one reason to change" translated to microservices architecture world means that particular service should have only one reason to change
2. Common Closure Principle - "The classes in a package should be closed together again the same kinds of changes. A change that affects a package affects all the classes in that package" translated to microservices architecture world means that it should be possible to limit the scope of incoming changes to only one team working on a set of services assigned to it. 

### Obstacles to decomposing an app into services

Even though it seems straightforward to decompose given application into services, there are always some obstacles along the road. Most commonly we meed following ones:
- network latency - we'd like to avoid expensive great amounts of round-trips between services
- reduced availability due to sync communication - medicine to that problem is async communication (with all its drawbacks)
- maintaining data consistency across services - ACID transactions doesn't exist in distributed world, let's make a friend out of eventual consistency and saga pattern 
- obtaining a consistent view of the data - even though it's possible to obtain consistent view of data from one particular service, it's not possible to obtain one global view and be perfectly sure it's consistent
- god classes preventing decomposition - DDD modeling comes with a help, using this technique we can easily spot god classes by putting attention to linguistic differences, split them and eventually put in smaller, better suited forms into right bounded context

## Defining service APIs

1. Assign system operations to previously identified services
2. Determine the APIs required to support collaboration between services
