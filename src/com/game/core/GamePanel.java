package com.game.core;

import com.game.db.LeaderboardDAO; // Import per la classifica
import com.game.db.MongoDBManager;
import com.game.db.PlayerDAO;
import com.game.entities.*;
import com.game.graphics.IdentificationScreen;
import com.game.graphics.LayeredBackground;
import com.game.graphics.LeaderboardScreen; // Import per la schermata classifica
import com.game.graphics.MainMenu;
import com.game.graphics.PauseMenu;
import com.game.inputs.KeyInput;
import com.game.levels.LevelManager;
import com.game.utils.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List; // Import per gestire la lista dei record
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    private Thread gameThread;
    private boolean running = false;
    private ArrayList<Entity> nemici;
    private int TimeSeconds = 0; // Il tempo in secondi
    private int TimeTicks = 0;   // Conta i frame
    private BufferedImage heartImage;
    
    // Dichiariamo l'oggetto CollisionChecker
    private CollisionChecker collisionChecker;

    private Player player;
    private MainMenu mainMenu;
    private PauseMenu pauseMenu;
    private LevelManager levelManager;
    private LayeredBackground layeredBg;

    // --- VARIABILI PER L'IDENTIFICAZIONE E CLASSIFICA ---
    private IdentificationScreen idScreen;
    private PlayerDAO playerDAO;
    private String currentNickname = ""; 

    private LeaderboardDAO leaderboardDAO;
    private LeaderboardScreen leaderboardScreen;
    private List<LeaderboardDAO.PlayerRecord> currentTopPlayers;

    // Lo stato iniziale è il MENU
    public static GameState state = GameState.MENU;

    public GamePanel() {
        this.setPreferredSize(new Dimension(Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA));
        this.setBackground(Color.CYAN);
        this.setDoubleBuffered(true); 
        this.setFocusable(true); 
        
        // Inizializzazione componenti
        this.addKeyListener(new KeyInput(this));
        this.setFocusable(true);
        
        // --- CONNESSIONE E INIZIALIZZAZIONE COMPONENTI DATABASE ---
        MongoDBManager.connect(); 
        playerDAO = new PlayerDAO();
        idScreen = new IdentificationScreen();
        
        leaderboardDAO = new LeaderboardDAO();
        leaderboardScreen = new LeaderboardScreen();

        layeredBg = new LayeredBackground();
        layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_solid_cloud.png", 0, 0, true, true);
        layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_solid_cloud.png", 0, 50, true, false);
        layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_color_desert.png", 0, 100, true, false);
        layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_solid_sand.png", 0, 450, true, false);
        
        levelManager = new LevelManager();
        collisionChecker = new CollisionChecker(levelManager);
        player = new Player(50, 480, 100, 100, this);
        nemici = levelManager.getListaNemici();
        mainMenu = new MainMenu();
        pauseMenu = new PauseMenu();
        
        try {
            heartImage = ImageIO.read(new File("res/Sprites/Tiles/Double/heart.png")); 
        } catch (IOException e) {
            System.err.println("Errore: Immagine del cuore non trovata!");
        }
    }

    public String getCurrentNickname() { return currentNickname; }
    public void setCurrentNickname(String nickname) { this.currentNickname = nickname; }
    public PlayerDAO getPlayerDAO() { return playerDAO; }

    // --- NUOVO METODO: RECUPERA LA CLASSIFICA AGGIORNATA ---
    public void fetchLeaderboard() {
        currentTopPlayers = leaderboardDAO.getTop10Players();
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
        final long TARGET_NS = 1_000_000_000L / 60;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long elapsed = now - lastTime;

            if (elapsed >= TARGET_NS) {
                lastTime = now;
                update(); 
                repaint(); 
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public void update() {
        switch (state) {
            case PLAYING:
                if (player != null) player.update();

                for (Entity nemicoCorrente : nemici) {
                    nemicoCorrente.update();
                }
                
                TimeTicks++; 
                if (TimeTicks >= 60) {
                    TimeSeconds++;
                    TimeTicks = 0;
                }
                break;
            default:
                break;
        }
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (layeredBg != null) {
            layeredBg.draw(g, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);
        }
        
        switch (state) {
            case MENU:
                mainMenu.draw(g); 
                break;

            case IDENTIFICATION:
                idScreen.draw(g, currentNickname, false); 
                break;

            case CONFIRM_PLAYER:
                idScreen.draw(g, currentNickname, true); 
                break;

            case PLAYING:
                renderGameWorld(g);
                drawUI(g);
                break;

            case PAUSE:
                renderGameWorld(g); 
                pauseMenu.draw(g); 
                break;

            case LEADERBOARD:
                // Ora usiamo la schermata vera passando i dati aggregati recuperati da MongoDB
                leaderboardScreen.draw(g, currentTopPlayers);
                break;
        }
    }

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
    }

    private void drawUI(Graphics g) {
        if (player == null) return;

        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));

        g.drawString("Monete " + player.getMonetePrese(), 20, 50);
        
        g.setColor(Color.RED); 
        int vite = player.getVite();

        disegnaVite(g, vite);
    }

    public void disegnaVite(Graphics g, int vite){
        for (int i = 0; i < vite; i++) {
            if (heartImage != null) {
                g.drawImage(heartImage, 10 + (i * 40), 55, 60, 60, null); 
            }
        }
    }
}