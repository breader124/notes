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

### Comparision of SOA and microservice architecture

| | SOA | Microservices |
| ----- | ----- | ----- |
| Communication | Smart pipes such as ESB including business logic | Dumb pipes based of e.g. REST or gRPC |
| Data | Global database common for all of the services | Data model and database per service |
| Typical service | Larger monolithic application | Small one |

---

Benefits of the microservices architecture:

Drawbacks of the microservices architecture: