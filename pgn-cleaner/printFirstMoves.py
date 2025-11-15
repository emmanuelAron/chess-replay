from pyspark.sql import SparkSession
from pyspark.sql.functions import col, slice

# ✅ 1. Initialiser la session Spark
spark = SparkSession.builder \
    .appName("CheckChessJSONL") \
    .getOrCreate()

# ✅ 2. Chemin du fichier
#path = "C:/Users/emman/Desktop/ironhackData/week7/chess_dataset/games_1990_cleaned_final_cleaned.jsonl"
path = "games_1990_cleaned.jsonl"


print(f"📂 Lecture du fichier : {path}\n")

# ✅ 3. Lecture du JSONL
try:
    df = spark.read.json(path)
except Exception as e:
    print("❌ Erreur de lecture du JSON :", e)
    spark.stop()
    exit(1)

# ✅ 4. Vérifier si le DataFrame est vide
count = df.count()
if count == 0:
    print("⚠️  Le DataFrame est vide ! Vérifie le chemin ou le contenu du fichier.")
    spark.stop()
    exit(0)

print(f"✅ Fichier chargé avec succès ({count} lignes)")

# ✅ 5. Afficher le schéma détecté
print("\n📘 Schéma détecté :")
df.printSchema()

# ✅ 6. Vérifier la présence des colonnes clés
expected_cols = {"white", "black", "moves", "date", "event"}
missing = expected_cols - set(df.columns)

if missing:
    print(f"\n⚠️ Colonnes manquantes dans le fichier : {missing}")
    print("👉 Vérifie que le fichier JSONL contient bien ces champs.")
else:
    print("\n✅ Toutes les colonnes attendues sont présentes.")

# ✅ 7. Afficher un aperçu (1 ligne)
print("\n📄 Exemple d'une ligne :")
df.show(1, truncate=False)

# ✅ 8. Filtrer une partie spécifique (si la colonne existe)
if "white" in df.columns:
    player_name = "Hernandez Velasco, Jesus"
    print(f"\n🔍 Recherche des parties jouées par {player_name}...\n")

    df_filtered = df.filter(col("white") == player_name)

    if df_filtered.count() == 0:
        print(f"⚠️ Aucune partie trouvée pour {player_name}.")
    else:
        df_filtered.select(
            "white", "black", "event", "date",
            slice("moves", 1, 10).alias("first_10_moves")
        ).show(truncate=False)
        # Ecrire premiere partie
        df_filtered.limit(1).coalesce(1).write.mode("overwrite").json("firstGame.json")

else:
    print("\n Impossible de filtrer : la colonne 'white' est absente du fichier.")

spark.stop()

