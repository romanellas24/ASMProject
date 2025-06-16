# STEPS
In questi comandi utilizzeró `kind` ma credo sia uguale con `minikube`.

1. Creare un cluster kind:
`kind create cluster --config kind_local_conf/cluster-config.yaml`
Il cluster andrà ad occupare le porte 8080 e 8443 dell'host in modo da poter comunicare direttamente usando `localhost:8080`, senza passare per il port forwarding.

2. Installare nginx fabric gateway (NGIX x gateway api):
`helm install ngf oci://ghcr.io/nginx/charts/nginx-gateway-fabric --create-namespace -n nginx-gateway --set service.create=false`

3. Deployare su kubernetes il servizio per il gateway 
`kubectl apply -f kind_local_conf/nodeport-config.yaml`

4. Deployare tutti i servizi utilizzando:
`sh deploy.sh`

In questo modo potrai accedere ai servizi del corriere usando http://localhost:8080.

## TODO:

* Documentazione automatica sfruttando la kubernetes api(?)
* Capire come cambia il deploy per effettivamente mettere in produzione sul server:
    * mettere HTTPS
* Creare n corrieri. Da vedere come fare insieme. 

(Idea: creare n cluster quanti sono i container e usare caddy (oppure un ulteriore gateway) davanti a tutto che smista. Esempio:
    * http://HOST/courier1/... viene smistato al primo cluster
    * http://HOST/courieri/... viene smistato all'i-esimo cluster)





