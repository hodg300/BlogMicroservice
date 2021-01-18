Blog Microservice
==========================

# What you’ll need

* Docker
* JDK 15 or later
* IDE that support Spring apps
* Gradle

# MongoDB

Please follow the guid in:
        
    https://hub.docker.com/_/mongo
    
For install + run MongoDB as an image
 

# Build

Open the project on your favorite IDE.

Run gradle build for building the project:

    gradle build


# How to run it?

Run the main function in src -> main -> BlogMicroserviceApplication
    
Make sure that the server is on listing to port 8083 on localhost:

    http://localhost:8083
    
# How to use it? 

After the server is running, you can use Swagger to send request to the service:

```
http://localhost:8083/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/
```
