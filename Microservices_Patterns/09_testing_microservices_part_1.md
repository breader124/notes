# Chapter 9 - Testing microservices: part 1

Different types of tests:
- unit tests - test small part of a service, such as a class
- integration tests - verify that a service can interact with infrastructure services such as databases and other app services
- component tests - acceptance tests for an individual service
- end to end tests - acceptance tests for the entire application

## Test quadrant

There are 4 categories defined in test quadrant:
- Q1 - support programming and are technology facing (unit and integration tests)
- Q2 - support programming and are business facing (component and end-to-end tests)
- Q3 - critique application and are business facing (usability and exploratory tests)
- Q4 - critique application and are technology facing (nonfunctional acceptance tests such as performance tests)

## Consumer-driven contract testing

**Pattern: Consumer-driven contract test** - Verify that a service meets the expectations of its clients.

**Pattern: Consumer-side contract test** - Verify that the client of a service can communicate with the service.

How workflow looks like:
1. Team responsible for consumer writes contract and gives it to provider e.g. by GH PR
2. Team responsible for provider generates tests and provider stub based on contract given
3. Provider service is tested with generated tests, while provider stub is published to e.g. Maven repo
4. Consumer service downloads provider stub and runs it during test phase in order to execute its test cases against it

## Deployment pipeline

Example deployment pipeline might consist of following stages:
1. Pre-commit tests stage - it runs unit tests before actually commiting changes made by developer
2. Commit tests stage - compiles the service, runs unit tests and triggeres statis code analysis
3. Integration tests stage - runs the integration tests
4. Component tests stage - runs the components tests for the service
5. Deploy stage - deploys the service into production

Going further down this pipeline, commited code is getting more and more ready to be deployed to production and released to customers. It might have some manual steps inside, but good practice is to reduce them to the minimum if only possible.

## Unit tests

Unit tests are intended to confirm that the smallest, integral piece of code (so-called unit) works properly. There are 2 types of unit tests:
- solitiary unit tests (tests a class in isolation using mock objects for the class's dependencies)
	- domain services
	- controllers
	- inbound and outbound messaging gateways
- sociable unit tests (tests a class and its dependencies)
	- entities
	- value objects
	- sagas
