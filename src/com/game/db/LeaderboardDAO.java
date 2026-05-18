package com.game.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeaderboardDAO {

    private MongoCollection<Document> collection;

    public LeaderboardDAO() {
        // Recupera la collezione "Players" per eseguire le query
        this.collection = MongoDBManager.getDatabase().getCollection("Players");
    }

    /**
     * Esegue una pipeline di aggregazione per calcolare i migliori giocatori.
     * Criterio: Punteggio massimo di monete in una singola sessione.
     */
    public List<PlayerRecord> getTop10Players() {
        List<PlayerRecord> leaderboard = new ArrayList<>();

        // Definizione della pipeline di aggregazione
        collection.aggregate(Arrays.asList(
            // 1. Spacchetta l'array "sessions". I giocatori senza sessioni vengono ignorati.
            Aggregates.unwind("$sessions"),
            
            // 2. Raggruppa per "_id" (nickname) e calcola i dati aggregati
            Aggregates.group("$_id",
                Accumulators.max("maxCoins", "$sessions.coins"),
                // Calcola la media del tempo. Chiamiamo questo alias "avarage"
                Accumulators.avg("avarage", "$sessions.timeSec") 
            ),
            
            // 3. Ordina in modo decrescente per monete (e in caso di parità, per tempo medio minore)
            Aggregates.sort(Sorts.orderBy(Sorts.descending("maxCoins"), Sorts.ascending("avarage"))),
            
            // 4. Limita ai primi 10 risultati per la GUI
            Aggregates.limit(10)
        )).forEach(doc -> {
            // Mappa ogni documento restituito dal DB nel nostro DTO Java
            String nickname = doc.getString("_id");
            int maxCoins = doc.getInteger("maxCoins", 0);
            
            // Gestione sicura del tipo di dato ritornato da MongoDB per la media
            double avgTime = 0.0;
            if (doc.get("avarage") instanceof Number) {
                avgTime = ((Number) doc.get("avarage")).doubleValue();
            }
            
            leaderboard.add(new PlayerRecord(nickname, maxCoins, avgTime));
        });

        return leaderboard;
    }

    /**
     * DTO (Data Transfer Object) interno per trasportare i dati dalla query alla grafica.
     */
    public static class PlayerRecord {
        private String nickname;
        private int maxCoins;
        private double avarageTime;

        public PlayerRecord(String nickname, int maxCoins, double avarageTime) {
            this.nickname = nickname;
            this.maxCoins = maxCoins;
            this.avarageTime = avarageTime;
        }

        public String getNickname() { return nickname; }
        public int getMaxCoins() { return maxCoins; }
        public double getAvarage() { return avarageTime; } 
    }
}