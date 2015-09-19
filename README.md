# Task project
[![Build Status][build-status-badge]][build-status-url]
[![Code Quality][code-quality-badge]][code-quality-url]
[![Code Coverage][code-coverage-badge]][code-coverage-url]

[![Download][download-badge]][download-url]
[![Release][release-badge]][release-url]
[![Tag][tag-badge]][tag-url]
[![Commits][commits-badge]][commits-url]
[![Issues][issues-badge]][issues-url]

[![License][license-badge]][license-url]
[![Chat][chat-badge]][chat-url]

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


[build-status-badge]: https://img.shields.io/travis/yeghishe/task.svg?style=flat-square
[build-status-url]: https://travis-ci.org/yeghishe/task
[code-quality-badge]: https://img.shields.io/codacy/df32a59f61e24b8ca83ed02246216e54.svg?style=flat-square
[code-quality-url]: https://www.codacy.com/app/ypiruzyan/task
[code-coverage-badge]: https://img.shields.io/codecov/c/github/yeghishe/task.svg?style=flat-square
[code-coverage-url]: https://codecov.io/github/yeghishe/task
[download-badge]: https://img.shields.io/maven-central/v/io.github.yeghishe/task_2.11.svg?style=flat-square
[download-url]: https://maven-badges.herokuapp.com/maven-central/io.github.yeghishe/task_2.11
[release-badge]: https://img.shields.io/github/release/yeghishe/task.svg?style=flat-square
[release-url]: https://github.com/yeghishe/task/releases
[tag-badge]: https://img.shields.io/github/tag/yeghishe/task.svg?style=flat-square
[tag-url]: https://github.com/yeghishe/task/tags
[commits-badge]: https://img.shields.io/github/commits-since/yeghishe/task/v0.1.3.svg?style=flat-square
[commits-url]: https://github.com/yeghishe/task/commits/master
[issues-badge]: https://img.shields.io/github/issues/yeghishe/task.svg?style=flat-square
[issues-url]: https://github.com/yeghishe/task/issues
[license-badge]: https://img.shields.io/badge/License-Apache%202-blue.svg?style=flat-square
[license-url]: LICENSE
[chat-badge]: https://img.shields.io/badge/gitter-join%20chat-brightgreen.svg?style=flat-square
[chat-url]: https://gitter.im/yeghishe/task
