#!/usr/bin/env bash

set -e

export APP_NAME=cf-archivist
export REGISTRY_NAME=hooverRegistry

cf push --no-start
cf bind-service $APP_NAME $REGISTRY_NAME
cf start $APP_NAME