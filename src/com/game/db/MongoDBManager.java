package com.game.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * CONNESSIONE AL DATABASE NOSQL (MongoDB)
 * - Inizializza il driver MongoDB e gestisce l'apertura/chiusura sicura della connessione.
 * - Utilizza il pattern Singleton: crea la connessione una sola volta e la riutilizza.
 * - È il ponte tra il gioco locale in Java e il database (in questo caso locale su localhost).
 */
public class MongoDBManager {
    
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    // Stringa di connessione per il database MongoDB in esecuzione sul tuo PC
    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "SuperNicoloDB";

    // Metodo per avviare la connessione
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

    // Metodo per ottenere il database da altre classi (es. dal DAO)
    public static MongoDatabase getDatabase() {
        if (database == null) connect();
        return database;
    }

    // Metodo per chiudere la connessione alla chiusura del gioco
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Connessione a MongoDB chiusa.");
        }
    }
}