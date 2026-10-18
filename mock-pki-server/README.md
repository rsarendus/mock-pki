# PKI Mock Server

A configurable server application for mocking PKI services.

* [Prerequisites](#prerequisites)
* [Build and Run](#build-and-run)
* [Configuration](#configuration)

## Prerequisites

For general prerequisites, see [here](../README.md#prerequisites).

## Build and Run

To build the server application as an executable JAR file, run the following command in the base directory of the project:

```shell
./mvnw clean package
```

The executable JAR file `mock-pki-server.jar` will be placed into the `target` directory of the `mock-pki-server` module.
To run the application from the base directory of the project, run the following command:

```shell
java -jar mock-pki-server/target/mock-pki-server.jar --spring.config.additional-location=file:/path/to/your/configuration-file
```

Information about alternative ways of configuring a Spring Boot application externally,
can be found [here](https://docs.spring.io/spring-boot/reference/features/external-config.html).

## Configuration

The mock services and the endpoints they provide, can be configured via the following properties.
For common Spring Boot properties see [here](https://docs.spring.io/spring-boot/appendix/application-properties/index.html).

| Property | Required | Type | Description |
| --- | --- | --- | --- |
| `mock.endpoints` | - | Array | A list of endpoints representing mock services. Defaults to an empty list if not specified. |
| `mock.endpoints[i].factory.argument` | ? | String | The argument string that is passed to the factory. The presence and format depends on the factory implementation. |
| `mock.endpoints[i].factory.type` | + | String | The fully qualified class name of the factory implementation used to instantiate the mock service providing the endpoint. |
| `mock.endpoints[i].method` | + | String | The HTTP method of the endpoint. Supported values: `GET`. |
| `mock.endpoints[i].path` | + | String | The path of the endpoint. |

The following examples specify a single HTTP GET endpoint on path `/example`.
The mock service providing the endpoint is instantiated via `example.package.ExampleFactory` using the specified argument string.

<details>
<summary>Environment Variables</summary>

```shell
MOCK_ENDPOINTS_0_METHOD='GET'
MOCK_ENDPOINTS_0_PATH='example'
MOCK_ENDPOINTS_0_FACTORY_TYPE='example.package.ExampleFactory'
MOCK_ENDPOINTS_0_FACTORY_ARGUMENT='factory argument string'
```
</details>

<details>
<summary>Properties</summary>

```properties
mock.endpoints[0].method=GET
mock.endpoints[0].path=example
mock.endpoints[0].factory.type=example.package.ExampleFactory
mock.endpoints[0].factory.argument=factory argument string
```
</details>

<details>
<summary>YAML</summary>

```yaml
mock:
  endpoints:
    - method: GET
      path: example
      factory:
        type: example.package.ExampleFactory
        argument: factory argument string
```
</details>
