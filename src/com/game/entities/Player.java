package com.game.entities;

import com.game.core.GamePanel;
import com.game.core.GameState;
import com.game.db.PlayerDAO;
import com.game.db.PlayerProfile; // Importiamo la classe del profilo
import com.game.utils.Constants;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {

    // L'array di immagini che rappresenta il giocatore (il suo sprite)
    private BufferedImage[] sprites;
    // Statistiche del giocatore
    private int vite = 3;         
    private int monetePrese = 0;
    // Variabili per l'invincibilità
    private boolean invincibile = false;
    private int invincibileTick = 0;

    //istanzio la classe PlayerDAO per gestire il salvataggio dei vari dati
    private PlayerDAO playerDAO = new PlayerDAO();
    
    // Sostituiamo la stringa con l'oggetto PlayerProfile
    private PlayerProfile profilo;

    // Variabili per gli Input (Tastiera)
    private boolean left, right, jump, down;

        // Variabili per gestire il tempo e i fotogrammi
    private int aniTick = 0;   // cronometro che conta i frame
    private int aniIndex = 0;  // indice del fotogramma attuale 
    private int aniSpeed = 10; // velocità dell'animazione 
    
    // Costanti per il movimento orizzontale
    private float playerSpeed = 5.0f; // Velocità camminata

    // Variabili per la fisica verticale (Salto e Gravità)
    private float ariaSpeed = 0f;      // La velocità sull'asse Y
    private float gravita = 0.5f;      // velocità di caduta
    private float forzaSalto = -10.0f; // Forza iniziale del salto
    private boolean inAria = true;     // giocatore in aria o a terra

    //Inizializza GamePanel per poterlo utilizzare (es. collisioni)
    private GamePanel gamePanel;

    public Player(float x, float y, int width, int height, GamePanel gp) {

        super(x, y, width, height);
        this.gamePanel = gp;

        //polimorfismo
        caricaImmagine();
        
        // Inizializziamo il profilo con il nome di test predefinito
        this.profilo = new PlayerProfile("GiocatoreTest");
    }

private void caricaImmagine() {
//carichiamo un array che conterra 4 immagini per l animazione del Player
        sprites = new BufferedImage[5]; 
        try {
            // Carichiamo la prima immagine alla posizione 0 abbassato
            sprites[0] = ImageIO.read(new File("res/Sprites/Characters/Double/character_pink_duck.png"));
            // Carichiamo la seconda immagine alla posizione fermo
            sprites[1] = ImageIO.read(new File("res/Sprites/Characters/Double/character_pink_jump.png"));
            // Carichiamo la terza  immagine alla posizione salto
            sprites[2] = ImageIO.read(new File("res/Sprites/Characters/Double/character_pink_idle.png"));
            // Carichiamo la quarta immagine alla posizione movimento1
            sprites[3] = ImageIO.read(new File("res/Sprites/Characters/Double/character_pink_walk_a.png"));
            // Carichiamo la quinta immagine alla posizione movimento2
            sprites[4] = ImageIO.read(new File("res/Sprites/Characters/Double/character_pink_walk_b.png"));

        } catch (IOException e) {
            System.err.println("Errore: Immagini del Player non trovate!");
        }
    }

// adattiamo la heatbox rispetto a entity
    @Override
    protected void initHitbox() {
        hitbox = new Rectangle((int) x+23, (int) y+30, width-50, height-31);
    }
        @Override
    protected void updateHitbox() {
        hitbox.x = (int) x+23;
        hitbox.y = (int) y+30;

    }

    // Metodo per aggiornare le coordinate del giocatore (logica del movimento, gravità, ecc.)
    @Override
    public void update() {
        aggiornaPosizione();
        if (invincibile) {
            invincibileTick++;
            if (invincibileTick >= 60) {
                invincibile = false;     // Torna vulnerabile
                invincibileTick = 0;     // Azzera il cronometro
            }
        }
        aggiornaAnimazione();
        // 1. Controlla se hai preso monete e se ti scontri con i nemici
        gamePanel.getCollisionChecker().checkCollision(this);
        gamePanel.getCollisionChecker().checkEnemyCollision(this, gamePanel.getNemici());
    }


    private void aggiornaPosizione() {
        // --- PARTE 1: MOVIMENTO ORIZZONTALE (X) ---
        if (left || right) {
            // Diciamo all'entità da che parte stiamo andando
            if (left) setDirection("LEFT");
            if (right) setDirection("RIGHT");

            // FONDAMENTALE 1: Diciamo al CollisionChecker a che velocità andiamo in orizzontale!
            speed = playerSpeed;

            // Resettiamo la collisione e chiediamo al GamePanel di controllare
            setCollisionOn(false);
            gamePanel.getCollisionChecker().checkTile(this);

            // Muoviti sull'asse X SOLO SE il checker dice che non c'è un muro
            if (!isCollisionOn()) {
                if (left) x -= playerSpeed;
                if (right) x += playerSpeed;
            }
        }else {
            setDirection("IDLE");
        }

        updateHitbox();

        // --- PARTE 2: SALTO E GRAVITÀ (Y) ---
        // Se premo salto e sono a terra, inizio a volare
        if (jump && !inAria) {
            ariaSpeed = forzaSalto; // -10.0f mi dà la spinta verso l'alto
            inAria = true;
        }

        // Se sono in aria (o sto saltando in su, o sto cadendo in giù)
        if (inAria) {
            // Applichiamo la gravità alla velocità (ad ogni frame vado sempre più veloce verso il basso)
            ariaSpeed += gravita;

            // Diciamo all'entità se stiamo andando SU o GIÙ per far controllare la testa o i piedi
            if (ariaSpeed < 0) setDirection("UP");
            else setDirection("DOWN");

            // FONDAMENTALE 2: Diciamo al CollisionChecker la nostra velocità di caduta.
            // Usiamo Math.abs() per trasformare numeri negativi (salto) in positivi per il Checker.
            speed = Math.abs(ariaSpeed);

            // Controllo collisioni verticale
            setCollisionOn(false);
            gamePanel.getCollisionChecker().checkTile(this);

            if (!isCollisionOn()) {
                // Se non sbatto, mi muovo verticalmente
                y += ariaSpeed;
            } else {
                // SE SBATTO:
                if (getDirection().equals("DOWN")) {
                    // Sbattevo in GIÙ: sono atterrato sul pavimento!
                    inAria = false;
                    ariaSpeed = 0;
                    // Calcola dove effettivamente stai atterrando (posizione proiettata)
                    int bottomPixel = (int)(hitbox.y + hitbox.height + speed);
                     int tileRow = bottomPixel / Constants.SIZE_BLOCCO;
                    // Allinea la Y al bordo superiore del tile
                    y = (tileRow * Constants.SIZE_BLOCCO) - hitbox.height - 30;
                } else if (getDirection().equals("UP")) {
                    // Sbattevo in SU: ho picchiato la testa sul soffitto!
                    ariaSpeed = 0; // Azzero la velocità di salita, inizio a cadere
                }
            }
        } else {
            // --- CONTROLLO BORDI (EDGE DETECTION) ---
            // Se non sono in aria (cammino a terra), devo assicurarmi che ci sia 
            // ancora il terreno sotto di me. Se cammino oltre un burrone, devo cadere!
            setDirection("DOWN");
            
            // FONDAMENTALE 3: Simulo di cadere di 1 solo pixel per vedere se c'è il vuoto sotto di me
            speed = 1.0f; 
            
            setCollisionOn(false);
            gamePanel.getCollisionChecker().checkTile(this); 
            
            if (!isCollisionOn()) {
                inAria = true; // C'è il vuoto, cadi!
            }
        }
    }   

    // disegno il giocatore sullo schermo in base all indice aniIndex (stato del movimento attuale)
    @Override
    public void draw(Graphics g) {
        if (sprites != null && sprites[aniIndex] != null) {
            g.drawImage(sprites[aniIndex], (int) x, (int) y, width, height, null);
        }
        
        drawHitbox(g);
    }

 //aggiorno l animazione in base al movimento(abbassato,salto,cammino,fermo)
private void aggiornaAnimazione() {
        if (inAria) {
            aniIndex = 1;
        } 
        else if (down) {
            aniIndex = 0;
        } 
        else if (left || right) {
            aniTick++;
            
            if (aniTick >= aniSpeed) {
                aniTick = 0; 
                if (aniIndex == 3) {
                    aniIndex = 4;
                } else {
                    aniIndex = 3;
                }
            }
        } 
        else {
            aniIndex = 2;
        }
    }
    //MONETE
    public void aggiungiMoneta() {
        monetePrese++; // Aumenta di 1
        System.out.println("Moneta presa! Totale: " + monetePrese);
    }
    public int getMonetePrese() {
        return monetePrese;
    }
    public int getVite() {
        return vite;
    }
public void rimuoviVita() {
        if (invincibile) return; 

        if (vite == 1) {
            vite--;
            System.out.println("Game Over!");
            resetPosition(50, 450);
            GamePanel.state = GameState.DEATH;
            gamePanel.resetPartita();
        } else {
            vite--;
            invincibile = true; // ATTIVA L'INVINCIBILITÀ!
            resetPosition(50, 450);

        }
    }
    
    //Funzioni Setter per il movimento
    public void setLeft(boolean left) {
        this.left = left;
    }
    public void setRight(boolean right) {
        this.right = right;
    }
    public void setJump(boolean jump) {
        this.jump = jump;
    }
    public void setDown(boolean down) {
        this.down = down;
    }

    public void saveWinningSession(){
        // 1 Recuperiamo i dati della partita dal player che sta giocando
        int moneteRaccolte = this.getMonetePrese();
        int viteRimanenti = this.getVite();
        long tempoImpiegato = gamePanel.getTimeSeconds();

        System.out.println("--- BANDIERA TOCCATA! ---");
        
        // Recuperiamo il nickname dinamico direttamente dal PlayerProfile
        String nicknameAttuale = profilo.getNickname();

        // 2 Assicuriamoci che il player esista altrimenti lo creiamo
        if(!playerDAO.playerExists(nicknameAttuale)){
            playerDAO.createNewPlayer(nicknameAttuale);
        }

        try{
        // 3 Salviamo la sessione vincente nel database
        playerDAO.saveWinningSession(nicknameAttuale, moneteRaccolte, viteRimanenti, tempoImpiegato);

                // Prima di visualizzare la classifica, diciamo al GamePanel di interrogare il DB
                gamePanel.fetchLeaderboard();
                GamePanel.state = GameState.LEADERBOARD;

        } catch (Exception e){
            System.err.println("Errore durante il salvataggio su MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Permette di aggiornare il nickname del profilo.
     * quando si chiederà al giocatore di inserire il nickname, si chiamerà 
     * questo metodo per aggiornarlo nel profilo
     */
    public void setNickname(String nuovoNickname) {
        if (this.profilo != null) {
            this.profilo.setNickname(nuovoNickname);
        }
    }
      
    public void resetMonete(){
        monetePrese=0;
    }
    public void resetVite(){
        vite=3;
    }
}
