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

