import json
from chess import Board

# === 🔧 CONFIGURATION ===
input_file = r"C:\Users\emman\Desktop\ironhackData\week7\chess_dataset\games_1990_cleaned_final_cleaned.jsonl"
output_file = r"C:\Users\emman\Desktop\ironhackData\week7\chess_dataset\games_1990_cleaned_valid.jsonl"

total_games = 0
valid_games = 0
invalid_games = 0
invalid_moves_count = 0

print("♟️ Vérification des parties d’échecs...\n")

with open(input_file, "r", encoding="utf-8") as fin, open(output_file, "w", encoding="utf-8") as fout:
    for line_number, line in enumerate(fin, start=1):
        try:
            game = json.loads(line.strip())
            moves = game.get("moves", [])
            white = game.get("white", "White")
            black = game.get("black", "Black")
        except Exception as e:
            print(f"❌ Ligne {line_number}: JSON invalide -> {e}")
            invalid_games += 1
            continue

        board = Board()
        game_invalid = False

        for i, move in enumerate(moves):
            try:
                board.push_san(move)  # validation du coup en notation SAN
            except Exception:
                print(f"🚨 Partie {line_number} | {white} vs {black} | Coup {i+1}: '{move}' invalide")
                game_invalid = True
                invalid_moves_count += 1
                break  # on arrête à la première erreur

        total_games += 1

        if not game_invalid:
            fout.write(json.dumps(game, ensure_ascii=False) + "\n")
            valid_games += 1
        else:
            invalid_games += 1

print("\n================ Résumé ================")
print(f"Nombre total de parties analysées : {total_games}")
print(f"✅ Parties valides exportées : {valid_games}")
print(f"❌ Parties invalides : {invalid_games}")
print(f"🚨 Coups invalides détectés : {invalid_moves_count}")
print(f"\n📁 Nouveau fichier créé : {output_file}")
