package com.game.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * CONNESSIONE AL DATABASE NOSQL (MongoDB)
 * - Inizializza il driver MongoDB e gestisce l'apertura/chiusura sicura della connessione.
 * - Utilizza il pattern Singleton: crea la connessione una sola volta e la riutilizza.
 * - È il ponte tra il gioco locale in Java e il database (in questo caso locale su localhost).
 * 
 * MongoDBManager.java dice di connettersi a SuperNicoloDB 
 * e PlayerDAO.java cerca la collezione Players
 * 
 * Arriva alla schermata di Inserimento Nickname (la classe IdentificationScreen). 
 * Quando inserisci un nome e premi INVIO, il gioco chiamerà il metodo createNewPlayer(nickname) del tuo PlayerDAO.
 */
public class MongoDBManager {
    
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null; // Essendo statiche, queste variabili sono uniche e condivise da tutto il gioco.

    // Stringa di connessione per il database MongoDB in esecuzione sul tuo PC
    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "DBSpaceBugGame";

    /**
     * Metodo per stabilire la connessione a MongoDB. Viene chaiamato la prima volta che si accede al database.
     * Se la connesione è già stata stabilita non fa nulla, evitando di aprire più connessioni (Pattern Singleton).
     */
    public static void connect() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(DB_NAME);
                System.out.println("Connessione a MongoDB LOCALE stabilita con successo!");
            } catch (Exception e) {
                System.err.println("Errore di connessione al Database locale: " + e.getMessage());
            }
        }
    }

    /**
     * Metodo per ottenere l'istanza del database.
     * Se la connessione non è ancora stata stabilita, la crea prima.
     * @return MongoDatabase - l'istanza del database a cui il gioco può accedere per query e aggiornamenti.
     */
    public static MongoDatabase getDatabase() {
        if (database == null) connect();
        return database;
    }

    /**
     * Metodo per chiudere la connessione alla chiusura del gioco.
     */
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Connessione a MongoDB chiusa.");
        }
    }
}


/**--------------------------IMPORTANTE PER CAPIRE PATTERN SINGLETON USATO---------------------------
 * Se non avessi usato questo pattern, 
 * ogni volta che chiami new PlayerDAO() o che salvi un punteggio, 
 * il tuo gioco aprirebbe una nuova connessione di rete verso MongoDB. 
 * Dopo 50 salvataggi avresti 50 "tubi" di connessione aperti in contemporanea, 
 * consumando tutta la RAM e rallentando vistosamente il gioco fino a farlo crashare.
 */