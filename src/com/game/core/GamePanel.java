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
import com.game.graphics.LayeredBackground;
import com.game.graphics.MainMenu;
import com.game.graphics.PauseMenu;
import com.game.inputs.KeyInput;
import com.game.levels.LevelManager;
import com.game.utils.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

//* // -Per disegnare qualcosa a schermo si utilizza la classe
// astratta JPANEL
// -Per consentire il movimento dei frame in continuazione
// abbiamo bisogno di un THREAD in background che continua 
// a girare e per questo implementiamo l'interfaccia RUNNABLE 
// (il motore del gioco)
//*
public class GamePanel extends JPanel implements Runnable {

    private Thread gameThread;
    private boolean running = false;
    private ArrayList<Entity> nemici;
    private int TimeSeconds = 0; // Il tempo in secondi
    private int TimeTicks = 0;   // Conta i frame
    
    //Dichiariamo l'oggetto CollisionChecker
    private CollisionChecker collisionChecker;

    private Player player;
    private Background bg;
    private MainMenu mainMenu;
    private PauseMenu pauseMenu;
    private LevelManager levelManager;

    // Lo stato iniziale è il MENU
    public static GameState state = GameState.MENU;

    public GamePanel() {
        // Impostiamo le dimensioni della finestra prendendole dalla classe Constants
        this.setPreferredSize(new Dimension(Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA));
        this.setBackground(Color.CYAN);
        this.setDoubleBuffered(true); // Serve per rendere il disegno più fluido
        this.setFocusable(true); // Permette al pannello di "ascoltare" i tasti
        
        // Inizializzazione componenti
        this.addKeyListener(new KeyInput(this));
        // Permette a questo pannello di interagire con la tastiera
        // dice al computer che questo pannello è pronto a ricevere 
        // input da tastiera
        this.setFocusable(true);
        // Creiamo lo sfondo collegandolo passandogli
        // il percorso dell'immagine che vogliamo
        bg = new Background("res/Sprites/Backgrounds/Double/background_color_desert.png");
        //(PER ORA INUTILE QUESTO WAGLIU)
        LayeredBackground layeredBg = new LayeredBackground(new String[]{
             "res/Sprites/Backgrounds/Double/background_solid_sky.png",
             "res/Sprites/Backgrounds/Double/background_solid_cloud.png",
             "res/Sprites/Backgrounds/Double/background_color_hills.png"
        }, 2, 2);

        levelManager = new LevelManager();
        // Creiamo il controllore delle collisioni
        collisionChecker = new CollisionChecker(levelManager);
        // Creiamo il giocatore alle coordinate iniziali
        player = new Player(30, 500, 100, 100, this);
        // Creiamo i nrmici
        nemici=levelManager.getListaNemici();
        mainMenu = new MainMenu();
        pauseMenu = new PauseMenu();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        running = true;
        gameThread.start();
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void run() {
        // Calcolo per mantenere i 60 FPS (Frames Per Second)
        final long TARGET_NS = 1_000_000_000L / 60;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long elapsed = now - lastTime;

            if (elapsed >= TARGET_NS) {
                lastTime = now;
                update(); // 1. Aggiorna dati
                repaint(); // 2. Disegna (chiama paintComponent)
            } else {
                Thread.yield();
            }
        }
    }

    public void update() {
        // Aggiorna la logica solo se stiamo giocando
        switch (state) {
   case PLAYING:
                if (player != null) player.update();
                
                TimeTicks++; 
                if (TimeTicks >= 60) {
                    TimeSeconds++;
                    TimeTicks = 0;
                }

                for (Entity nemicoCorrente : nemici) {
                    nemicoCorrente.update();
                }
        }
    }

    // Serve al Player per prendere i controlli fisici
    public CollisionChecker getCollisionChecker() {
    return collisionChecker;
}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Lo sfondo (sempre presente)
        if (bg != null) bg.draw(g, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

        // 2. Rendering basato sullo stato
        switch (state) {
            case MENU:
                mainMenu.draw(g); 
                break;

            case PLAYING:
                renderGameWorld(g);
                g.drawString("Tempo: " + TimeSeconds + "s", 20, 30);
                g.drawString("Monete: " + player.getMonetePrese(), 20, 50);
                g.drawString("Vite: " + player.getVite(), 20, 70);

                break;

            case PAUSE:
                renderGameWorld(g); 
                pauseMenu.draw(g); // Sovrappone il menu di pausa
                break;

            case LEADERBOARD:
                g.setColor(Color.YELLOW);
                g.drawString("CLASSIFICA - In attesa di MongoDB...", 500, 300);
                break;
        }
    }

    // Metodo di supporto per disegnare il mondo di gioco
    private void renderGameWorld(Graphics g) {
        if (levelManager != null) levelManager.draw(g);
        if (player != null) player.draw(g);

        for (Entity nemicoCorrente : nemici) {
            nemicoCorrente.draw(g);
        }
    }


    public void resetPartita() {
        TimeSeconds = 0;
        TimeTicks = 0;
        
        // Qui in futuro aggiungerai anche:
        // player.resetPosizione();
        // ricaricaNemici();
    }
    
}