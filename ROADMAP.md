# TODOS

## CODE:

* BANK:

* ACMEAT:

* COURIER:
    * [ ] (Test per la documentazione automatica)

* LOCAL:
    2 microservizi:

    * [x] FE del locale con cui il ristoratore:
        * [x] Cambiamento menu'
        * [x] Indisponibilita'
        * [x] Accettare ordini (Puó esere fatto anche in modo automatico. Ad esempio non posso prendere piú di 3 ordini alla stessa ora).
    * [x] REST API:
        * [x] Cambiare menu'
        * [x] Accettare ordine/rifiutare
    * [x] DATABASE
    * [x] Rabbit message broker 

## SYSTEM/DEPLOYMENT
* [ ] Deployment finale NON locale:
    * [ ] deployare n corrieri
    * [ ] deployare m locali

* BANK:
    * [ ] Aggiunta di una tabella per vedere i C/C - Con i saldi
    * [ ] Aggiunta di un form per creare le carte di credito
    * [ ] Aggiungere una chiamata per fare in modo che i token sia resi non rimborsabili
    * [ ] Aggiungere una tabella per vedere le transazioni
* ACMEAT:


* COURIER:
    * [ ] Aggiunta Prometheus per monitoraggio 
    * [ ] (Load balancing?)
    * [ ] Aggiungere AutoScaling orizzontale automatico

    `https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough/`

* LOCAL:
    * [x] Deployment in locale:
        * [x] Immagine docker
        * [x] Deployements/Services
        * [x] Gateway
    * [ ] Aggiunta Prometheus per monitoraggio 
    * [ ] (Load balancing?)
    * [ ] Aggiungere AutoScaling orizzontale automatico

## DOCS
* [ ] Relazione (varie fasi della modellazione e dello sviluppo).
* [ ] Coreografia (penso si intenda il BPMN totale tra le diverse aziende)
* [ ] BPMS

* BANK:
    * [ ] UML
    * [ ] BPMN documentativo
    * [ ] documentazione OpenAPI automatica

* ACMEAT:
    * [ ] UML
    * [ ] BPMN documentativo
    * [ ] documentazione OpenAPI automatica

* COURIER:
    * [ ] UML
    * [ ] BPMN documentativo
    * [ ] documentazione OpenAPI automatica

* LOCAL:
    * [ ] UML
    * [ ] BPMN documentativo
    * [x] documentazione OpenAPI automatica
    
