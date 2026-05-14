package com.game.core;

/**
 * MOTORE DEL GIOCO E GAMELOOP (JPanel + Runnable)
 * - È la classe su cui viene disegnato tutto e il motore che aggiorna i dati
 * - Contiene il GameLoop (un thread che gira a 60 FPS costanti)
 * - Ad ogni frame chiama due metodi fondamentali:
 * 1. update(): aggiorna le posizioni di Player, Nemici e Mappa.
 * 2. repaint(): pulisce lo schermo e ridisegna tutto nelle nuove posizioni.
 * - Gestisce le liste dinamiche (es. ArrayList<Entity> per i nemici).
 */
import com.game.entities.*;
import com.game.graphics.Background;
import com.game.inputs.KeyInput;
import com.game.levels.LevelManager;
import com.game.utils.Constants;
import java.awt.Color; //importa la classe Constants per accedere alle costanti di gioco
import java.awt.Dimension; //importa la classe Constants per accedere alle costanti di gioco
import java.awt.Graphics; //importa la classe Constants per accedere alle costanti di gioco
import javax.swing.JPanel;
//* 
// -Per diseganre qualcosa a schermo si utilizza la classe
// astratta JPANEL
// -Per consentire il movimento dei frame in continuazione
// abbiamo bisogno di un THREAD in background che continua 
// a girare e per questo implementiamo l'interfaccia RUNNABLE 
// (il motore del gioco)
//*
public class GamePanel extends JPanel implements Runnable {
    
    // Variabili che utilizzeremo nel metodo
    // startGameThread() per far girare il gioco
    // con RUNNABLE 
    private Thread gameThread;
    private boolean running = false;
    
    //Dichiariamo l'oggetto CollisionChecker
    private CollisionChecker collisionChecker;

    //Dichiariamo l'oggetto Player
    private Player player;
    // dichiariamo l oggetto enemy(nemico)
    private Enemy enemy;
    private Background bg;

    //Dichiariamo l'oggetto Livello
    private LevelManager levelManager;

    //Costruttore del GamePanel
    public GamePanel(){

        // Imposta la grandezza in base alle costanti
        // contenute in Constants nella cartella utils
        this.setPreferredSize(new Dimension(Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA));
        // Colore di sfondo di default 
        this.setBackground(Color.CYAN);
        // Evita che la grafica "sfarfalli" (flickering)
        // Permette al computer di caricare prima i frame
        // successivi, nascondendoli, per poi mostrarli 
        // questo grazie a true
        this.setDoubleBuffered(true);
        // Collega l'ascoltatore della tastiera al pannello
        this.addKeyListener(new KeyInput(this));
        // Permette a questo pannello di interagire con la tastiera
        // dice al computer che questo pannello è pronto a ricevere 
        // input da tastiera
        this.setFocusable(true);
        // Creiamo lo sfondo collegandolo passandogli
        // il percorso dell'immagine che vogliamo
        bg = new Background("res/Sprites/Backgrounds/Default/background_clouds.png");
        // Creiamo il livello (che carica la mappa e i blocchi)
        levelManager = new LevelManager();
        // Creiamo il controllore delle collisioni
        collisionChecker = new CollisionChecker(levelManager);

        // Creiamo il giocatore alle coordinate iniziali
        player = new Player(30, 500, 100, 100, this);
        //creiamo il primo nemico
        enemy = new Enemy(400, 300, 60, 60);
    }

    public void startGameThread() {
        // Crea un nuovo thread che esegue il metodo
        // run() inseriamon this per specificare 
        // questa classe
        gameThread = new Thread(this); 
        gameThread.start(); 
        running = true;
    }

    //Getter er il player che ci servirà (es. per la tastiera)
    public Player getPlayer() {
        return player;
    }
    

    @Override
    public void run() {
        // Finchè running è true eseguiamo ininterrottamente
        // 3 metodi
        while(running){
            // Ricalcoliamo le posizioni di player, nemici e mappa
            update();
            // E' un comando di java che pulisce lo schermo e
            // richiama il metodo paintComponent() (internamente)
            // per ridisegnare tutto a schermo
            repaint();
            try {
                // Dice al thread di rallentare (di dormire 16 ms)
                // il tempo è calcolato per andare a circa 60 FPS
                //(perché 1000 millisecondi / 16 ms = 62.5 FPS)
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void update(){
        // Il GamePanel non fa i calcoli, 
        // ordina agli altri di farli!
        
        if (player != null) {
            // La classe Player calcola la propria gravità 
            // e i tasti premuti
            player.update(); 
            enemy.update();
        } 
    }

    // Serve al Player per prendere i controlli fisici
    public CollisionChecker getCollisionChecker() {
    return collisionChecker;
}

    @Override
    public void paintComponent(Graphics g){
        // Si richiama il metodo nella super classe (JPanel) 
        // per assicurarsi che tutto venga disegnato correttamente
        // Prende solo i numeri già calcolati dall'update e usa 
        // Graphics g per stampare a schermo un immagine
        super.paintComponent(g);

        // Lo sfondo usa la sua funzione draw per apparire
        if (bg != null) {
            bg.draw(g, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);
        }

        // Disegna il livello (Layer intermedio)
        if (levelManager != null) {
        levelManager.draw(g); // <-- Aggiungi questa riga qui
        }

        // Il giocatore usa la sua funzione draw per apparire
        if (player != null) {
            player.draw(g);
        }
        // il nemico appare utilizzando la sua funzione draw
        if (enemy!=null){
            enemy.draw(g);
        }

        // Libera la memoria della grafica occupata in precedenza
        g.dispose();
        
    }
}