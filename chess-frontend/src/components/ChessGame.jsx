import React, { useEffect, useState } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";

const ChessGame = () => {
  const [game] = useState(() => new Chess());
  const [position, setPosition] = useState(game.fen());
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const ws = new WebSocket("ws://localhost:8080/chess");

    ws.onopen = () => {
      console.log("WebSocket connecté");
      setMessages((prev) => [...prev, "WS connecté"]);
    };

    ws.onmessage = (event) => {
      let raw;

      try {
        raw = JSON.parse(event.data);       // { move: "e4" }
      } catch (_) {
        raw = event.data;                   // "e4"
      }

      console.log("WS → Coup reçu :", raw);

      // extraction du coup sous forme string
      const move = typeof raw === "string" ? raw : raw.move;

      const result = game.move(move);
      if (result) {
        console.log("Coup appliqué :", move);
        setPosition(game.fen());
        setMessages((prev) => [...prev, `Coup reçu : ${move}`]);
      } else {
        console.warn("Coup invalide :", move);
      }
    };

    return () => ws.close();
  }, [game]);

  return (
    <div style={{ textAlign: "center", marginTop: 20 }}>
      <h2>♟️ Test ChessGame (WebSocket)</h2>

      <Chessboard id="test-board" position={position} boardWidth={400} />

      <div data-testid="fen">{position}</div>

      <ul data-testid="ws-messages">
        {messages.map((msg, index) => (
          <li key={index}>{msg}</li>
        ))}
      </ul>
    </div>
  );
};

export default ChessGame;
