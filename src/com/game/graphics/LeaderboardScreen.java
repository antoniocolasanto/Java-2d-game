package com.game.graphics;

import com.game.db.LeaderboardDAO.PlayerRecord;
import com.game.utils.Constants;
import java.awt.*;
import java.util.List;

/**
 * GESTIONE DELLA SCHERMATA CLASSIFICA (LEADERBOARD)
 * - Mostra i dati dei migliori giocatori recuperati dal database.
 * - Genera un'interfaccia a tabella con allineamenti specifici.
 * - Gestisce dinamicamente i colori per evidenziare il podio (Oro, Argento, Bronzo).
 * - Gestisce il caso in cui il database sia vuoto.
 */
public class LeaderboardScreen {

    /**
     * Disegna la schermata della classifica a schermo.
     * * @param g Il pennello Graphics utilizzato per disegnare
     * @param topPlayers Lista degli oggetti top 10 PlayerRecord estratti dal Database
     */
    public void draw(Graphics g, List<PlayerRecord> topPlayers) {
        //Facciamo casting. Graphics2D è una classe che estende (cioè eredita da) Graphics. Serve per manngiori funzionalità di didesgno.
        Graphics2D g2 = (Graphics2D) g;

        // Sfondo semitrasparente blu molto scuro per far risaltare il testo
        g2.setColor(new Color(20, 20, 50, 230));
        g2.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

        // --- TITOLO ---
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.setColor(Color.ORANGE);
        String title = "TOP 10 GIOCATORI";
        int titleX = (Constants.LARGHEZZA_FINESTRA - g2.getFontMetrics().stringWidth(title)) / 2;
        g2.drawString(title, titleX, 80);

        // --- INTESTAZIONI DELLA TABELLA ---
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.CYAN);
        int startY = 150;
        
        g2.drawString("POS", 150, startY);
        g2.drawString("NICKNAME", 250, startY);
        g2.drawString("RECORD MONETE", 450, startY);
        g2.drawString("TEMPO MEDIO", 680, startY);
        
        // Linea divisoria orizzontale sotto le intestazioni
        g2.drawLine(100, startY + 10, Constants.LARGHEZZA_FINESTRA - 100, startY + 10);

        // --- STAMPA DEI RECORD ---
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        
        int y = startY + 40;
        int rank = 1;

        if (topPlayers == null || topPlayers.isEmpty()) {
            g2.setColor(Color.WHITE);
            g2.drawString("Nessuna partita registrata nel Database.", 300, y + 40);
        } else {
            for (PlayerRecord record : topPlayers) {
                
                // Assegna colori speciali per il podio (primi 3 classificati)
                if (rank == 1) g2.setColor(new Color(255, 215, 0));       // Oro
                else if (rank == 2) g2.setColor(new Color(192, 192, 192)); // Argento
                else if (rank == 3) g2.setColor(new Color(205, 127, 50));  // Bronzo
                else g2.setColor(Color.WHITE);                             // Resto della classifica

                // Stampa i valori incolonnati
                g2.drawString("#" + rank, 160, y);
                g2.drawString(record.getNickname().toUpperCase(), 250, y);
                g2.drawString(String.valueOf(record.getMaxCoins()), 500, y);
                
                // Formatta il tempo medio a 2 cifre decimali
                g2.drawString(String.format("%.2f sec", record.getAvarage()), 700, y);
                
                // Incrementa le variabili per la riga successiva
                y += 35;
                rank++;
            }
        }

        // --- ISTRUZIONE D'USCITA ---
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(Color.LIGHT_GRAY);
        String escText = "Premi [ESC] per tornare al Menu";
        int escX = (Constants.LARGHEZZA_FINESTRA - g2.getFontMetrics().stringWidth(escText)) / 2;
        g2.drawString(escText, escX, Constants.ALTEZZA_FINESTRA - 40);
    }
}