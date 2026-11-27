# api-cp-crime-results-bulkresulting

OpenAPI contract for the Common Platform bulk resulting for enforcement service
([service-cp-crime-results-bulkresulting](https://github.com/hmcts/service-cp-crime-results-bulkresulting)).

The build generates Spring interfaces and models from the specification and publishes them as a jar:

| Generated | Package |
|---|---|
| API interfaces | `uk.gov.hmcts.cp.bulkresulting.openapi.api` |
| Models | `uk.gov.hmcts.cp.bulkresulting.openapi.model` |

> API definitions follow the [HMCTS RESTful API Standards](https://hmcts.github.io/restful-api-standards/).

## Endpoints

Paths are relative to the service context path (`/bulkresulting`).

| Method | Path | Purpose |
|---|---|---|
| GET | `/hearings` | Enforcement hearings at a court that are eligible for bulk resulting, one page at a time, with the caller's in-flight count |
| GET | `/config` | Settings the UI must follow: `maxBatchSize` and `searchWindowDays` |

Every call needs the `CJSCPPUID` header, which the gateway sets from the user's session. Errors are returned as
RFC 9457 problem details (`application/problem+json`).

## Build

```bash
./gradlew build
```

The specification is `src/main/resources/openapi/openapi-spec.yml`.

## Versioning and publishing

- Pushes to `main` publish a draft artefact `<projectVersion>-<short-sha>` (see `gradle.properties`).
- A GitHub release publishes the release version from the tag and the Swagger UI.

See [`docs`](./docs) for the versioning strategy, file conventions and the GitHub Actions workflows.

## License

This project is licensed under the [MIT License](LICENSE).
