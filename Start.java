import javax.swing.JFrame;

public class Start{
    public static void main(String[] args) {
        // Crea la finestra base
        JFrame window = new JFrame();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Tiny Dungeon - AWT/Swing");

        // Crea e aggiungi il livello di gioco
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        // Adatta la finestra alle dimensioni definite nel GamePanel (1536x960)
        window.pack();

        // Mettila al centro dello schermo e rendila visibile
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}