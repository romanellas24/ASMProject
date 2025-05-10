# TODOS

## CODE:

* BANK:

* ACMEAT:

* COURIER:
    * [ ] (Test per la documentazione automatica)

* LOCAL:
    2 microservizi:

    * [ ] FE del locale con cui il ristoratore:
        * [ ] Cambiamento menu'
        * [ ] Indisponibilita'
        * [ ] Accettare ordini (Puó esere fatto anche in modo automatico. Ad esempio non posso prendere piú di 3 ordini alla stessa ora).
    * [ ] REST API:
        * [ ] Cambiare menu'
        * [ ] Accettare ordine/rifiutare
    * [ ] DATABASE

    Idea implementazione:
    * [ ] Per il menu' si possono salvare, per ogni locale, n piatti totali. Il ristoratore, nel FE, puo' selezione i piatti tra gli n totali e quelli diventeranno il menu' del giorno. In questo modo si puo' popolare il db direttamente.
    * [ ] Autenticazione molto basic per fare entrare il ristoratore e non chiunque (Controllo nel gateway in modo da bloccare )

## SYSTEM/DEPLOYMENT
* [ ] Deployment finale NON locale:
    * [ ] deployare n corrieri
    * [ ] deployare m locali

* BANK:

* ACMEAT:


* COURIER:
    * [ ] Aggiunta Prometheus per monitoraggio 
    * [ ] (Load balancing?)
    * [ ] Aggiungere AutoScaling orizzontale automatico

    `https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough/`

* LOCAL:
    * [ ] Deployment in locale:
        * [ ] Immagine docker
        * [ ] Deployements/Services
        * [ ] Gateway
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
    * [ ] documentazione OpenAPI automatica
    