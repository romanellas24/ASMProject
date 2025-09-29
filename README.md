# ACMEat / ASMProject

**ACMEat** è un progetto universitario sviluppato per il corso di *Architetture dei Sistemi a Microservizi (ASM)*.  
Il sistema implementa un’architettura a microservizi in cui diversi servizi indipendenti collaborano per simulare un ecosistema reale di gestione ordini, pagamenti, logistica e geolocalizzazione.

---

## 📑 Indice

1. [Introduzione](#-introduzione)  
2. [Obiettivi](#-obiettivi)  
3. [Architettura](#-architettura)  
4. [Componenti](#-componenti)  
5. [Tecnologie](#-tecnologie)  
6. [Requisiti](#-requisiti)  
7. [Installazione & Avvio](#-installazione--avvio)  
8. [Configurazione](#-configurazione)  
9. [API](#-api)  
10. [Flussi di Esecuzione](#-flussi-di-esecuzione)  
11. [Testing](#-testing)  
12. [Deployment](#-deployment)  
13. [Roadmap](#-roadmap)  
14. [Contribuire](#-contribuire)  
15. [Licenza](#-licenza)  
16. [Contatti](#-contatti)  

---

## 📌 Introduzione

Il progetto **ACMEat** è un caso di studio per sperimentare architetture a microservizi.  
L’obiettivo è integrare più servizi — *banca, ristoranti, corrieri, GIS* — che collaborano tra loro per gestire l’intero ciclo di vita di un ordine di cibo, dal pagamento alla consegna.

---

## 🎯 Obiettivi

- Scomposizione del sistema in microservizi indipendenti.  
- Comunicazione tra servizi tramite API e protocolli standard.  
- Scalabilità, resilienza ed estensibilità dell’architettura.  
- Gestione di casi reali: pagamento, logistica, ristorazione, mappe.  
- Fornire un progetto didattico facilmente estendibile.  

---

## 🏗 Architettura

Il sistema è composto da diversi microservizi principali:

- **ACMEat (Core)** → Orchestratore centrale che gestisce l’intero flusso.  
- **Bank (JolieBank)** → Gestisce i pagamenti e le transazioni.  
- **Couriers** → Gestisce i corrieri e le consegne.  
- **GIS** → Fornisce servizi di geolocalizzazione, mappe e calcolo distanze.  
- **Restaurants** → Gestisce i ristoranti, i menu e la disponibilità.  

```mermaid
flowchart LR
    Client((Cliente)) --> ACMEat
    ACMEat --> Bank
    ACMEat --> Restaurants
    ACMEat --> GIS
    ACMEat --> Couriers
    Couriers --> Client
