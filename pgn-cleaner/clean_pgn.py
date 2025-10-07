from pyspark.sql import SparkSession
import re
import os
import sys

def clean_pgn(input_file):
    spark = SparkSession.builder.appName("PGN Cleaner Enhanced").getOrCreate()
    sc = spark.sparkContext

    print(f"🔹 Lecture du fichier brut : {input_file}")

    # Lecture du fichier texte
    lines = sc.textFile(input_file)

    # --- Filtre 1 : suppression du BOM et des lignes vides ---
    def remove_bom(line):
        return line.replace("\ufeff", "").strip()

    lines = lines.map(remove_bom).filter(lambda x: x != "")

    # --- Filtre 2 : suppression des tournois corrompus ou fantômes ---
    def is_not_dummy_event(line):
        # Supprime les Event non pertinents
        bad_keywords = [
            "OL Wuert", "BL2-Nord", "Wuertt", "Oberliga", "Bezirks", "Kreisklasse",
            "Landesliga", "Unterfr", "Niederbayer", "Wuerttemb", "Nordbayer", "Ostbayer"
        ]
        if line.startswith("[Event"):
            return not any(k in line for k in bad_keywords)
        return True

    lines = lines.filter(is_not_dummy_event)

    # --- Filtre 3 : garder uniquement métadonnées et coups valides ---
    def is_valid_pgn_line(line):
        # Balises PGN valides
        if line.startswith("[") and line.endswith("]"):
            return True
        # Lignes de coups valides : doivent contenir chiffres + lettres
        if re.match(r"^\d+\.\s*[A-Za-z]", line):
            return True
        return False

    lines = lines.filter(is_valid_pgn_line)

    # --- Filtre 4 : corriger les guillemets mal fermés ---
    def fix_unclosed_quotes(line):
        if line.startswith("[") and line.count('"') == 1:
            return line + '" ]'
        return line

    lines = lines.map(fix_unclosed_quotes)

    # --- Filtre 5 : suppression des lignes anormales (mélange de noms dans les coups) ---
    def remove_corrupted_moves(line):
        # Exemples de lignes corrompues : "Sherbakov,R-Poluljahov,A/Kahovka"
        if re.search(r"[A-Za-z]+,[A-Za-z]+-", line) and not line.startswith("["):
            return ""
        return line

    lines = lines.map(remove_corrupted_moves).filter(lambda x: x != "")

    # Collecte et sauvegarde
    cleaned_lines = lines.collect()

    output_file = input_file.replace(".pgn", "_cleaned_final.pgn")
    with open(output_file, "w", encoding="utf-8") as f:
        for line in cleaned_lines:
            f.write(line.strip() + "\n")

    print("✅ Nettoyage terminé avec succès.")
    print(f"📂 Fichier sauvegardé : {output_file}")

    spark.stop()

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("❌ Usage: python clean_pgn_chesslib_ready.py <chemin_du_fichier.pgn>")
        sys.exit(1)

    input_file = sys.argv[1]
    if not os.path.exists(input_file):
        print(f"❌ Fichier introuvable : {input_file}")
        sys.exit(1)

    clean_pgn(input_file)
