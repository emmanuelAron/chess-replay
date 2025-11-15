import React, { useEffect, useState, useRef } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";
import { fromEvent, interval } from "rxjs";
import { map, concatMap } from "rxjs/operators";

const ChessGame = () => {
  const [game, setGame] = useState(new Chess());
  const [position, setPosition] = useState("start");
  const [moveCount, setMoveCount] = useState(0);
  const [turn, setTurn] = useState("White");
  const [players, setPlayers] = useState({ white: "White", black: "Black" });
  const currentGameId = useRef(null);

  useEffect(() => {
    const socket = new WebSocket("ws://localhost:8080/chess-stream");

    const moves$ = fromEvent(socket, "message").pipe(
      map((event) => JSON.parse(event.data)),
      // 👇 traite chaque coup avec un délai de 1s
      concatMap((moveData) => interval(1000).pipe(map(() => moveData)))
    );

    const subscription = moves$.subscribe((data) => {
      const { move, moveIndex, gameId, white, black } = data;

      // 🔄 Nouvelle partie détectée
      if (moveIndex === 0 || gameId !== currentGameId.current) {
        console.log("🔄 Nouvelle partie détectée :", gameId);
        const newGame = new Chess();
        setGame(newGame);
        setPosition("start");
        currentGameId.current = gameId;
        setPlayers({ white, black });
        setMoveCount(0);
        setTurn("White");
        return;
      }

      try {
        const newGame = new Chess(game.fen());
        const result = newGame.move(move);

        if (result) {
          setGame(newGame);
          setPosition(newGame.fen());
          setMoveCount(moveIndex + 1);
          setTurn(newGame.turn() === "w" ? "White" : "Black");
          console.log("✅ Coup appliqué :", move);
        } else {
          console.warn("⛔ Coup invalide :", move);
        }
      } catch (err) {
        console.error("❌ Erreur coup :", move, err);
      }
    });

    socket.onopen = () => console.log("✅ WebSocket connecté (rxjs)");
    socket.onclose = () => console.warn("⚠️ WebSocket fermé");
    socket.onerror = (err) => console.error("❌ Erreur WS:", err);

    return () => {
      console.log("🔌 Fermeture du socket proprement");
      subscription.unsubscribe();
      socket.close();
    };
  }, []);

  return (
    <div style={{ width: 400, textAlign: "center" }}>
      <h2>Partie en direct</h2>
      <h4>{players.white} vs {players.black}</h4>

      <Chessboard position={position} arePiecesDraggable={false} />

      <div style={{ marginTop: 10, fontWeight: "bold" }}>
        ♟️ Coup {moveCount} — Trait aux {turn === "White" ? "Blancs" : "Noirs"}
      </div>
    </div>
  );
};

export default ChessGame;
