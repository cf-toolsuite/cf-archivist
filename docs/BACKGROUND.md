# VMware Tanzu Application Service > Archivist

## Background

You're a platform operator and you've managed to get [cf-hoover](https://github.com/cf-toolsuite/cf-hoover) deployed. It's happily aggregating usage data from multiple foundations. But now you want retain snapshot data across foundations spanning multiple collection intervals.

`cf-archivist` closes this gap by allowing you to collect and persist snapshots in a time-series based schema.  You may also define and manage query policies in a Git repo, then schedule execution of one, many or all of them, capture the results as email attachments, and finally send these to designated recipients.

There's also a basic user-interface that let's you filter and review detail snapshot data list applications and service instances.

(More capabilities are planned).
