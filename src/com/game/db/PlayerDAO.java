package com.game.db;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.ArrayList;

/**
 * DATA ACCESS OBJECT (DAO) PER IL GIOCATORE
 * - Si occupa di eseguire le operazioni CRUD (Le query) (Create, Read, Update, Delete) sulla collezione "Players".
 * - Isola la logica del database dalla logica del gioco: il GamePanel chiama i metodi di questa 
 * classe senza doversi preoccupare di come MongoDB funziona internamente.
 */
public class PlayerDAO {

    private MongoCollection<Document> collection;

    public PlayerDAO() {
        // Recupera la collezione (tabella) "Players" dal database
        this.collection = MongoDBManager.getDatabase().getCollection("Players");
    }

    /**
     * Controlla se un nickname è già registrato nel database (SELECT/FIND).
     */
    public boolean playerExists(String nickname) {
        Document found = collection.find(new Document("_id", nickname)).first();
        return found != null;
    }

    /**
     * Crea un nuovo documento per un nuovo giocatore con un array di sessioni vuoto (INSERT).
     */
    public void createNewPlayer(String nickname) {
        if (!playerExists(nickname)) {
            Document newPlayer = new Document("_id", nickname)
                                    .append("sessions", new ArrayList<Document>());
            collection.insertOne(newPlayer);
            System.out.println("Nuovo giocatore creato: " + nickname);
        }
    }

    /**
     * Salva i dati di una sessione vinta aggiungendoli all'array "sessions" del giocatore (UPDATE).
     * Utilizza l'operatore $push di MongoDB per inserire l'elemento nell'array.
     */
    public void saveWinningSession(String nickname, int coins, int lives, long timeSec) {
        Document session = new Document("coins", coins)
                              .append("livesRemaining", lives)
                              .append("timeSec", timeSec);
        
        collection.updateOne(
            new Document("_id", nickname),
            new Document("$push", new Document("sessions", session))
        );
        System.out.println("Sessione salvata per il giocatore: " + nickname);
    }
}