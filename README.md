# quarkus-crud-reactive-mongodb project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

## Run mongodb in Docker container

    docker run -ti --rm -p 27017:27017 mongo:4.0

## Run quarkus in Docker container
    
    mvn package
    docker build -f src/main/docker/Dockerfile.jvm -t quarkus-crud-reactive-mongodb-jvm .

## Running app with docker-compose
    
    docker-compose down
    docker-compose up --build

## Running the application in dev mode  

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

#
