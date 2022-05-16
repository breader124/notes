# Chapter 6 - Developing business logic with event sourcing

## Developing business logic using event sourcing

**Pattern: Event sourcing** - Persist an aggregate as a sequence of domain events that represent state changes.

### The trouble with traditional persistence

1. Object-Relational impedance mismatch - there's conceptual mismatch between tabular data and rich, graph-structured domain models
2. Lack of aggregate history
3. Implementing audit logging is redious and error prone
4. Event publishing is bolted on to the business logic - additional effort must be put into the development to enable emitting domain events

### Overview of event sourcing

Event store is a database holding state changes of an entity rather than the entity itself.

Restoring state of an aggregate is accomplished in 3 steps:
1. Load the events for the aggregate
2. Create an aggregate instance by using its default constructor
3. Iterate through the events, calling apply()

In contrary to using events as additional feature only for the sake of notifying consumers about some business related changes, when using event sourcing there's no choice between emitting minimal or rich domain events. Particular domain event needs to hold as much data as it's neede to restore the state of an aggregate.

Restructuring the aggregate's method from traditional one to event sourcing based one assumes splitting this method into 2 separate ones. First one takes command as a argument, validates it, and without any changes in the aggregate itself publish domain events. Second method is intended to introduce state changes based on those events whenever in the future they are retrieved from an event store and applied to aggregate. As event represents something what already happened in the past, method applying event **cannot fail**.

Concurrent updates, similarly to the traditional world, can be handled using optimistic locking. 

### Using event sourcing and publishing events

There are two options to consider if we want to publish events from event store:
1. Use polling to publish events - add one more field to the event store containing information if event has already been published or not, fetch events that are not published, sort them appropriately, publish them and change mentioned field value
2. Use transaction log tailing

### Using snapshots to improve performance

For long-living aggregates it might be inefficient to restore the state purely from many events. In such cases, the solution is to introduce snapshots that are created with given frequency. Restoring the state with using snapshots assumes fetching snapshot first and then events happened after the moment of snapshot creation and applying them on top of it.

### Idempotent message processing

In case of using:
- RDBMS-based event store - create table storing ids of processed events and check it before starting processing
- NoSQL-based event store - store event id in the events generated while processing it, duplicate detection is done then based on checking if none of the aggregate's events contain currently received event id; in case of aggregate actions that doesn't emit events naturally, there's need to modify them and emit pseudo event solely for the purpose of recording process event ids 

### Evolving domain events

Domain events schema evolution can be handled by tools like Protobuf or Avro. Using them with correct compatibility level set, there's no risk of breaking consumers of events we publish.

Event store might be migrated through upcasting. It means that after retrievieng event in obsolete version from event store, it's updated by aforementioned upcaster and eventually stored again in renovated form. Whenever it is fetched again from the event store, upcaster won't come into play, because event will be in desired version from the very beginning.

### Benefits of event sourcing

1. Reliably published domain events - there's no option to not publish an important event if entire application is based on the concept of publishing events, this way we gain guarantee that history we built is accurate
2. Preserves the history of aggregates - it's possible to restore the state from any point in time if it's required e.g. for an audit
3. Mostly avoids the O/R impedance mismatch problem - events are easy to be serialized and stored
4. Provides developers with a time machine - it's easier to implement unanticipated requirements, because we have the full history of interatctions with our applications

### Drawbacks of event sourcing

1. Different programming model that has a learning curve
2. Complexity of a message-based application - there's need to take care of deduplication, at-least-once delivery etc.
3. Evolving events can be tricky - as events can reside in event store in a wide variety of versions, there's a chance that service's code will be bloated with code designed specially to deal with that, it can be solved with using *upcaster* described above
4. Deleting data is tricky - as event sourcing is designed to store data forever, removing the data is complicated while using it and soft delete is not always an option due to GDPR regulations
5. Querying the event store is challenging

There are 2 main ways to achieve GDPR compliance:
- encrypt events storing user's personal information with a key that will be removed when user wants his data to be erased
- if personal data is used as an eventId, better solution might be to introduce *pseudooptimization*, it is based on introducing another unique value as a replacement for personal information and storing association between those two in separate DB, whenever user wants to be forgotten, we can remove the association and this way achieve GDPR compliance
