# Chapter 4 - Managing transactions with sagas

## Transaction management in a microservice architecture

In microservices based systems there is sometimes need to update different databases while processing one request. Of course, there is also need to keep data in consistent shape after all, even when something fails along the road. Contrary to monolithic apps, microservices based systems don't have an option to easily update all the data in one transaction, because mentioned data resides in different databases. 

Solution to problem described above could be to use distributed transaction utilising two-phase commit (2PC) under the hood. However, even though it's theoretically possible, serious drawbacks exist:
- many modern technologies, such as NoSQL databases or lightweight message brokers, don't support the standard for distributed transactions as it was described in X/Open DTP
- 2PC requires all the services participating in the transaction to be available, as system's availability is a product of availability of all single services, this requirement is almost fatal when aim is to create HA system

## Using the Saga pattern to maintain data consistency

**Pattern: Saga** - Maintain data consistency accross services using a sequence of local transactions that are coordinated using asynchronous messaging.

How Saga is different from distributed transactions:
- it lacks isolation, effectively following ACD, rather than ACID
- it must be rolled back using compensation transactions, because whenever failure occurs, some local transactions might be already committed
- it doesn't have negative consequences regarding system availability

### Compensating transactions

Unfortunately it's not possible with Sagas to rollback all the data modifications in case of spotted issue easily with ROLLBACK statement. To do that, there is need to execute several **compensating transactions**. It's worth mentioning that not all steps of the process need to be compensated, those are:
- read-only steps
- steps followed by steps that always succeed

## Coordinating sagas

There are 2 ways to structure saga's coordination logic:
- choreography
- orchestration

### Choreography-based sagas

There is no central coordinator responsible for saga execution. Instead of that, participants communicates with each other based on events exchanged through relevant channels.

While creating choreography-based saga, there is need to remember to:
- ensure that transactional messaging is being used
- establish some *correlationId* to make sure that received event can be used to perform some action on particular business object

Benefits:
- simplicity - services publish events when they create, update or delete business objects
- loose coupling - participants don't need to know about each other

Drawbacks:
- more difficult to understand - there's no central place describing how saga works
- cyclic dependencies between services - not a problem in most cases, but is considered as a design smell
- sometimes risk of a tight coupling - in case of complex scenarios, it might be a problem that some services will need to be updated in lockstep with other ones

When to use: in case of simple sagas, for complex ones it's better to use orchestration

### Orchestration-based sagas

There is one central coordinator taking care of the saga deciding which step needs to be executed when. It communicates with services using commands/async request-reply 

Benefits:
- simpler dependencies - services are not dependent on each other
- loose coupling - services know only about orchestrator
- improves separation of concerns and simplifies the business logic

Drawbacks:
- risk of centralizing too much business logic in the orchestrator

When to use: almost everytime except for the simplest cases

## Handling the lack of isolation

Because of its distributed nature, saga doesn't have a property of isolation and is effectively only ACD instead of ACID:
- A - All changes are committed or all are rollbacked
- C - Integrity within the service is handled by local database
- D - Durability of the local data is handled by local database

*Anomalies* that can occurr because of lack of isolation:
- lost updates - one saga overwrites without reading changes made by another saga
- dirty reads - a transaction or a saga reads the updates made by a saga that has not yet completed those updates
- fuzzy/nonrepeatable reads - two different steps of a saga read the same data and get different results because another saga has made updates

**Countermeasure** - action that can be taken into consideration to eliminate or at least limit the risk coming from lack of isolation

To efficiently talk about contermeasures, there is need to introduce proper naming for transactions participating in a saga:
- compensatable transactions - Transaction that can be potentially rolled back by using a compensating transaction.
- pivot transaction - Go/no-go point in a saga. If pivot transaction commits, saga will run until completion. It can be transaction that's neither compensatable nor retriable. Alternatively, it can be last compensatable transaction or first retriable transaction.
- retriable transaction - Transaction that follow pivot transaction and is guaranteed to succeed.

### Countermeasure: Semantic Lock

Saga's compensatable transaction sets a flag in any record it creates or updates. This flag indicates that saga is not committed and record could potentially change. Flag is cleared by retriable transaction or by compensating transaction.

### Countermeasure: Commutative updates

Design update operations to be commutative. It means that update operations can be executed in any order.

### Countermeasure: Pessimistic view

Reorder steps of saga to minimize risk of dirty reads. For example, conflicting transaction might be changed from being compensatable transaction to become retriable transaction. Thanks to that, it won't be possible for dirty read to happen.

### Countermeasure: Reread value

This countermeasure prevents lost updates. Saga using this pattern rereads record before updating it in order to verify if it's unchanged. If that's false, saga aborts and starts executing compensating transactions. This approach utilizes Optimistic Offline Lock pattern.

### Countermeasure: Version file

This countermeasure is based on existence of some kind of version file recording operations that are performed on a record and reordering them in order to make them eventually commutative.

### Countermeasure: By value

Based on the incoming request, application decides whether to execute operation using saga or distributed transaction. Most commonly distributed transactions are chosen for most important, high-risk operations, while sagas are chosen for low priority operations.
