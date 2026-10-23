# PKI Mock Server

A configurable server application for mocking PKI services.

* [Prerequisites](#prerequisites)
* [Build and run](#build-and-run)
* [Configuration](#configuration)
  - [Nested JSON/YAML configuration](#nested-jsonyaml-configuration)
  - [Mock service factories](#mock-service-factories)
    - [Generic mock service factories](#generic-mock-service-factories)
  - [Nested factories](#nested-factories)
    - [Response headers configurer factories](#response-headers-configurer-factories)

## Prerequisites

See the [general prerequisites](../README.md#prerequisites).

## Build and run

To build the server application as an executable JAR file, run the following command in the base directory of the project:

```shell
./mvnw clean package
```

The executable JAR file `mock-pki-server.jar` will be placed into the `target` directory of the `mock-pki-server` module.
To run the application from the base directory of the project, run the following command:

```shell
java -jar mock-pki-server/target/mock-pki-server.jar --spring.config.additional-location=file:/path/to/your/configuration-file
```

For other ways to configure a Spring Boot application externally,
see the [Spring Boot externalized configuration documentation](https://docs.spring.io/spring-boot/reference/features/external-config.html).

## Configuration

The mock services and the endpoints they provide can be configured via the following properties.
For common Spring Boot application properties, see the [Spring Boot documentation](https://docs.spring.io/spring-boot/appendix/application-properties/index.html).

| Property | Required | Type | Description |
| --- | --- | --- | --- |
| `mock.endpoints` | - | Array | A list of endpoint definitions for each mock service. Optional; defaults to an empty list. |
| `mock.endpoints[i].factory.argument` | ? | String | The argument string that is passed to the factory. Requirement and format depend on the [mock service factory implementation](#mock-service-factories). |
| `mock.endpoints[i].factory.type` | + | String | The fully qualified class name of the [mock service factory implementation](#mock-service-factories) used to instantiate the mock service providing the endpoint. |
| `mock.endpoints[i].method` | + | String | The HTTP method of the endpoint. Supported values: `GET`. |
| `mock.endpoints[i].path` | + | String | The path of the endpoint. |

The following examples define a single HTTP GET endpoint on path `/example`.
The mock service providing the endpoint is instantiated via `example.package.ExampleFactory` using the specified argument string.

<details>
<summary>Environment variables</summary>

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

### Nested JSON/YAML configuration

Factories that need more than one input expect a YAML- or JSON-formatted argument string.
The following examples show how to pass nested configuration as an argument string in each configuration format.

<details>
<summary>Environment variables</summary>

Compact inline YAML or JSON can be assigned to environment variables.

```shell
..._FACTORY_TYPE='example.package.ExampleFactory'
..._FACTORY_ARGUMENT='{scalar: value}'
```
```shell
..._FACTORY_TYPE='example.package.ExampleFactory'
..._FACTORY_ARGUMENT='{"scalar": "value"}'
```
</details>

<details>
<summary>Properties</summary>

Compact inline YAML or JSON can be assigned to argument string properties.

```properties
....factory.type=example.package.ExampleFactory
....factory.argument={scalar: value}
```
```properties
....factory.type=example.package.ExampleFactory
....factory.argument={"scalar": "value"}
```
</details>

<details>
<summary>YAML</summary>

The most convenient way to insert nested YAML into an argument string is to use the literal block scalar (`|`).
This enables the argument string to be formatted as indentation-based block style YAML.

```yaml
...:
  type: example.package.ExampleFactory
  argument: |
    scalar: value
```

Alternatively, compact inline YAML or JSON can be assigned to argument string properties.

```yaml
...:
  type: example.package.ExampleFactory
  argument: '{scalar: value}'
```
```yaml
...:
  type: example.package.ExampleFactory
  argument: '{"scalar": "value"}'
```
</details>

### Mock service factories

The following chapters list available factories meant for instantiating specific implementations of mock services.

#### Generic mock service factories

##### [`ee.ristoseene.pki.mock.server.service.generic.factory.EmptyResponseServiceControllerFactory`](src/main/java/ee/ristoseene/pki/mock/server/service/generic/factory/EmptyResponseServiceControllerFactory.java)

A factory for creating instances of mock service implementations that:

* Do not interact with incoming requests.
* Emit responses with:
  - Specified HTTP status code.
  - Specified HTTP response headers.
  - No response body.

The argument string of the factory is expected to be in [JSON/YAML format](#nested-jsonyaml-configuration), specifying the following properties:

| Property | Required | Type | Description |
| --- | --- | --- | --- |
| `httpHeadersConfigurerFactory.argument` | ? | String | The argument string that is passed to the factory. Requirement and format depend on the [response headers configurer factory implementation](#response-headers-configurer-factories). |
| `httpHeadersConfigurerFactory.type` | - | String | The fully qualified class name of the [response headers configurer factory implementation](#response-headers-configurer-factories) used to instantiate configurers emitting HTTP response headers. Optional; defaults to [`ee.ristoseene.pki.mock.server.http.header.factory.NoOpResponseHeadersConfigurerFactory`](#eeristoseenepkimockserverhttpheaderfactorynoopresponseheadersconfigurerfactory). |
| `httpStatus` | + | Number | HTTP status code of emitted responses. Must be in the range of `[100..999]`. |

A set of example configuration files for this factory can be found [under test resources](src/test/resources/configuration/empty-response-service).

### Nested factories

The following chapters list various types of factories meant for instantiating injectable subcomponents that are consumed by other factories.

#### Response headers configurer factories

##### [`ee.ristoseene.pki.mock.server.http.header.factory.NoOpResponseHeadersConfigurerFactory`](src/main/java/ee/ristoseene/pki/mock/server/http/header/factory/NoOpResponseHeadersConfigurerFactory.java)

A factory for creating instances of HTTP response configurer implementations that emit no HTTP headers.
