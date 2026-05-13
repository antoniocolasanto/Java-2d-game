# Java-2d-game
Java 2D Game è un progetto sviluppato in Java da un team di 3 persone. L’obiettivo è creare un videogioco 2D applicando principi di programmazione orientata agli oggetti.
Il progetto è stato sviluppato tramite GitHub.

-------------------------------------

Per clonare il progetto:
Ogni membro sul proprio PC entra in una cartella:

cd java-2d-game

git clone https://github.com/AntonioColasanto/Java-2d-game.git

-------------------------------------

LE CARTELLE SONO COMPOSTE IN QEUSTO MODO:
📂 res
    📂 Map
    📂 Sounds
    📂 Sprites
        📂 Background
        📂 Characters
        📂 Enemies
        📂 Tiles

📂 src

    📂 com

        📂 game 

            📂 core
                GamePanel.java

            📂 db
                LeaderboardDAO.java
                MongoDBManager.java
                PlayerDAO.java
                PlayerProfile.java
                RedisManager.java

            📂 entities
                Enemy.java
                Entity.java
                Player.java

            📂 graphics
                Background.java
                Renderer.java

            📂 inputs
                KeyInput.java

            📂 levels
                LevelManager.java
            
            📂 main
                GameWindow.java
                Main.java

            📂 utils
                Constants.java