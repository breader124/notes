# Chapter 10 - Testing microservices: part 2

## Writing integration tests

Integration tests are the layer above unit tests. They focus on testing integration with other services, no matter if this integration is achieved via REST, event, etc. There are 2 techniques to make integration tests fast and reliable:
- test only service's adapter classes along with their dependencies instead of entire application
- use contracts for testing to simulate the respose of other side of communication channel

## Developing component tests

**Pattern: Service component test** - Test a service in isolation.

### Designing component tests

There are 2 options of designing component tests:
- in-process component tests - tested application run in the same JVM as tests, it uses mocks and stubs of external world, this way tests are easier to write, but on the other hand more brittle and don't reflect reality perfectly
- out-of-process component tests - tested application is packaged to deployable format and tested using real (might be dockerized) DBs, message brokers etc., this way env for testing better resembles reality and this way we can achieve higher "code coverage", downside of this approach is that there's need to somehow prepare communication with other services, most commonly that is done using e.g. Wiremock by manual configuration of expected responses to given requests

It's generally good idea to try to use technologies like Gherkin + Cucumber to speed up the process of writing component tests and make them more readable for business and QAs. Secondly, manually translating requirements to code might be error-prone.

## Writing end-to-end tests

Write as few of them as possible. It's good idea to consider testing *user journeys* instead of particular steps that user can do in the system. Exemplary user journey could be that user:
- creates order,
- revises order,
- cancels order.
This way it's possible to minimize the total amount of those brittle and heavy tests.
