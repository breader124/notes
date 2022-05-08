# Chapter 1 - Escaping monolithic hell

## Monolithic architecture

Advantages of monolithic architecture:
- simple to develop
- easy to make radical changes to the application
- straightforward to test	
- straightforward to deploy
- easy to scale (in terms of possibility to run multiple instances behing load balancer)

Drawbacks of monolithic architecture (even though it's most probably the  best choice at the beginning!):
- complexity intimidates developers
- development is slow
- path from commit to deployment is too long
- scaling is difficult
- delivering a reliable monolith is challenging
- locked into increasingly obsolete technology stack

## Scale cube

Scale cube is a scalability model consisting of 3 axises, each of them has its own purpose:
- X-axis scaling load balances requests across multiple instances. It's effectively scaling by cloning approach. This is a great way of improving the capacity and availability of an application.
- Z-axis scaling router requests based on an attribute of the requests. It's effectively scaling by partitioning approach. This is a great way to scale app to handle increaing transaction and data volumes.
- Y-axis scaling functionally decomposes an application into services; scaling by functional decomposition. It's effectively scaling by functional decomposition. This is a great way to scale development speed and independency of teams involved in software creation process.

## Microservices architecture

**Microservice architecture** is an architectural style that functionally decomposes an application into a set of services. Each services has a focused, cohesive set of responsibilities.

Benefits of the microservices architecture:
- It enables CI and deployment of large, complex applications
- Services are small and esaily maintainable
- Services are independently deployable
- Services are independently scalable
- Microservice architecture enable teams to be autonomous
- It allows easy experimenting and adoption of new technologies
- It has better fault isolation

Drawbacks of the microservices architecture:
- Finding the right set of services is challenging, there's a risk of building *distributed monolith* when failed
- Distributed systems are complex (in terms of keeping data consistency, intercommunication, queries etc.)
- Deploying features that span multiple services requires careful coordination
- Deciding when to adopt the microservice architecture is difficult, as stated earlier it's almost always better to start with monolithic application and then evolve

### Comparision of SOA and microservice architecture

| | SOA | Microservices |
| ----- | ----- | ----- |
| Communication | Smart pipes such as ESB including business logic | Dumb pipes based of e.g. REST or gRPC |
| Data | Global database common for all of the services | Data model and database per service |
| Typical service | Larger monolithic application | Small one |

## Pattern language

Most commonly seen components of a pattern:
- Forces - issues that you need to address when solving a problem
- Resulting context - consequences of applying a pattern (beneftis, drawbacks, issues)
- Related patterns - patterns related to currently used one related by one of the following relationships:
	- predecessor - pattern, which motivates need for this pattern
	- successor - pattern, which solved problem introduced by this pattern
	- alternative - pattern providing an alternative solution to this pattern
	- generalization - pattern that is general solution to a problem
	- specialization - a specialized form of a particular pattern

Problems introduced by microservices architecture can be divided into 3 high-level groups:
- infrastructure patterns - mostly infrastructure issues not related to development
- application infra patterns - infra issues impacting development
- application patterns - issues faced by developers during their daily work

Main groups of microrservices related patterns:
- decomposing application into services - kind of an art, but there are strategies, which might more or less help
- communication - as microservices based system is a distributed system, IPC plays a big role, there's need to:
	- choose communication style 
	- solve discovery related problems
	- ensure reliability on a satisfying level
	- solve issue with transactional messaging 
	- decide how our apps communicate with external services
- data consistency - how to approach an issue of a distributed transaction ( :x )
- querying data - how to query data owned by another service?
- service deployment pattern - having lots of moving parts, there's need to build automated pipeline taking care of deployments
- observability - tracking apps behavior in distributed world is a challenge
- automated testing - running single application is most commonly not enough to test functionality test end to end
- cross-cutting concerns - independently from technology, each service needs to solve issues related to observability, externalized config etc.
- security - how particular service can know caller's identity?

## Process and organization

### Organizational side of adopting microservices architecture

Using microservices patterns is absolutely not enough to successfully build distributed systems utilizing this architectural approach. This architecture requires process support in a form of DevOps culture and company's organization, which shoud be formed with small, autonomous, cross-functional teams. Having all these 3 factors in place, it's possible to enable rapid, frequent and reliable delivery of software.

**Communication overhead of a team of size N is O(N^2)**

### DevOps culture

To measure progress in adopting DevOps culture, we can introduce following metrics:
- deployment frequency
- lead time
- mean time to recover
- change failure rate

### Human side of adopting microservices

3 stages describing how people react to change:
1. Ending, Losing and Letting go - people resist when someone wants them to change old habits, they have a felling of missing old days
2. The Neutral Zone - internediate stage between old and new, people are mostly confused and trying to learn new ways of working
3. The New Beginning - final stage, where people used to new ways of doing things and are starting to experience the benefits
