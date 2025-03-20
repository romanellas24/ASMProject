## API Gateway
* GET ../availability: given restaurant's address, client's address and expected delivery time, the system checks for free vehicle in time requested and returns the price for delivery.

* PUT ../availability: given restaurant's address, client's address and time expected for delivery, the system allocates a vehicle.

* PUT ../delivery/{vehicleId}: used by couriers to change the state, i.e. start using vehicle or vehicle returned.

## At least 2 services

### Allocation
Given time start and time end of desidered allocation, the system allocate a vehicle if available.

### Tracking

This service responds to following requests made by couriers:
* start using vehicle.
* vehicle returned.

This is the part of Commands in CQRS pattern.

## Assumptions
Every courier has n vehicle.
The choice of allocation of vehicles is determined by the system.

## Problems:
Imagine that a courier has only one vehicle,
imagine that there is another company called "pippoeat", and the time of waiting response is only 10 seconds (acmeat 15).
How to handle that problem?
1. client A from ACMEat checks for availability (checks vehicles)
2. client B from pippoeat checks for availability (checks vehicles) 
3. client B (pippoeat) allocates the only vehicle available
4. now the vehicle isn't available for client A but ACMEat thinks the opposite, so ACMEat can't allocate a vehicle

Is this handled by ACMEat? 
Otherwise, any courier can set a timeout of unavailability of vehicle when the GET ../availabily is requested.


---

## COMANDI

### PROGETTO

Buildare il progetto
(usare o il build o il comando mvn package)

### DOCKER

Creare immagine docker:

    docker build -t NOME_APP:VERSIONE_APP /Path/to/Dockerfile

esempio (daremo sempre questo nome):

    docker build -t courier_app_asm:0.1 .

Per vedere le immagini docker locali, usare:

    docker images

Per runnare immagine e pubblicare una porta (accedere localhost):

    docker run -p 127.0.0.1:80:CONTAINER_PORT ID_IMAGE

    docker run -p 127.0.0.1:80:8080 e97eda8a1e34

Per pushare l'immagine, bisogna effettuare prima il tag poi il push

    docker tag ID_IMAGE tiesta/NAME

    docker push tiesta/NAME

### MINIKUBE
Per startare minikube, usare:

    minikube start

Per applicare la configurazione (yaml):

    kubectl apply -f app-deployment.yaml

Per vedere i deployment:

    kubectl get deployments

Per vedere i pods: 
    
    kubectl get pods


https://benstitou-anas.medium.com/deploy-java-spring-application-with-mysql-db-on-kubernetes-1e456271c6a1

