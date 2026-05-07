# Java-2d-game
Java 2D Game è un progetto collaborativo sviluppato in Java da un team di 3 persone. L’obiettivo è creare un videogioco 2D con una struttura modulare e organizzata, applicando principi di programmazione orientata agli oggetti e sviluppo collaborativo tramite Git e GitHub.

--------------------------------

Ogni membro sul proprio PC, entra nella cartella:

cd java-2d-game
e poi: git clone https://github.com/AntonioColasanto/Java-2d-game.git

--------------------------------------

COMANDI GIT

Regole fondamentali di lavoro in team (IMPORTANTISSIMO)

BRANCH SEPARATI

Ogni persona crea un branch:

Esempio:

git checkout -b player-system

Oppure:

git checkout -b enemy-ai
git checkout -b map-system
6) Flusso di lavoro quotidiano
1. Aggiorna il progetto

Prima di iniziare a lavorare:

git pull origin main
2. Lavora sul tuo branch

Modifica codice, aggiungi feature.

3. Salva le modifiche
git add .
git commit -m "Aggiunto movimento player"
4. Carica su GitHub
git push origin player-system
