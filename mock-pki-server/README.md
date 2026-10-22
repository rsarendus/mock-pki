# PKI Mock Server

A configurable server application for mocking PKI services.

* [Prerequisites](#prerequisites)
* [Build and Run](#build-and-run)
* [Configuration](#configuration)
  - [Nested JSON/YAML configuration](#nested-jsonyaml-configuration)
  - [Mock service factories](#mock-service-factories)
    - [Generic mock service factories](#generic-mock-service-factories)
  - [Nested factories](#nested-factories)
    - [Response headers configurer factories](#response-headers-configurer-factories)

## Prerequisites

For general prerequisites, see [here](../README.md#prerequisites).

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

Information about alternative ways of configuring a Spring Boot application externally,
can be found [here](https://docs.spring.io/spring-boot/reference/features/external-config.html).

## Configuration

The mock services and the endpoints they provide, can be configured via the following properties.
For common Spring Boot properties see [here](https://docs.spring.io/spring-boot/appendix/application-properties/index.html).

| Property | Required | Type | Description |
| --- | --- | --- | --- |
| `mock.endpoints` | - | Array | A list of property-blocks specifying the endpoints of each mock service. Defaults to an empty list if not provided. |
| `mock.endpoints[i].factory.argument` | ? | String | The argument string that is passed to the factory. The presence and format depends on the factory* implementation. |
| `mock.endpoints[i].factory.type` | + | String | The fully qualified class name of the factory* implementation used to instantiate the mock service providing the endpoint. |
| `mock.endpoints[i].method` | + | String | The HTTP method of the endpoint. Supported values: `GET`. |
| `mock.endpoints[i].path` | + | String | The path of the endpoint. |

\* A list of available mock service factories can be found [here](#mock-service-factories).

The following examples specify a single HTTP GET endpoint on path `/example`.
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

Factories that require multiple units of information passed into them, expect YAML (or JSON) formatted argument strings.
The following examples

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

The most convenient way to insert nested YAML into an argument string, is to use the literal style block (denoted by the block header indicator `|`).
This enables the argument string to be formatted as indentation based block style YAML.

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

Factories instantiating specific implementations of mock services.

#### Generic mock service factories

Factories for mock services emitting generic HTTP responses.

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
| `httpHeadersConfigurerFactory.argument` | ? | String | The argument string that is passed to the factory. The presence and format depends on the factory* implementation. |
| `httpHeadersConfigurerFactory.type` | - | String | The fully qualified class name of the factory* implementation used to instantiate configurers emitting HTTP response headers. Defaults to [`ee.ristoseene.pki.mock.server.http.header.factory.NoOpResponseHeadersConfigurerFactory`](#eeristoseenepkimockserverhttpheaderfactorynoopresponseheadersconfigurerfactory) if not specified. |
| `httpStatus` | + | Number | HTTP status code of emitted responses. Must be in the range of `[100..999]`. |

\* A list of available HTTP response headers configurer factories can be found [here](#response-headers-configurer-factories).

Specific examples for configuring this factory can be found [here](src/test/resources/configuration/empty-response-service).

### Nested factories

Factories nested in the configuration of other factories.

#### Response headers configurer factories

Factories instantiating HTTP response headers configurers.

##### [`ee.ristoseene.pki.mock.server.http.header.factory.NoOpResponseHeadersConfigurerFactory`](src/main/java/ee/ristoseene/pki/mock/server/http/header/factory/NoOpResponseHeadersConfigurerFactory.java)

A factory for creating instances of HTTP response configurer implementations that emit no HTTP headers.
The implementation is guaranteed to not interact with response headers in any way.
