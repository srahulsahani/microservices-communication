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

## ADD EUREKA SERVER
* Create springboot project with given dependency
~~~ 
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency> 
~~~

* Configure with these properties:
~~~
server:
  port: 8070

eureka:
  instance:
    hostname: localhost
  client:
    fetchRegistry: false
    registerWithEureka: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
~~~
* Enable @EnableEurekaServer annotation in springboot main class, then build and run application and check at 
http://localhost:8070

## ADD APIGATEWAY or EDGESERVER
* Create springboot project and add these two dependencies
* For Gateway
~~~
                <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-gateway</artifactId>
		</dependency>
~~~
* For registering with EurekaServer
~~~
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
~~~

* ADD following configurations to application.yml
~~~
spring:
  application:
    name: "gatewayserver"
  config:
    import: "optional:configserver:http://localhost:8071/"
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lowerCaseServiceId: true
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    gateway:
      enabled: true
  info:
    env:
      enabled: true
~~~

* NOTE
  * Remove **spring-boot-devtools** dependency
  * Place this configuration for **apigateway** in **config-server** repository and name the file as gatewayserver.yml
~~~ 
server:
  port: 8072

eureka:
  instance:
    preferIpAddress: true
  client:
    registerWithEureka: true
    fetchRegistry: true
    serviceUrl:
      defaultZone: "http://localhost:8070/eureka/"
~~~

* Always start services in following order
* > Config-server > eureka-server> your-custom-apps(accounts,loans,cards) > apigateway
* Reason: 
  * configuration needs to be fetched for all services
  * service-registry(eurekaserver) needs to register all services
  * apigateway will automatically routes to you custom applications
  * Note: apigateway will only routes to custom apps when **spring.config.cloud.gateway.discovery.locator.enabled = true**
  * Also, new url will be: http://<dns>:<port-of-api-gateway>/<service-name>/<custom-app-api>
    * e.g http://localhost:8072/accounts/api/create, here accounts is serivice name registed with eureka server.
    * Use service name in smallcase only if **lowerCaseServiceId: true** else use in CAPS
    * e.g http://localhost:8072/ACCOUNTS/api/create