# Progetto HOTELIER

## Introduzione
Questo progetto è un sistema di gestione per hotel che include funzionalità di registrazione, login, ricerca hotel, inserimento recensioni e visualizzazione badge. L'obiettivo principale è fornire una piattaforma dove gli utenti possono cercare hotel, recensirli e visualizzare i propri badge in base alle recensioni inserite.

## Struttura del Progetto
Il progetto è organizzato nelle seguenti directory e file principali:

```plaintext
.vscode/
    settings.json
src/
    Client/
        HOTELIERClientMain.java
        InputCheck.java
    Data/
        Database.java
        Hotels.json
        Users.json
    H_U_R/
        Hotel.java
        Review.java
        User.java
    libs/
        gson-2.11.0.jar
    Server/
        DataHandlerHotels.java
        DataHandlerUsers.java
        HOTELIERServerMain.java
        LeggiHotelsFile.java
        Ranking.java
        Ratings.java
        Share.java
        ThreadWorker.java
```
# Componenti Principali
## Client
HOTELIERClientMain.java: Classe principale del client che gestisce l'interfaccia utente e le richieste al server.
InputCheck.java: Classe per la validazione degli input dell'utente.
## Data
Database.java: Classe per la gestione del database degli utenti.
Hotels.json: File JSON contenente i dati degli hotel.
Users.json: File JSON contenente i dati degli utenti.
## H_U_R (Hotel, User, Review)
Hotel.java: Classe per la gestione degli hotel.
Review.java: Classe per la gestione delle recensioni.
User.java: Classe per la gestione degli utenti.
## Server
HOTELIERServerMain.java: Classe principale del server.
ThreadWorker.java: Classe per la gestione dei thread del server.
LeggiHotelsFile.java: Classe per la lettura e scrittura dei dati degli hotel.
Ranking.java: Classe per il calcolo del ranking degli hotel.
Ratings.java: Classe per la gestione dei punteggi degli hotel.
Share.java: Classe per la gestione della condivisione dei dati tra i client.
DataHandlerHotels.java: Classe per la gestione dei dati degli hotel.
DataHandlerUsers.java: Classe per la gestione dei dati degli utenti.
# Funzionalità Principali
## Registrazione
Gli utenti possono registrarsi fornendo un username e una password. I dati vengono salvati nel file Users.json.

## Login
Gli utenti possono effettuare il login fornendo le proprie credenziali. Se le credenziali sono corrette, l'utente viene autenticato e può accedere alle funzionalità del sistema.

## Ricerca Hotel
Gli utenti possono cercare hotel per nome e città. I dati degli hotel vengono letti dal file Hotels.json.

## Inserimento Recensioni
Gli utenti possono inserire recensioni per gli hotel. Ogni recensione include punteggi per pulizia, posizione, servizi e prezzo. Le recensioni vengono salvate nel file Hotels.json.

## Visualizzazione Badge
Gli utenti possono visualizzare i propri badge in base al numero di recensioni inserite. I badge vengono aggiornati automaticamente in base alle recensioni.

# Guida all'Uso
## Compilazione ed Esecuzione
Per compilare ed eseguire il progetto, segui questi passi:

Apri un terminale e naviga nella cartella del progetto.
Compila il progetto con il comando:
