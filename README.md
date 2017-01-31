# Integration testing showcase


### Goal
The goal of this project is to demonstrate three different approaches of integration testing.
* __Sprint MVC with Servlet API mock objects__ -  Integration tests are implemented using the Spring MVC Test framework. This framework can be used in conjunction with any testing framework, such as JUnit. The Spring MVC Test framework does not use a running Servlet container, but rather uses Servlet API mock objects. It provides full Spring MVC runtime behavior and provides support for loading actual Spring configuration in the test context.
[Class implementation example](https://github.com/execom-eu/java-integration-testing-showcase/blob/master/src/test/java/eu/execom/labs/test_integration_showcase/controller/PersonControllerTest.java)
* __Sprint MVC with full running servlet container and Spring Boot__ - For a full end-to-end integration testing, with a running servlet container, we do it by using Spring Boot. This would also allow loading the actual Spring configuration in the test context.
[Class implementation example](https://github.com/execom-eu/java-integration-testing-showcase/blob/master/src/test/java/eu/execom/labs/test_integration_showcase/controller/PersonControllerSpringBootTests.java)
* __Usage of http client__ - Integration test are implemented using Apache HTTP client which we run with JUnit and target server endpoints expecting specific JSON messages.
This approach requires that services are deployed on a server. 
[Class implementation example](https://github.com/execom-eu/java-integration-testing-showcase/blob/master/src/test/java/eu/execom/labs/test_integration_showcase/controller/PersonControllerHttpClientTest.java)

### Docker
It is a good practice to use Docker for a integration testing because of its ability to create fully isolated component and its high portability. 
Introduction and usage example with Docker can be found below:
[Docker overview](https://github.com/execom-eu/java-integration-testing-showcase/blob/master/documentation/DockerOverview.md)


