# VMware Tanzu Application Service > Archivist

## How to configure

Make a copy of then edit the contents of the `application.yml` file located in `src/main/resources`.  A best practice is to append a suffix representing the target deployment environment (e.g., `application-pws.yml`, `application-pcfone.yml`). You will need to provide administrator credentials to Apps Manager for the foundation if you want the butler to keep your entire foundation tidy.

> You really should not bundle configuration with the application. To take some of the sting away, you might consider externalizing and/or [encrypting](https://blog.novatec-gmbh.de/encrypted-properties-spring/) this configuration.

### To set the operations schedule

`cf-archivist` periodically queries an instance of `cf-hoover` to obtain foundation data.  It does this by configuring a Spring Cloud Loadbalancer [filter](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#webflux-with-reactive-loadbalancer) for use with [WebClient](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-webclient.html#boot-features-webclient).

Update the value of the `cron.collection` property in `application.yml`.  Consult this [article](https://www.baeldung.com/spring-scheduled-tasks) and the [Javadoc](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html#cron--) to understand how to tune it for your purposes.

### General configuration notes

If you copied and appended a suffix to the original `application.yml` then you would set `spring.profiles.active` to be that suffix

```
-Dspring.profiles.active=pws
```

or

```
SPRING_PROFILES_ACTIVE=pws
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

> Review the sample scripts for deploying [postgres](../scripts/deploy.postgres.sh) and [mysql](../scripts/deploy.mysql.sh).  And consult the corresponding secrets samples for [postgres](../samples/secrets.pws.with-postgres.json) and [mysql](../samples/secrets.pws.with-mysql.json) when you intend to transact an externally hosted database.
