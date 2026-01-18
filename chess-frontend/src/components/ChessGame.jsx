import React, { useEffect, useRef, useState } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";

const ChessGame = () => {
  const [game] = useState(() => new Chess());
  const [position, setPosition] = useState(game.fen());
  const [messages, setMessages] = useState([]);

  const wsRef = useRef(null); // clé de stabilité

  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(() => {
    if (wsRef.current) return; // empêche reconnexion
    console.log("WS_URL =", process.env.REACT_APP_WS_URL);

    const WS_URL = process.env.REACT_APP_WS_URL;
    console.log("Tentative de connexion WebSocket…", WS_URL);
    //const ws = new WebSocket("ws://localhost:8080/chess");//local mode
    const ws = new WebSocket(WS_URL); //production mode

    wsRef.current = ws;

    ws.onopen = () => {
      console.log("WebSocket connecté");
      setMessages((prev) => [...prev, "WS connecté"]);
    };

    ws.onmessage = (event) => {
        console.log("WS message received:", event.data);
      let raw;
      try {
        raw = JSON.parse(event.data);
      } catch {
        raw = event.data;
      }

      const move = typeof raw === "string" ? raw : raw.move;

     // try {
        //const result = game.move(move);
        const result = game.move(move, { sloppy: true });
        if (result) {
          setPosition(game.fen());
          setMessages((prev) => [...prev, `Coup reçu : ${move}`]);
        }else{
            console.warn("Coup non appliqué (sloppy):", move);
            }
     // } catch (e) {
     //   console.warn("Coup ignoré :", move);
     // }
    };

    ws.onerror = () => {
      console.warn("Erreur WebSocket");
    };

    ws.onclose = () => {
      console.warn("WebSocket fermé");
      wsRef.current = null;
    };

    return () => {
      ws.close();
    };
  }, []); // dépendances VIDES

  return (
    <div style={{ textAlign: "center", marginTop: 20 }}>
      <h2>♟️ Test ChessGame (WebSocket)</h2>
      <Chessboard position={position} boardWidth={400} />
      <ul>
        {messages.map((m, i) => (
          <li key={i}>{m}</li>
        ))}
      </ul>
    </div>
  );
};

export default ChessGame;
