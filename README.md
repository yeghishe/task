# Task project

[![Build Status](https://travis-ci.org/yeghishe/task.svg?branch=master)](https://travis-ci.org/yeghishe/task)
[![Codacy Badge](https://api.codacy.com/project/badge/df32a59f61e24b8ca83ed02246216e54)](https://www.codacy.com/app/ypiruzyan/task)
[![Join the chat at https://gitter.im/yeghishe/task](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/yeghishe/task?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Task project is designed to run tasks in reliable manner.
It can run as a stand alone app or be embedded into another akka project both as publisher(s) and/or consumer(s). 

Use cases would be queueing, live data migration, sending events to Mixpanel, download files from a server and put on S3, etc.

## Overview or architecture
[Blog post about task library](http://yeghishe.github.io/2015/09/16/open-sourcing-task-library-for-queueing-reliable-task-execution.html)


## Current features
 * Sending a message from actor A to B in a reliable way (proxy consumers)
 * Segment consumer
 * Push consumer (Apns, Gcm, Urban Airship)
 * Currently RabbitMQ is used as queueing provider but can be swapped out in favor of kafka or something else
 * Later persistent actors can be used instead of queueing

## Dependencies

 * Java (latest preferred)
 * Sbt (check project/build.properties for version)
 * Currently Rabbit is used, makes sure it's running on localhost:5672 and user guest/guest is enabled or modify config
 
## Future contributions

 * Kafka backend
 * Support for json maessages to have non scala producers also
 * Designated S3 consumer to put content on S3, to download files from other servers and put on S3
 * Designated Mixpanel consumer
