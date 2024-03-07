# VMware Tanzu Application Service > Archivist

[![Alpha](https://img.shields.io/badge/Release-Alpha-orange)](https://img.shields.io/badge/Release-Alpha-orange) ![Github Action CI Workflow Status](https://github.com/cf-toolsuite/cf-archivist/actions/workflows/maven.yml/badge.svg) [![Known Vulnerabilities](https://snyk.io/test/github/cf-toolsuite/cf-archivist/badge.svg?style=plastic)](https://snyk.io/test/github/cf-toolsuite/cf-archivist) [![Release](https://jitpack.io/v/cf-toolsuite/cf-archivist.svg)](https://jitpack.io/#cf-toolsuite/cf-archivist/master-SNAPSHOT) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


You're a platform operator and you've managed to get [cf-hoover](https://github.com/cf-toolsuite/cf-hoover) deployed. It's happily aggregating usage data from multiple foundations. But now you want retain snapshot data across foundations spanning multiple collection intervals.

`cf-archivist` closes this gap by allowing you to collect and persist snapshots in a time-series based schema.  You may also define and manage query policies in a Git repo, then schedule execution of one, many or all of them, capture the results as email attachments, and finally send these to designated recipients.

There's also a basic user-interface that let's you filter and review detail snapshot data list applications and service instances.  More capabilities are planned.

# Table of Contents

  * [Prerequisites](#prerequisites)
  * [Tools](#tools)
  * [Clone](#clone)
  * [How to configure](#how-to-configure)
  * [How to Build](#how-to-build)
  * [How to Run with Maven](#how-to-run-with-maven)
  * [How to check code quality with Sonarqube](#how-to-check-code-quality-with-sonarqube)
  * [How to deploy to VMware Tanzu Application Service](#how-to-deploy-to-vmware-tanzu-application-service)
    * [using scripts](#using-scripts)

## Prerequisites

Required

* [cf-hoover](https://github.com/cf-toolsuite/cf-hoover)
* [VMware Tanzu Application Service](https://pivotal.io/platform/pivotal-application-service) 2.11 or better
* [Spring Cloud Services, Service Registry](https://docs.pivotal.io/spring-cloud-services/3-1/common/service-registry/index.html) 3.1.x or better


## Tools

* [git](https://git-scm.com/downloads) 2.40.0 or better
* [JDK](http://openjdk.java.net/install/) 17 or better
* [cf](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html) CLI 8.6.1 or better


## Clone

```
git clone https://github.com/cf-toolsuite/cf-archivist.git
```


## How to configure

Make a copy of then edit the contents of the `application.yml` file located in `src/main/resources`.  A best practice is to append a suffix representing the target deployment environment (e.g., `application-pws.yml`, `application-pcfone.yml`). You will need to provide administrator credentials to Apps Manager for the foundation if you want the butler to keep your entire foundation tidy.

> You really should not bundle configuration with the application. To take some of the sting away, you might consider externalizing and/or [encrypting](https://blog.novatec-gmbh.de/encrypted-properties-spring/) this configuration.

### To set the operations schedule

`cf-archivist` periodically queries an instance of `cf-hoover` to obtain foundation data.  It does this by configuring a Spring Cloud Loadbalancer [filter](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#webflux-with-reactive-loadbalancer) for use with [WebClient](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-webclient.html#boot-features-webclient).

Update the value of the `cron.collection` property in `application.yml`.  Consult this [article](https://www.baeldung.com/spring-scheduled-tasks) and the [Javadoc](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html#cron--) to understand how to tune it for your purposes.

### General configuration notes

If you copied and appended a suffix to the original `application.yml` then you would set `spring.profiles.active` to be that suffix

E.g., if you had a configuration file named `application-pws.yml`

```
./mvw spring-boot:run -Dspring.profiles.active=pws
```

### Using an external database

By default `cf-archivist` employs an in-memory [H2](http://www.h2database.com) instance.

If you wish to configure an external database you must set set `spring.r2dbc.*` properties as described [here](https://github.com/spring-projects-experimental/spring-boot-r2dbc).

Before you `cf push`, stash the credentials for your database in `config/secrets.json` like so

```
{
  "R2DBC_URL": "rdbc:<database-provider>://<server>:<port>/<database-name>",
  "R2DBC_USERNAME": "<username>",
  "R2DBC_PASSWORD": "<password>"
}
```

> Replace place-holders encapsulated in `<>` above with real credentials

Or you may wish to `cf bind-service` to a database service instance. In this case you must abide by a naming convention. The name of your service instance must be `cf-archivist-backend`.

[DDL](https://en.wikipedia.org/wiki/Data_definition_language) scripts for each supported database provider are managed underneath [src/main/resources/db](src/main/resources/db). Supported databases are: [h2](src/main/resources/db/h2/schema.ddl), [mysql](src/main/resources/db/mysql/schema.ddl) and [postgresql](src/main/resources/db/postgresql/schema.ddl).

> Review the sample scripts for deploying [postgres](scripts/deploy.postgres.sh) and [mysql](scripts/deploy.mysql.sh).  And consult the corresponding secrets samples for [postgres](samples/secrets.pws.with-postgres.json) and [mysql](samples/secrets.pws.with-mysql.json) when you intend to transact an externally hosted database.

### Managing policies

Creation and deletion of policies are managed via API endpoints by default. When an audit trail is important to you, you may opt to set `cf.policies.git.uri` -- this property specifies the location of the repository that contains policy files in JSON format.

When you do this, you shift the lifecycle management of policies to Git.  You will have to specify additional configuration, like

* `cf.policies.git.commit` the commit id to pull from
  * if this property is missing the latest commit will be used
* `cf.policies.git.filePaths` an array of file paths of policy files

If you want to work with a private repository, then you will have to specify

* `cf.policies.git.username`
* `cf.policies.git.password`

one or both are used to authenticate.  In the case where you may have configured a personal access token, set `cf.policies.git.username` equal to the value of the token.

#### Query policies

Query policies are useful when you want to step out side the canned snapshot reporting capabilities and leverage the underlying [schema](https://github.com/cf-toolsuite/cf-archivist/tree/master/src/main/resources/db) to author one or more of your own queries and have the results delivered as comma-separated value attachments using a defined email notification [template](https://github.com/cf-toolsuite/cf-archivist/blob/master/src/main/java/io/pivotal/cfapp/domain/EmailNotificationTemplate.java).

As mentioned previously the policy file must adhere to a naming convention

* a filename ending with `-QP.json` encapsulates an individual [QueryPolicy](src/main/java/io/pivotal/cfapp/domain/QueryPolicy.java)

If you intend to deploy query policies you must also configure the `notification.engine` property.  You can define it in your

application-{env}.yml

```
notification:
  engine: <engine>
```

or

secrets-{env}.json

```
  "NOTIFICATION_ENGINE": "<engine>"
```

> Replace `<engine>` above with one of either `java-mail`, or `sendgrid`

Furthermore, you will need to define additional properties depending on which engine you chose.  Checkout [application.yml](https://github.com/cf-toolsuite/cf-archivist/blob/master/src/main/resources/application.yml) to get to know what they are.

E.g, if you intended to use [sendgrid](https://www.sendgrid.com) as your email notification engine then your secrets-{env}.yml might contain

```
  "NOTIFICATION_ENGINE": "sendgrid",
  "SENDGRID_API-KEY": "replace_me"
```

### To set the operations schedule

Update the value of the `cron` properties in `application.yml`.  Consult this [article](https://riptutorial.com/spring/example/21209/cron-expression) and the [Javadoc](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html#cron--) to understand how to tune it for your purposes.

> `cron` has two sub-properties: `collection` and `execution`.  Make sure `execution` is scheduled to trigger after `collection`.


## How to Build

```
./mvnw --batch-mode --update-snapshots -DskipTests -P production verify
```

### Alternatives

The below represent a collection of Maven profiles available in the Maven POM.

* MySQL (mysql)
  * adds a dependency on [r2dbc-mysql](https://github.com/asyncer-io/r2dbc-mysql)
* Postgres (postgres)
  * adds a dependency on [r2dbc-postrgesql](https://github.com/pgjdbc/r2dbc-postgresql)
* Log4J2 logging (log4j2)
  * swaps out [Logback](http://logback.qos.ch/documentation.html) logging provider for [Log4J2](https://logging.apache.org/log4j/2.x/manual/async.html) and [Disruptor](https://lmax-exchange.github.io/disruptor/user-guide/index.html#_introduction)
* Native image (native)
  * uses [Spring AOT](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#spring-aot-maven) to compile a native executable with [GraalVM](https://www.graalvm.org/docs/introduction/)

```
./mvnw --batch-mode --update-snapshots -DskipTests -P production verify -Drdbms=mysql
```
> Work with MySQL backend

```
./mvnw --batch-mode --update-snapshots -DskipTests -P production verify -Drdbms=postgres
```
> Work with Postgres backend

```
./mvnw --batch-mode --update-snapshots -DskipTests -P production verify -Plog4j2
```
> Swap out default "lossy" logging provider

``
# Using Cloud Native Buildpacks image
./mvnw spring-boot:build-image -Pnative --batch-mode --update-snapshots -DskipTests -Pproduction

# Using pre-installed Graal CE
./mvnw native:compile --batch-mode --update-snapshots -DskipTests -Pproduction -Pnative
```


## How to Run with Maven

If you intend to run `cf-archivist` in a local development environment, you must first:

* Launch a standalone instance of [Eureka server](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-eureka-server.html)
* Launch an instance of [cf-hoover](https://github.com/cf-toolsuite/cf-hoover#how-to-run-with-gradle)

Then:

```
./mvw spring-boot:run -Dspring.profiles.active={target_foundation_profile}
```
where `{target_foundation_profile}` is something like `pws` or `pcfone`

> You'll need to manually stop to the application with `Ctrl+C`


## How to check code quality with Sonarqube

Launch an instance of Sonarqube on your workstation with Docker

```
docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube
```

Then make sure to add goal and required arguments when building with Maven. For example:

```
mvn clean package sonar:sonar -Dsonar.token=cf-archivist -Dsonar.login=admin -Dsonar.password=admin
```

Then visit `http://localhost:9000` in your favorite browser to inspect results of scan.




## How to deploy to VMware Tanzu Application Service

Please review the [manifest.yml](manifest.yml) before deploying.

### using scripts

Deploy the app (bound to a pre-existing instance of Spring Cloud Service Registry)

```
./scripts/deploy.sh
```
> Assumes a Spring Cloud Services service registry instance named hooverRegistry has already been provisioned

Shutdown and delete the app with

```
./scripts/destroy.sh
```
