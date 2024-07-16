# MICROSERVICE COMMUNICATION
Generate jar packing of services by mentioning <packaging>jar<packaging> in pom.xml file,clear target folder contents and do a maven clean & install


### Run SpringBoot Jar using Maven and Java Command
* STEP 1: Go to your microservice root directory and run "mvn spring-boot:run" cmd
* STEP 2: Go to your microservice root directory and execute "java -jar .\target\<service-jar-name>.jar", 
    example: java -jar .\target\accounts-0.0.1-SNAPSHOT.jar

### Steps to create and run Docker image 
###### ALWAYS RUN THE DOCKER DESKTOP WHILE EXECUTING ANY DOCKER COMMAND OR BUILDING DOCKER IMAGE
> 1. #### Using Dockerfile and docker build cmd
* Create the Dockerfile
* Run this command in your application folder : docker build . -t <dockerusername>/<applicationname>:<version>
* "docker build . -t srahulsahani/accounts:s4"

> 2. #### Using BuildPacks e.g Paketo BuildPacks
* Add application name <image>
* "dockerusername/${project.artifactId}" inside plugins>plugin>configuration>image>name
* Go to application directory and execute "mvn spring-boot:build-image, if issue still persists try changing springboot-starter-parent dependency
  "
* NOTE: if following error is there
> Execution default-cli of goal org.springframework.boot:spring-boot-maven-plugin:3.3.0:build-image failed: Illegal char <:> at index 5: npipe:////./pipe/dockerDesktopLinuxEngine
* Go to Maven in Intellij, find your service e.g accounts>plugins>spring-boot, execute: spring-boot:build-image
* In pom.xml, under plugins>plugin, this dependency should be there "spring-boot-maven-plugin" with version same as spring-boot-starter-parent

> 2. #### Using Google Jib
* Add the required libraries
~~~ <groupId>com.google.cloud.tools</groupId>
			<plugin>
				<groupId>com.google.cloud.tools</groupId>
				<artifactId>jib-maven-plugin</artifactId>
				<version>3.4.1</version>
				<configuration>
					<to>
						<image>srahulsahani/${project.artifactId}:s6</image>
					</to>
				</configuration>
			</plugin>
~~~
* Execute "mvn compile jib:dockerBuild"


### Docker run port mapping signature
#### docker run -p <exposed-port>:<application-port> <docker-image-name>
###### Attached Mode
> docker run -p 8080:8080 srahulsahani/accounts:s4
###### Detached Mode
> docker run -d -p 8080:8080 srahulsahani/accounts:s4

## PUSH DOCKER IMAGES TO DOCKER HUB
* Command to push images to docker hub
> docker image push docker.io/<docker-username>/<service-name>:tag
> > docker image push docker.io/srahulsahani/accounts:s4

## INSTALL RABBITMQ in DOCKER
* Prerequisites- DOCKER
> docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management


## PULL AND SETUP MYSQL in DOCKER
* Prerequisite- DOCKER
* > docker run -p 3305:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accountsdb -d mysql
* > docker run -p 3307:3306 --name loansdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=loansdb -d mysql
* > docker run -p 3308:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb -d mysql

* if we are running all database container and microservice in same network, we should provide port as 3306
~~~
  network-deploy-service:
    networks:
      - eazybank
~~~



