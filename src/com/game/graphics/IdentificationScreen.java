package com.game.graphics;

import com.game.utils.Constants;
import java.awt.*;

/**
 * GESTORE DELL'INTERFACCIA DI IDENTIFICAZIONE
 * - Disegna la schermata in cui il giocatore digita il proprio Nickname.
 * - Mostra l'input in tempo reale.
 * - Gestisce visivamente la fase di conferma (es. "Il giocatore esiste già. Sei tu?").
 */
public class IdentificationScreen {

    public void draw(Graphics g, String currentName, boolean askConfirmation) {
        Graphics2D g2 = (Graphics2D) g;

        // Sfondo scuro semitrasparente
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

        g2.setFont(new Font("Arial", Font.BOLD, 40));

        if (!askConfirmation) {
            // FASE 1: INSERIMENTO NOME
            g2.setColor(Color.WHITE);
            drawCenteredText(g2, "INSERISCI IL TUO NICKNAME", 200);

            // Disegna il box di testo
            g2.setColor(new Color(50, 50, 50));
            g2.fillRoundRect(Constants.LARGHEZZA_FINESTRA / 2 - 200, 250, 400, 60, 20, 20);
            g2.setColor(Color.CYAN);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(Constants.LARGHEZZA_FINESTRA / 2 - 200, 250, 400, 60, 20, 20);

            // Disegna il nome digitato
            g2.setColor(Color.YELLOW);
            drawCenteredText(g2, currentName + (System.currentTimeMillis() % 1000 < 500 ? "_" : ""), 290); // Effetto cursore lampeggiante

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.LIGHT_GRAY);
            drawCenteredText(g2, "Premi [INVIO] per confermare", 350);

        } else {
            // FASE 2: CONFERMA IDENTITA'
            g2.setColor(Color.ORANGE);
            drawCenteredText(g2, "BENTORNATO, " + currentName.toUpperCase() + "!", 200);
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 30));
            drawCenteredText(g2, "Questo profilo esiste gia nel Database.", 260);
            drawCenteredText(g2, "Sei tu?", 320);

            g2.setFont(new Font("Arial", Font.BOLD, 35));
            g2.setColor(Color.GREEN);
            g2.drawString("[ S ] SI, gioca", Constants.LARGHEZZA_FINESTRA / 2 - 250, 400);
            
            g2.setColor(Color.RED);
            g2.drawString("[ N ] NO, cambia nome", Constants.LARGHEZZA_FINESTRA / 2 + 10, 400);
        }
    }

    // Metodo helper per centrare il testo orizzontalmente
    private void drawCenteredText(Graphics2D g2, String text, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (Constants.LARGHEZZA_FINESTRA - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }
}