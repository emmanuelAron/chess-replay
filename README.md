Bonjour,

Voici un projet qui m'intéresse beaucoup car autour des échecs.  (une véritable passion)
Il permet de rejouer des parties d'echecs coups par coups , un peu comme dans un film!  (temps réel)
C'est un projet en cours de développement et ce que vous voyez à ce jour n'est que le début du projet ... 
Il fait intervenir différentes technos en informatique, je cherchais initialement un projet autour du traitement de données avec spark...  
Partant d'un fichier pgn (format propre aux échecs), il y a du retraitement de données avec python, spark...  
J'ai fait le choix technique de transformer ces données en jsonl car le pgn pur m'a confronté à trop de problemes dans le nettoyage, validation des données...  
Premier test sur un fichier de 66 000 parties, je transforme ce pgn en jsonl (utilisé dans le monde big data).
Ce jsonl est transmis à kafka via un consumer et un consumer et on voit les coups défiler coté backend sur la console toutes les X secondes...(fait)
Ensuite je souhaiterai utiliser react-chessboard pour faire apparaitre un échiquier visuel sous forme web qui fera défiler les parties à une vitesse V (chaque coups).  
Ensuite, j'aimerai plusieurs echiquiers qui défilent à cette vitesse...Chaque échiquier serait un Consumer kafka d'une même source (pouvant etre tres grande) de données.  
Ensuite j'aimerai qu'à coté de chaque échiquier s'affiche des statistiques générées avec spark (pandas?) , par exemple le taux de victoire de chaque joueur de la partie en cours, ses ouvertures les plus jouées...A voir...  
Spark sera préféré à pandas car ce sera un contexte big data.
Enfin j'aimerai envoyer ces données sur plusieurs partitions spark , et réfléchir aux problématiques de performance et de traitement de données massives.

Technos:
Java 17
Spring boot avec socket
Kafka
Maven,Docker
Python, Spark
React (pas encore implémenté)



https://drive.google.com/file/d/1IDeHXMiDUqB8Z41WPCO_oQpaZH_oeZn2/view?usp=sharing
