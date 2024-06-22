# MICROSERVICE COMMUNICATION
Generate jar packing of services by mentioning <packaging>jar<packaging> in pom.xml file,clear target folder contents and do a maven clean & install


### Run SpringBoot Jar using Maven and Java Command
* STEP 1: Go to your microservice root directory and run "mvn spring-boot:run" cmd
* STEP 2: Go to your microservice root directory and execute "java -jar .\target\<service-jar-name>.jar", 
    example: java -jar .\target\accounts-0.0.1-SNAPSHOT.jar

### Steps to create and run Docker image 
* Run this command in your application folder : docker build . -t <dockerusername>/<applicationname>:<version>
* "docker build . -t srahulsahani/accounts:s4"

### Docker run port mapping signature
#### docker run -p <exposed-port>:<application-port> <docker-image-name>
###### Attached Mode
> docker run -p 8080:8080 srahulsahani/accounts:s4
###### Detached Mode
> docker run -d -p 8080:8080 srahulsahani/accounts:s4

  