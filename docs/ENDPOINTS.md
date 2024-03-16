# VMware Tanzu Application Service > Archivist

## Endpoints

These REST endpoints have been exposed for reporting and administrative purposes.


### Snapshot

Point in time capture of all workloads

```
GET /snapshot/detail/ai
```
> Provides filterable list of all applications (by collection time, foundation, organization and space)

```
GET /snapshot/detail/si
```
> Provides filterable list of all service instances (by collection time, foundation, organization and space)


### Cache refresh

```
POST /cache/refresh
```
> Triggers snapshot data collection from cf-hoover and refreshes cache
