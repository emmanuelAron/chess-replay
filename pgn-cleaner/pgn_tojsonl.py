import json
import re
import sys
import os
from tqdm import tqdm

def parse_pgn(pgn_file):
    """Parse PGN file and yield one complete game starting at '1.'."""
    with open(pgn_file, "r", encoding="utf-8", errors="ignore") as f:
        buffer = []
        for line in f:
            stripped = line.strip()

            # 🟡 Détection d'un nouveau début de partie
            if re.match(r"^1\.", stripped) and buffer:
                # si la partie précédente contient au moins un coup, on l'envoie
                if any(re.match(r"^\d+\.", l) for l in buffer):
                    yield "\n".join(buffer)
                buffer = []

            buffer.append(stripped)

        # dernière partie du fichier
        if any(re.match(r"^\d+\.", l) for l in buffer):
            yield "\n".join(buffer)


def extract_game_data(raw_game):
    """Extract metadata and moves from one PGN block."""
    tags = dict(re.findall(r'\[(\w+)\s+"(.*?)"\]', raw_game))

    # Nettoyage du texte
    moves_text = re.sub(r'\[.*?\]', '', raw_game)
    moves_text = re.sub(r'\d+\.', '', moves_text)
    moves_text = re.sub(r'\s+', ' ', moves_text).strip()

    # Couper à la fin de la partie
    moves_text = re.split(r'\s(1-0|0-1|1/2-1/2)\s?', moves_text)[0]

    # Extraire les coups en SAN
    moves = re.findall(r'\b[a-hRNBQKO0-9x+=#-]+\b', moves_text)

    return {
        "event": tags.get("Event", ""),
        "site": tags.get("Site", ""),
        "date": tags.get("Date", ""),
        "white": tags.get("White", ""),
        "black": tags.get("Black", ""),
        "result": tags.get("Result", ""),
        "eco": tags.get("ECO", ""),
        "moves": moves
    }


def convert_pgn_to_jsonl(pgn_path):
    """Convert PGN file into JSONL (1 game per line)."""
    output_path = pgn_path.replace(".pgn", "_cleaned.jsonl")

    total_games = valid_games = 0

    with open(output_path, "w", encoding="utf-8") as out:
        for raw_game in tqdm(parse_pgn(pgn_path), desc="♟️ Conversion des parties"):
            total_games += 1
            data = extract_game_data(raw_game)
            if not data["moves"] or len(data["moves"]) < 4:
                continue
            json.dump(data, out, ensure_ascii=False)
            out.write("\n")
            valid_games += 1

    print(f"\nConversion terminée !")
    print(f"Parties totales : {total_games}")
    print(f"Parties valides : {valid_games}")
    print(f"Fichier généré : {output_path}")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("❌ Usage: python pgn_to_jsonl.py <chemin_du_fichier.pgn>")
        sys.exit(1)

    input_file = sys.argv[1]
    if not os.path.exists(input_file):
        print(f"❌ Fichier introuvable : {input_file}")
        sys.exit(1)

    convert_pgn_to_jsonl(input_file)
