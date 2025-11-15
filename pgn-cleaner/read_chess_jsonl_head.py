from pyspark.sql import SparkSession

# session Spark
spark = SparkSession.builder \
    .appName("Read JSONL Head") \
    .getOrCreate()

# Chemin
file_path = "C:/Users/emman/Desktop/ironhackData/week7/chess_dataset/games_1990_cleaned_final_cleaned.jsonl"

# Lire le JSONL
# fichiers JSONL = JSON par ligne
df = spark.read.json(file_path)

# Afficher la structure et les 3 premières lignes
df.printSchema()
df.select("white", "black", "moves").show(3, truncate=False)

# colonnes disponibles :
print("Colonnes disponibles :", df.columns)

# ✅ Sauvegarder un petit échantillon dans un nouveau fichier
sample_path = "C:/Users/emman/Desktop/ironhackData/week7/chess_dataset/sample_3.json"
df.limit(3).coalesce(1).write.mode("overwrite").json("sample_preview.json")

print(f"✅ Échantillon exporté vers : {sample_path}")

# ✅ 7. Arrêter Spark
spark.stop()
