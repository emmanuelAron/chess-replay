import React, { useState } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";

const ChessGame = () => {
  // on crée une seule instance du jeu
  const [game] = useState(() => new Chess());
  const [position, setPosition] = useState(game.fen());
  const moves = ["e4", "c5", "Nf3"];

  const playNextMove = () => {
    const currentMove = moves[game.history().length];
    if (!currentMove) {
      console.log("Tous les coups ont été joués !");
      return;
    }

    const result = game.move(currentMove);
    if (result) {
      console.log(`Coup joué : ${currentMove}`);
      setPosition(game.fen());
    } else {
      console.warn(`Coup invalide : ${currentMove}`);
    }
  };

  const resetGame = () => {
    game.reset(); // méthode native de chess.js
    setPosition(game.fen());
    console.log("Plateau réinitialisé");
  };

  return (
    <div style={{ textAlign: "center", marginTop: 20 }}>
      <h2>♟️ Test ChessGame (sans WebSocket)</h2>
      <Chessboard id="test-board" position={position} boardWidth={400} />
      <div style={{ marginTop: 15 }}>
        <button onClick={playNextMove}>Jouer le prochain coup</button>
        <button onClick={resetGame} style={{ marginLeft: 10 }}>
          🔁 Réinitialiser
        </button>
      </div>
    </div>
  );
};

export default ChessGame;
