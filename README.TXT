# TP 2

Noms: Stefan Roman, Simo Hakim

https://github.com/JuiceBox27/IFT3913_TP1/tree/TP2_V2

Librairie utilisée: https://github.com/mauricioaniche/ck 

## Pour voir les résultats de l'étude jfreechart il suffit d'aller dans le dossier etude-jfreechart et voir les trois fichiers de résultats que l'application génère.

## Pour accéder au projet il suffit de suivre les étapes de création de jars.

## Création des jars

Toutes les commandes sont assumées du root du projet.

### Premièrement compiler le code en classes

javac ./src/TP2/*.java -d ./bin/TP2/* -cp ./lib/* -sourcepath ./src/TP2/

### Deuxièmement créer les jars

jar cvfe jars/AnalysisTool.jar tp2.AnalysisTool -C bin/ tp2/ lib/

# Comment l'utiliser

java -jar AnalysisTool.jar <chemin-de-l'entrée> <dossier-à-la-sortie.csv>

ex.: java -jar AnalysisTool.jar ../../jfreechart/src/ ./etude-jfreechart

Il est nécessaire d'avoir l'outil de ligne de commande git pour utiliser le programme.

Veuillez prévoir un peu de temps pour laisser le programme s'exécuter. Il a un temps moyen de 150 secondes (2min30).
Il semblait raisonnable comme temps tenant compte que plusieurs outils d'analyse de code ont un temps similaire pour des projets de taille similaires.
Ceci est une évidence par pratique personnel et en milieu professionnel d'usage d'autres outils. 

## Commentaires auto-générés
https://github.com/mintlify/writer

1. Vous devez clairement identifier où et comment l'IA générative a été utilisée. 

L'IA a été utilisée pour créer les commentaires de chaque méthode. 
J'ai modifié quelques commentaires mais on peut considérer que tous les commentaires définissant une méthode sont générés par l'IA.


2. Vous devez ajouter une annexe détaillée qui énumère toutes vos prompts à l'IA, ainsi que les réponses exactes reçues.
Je n'ai pas de prompts en soit mais j'imagine que les méthodes en soit ont étés passées à l'IA.

3. Dans votre travail, vous devez faire référence à la prompt pertinente dans votre index.

4. Vous devez inclure un bref texte dans lequel vous expliquez, quantitativement et qualitativement :

a) comment vous avez utilisé l'IA générative:
L'IA a été utilisée pour créer les commentaires de chaque méthode. 
J'ai modifié quelques commentaires mais on peut considérer que tous les commentaires définissant une méthode sont générés par l'IA.

b) les risques pour la véracité, l'exactitude et la qualité de votre travail: 
Les risques sont que l'IA ne comprenne pas entièrement le but de la fonction alors il se peut que son explication ne soit pas entièrement vraie.
Les risques à l'exactitude sont à peu près les mêmes car il est possible que ce ne soit pas compris que font les différentes classes, les paramètres ou le code.
Alors si quelqu'un lit les commentaires cette personne peut se retrouver à utiliser la fonction et perdre son temps à débugger en étant plus confus par l'information différente de l'IA et du code.
La qualité du code prends un coup si les commentaires sont erronés. C'est pire que de ne pas en avoir car ça peut perdre le temps à quelqu'un qui essaie de comprendre rapidement le code.

c) les mesures concrètes que vous avez prises pour atténuer ces risques:
Relire les commentaires et modifier les commentaires. Relire même le code pour m'assurer que ce n'était pas moi qui avait écrit du code illogique.

d) la valeur que vous avez vous-même ajoutée au contenu généré par l'IA, par exemple les moments où vous avez dû intervenir pour corriger ou orienter l'IA générative:
L'exactitude de mon intention dans les fonctions et dans le code. C'est-à-dire mettre en commentaires ce que je voulais que cette fonction soit pour le program non-seulement ce qu'elle fait.