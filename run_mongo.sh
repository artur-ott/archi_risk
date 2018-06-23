#!/bin/bash

service mongodb start
echo "Wait for mongo to start"
while ! nc -vz localhost 27017; do sleep 1; done
mongo
