# Task project

Task project is designed to run tasks in reliable manner.
It can run as a stand alone app or be embedded into another akka project both as publisher(s) and/or consumer(s). 
Current implementation allows:
 
 * Sending a message from actor A to B in a reliable way
 * Currently RabbitMQ is used as queueing provider but can be swapped out in favor of kafka or something else
 * Later persistent actors can be used instead of queueing

Use cases would be queueing, live data migration, sending events to Mixpanel, download files from a server and put on S3, etc.

## Dependencies

 * Java (latest preferred)
 * Sbt (check project/build.properties for version)
 * Currently Rabbit is used, makes sure it's running on localhost:5672 and user guest/guest is enable or modify config
 
## Future contributions

 * Designated Mixpanel consumer
 * Designated S3 consumer to put content on S3, to download files from other servers and put on S3
 * Designated Push notifier consumer to send pushes to iOS and Android devices  
 * Designated consumer for Web Socket pushes
# task
