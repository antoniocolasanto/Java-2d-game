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
import com.game.graphics.MainMenu;
import com.game.graphics.PauseMenu;
import com.game.inputs.KeyInput;
import com.game.levels.LevelManager;
import com.game.utils.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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

    private Player player;
    private Bee bee;
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
        
        // Inizializziamo gli oggetti necessari
        bg = new Background("res/Sprites/Backgrounds/Default/background_clouds.png");
        levelManager = new LevelManager();
        player = new Player(100, 500, 100, 100);
        bee = new Bee(400, 300, 60, 60);
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
                if (bee != null) bee.update();
                break;
            default:
                break;
        }
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
        if (bee != null) bee.draw(g);
    }
}