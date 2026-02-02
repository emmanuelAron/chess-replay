import React, { useEffect, useRef, useState } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";

const ChessGame = ({ onMove, onWsReady, resetKey }) => {
  // Persistent chess engine (does NOT trigger re-render)
  const gameRef = useRef(new Chess());

  // Persistent WebSocket reference
  const wsRef = useRef(null);

  // Board position (this DOES trigger re-render)
  const [position, setPosition] = useState(gameRef.current.fen());

  /* --------------------------------------------------
     RESET LOGIC when a new replay starts
     -------------------------------------------------- */
  useEffect(() => {
    console.log("♻️ Reset chess engine");
    gameRef.current = new Chess();              // reset logic
    setPosition(gameRef.current.fen());         // reset UI
  }, [resetKey]);

  /* --------------------------------------------------
     WebSocket lifecycle (created ONCE)
     -------------------------------------------------- */
  useEffect(() => {
    if (wsRef.current) return; // already connected

    const ws = new WebSocket(process.env.REACT_APP_WS_URL);
    wsRef.current = ws;

    ws.onopen = () => {
      console.log("🟢 WebSocket connected");
      onWsReady?.(true);
    };

    ws.onmessage = (event) => {
      const move = event.data;
      console.log("WS message received:", move);

      const result = gameRef.current.move(move, { sloppy: true });
      if (result) {
        setPosition(gameRef.current.fen());
        onMove?.(move);
      }
    };

    ws.onclose = () => {
      console.log("🔴 WebSocket closed");
      wsRef.current = null;
      onWsReady?.(false);
    };

    return () => {
      ws.close();
    };
  }, []);

  return (
    <Chessboard
      position={position}
      boardWidth={400}
    />
  );
};

export default ChessGame;

