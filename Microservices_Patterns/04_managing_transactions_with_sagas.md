# Chapter 4 - Managing transactions with sagas

## Transaction management in a microservice architecture

In microservices based systems there is sometimes need to update different databases while processing one request. Of course, there is also need to keep data in consistent shape after all, even when something fails along the road. Contrary to monolithic apps, microservices based systems don't have an option to easily update all the data in one transaction, because mentioned data resides in different databases. 

Solution to problem described above could be to use distributed transaction utilising two-phase commit (2PC) under the hood. However, even though it's theoretically possible, serious drawbacks exist:
- many modern technologies, such as NoSQL databases or lightweight message brokers, don't support the standard for distributed transactions as it was described in X/Open DTP
- 2PC requires all the services participating in the transaction to be available, as system's availability is a product of availability of all single services, this requirement is almost fatal when aim is to create HA system

## Using the Saga pattern to maintain data consistency

**Saga** - Maintain data consistency accross services using a sequence of local transactions that are coordinated using asynchronous messaging.

How Saga is different from distributed transactions:
- it lacks isolation, effectively following ACD, rather than ACID
- it must be rolled back using compensation transactions, because whenever failure occurs, some local transactions might be already committed
- it doesn't have negative consequences regarding system availability

### Compensating transactions

Unfortunately it's not possible with Sagas to rollback all the data modifications in case of spotted issue easily with ROLLBACK statement. To do that, there is need to execute several **compensating transactions**. It's worth mentioning that not all steps of the process need to be compensated, those are:
- read-only steps
- steps followed by steps that always succeed
