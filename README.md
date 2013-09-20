Jacoco Merge
============

Tool to merge jacoca generated coverage metrics and create a single .exec file and report.

Supported sources for metrics are:

1) .exec files
2)  jacaco agents (running in tcpserver mode)


Setting up a jacoco agent on jboss
----------------------------------
See here for more: http://www.eclemma.org/jacoco/trunk/doc/agent.html

Download standalone jacoco zip file from:
http://www.eclemma.org/jacoco/index.html

and extract to c:/tools.  The agent jar file itself should be found in c:/tools/jacoco/lib/jacocoagent.jar  (don't bother trying the maven hosted version of this as it doesn't work!)


Start the jboss server with the following JAVA_OPTS set:

`set JAVA_OPTS=-javaagent:c:/tools/jacoco/lib/jacocoagent.jar=address=localhost,port=6300,output=tcpserver,includes=org.coder36.*`

Warning
-------
Be careful what classes you include, as the metrics will be stored in memory and it is easy to run out of permgen space.

Building
--------
`git clone
mvn clean install`


Usage
-----

`java -jar target/jacoco-merge-jar-with-dependencies.jar url=<host:port> url=<host:port> file=<file> file=<file> out=<execFile> reportDir=<outputFolder> classDir=<classFolder> srcDir=<sourceFolder> reset`

If you pass the `reset` flag, this will instruct the agents to reset their counts.

Example, using the JAVA_OPTS setup from above:
`java url=localhost:6300 out=c:/temp/jacoco.exec`

To also generate a report:

`java url=localhost:6300
out=c:/temp/jacoco.exec
classDir=c:/src/java/jsfdemo/target/classes
srcDir=C:/src/java/jsfdemo/src/main/java
reportDir=C:/src/java/jsfdemo/target/report
reset`
