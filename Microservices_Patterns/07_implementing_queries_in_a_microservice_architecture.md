# Chapter 7 - Implementing queries in a microservice architecture

There are 2 patterns for implementing queries in microservices architecture:
- API composition pattern
- Command Query Responsibility segregation (CQRS) pattern

## Querying using API composition pattern

### Overview of the API composition pattern

**Pattern: API composition** - Implement a query that retrieves data from several services by querying each service via its API and combining the results.

This pattern is the first solution that should come to your mind whenever you need to query data scattered among different services. It's relatively simple and straightforward. On the other hand, there are cases when it can't be applied.

There are 2 participants in API composition pattern:
- API composer - might be web app/mobile app as well as BFF exposing query API
- provider service - service providing required information firstly querying it from its own DB and then exposing via API

### API composition design issues

There are 2 main issues that might appear when applying this pattern:
- deciding which component in architecture is an API composer, different components might take this role:
	- web application
	- API Gateway
	- standalone service
- how to write efficient aggregation logic

API composer should use reactive programming model to minimize latency. Best case would be to execute all calls in parallel.

### Benefits and drawbacks of API composition pattern

Drawbacks:
- increased overhead - multiple DB queries, multiple requests...
- risk of reduced availability - general availability is a product of availabilities of involved services
- lack of transactional data consistency - as query might span multiple DBs, it cannot be done in isolation and data from different services might be in inconsistent state

## Using the CQRS pattern

**Pattern: Command query responsibility segregation** - Implement a query that needs data from several services by using events to maintain a read-only view that replicates data from the services.

### Motivation for using CQRS

In case of:
- multiple services to query: let's imagine a case when we want to filter and sort results, not all of the services have enough data for filter by some attribute, then this responsibility goes to the API composer which needs to fetch massive amount of data and then reimplement filtering logic that should belong to DB
- single service to query: 
	- let's imagine that we want to find nearest restaurants, for that purpose we need to store location data somewhere, storing them in ordinary database is dramatically nonefficient so we'd need to use some geospatial db of plugin supporting querying geospatial data, in such case we effectively create replica of our data and have a sign that CQRS should be used
	- fact that service owns some data doesn't mean that it has to expose the possibility to query that data, sometimes handling commands and querying are totally different from business perspective and in order to keep good level of separation of concerns, they need to be implemented in different components

### Overview of CQRS

Idea behind CQRS is to separate commands and queries and make them use different databases. The one serving the data for query operations is synchronized with the one handling commands through events. Thanks to that, there might be two different data models: main, domain one and the one optimized for reads. There are 2 options to implement CQRS:
- inside 1 service clearly separating part responsible for writes and reads, they of course should use different DBs
- create standalone service solely for the purpose of querying the data, this approach is the best one in case of queries not belonging to any service, then new service listens to events emitted by several services and builds joined model based on them

### Benefits and drawbacks of CQRS

Benefits:
- enables the efficient implementation of queries in a microservice architecture, especially when using event sourcing
- enables the efficient implementation of diverse queries (e.g. for querying geospatial data)
- makes querying possible in an event sourcing-based application
- improves separation of concerns

Drawbacks:
- more complex architecture
- dealing with the replication lag (read just after write might return old version of an aggregate)

**Advice: Use API composition pattern whenever possible and CQRS only if you must**

## Designing CQRS views

While building CQRS view there is need to:
- choose DB and design the schema:
	- first choice is to try with NoSQL as they are pretty scalable and support rich data model
	- fallback to RDBMS when NoSQL turns out to not be an option, let's say in case of complex queries executed on DB
- make sure that updates executed after receiving events are fast and efficient - anaylze updates that will be executed, if we'll be querying data only by primary key before updating it, then all NoSQL document DBs will be fine for that purpose, on the other hand, if we want to query by foreign key, then there's need to make sure that chosen DB supports that with great efficiency
- handle concurrency issues - investigate if events come from one aggregate or not, if not, then use optimistic or pessimistic locking
- handle idempotency issues - do deduplication or design idempotent event handlers
- decide how to enable client of the view to cope with the replication lag - view can detect incosistency following way: command operation returns id of emitted event to the client, client with this id queries view service, if event hasn't been processed yet, then view service returns error to the client, on the service side same mechanism as in case of deduplication is used
- decide what to do when there's need to build new or rebuilt the view - there is need to store events somewhere outside of the message broker to be able to access them later and reprocess in case of a need
