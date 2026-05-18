package com.game.graphics;

import com.game.db.LeaderboardDAO.PlayerRecord;
import com.game.utils.Constants;
import java.awt.*;
import java.util.List;

public class LeaderboardScreen {

    public void draw(Graphics g, List<PlayerRecord> topPlayers) {
        Graphics2D g2 = (Graphics2D) g;

        // Sfondo semitrasparente scuro per far risaltare il testo
        g2.setColor(new Color(20, 20, 50, 230));
        g2.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

        // Titolo
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.setColor(Color.ORANGE);
        String title = "TOP 10 GIOCATORI";
        int titleX = (Constants.LARGHEZZA_FINESTRA - g2.getFontMetrics().stringWidth(title)) / 2;
        g2.drawString(title, titleX, 80);

        // Intestazioni della tabella
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.CYAN);
        int startY = 150;
        g2.drawString("POS", 150, startY);
        g2.drawString("NICKNAME", 250, startY);
        g2.drawString("RECORD MONETE", 450, startY);
        g2.drawString("TEMPO MEDIO", 680, startY);
        
        // Linea divisoria
        g2.drawLine(100, startY + 10, Constants.LARGHEZZA_FINESTRA - 100, startY + 10);

        // Stampa i record
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        
        int y = startY + 40;
        int rank = 1;

        if (topPlayers == null || topPlayers.isEmpty()) {
            g2.setColor(Color.WHITE);
            g2.drawString("Nessuna partita registrata nel Database.", 300, y + 40);
        } else {
            for (PlayerRecord record : topPlayers) {
                // Colori speciali per il podio
                if (rank == 1) g2.setColor(new Color(255, 215, 0)); // Oro
                else if (rank == 2) g2.setColor(new Color(192, 192, 192)); // Argento
                else if (rank == 3) g2.setColor(new Color(205, 127, 50)); // Bronzo
                else g2.setColor(Color.WHITE);

                g2.drawString("#" + rank, 160, y);
                g2.drawString(record.getNickname().toUpperCase(), 250, y);
                g2.drawString(String.valueOf(record.getMaxCoins()), 500, y);
                // Utilizza il metodo standardizzato per la media e formatta a 2 decimali
                g2.drawString(String.format("%.2f sec", record.getAvarage()), 700, y);
                
                y += 35;
                rank++;
            }
        }

        // Istruzione per tornare al menu
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(Color.LIGHT_GRAY);
        String escText = "Premi [ESC] per tornare al Menu";
        int escX = (Constants.LARGHEZZA_FINESTRA - g2.getFontMetrics().stringWidth(escText)) / 2;
        g2.drawString(escText, escX, Constants.ALTEZZA_FINESTRA - 40);
    }
}