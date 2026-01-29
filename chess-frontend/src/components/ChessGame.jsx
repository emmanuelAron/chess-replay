import React, { useEffect, useRef, useState } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";

const ChessGame = ({ onMove, resetKey }) => {
  const gameRef = useRef(new Chess());
  const wsRef = useRef(null);

  const [position, setPosition] = useState(gameRef.current.fen());

  // RESET when variation changes
  useEffect(() => {
    gameRef.current = new Chess();
    setPosition(gameRef.current.fen());
  }, [resetKey]);

  // WebSocket created once
  useEffect(() => {
    if (wsRef.current) return;

    const ws = new WebSocket(process.env.REACT_APP_WS_URL);
    wsRef.current = ws;

    ws.onopen = () => {
      console.log("WebSocket connected");
    };

    ws.onmessage = (event) => {
        console.log("WS message received:", event.data);
      const move = event.data;

      const result = gameRef.current.move(move, { sloppy: true });
      if (result) {
        setPosition(gameRef.current.fen());
        onMove?.(move);
      }
    };

    ws.onclose = () => {
      console.log("WebSocket closed");
      wsRef.current = null;
    };

    return () => {
      ws.close();
    };
  }, []); //Empty dependency

  return <Chessboard position={position} boardWidth={400} />;
};

export default ChessGame;

