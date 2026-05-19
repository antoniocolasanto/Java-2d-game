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
import com.game.graphics.LayeredBackground;
import com.game.graphics.MainMenu;
import com.game.graphics.PauseMenu;
import com.game.inputs.KeyInput;
import com.game.levels.LevelManager;
import com.game.utils.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
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
    private BufferedImage heartImage;
    
    //Dichiariamo l'oggetto CollisionChecker
    private CollisionChecker collisionChecker;

    private Player player;
    private MainMenu mainMenu;
    private PauseMenu pauseMenu;
    private LevelManager levelManager;
    private LayeredBackground layeredBg;

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
        
        //Qui istanziamo lo sfondo mappabile come vogliamo
        layeredBg = new LayeredBackground();

        // 1. IL CIELO (Lo mettiamo come primissimo livello, partendo da 0,0. Coprirà tutta la fascia vuota!)
        layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_solid_cloud.png", 0, 0, true, true);

        // 2. LE NUVOLE (Partono da Y=50 e si ripetono SOLO in orizzontale, così non coprono gli alberi sotto)
        layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_solid_cloud.png", 0, 50, true, false);

        // 3. GLI ALBERI (Partono da Y=100 e si ripetono SOLO in orizzontale)
        layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_color_desert.png", 0, 100, true, false);

        // 4. L'ERBA / TERRENO (Partono da Y=450 e si ripetono SOLO in orizzontale)
        layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_solid_sand.png", 0, 450, true, false);
        levelManager = new LevelManager();
        // Creiamo il controllore delle collisioni
        collisionChecker = new CollisionChecker(levelManager);
        // Creiamo il giocatore alle coordinate iniziali
        player = new Player(50, 480, 100, 100, this);
        // Creiamo i nrmici
        nemici=levelManager.getListaNemici();
        mainMenu = new MainMenu();
        pauseMenu = new PauseMenu();
        try {
            // CAMBIA QUESTO PERCORSO CON QUELLO VERO DELLA TUA IMMAGINE!
            heartImage = ImageIO.read(new File("res/Sprites/Tiles/Double/heart.png")); 
        } catch (IOException e) {
            System.err.println("Errore: Immagine del cuore non trovata!");
        }
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
                // Avoid busy-waiting; sleep briefly to reduce CPU usage
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
        // Aggiorna la logica solo se stiamo giocando
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

                for (Entity nemicoCorrente : nemici) {
                    nemicoCorrente.update();
                }
                break;
            default:
                break;
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
        if (layeredBg != null) {
            layeredBg.draw(g, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);
        }
        // 2. Rendering basato sullo stato
        switch (state) {
            case MENU:
                mainMenu.draw(g); 
                break;

            case PLAYING:
                renderGameWorld(g);
                drawUI(g);

                break;

            case PAUSE:
                renderGameWorld(g); 
                pauseMenu.draw(g); // Sovrappone il menu di pausa
                break;

            case LEADERBOARD:
                g.setColor(Color.YELLOW);
                g.drawString("CLASSIFICA - In attesa di MongoDB...", 500, 300);
                break;
               case DEATH:
                // 1. Disegna il mondo di gioco bloccato sullo sfondo
                renderGameWorld(g);

                // Facciamo il cast a Graphics2D per sbloccare effetti grafici avanzati
                Graphics2D g2 = (Graphics2D) g;
                
                // Attiviamo l'antialiasing per rendere i font liscissimi e professionali
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // 2. SFONDO: Un gradiente sfumato semi-trasparente dal nero a un rosso scuro "cupo"
                GradientPaint sfumaturaSfondo = new GradientPaint(
                    0, 0, new Color(0, 0, 0, 220), 
                    0, Constants.ALTEZZA_FINESTRA, new Color(35, 5, 5, 200)
                );
                g2.setPaint(sfumaturaSfondo);
                g2.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

                // 3. TITOLO "GAME OVER" CON EFFETTO OMBRA PROFONDA
                String titolo = "GAME OVER";
                g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 65));
                
                // Calcoliamo la X dinamicamente per centrare la scritta al millimetro su qualsiasi schermo
                int xTitolo = (Constants.LARGHEZZA_FINESTRA - g2.getFontMetrics().stringWidth(titolo)) / 2;
                int yTitolo = 260;

                // Disegniamo prima l'ombra nera (spostata in basso a destra)
                g2.setColor(Color.BLACK);
                g2.drawString(titolo, xTitolo + 4, yTitolo + 4);
                // Disegniamo il testo principale sopra in rosso intenso
                g2.setColor(new Color(220, 20, 20));
                g2.drawString(titolo, xTitolo, yTitolo);

                // 4. ISTRUZIONE INSERITA COME TESTO ELEGANTE (Senza pulsanti di restart)
                String istruzione = "PREMI INVIO PER TORNARE AL MENU";
                g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
                
                // Centriamo dinamicamente anche l'istruzione
                int xIstruzione = (Constants.LARGHEZZA_FINESTRA - g2.getFontMetrics().stringWidth(istruzione)) / 2;
                int yIstruzione = 370;

                // Ombra sottile per la leggibilità
                g2.setColor(Color.BLACK);
                g2.drawString(istruzione, xIstruzione + 2, yIstruzione + 2);
                // Testo bianco brillante pulito
                g2.setColor(Color.WHITE);
                g2.drawString(istruzione, xIstruzione, yIstruzione);
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
        player.resetVite();
        player.ResetMonete();
        player.resetPosition(50, 480);

        levelManager = new LevelManager();

                collisionChecker = new CollisionChecker(levelManager);
        
  
        nemici = levelManager.getListaNemici();
    }
    // Metodo per disegnare l'Interfaccia Utente (UI)
    private void drawUI(Graphics g) {
        // Se il player non esiste ancora, non disegnare nulla per evitare errori
        if (player == null) return;

        // 1. Impostiamo colore bianco e un font bello grande per le scritte
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));

        // Disegniamo il contatore delle monete (X=20, Y=60 per non sovrapporlo al tempo)
        g.drawString("Monete " + player.getMonetePrese(), 20, 50);
        
        g.setColor(Color.RED); // Cambiamo colore in rosso per i cuori
        int vite = player.getVite();

        disegnaVite(g, vite);
        // --- 3. DISEGNAMO IL TIMER CON SFONDO UI ---
        
        // Calcoliamo i minuti e i secondi
        int minuti = TimeSeconds / 60;
        int secondi = TimeSeconds % 60;
        String testoTempo = String.format("Tempo: %02d:%02d", minuti, secondi);
        
        // Calcoliamo la X per centrare tutto
        int xTempo = (Constants.LARGHEZZA_FINESTRA - g.getFontMetrics().stringWidth(testoTempo)) / 2;
        int yTempo = 40; 
        
        // 3a. Disegniamo lo sfondo semi-trasparente (Badge)
        int padding = 12; // Spazio extra ai lati del testo
        int larghezzaSfondo = g.getFontMetrics().stringWidth(testoTempo) + (padding * 2);
        int altezzaSfondo = 30; // Altezza del rettangolo
        
        g.setColor(new Color(0, 0, 0, 150)); // Nero con trasparenza (150 su 255)
        // Disegniamo un rettangolo con gli angoli arrotondati dietro al testo
        g.fillRoundRect(xTempo - padding, yTempo - 22, larghezzaSfondo, altezzaSfondo, 15, 15);
        
        // 3b. Disegniamo il testo bianco sopra al badge
        g.setColor(Color.WHITE);
        g.drawString(testoTempo, xTempo, yTempo);;
    }

    public void disegnaVite(Graphics g, int vite){
    
        // Ciclo magico: disegna un'immagine del cuore per ogni vita che possiedi
        for (int i = 0; i < vite; i++) {
            // Controlliamo che l'immagine sia stata caricata correttamente
            if (heartImage != null) {
                // X = 75 + (i * 30), Y = 78, Larghezza = 25, Altezza = 25
                // Puoi cambiare 25, 25 per fare i cuoricini più grandi o più piccoli!
                g.drawImage(heartImage, 10 + (i * 40), 55, 60, 60, null); 
            }
        }
    }
    public ArrayList<Entity> getNemici() {
        return nemici;
    }
}