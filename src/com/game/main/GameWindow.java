/**
 * GESTORE DELLA FINESTRA (JFrame)
 * - Crea la finestra di Windows/Mac (titolo, dimensioni, pulsante di chiusura X).
 * - Prende il GamePanel (che contiene il gioco vero e proprio) e lo "incolla" dentro la finestra.
 * - Assicura che la finestra non sia ridimensionabile se non volete gestire il resize dinamico.
 * 
 * Questa classe crea solo la cornice (finestra) visiva del gioco.
 * Non sa assolutamente nulla di regole fisiche, nemici, gravità o database. 
 * Funziona esclusivamente come "contenitore" per il nostro motore grafico
 */

package com.game.main;

import com.game.core.GamePanel; // Importa la classe GamePanel dal nostro pacchetto core, necessaria per collegare il motore del gioco alla finestra
import javax.swing.JFrame; // Importa la classe JFrame dalla libreria standard Java Swing, che fornisce gli strumenti per creare finestre sul sistema operativo


/**
 * COSTRUTTORE E DEPENDENCY INJECTION
 * Invece di costringere la finestra a fare una "new GamePanel()" al suo interno, 
 * glielo passiamo dall'esterno tramite i parametri. Così facendo la GameWindow è più flessibile e riutilizzabile,
 * permette di testare la finestra con GamePanel diversi se necessario.
 */
public class GameWindow {

    private JFrame jframe;
    private GamePanel gamePanel;

    public GameWindow(GamePanel gamePanel) {
        this.gamePanel = gamePanel; // Salva il pannello passato come parametro nella variabile interna della classe
        createWindow(); // Chiama il metodo dedicato per configurare e mostrare la finestra
    }
    
    // CONFIGURAZIONE VISIVA DELLA FINESTRA
    // Metodo privato affinché non possa essere chiamato per sbaglio dall'esterno (evitando di generare finestre doppie)
    private void createWindow() {

        // 1. Inizializza l'oggetto finestra e assegna il titolo che apparirà nella barra superiore
        jframe = new JFrame("Super Nicolo");

        // 2. Uccide forzatamente il processo Java associato al gioco quando l'utente clicca la 'X' rossa. 
        // Senza questo, la finestra sparirebbe ma il gioco continuerebbe a consumare CPU/RAM in background.
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 3. Aggiunta del contenuto visivo
        jframe.add(this.gamePanel);

        // 4. Disabilitando il resize, impediamo all'utente di trascinare i bordi e sfasare la grafica.
        jframe.setResizable(false);

        // 5.Invece di settare le dimensioni manualmente, pack() ordina alla finestra di "avvolgersi" 
        // attorno alle dimensioni richieste dal GamePanel interno.
        jframe.pack(); 

        // 6. Passando 'null', diciamo al sistema operativo di centrare la finestra esattamente in mezzo allo schermo.
        // Va messo sempre DOPO pack(), altrimenti verrebbe centrata una finestra senza dimensioni.
        jframe.setLocationRelativeTo(null);

        // 7. L'operazione finale che accende i pixel sullo schermo e rende tutto visibile al giocatore.
        jframe.setVisible(true);

    }
}