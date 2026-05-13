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
        level1.txt
        level2.txt
        level3.txt
    📂 Sounds
        sfx_dump.ogg
        sfx_coin.ogg
        sfx_disappear.ogg
        sfx_gem.ogg
        sfx_hurt.ogg
        sfx_jump_high.ogg
        sfx_jump.ogg
        sfx_magic.ogg
        sfx_select.ogg
        sfx_throw.ogg
    📂 Sprites
        📂 Background
            📂 Default
            📂 Double

        📂 Characters
            📂 Default
            📂 Double
        📂 Enemies
            📂 Default
            📂 Double
        📂 Tiles
            📂 Default
            📂 Double

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