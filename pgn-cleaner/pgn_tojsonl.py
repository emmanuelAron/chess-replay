import json
import re
import sys
import os

def parse_pgn(pgn_file):
    """Parse a PGN file and yield one game (metadata + moves) at a time."""
    with open(pgn_file, "r", encoding="utf-8", errors="ignore") as f:
        buffer = []
        for line in f:
            if line.strip() == "" and buffer:
                yield "\n".join(buffer)
                buffer = []
            else:
                buffer.append(line.strip())
        if buffer:
            yield "\n".join(buffer)

def extract_game_data(raw_game):
    """Extract key metadata and moves from a single PGN string."""
    tags = dict(re.findall(r'\[(\w+)\s+"(.*?)"\]', raw_game))
    moves_text = re.sub(r'\[.*?\]', '', raw_game)  # remove metadata blocks
    moves_text = re.sub(r'\d+\.', '', moves_text)  # remove move numbers
    moves_text = re.sub(r'\s+', ' ', moves_text).strip()

    moves = re.findall(r'\b[a-hRNBQKO0-9x+=#-]+\b', moves_text)
    moves = [m for m in moves if not m.endswith("1-0") and not m.endswith("0-1") and not m.endswith("1/2-1/2")]

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
    """Main function: convert a PGN file into JSONL format."""
    output_path = pgn_path.replace(".pgn", "_cleaned.jsonl")

    with open(output_path, "w", encoding="utf-8") as out:
        for raw_game in parse_pgn(pgn_path):
            data = extract_game_data(raw_game)
            if data["moves"]:
                json.dump(data, out, ensure_ascii=False)
                out.write("\n")

    print(f"✅ Conversion terminée !")
    print(f"📂 Fichier généré : {output_path}")
    return output_path


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("❌ Usage: python pgn_to_jsonl.py <chemin_du_fichier.pgn>")
        sys.exit(1)

    input_file = sys.argv[1]
    if not os.path.exists(input_file):
        print(f"❌ Fichier introuvable : {input_file}")
        sys.exit(1)

    convert_pgn_to_jsonl(input_file)
