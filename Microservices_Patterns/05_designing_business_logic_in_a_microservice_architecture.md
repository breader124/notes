# Chapter 05 - Designing business logic in a microservice architecture

## Business logic organization patterns

### Transaction script pattern

**Transaction script** - Organize the business logic as a collection of procedural transaction scripts, one for each type of request.

Creating transaction script based application is all about creation of 2 layers, one handling requests and minimal business logic and one for persistence. Business logic layer executes minimal logic placed inside, stores the data with help of persistence layer to finally return a response to the caller. Important thing is that persistence layer cannot contain anything else except for very simple code for modifying data in DB.

Main drawback of using transaction script pattern is that it's extremely difficult to modify it when changes come.

### Domain model pattern

**Domain model** - Organize the business logic as an object model consisting of classes that have state and behaviour.

Applying domain model pattern leads us to create object model consisting of a network of relatively small classes, where majority of them contains both data and behaviors. Using this pattern makes expressing business logic easier and more futureproof, as created model most probably should be easy to be extended. Additionally, it's easy to test, especially in comparison to the code following transaction script pattern.

### Domain-driven design

There are 2 main areas of DDD: strategic and and tactical.

Strategic is more about discovering the right division of a problem. Firstly, in a problem space we analyze existing solution trying to find subdomains of our model and assign them correct classification (core, supportive, generic). Then, in a solution space, we make a transition from AS-IS to TO-BE state of our solution. Based on the previously discovered subdomains, we try to find Bounded Contexts, which in the future will most probably become services.

Tactical DDD is a set of widely adopted building blocks that developers use to implement business logic. They are:
- Entity - an object that has a persistent identity, it must be possible to distinguish it from another
- Value object - an object that is a collection of values, they can be used interchangeably 
- Factory - an object or method responsible for creation objects that are too complex to be created via constructor
- Repository - an object that provides access to persistent entities and encapsulates mechanism for accessing DB
- Service - an object implementing business logic that doesn't belong to any entity or value object (be careful, lots of them might be a sign of poor design!)
