# Simplified Fidic

## Running Project

In order to ease project run, it was used Docker, allowing consistence between
different systems, setting up Virtual Machine isolating environment ecosystem

For the following steps, go to `@/SimplifiedFidic/SimplifiedFidic`

### Setting Environment Variables

Create a `.env` file with same parameters as `.env.example` and set it on port `80`

It's important to set at 80, as it was configured by Docker.

### Running Project

Once at given project folder, and with Docker set, run `docker-compose up`
If not willing to run through Docker container, you can run using `JVM 21 and Gradle 8`
But would recommend using Intellij IDEA, as it has a lot of features that ease development and run.

Once service is up, you can post request the API through `http://localhost/api/simplifiedFidic`

## Architecture

The application used a variation of Clean Architecture, using Spring Framework, which is very popular among JVM community.

Data Transfer Object (DTO) folder could be isolated into new project, and also a connector Lib, allowing to import service functionalities
with other JVM services that uses similar Java version or above, easing cross-team implementation.

The Solution was dealt as a REST API, allowing to be consumed by any client that can handle HTTP requests, being it internal or not.
For Internal scenarios, it's recommended use of GRPC protocol, allowing different services of different languages communicate and know it's interface.

The Controller (Border) Has a single route:

```http
    {baseUrl}/api/simplifiedFidic
```

Internally Service has 2 different public functions.

````
    distributeFidicCascade
    processFidicInstruction
````

This was decided aiming service evolution, to handle the fidic instructions as they are sent by other services through the day/week/month

Note:
The Distribution function was not as good as author wish, the configurations for each fee would be better handled by a database, allowing to change it without code changes
and also allowing to add new fees without code changes, but as it was out of scope, it was not implemented.

Unit tests followed was divided by [functions as class](https://www.linkedin.com/pulse/test-structure-continuous-integration-teixeira-soares-de-almeida-heaqf/).

## ATTENTION

Input was changed to handle currency information as LONG instead of DOUBLE, due to [float imprecision](https://communities.actian.com/s/article/Floating-Point-Numbers-Causes-of-Imprecision)
The amount values are set as E'N', which means that the value is 10^N, so 1020 in amount E2 means 10.20.
