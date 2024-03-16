# VMware Tanzu Application Service > Archivist

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
