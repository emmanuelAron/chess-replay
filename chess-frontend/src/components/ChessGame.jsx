import React, { useEffect, useState } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";

const ChessGame = () => {
  const [game] = useState(new Chess());
  const [position, setPosition] = useState("start");
  const [lastMoveIndex, setLastMoveIndex] = useState(-1);
  const [players, setPlayers] = useState({ white: "White", black: "Black" });
  const [boardKey, setBoardKey] = useState(0);


  useEffect(() => {
    const socket = new WebSocket("ws://localhost:8080/chess-stream");

    socket.onopen = () => {
      console.log("✅ WebSocket connected");
    };

    socket.onclose = () => {
      console.warn("⚠️ WebSocket fermé");
    };

    socket.onerror = (err) => {
      console.error("❌ WebSocket error:", err);
    };

    socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      const move = data.move;
      const moveIndex = data.moveIndex;

      console.log("📥 Reçu :", data);
      console.log("⏳ FEN actuelle :", game.fen());

      // Ne pas rejouer un coup déjà joué
      if (moveIndex <= lastMoveIndex) {
        console.log("⏭ Coup ignoré (déjà joué ou en retard) :", move);
        return;
      }

      // Redémarrer une partie si moveIndex === 0
      if (moveIndex === 0) {
        console.log("🔄 Nouvelle partie, reset échiquier");
        game.reset();
        setPosition("start");
        setLastMoveIndex(-1);
      }

      // Afficher les noms des joueurs si présents
      setPlayers({
        white: data.white || "White",
        black: data.black || "Black",
      });

      try {
        const result = game.move(move);
        if (result) {
          setPosition(game.fen());
          setLastMoveIndex(moveIndex);
          setBoardKey(prev => prev + 1); // 👈 force re-render
          console.log("✅ Coup appliqué :", move);

          // ✅ Vérifie si le roi est en échec (nouvelle API)
          if (game.isCheck()) {
            console.warn("♟️ Le roi est en échec !");
          }
        } else {
          console.warn("⛔ Coup invalide ignoré :", move, "FEN :", game.fen());
        }
      } catch (err) {
        console.error("❌ Erreur traitement coup :", move, err);
      }
    };

    return () => {
      socket.close();
    };
  }, [game, lastMoveIndex]);

  return (
    <div style={{ width: 400 }}>
      <h2>Partie en direct</h2>
      <h4>{players.white} vs {players.black}</h4>
      <Chessboard 
      key={boardKey} // 👈 force mise à jour visuelle
      position={position} 
      arePiecesDraggable={false} />
    </div>
  );
};

export default ChessGame;
