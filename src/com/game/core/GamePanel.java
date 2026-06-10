package com.game.core;

// Pacchetto database del gioco
import com.game.db.*;
// Entità e grafica del gioco
import com.game.entities.*;
import com.game.graphics.*;
// Input, livelli e utility del gioco
import com.game.inputs.KeyInput;
import com.game.levels.LevelManager;
import com.game.utils.Constants;
// Librerie standard Java (AWT, Swing, IO, Util)
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Pannello centrale che gestisce il fulcro applicativo.
 * Funge da motore del Game Loop, connettore con il Database, area preposta 
 * alla renderizzazione a schermo degli elementi e accentratore degli input utente.
 */
public class GamePanel extends JPanel {

    private javax.swing.Timer gameTimer;
    private ArrayList<Entity> nemici;
    private int TimeSeconds = 0;
    private int TimeTicks = 0;
    private BufferedImage heartImage;
    
    // Dichiariamo l'oggetto CollisionChecker
    private CollisionChecker collisionChecker;

    private Player player;
    private MainMenu mainMenu;
    private PauseMenu pauseMenu;
    private LevelManager levelManager;
    private LayeredBackground layeredBg;

    //VARIABILI PER L'IDENTIFICAZIONE E CLASSIFICA
    private IdentificationScreen idScreen;
    private PlayerDAO playerDAO;
    private String currentNickname = ""; 

    private LeaderboardDAO leaderboardDAO;
    private LeaderboardScreen leaderboardScreen;
    private List<LeaderboardDAO.PlayerRecord> currentTopPlayers;

    // Lo stato iniziale è il MENU
    public static GameState state = GameState.MENU;

    /**
     * Costruttore dell'intero pannello di rendering.
     * Inizializza istanze UI, attiva il sistema dei DAO per MongoDB 
     * e prepara il caricamento della logica fisica dei nemici e delle collisioni.
     */
    public GamePanel() {
    
    // --- 1. IMPOSTAZIONI DEL PANNELLO ---
    // Definisce le dimensioni, il colore di sfondo e ottimizza il rendering
    this.setPreferredSize(new Dimension(Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA));
    this.setBackground(Color.CYAN);
    this.setDoubleBuffered(true); // Rende il disegno a schermo più fluido ed evita sfarfallii (flickering)

    // --- 2. GESTIONE INPUT ---
    // Dice al sistema che questo pannello ha il focus ed è pronto a ricevere input da tastiera
    this.setFocusable(true); 
    this.addKeyListener(new KeyInput(this)); // Collega il KeyListener per gestire la pressione dei tasti

    // --- 3. DATABASE E GESTIONE DATI ---
    // Connessione al DB e inizializzazione dei Data Access Object (DAO)
    MongoDBManager.connect(); 
    playerDAO = new PlayerDAO();
    leaderboardDAO = new LeaderboardDAO();

    // --- 4. MENU E SCHERMATE UI ---
    // Inizializzazione di tutte le interfacce grafiche slegate dal gameplay attivo
    mainMenu = new MainMenu();
    pauseMenu = new PauseMenu();
    idScreen = new IdentificationScreen();
    leaderboardScreen = new LeaderboardScreen();

    // --- 5. CARICAMENTO ASSET E SFONDI ---
    // Caricamento dell'immagine del cuore (vita del giocatore)
    try {
        heartImage = ImageIO.read(new File("res/Sprites/Tiles/Double/heart.png")); 
    } catch (IOException e) {
        System.err.println("Errore: Immagine del cuore non trovata!");
    }

    // Creazione dello sfondo a scorrimento (Parallax/Layered)
    layeredBg = new LayeredBackground();
    layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_solid_cloud.png", 0, 0, true, true);
    layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_solid_cloud.png", 0, 50, true, false);
    layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_color_desert.png", 0, 100, true, false);
    layeredBg.addLayer("res/Sprites/Backgrounds/Double/background_solid_sand.png", 0, 450, true, false);

    // --- 6. MOTORE DI GIOCO ED ENTITÀ ---
    // Inizializzazione della mappa, delle collisioni e del giocatore
    levelManager = new LevelManager();
    collisionChecker = new CollisionChecker(levelManager);
    player = new Player(10, 10, 100, 100, this);
    
    // Recupero la lista dei nemici dal livello
    nemici = levelManager.getListaNemici();

    // Cicliamo tutta la lista dei nemici e passiamo loro il riferimento a questo 
    // GamePanel in modo che possano interagire con l'ambiente di gioco
    for (Entity nemico : nemici) {
        nemico.setGamePanel(this);
    }
}

    /** * Restituisce la stringa del nome del giocatore attuale in uso a schermo.
     * @return currentNickname Il nickname.
     */
    public String getCurrentNickname() { return currentNickname; }
    
    /** * Applica un nuovo nome al player prima della query e conferma profilo.
     * @param nickname Il nuovo string set.
     */
    public void setCurrentNickname(String nickname) { this.currentNickname = nickname; }
    
    /**
     * Fornisce accesso al sistema DAO in memoria per evitare allocazioni inefficaci multiple.
     * @return L'oggetto playerDAO inizializzato.
     */
    public PlayerDAO getPlayerDAO() { return playerDAO; }

    /**
     * Invoca in fase differita la ricostruzione della cache per la classifica globale estraendo i dati dal Database.
     */
    // --- NUOVO METODO: RECUPERA LA CLASSIFICA AGGIORNATA ---
    public void fetchLeaderboard() {
        currentTopPlayers = leaderboardDAO.getTop10Players();
    }

    /**
     * Accende il metronomo di gioco bloccando l'aggiornamento logico-visivo su specifici Frame-Per-Second (60Hz).
     */
    // GAME LOOP (Motore del gioco)
    public void startGameThread() {
        // Il Timer di Java eseguirà questa parentesi graffa 60 volte al secondo (1000 millisecondi / 60)
        gameTimer = new javax.swing.Timer(1000 / 60, e -> {
            update();
            repaint();
        });
        gameTimer.start();
    }

    /**
     * Fornisce il puntatore di memoria dell'entità Player per logiche esterne e Input.
     * @return Il giocatore attivo e giocabile.
     */
    // GETTERS 
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Estrapola il calcolo formale dei secondi puri passati all'interno della sessione di gioco.
     * @return L'integer temporale in secondi.
     */
    public int getTimeSeconds() {
        return TimeSeconds;
    } 

    /**
     * Consente a tutti gli elementi di servirsi del calcolatore di fisica senza doverlo ricreare.
     * @return Il Checker delle Hitbox e direzioni.
     */
    // Serve al Player per prendere i controlli fisici
    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    /**
     * Metodo esecutivo ad altissima frequenza che ricalcola attivamente e prima del disegno le regole 
     * di spostamento, i tic e i pattern comportamentali dell'Intelligenza Artificiale.
     */
    public void update() {
        switch (state) {
            case PLAYING:
                if (player != null) player.update();

                // Aggiorniamo i nemici
                for (Entity nemicoCorrente : nemici) {
                    nemicoCorrente.update();
                }
                
                // Gestione del Timer
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

    /**
     * Sovrascrittura vitale della libreria visiva AWT che imprime la bufferizzazione 
     * in pixel in base al GameState, processando gli sfondi multilivello e i filtri overlay (es. Pause, Morte).
     * * @param g L'istanza fondamentale per applicare colori, rendering testuali e geometrie su JPanel.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (layeredBg != null) {
            layeredBg.draw(g, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);
        }
        
        // 2. Rendering basato sullo stato
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
                
            case DEATH:
                // 1. Disegna il mondo di gioco bloccato sullo sfondo
                renderGameWorld(g);

                // Facciamo il cast a Graphics2D per sbloccare effetti grafici avanzati
                Graphics2D g2 = (Graphics2D) g;
                
                // Facciamo comparire i font dopo la morte
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // SFONDO
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

                // Disegniamo prima l'ombra nera
                g2.setColor(Color.BLACK);
                g2.drawString(titolo, xTitolo + 4, yTitolo + 4);
                // Disegniamo il testo principale sopra in rosso intenso
                g2.setColor(new Color(220, 20, 20));
                g2.drawString(titolo, xTitolo, yTitolo);

                // Istruzione per rendere il testo più leggibile
                String istruzione = "PREMI INVIO PER TORNARE AL MENU";
                g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
                
                // Centriamo dinamicamente anche l'istruzione
                int xIstruzione = (Constants.LARGHEZZA_FINESTRA - g2.getFontMetrics().stringWidth(istruzione)) / 2;
                int yIstruzione = 370;

                // Ombra dietro al testo
                g2.setColor(Color.BLACK);
                g2.drawString(istruzione, xIstruzione + 2, yIstruzione + 2);
                // Testo bianco
                g2.setColor(Color.WHITE);
                g2.drawString(istruzione, xIstruzione, yIstruzione);
                break;
        }
    }

    /**
     * Stampa in cascata il Level Design dei blocchi base, dopodiché sovrappone i layer 
     * di nemici attivi e dello Sprite del Player.
     * * @param g Il target visivo per elaborare il disegno grafico.
     */
    private void renderGameWorld(Graphics g) {
        if (levelManager != null) levelManager.draw(g);
        if (player != null) player.draw(g);

        for (Entity nemicoCorrente : nemici) {
            nemicoCorrente.draw(g);
        }
    }

    /**
     * Applica un reset forzato ripulendo i parametri in gioco ed estraendo
     * la mappa incontaminata dal LevelManager per le nuove interazioni pulite.
     */
    public void resetPartita() {
        TimeSeconds = 0;
        TimeTicks = 0;
        player.resetVite();
        player.resetMonete();
        player.resetPosition(50, 350);

        levelManager = new LevelManager();
        collisionChecker = new CollisionChecker(levelManager);
        nemici = levelManager.getListaNemici();

        //Quando viene resettata la partita vengono
        // reimpostati anche i nemici nel GamePanel
        for (Entity nemico : nemici) {
            nemico.setGamePanel(this);
        }
    }
    
    /**
     * Genera e dimensiona gli strumenti testuali dell'interfaccia (User Interface) come Score Monete,
     * UI delle vite restanti (cuori) e badge ad alta leggibilità per il Timer della prestazione in corso.
     * * @param g L'istanza fondamentale Graphics.
     */
    // Metodo per disegnare l'Interfaccia Utente (UI)
    private void drawUI(Graphics g) {
        if (player == null) return;

        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));

        g.drawString("Monete " + player.getMonetePrese(), 20, 50);
        
        g.setColor(Color.RED); 
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
        g.drawString(testoTempo, xTempo, yTempo);
    }

    /**
     * Cicla la stampa fisica in scala dello sprite "cuore" in proporzione precisa 
     * alla statistica HP ancora viva che la variabile interna restituisce.
     * * @param g Il target Graphics di disegno su finestra.
     * @param vite La traccia in numeri dell'energia o della permanenza di gioco.
     */
    public void disegnaVite(Graphics g, int vite){
        // Ciclo magico: disegna un'immagine del cuore per ogni vita che possiedi
        for (int i = 0; i < vite; i++) {
            if (heartImage != null) {
                g.drawImage(heartImage, 10 + (i * 40), 55, 60, 60, null); 
            }
        }
    }
    
    /**
     * Fornisce la lista attivamente elaborata di mostri. 
     * Utile a test ed entità per convalidare cicli for o array dinamici condivisi.
     * * @return L'arraylist dinamico.
     */
    public ArrayList<Entity> getNemici() {
        return nemici;
    }
}