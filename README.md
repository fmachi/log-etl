# log-etl

- Test Environment
In order to write and test the log-etl application, I decided to create a Docker File in order to run a mysql
server.
The docker file is placed into docker/docker-compose.yml. You can connect to the port 33601, selecting the logdatabase
schema with credentials logwriter/logwriter.
I assume that the tables are already there. You can find the scripts for the table creation into the folder
docker/init-scripts.

- Application building
The project has been build using maven. So all the dependencies and the build phase are handled this way.
I also used spring-boot and if you package the artifact you are producing a fat jar artifact that you can run
with the following command:

java -jar ./target/access-analyzer-1.0-SNAPSHOT.jar --startDate=2017-01-01.13:12:29 --duration=hourly --threshold=180

- Solution
the solution has been split into three different parts
- 1) cleaning the database
- 2) log access feeding
- 3.1)checking whether any ip exceeded the given number of access
- 3.2) adding ips to blacklist table

- RxJava
The log access feeder has been writtern with RxJava, I wanted to spend some time in order to check if it could be
used to run batch insert in parallel supporting backpressure too

- rx java
- hexagonal architecture
- how to run

java -jar ./target/access-analyzer-1.0-SNAPSHOT.jar --startDate=2017-01-01.13:12:29 --duration=hourly --threshold=180

// TODO: searching for the file
// TODO: add select query
