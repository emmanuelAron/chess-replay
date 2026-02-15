import React, { useEffect, useRef, useState } from "react";
import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";

const ChessGame = ({ onMove, onWsReady, resetKey, onStat }) => {
  const gameRef = useRef(new Chess());// Persistent chess engine (does NOT trigger re-render)
  const wsRef = useRef(null);// Persistent WebSocket reference
  const [position, setPosition] = useState(gameRef.current.fen());// Board position (this DOES trigger re-render)

  /* --------------------------------------------------
     RESET LOGIC when a new replay starts
     -------------------------------------------------- */
  useEffect(() => {
    console.log("Reset chess engine");
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
      console.log("WebSocket connected");
      onWsReady?.(true);
    };

   ws.onmessage = async (event) => {
     let data ;
     try{
        data = JSON.parse(event.data);
        console.log("WS message received:", data);
     }catch{
        console.warn("Received non-JSON message:", event.data);
         }

     // ================================
     // STAT MESSAGE
     // ================================
     if (data.type === "STAT") {

       onStat?.(data.message);

         setTimeout(() => {
           onStat?.(null);
         }, data.pauseMs);

         return;
     }

     // ================================
     // MOVE MESSAGE
     // ================================
     if (data.type === "MOVE") {
       const sanMove = data.move?.san;

       if (!sanMove) return;

       const result = gameRef.current.move(sanMove, { sloppy: true });

       if (result) {
         setPosition(gameRef.current.fen());
         onMove?.(sanMove);
       }
     }
   };

    ws.onclose = () => {
      console.log("WebSocket closed");
      wsRef.current = null;
      onWsReady?.(false);
    };

    return () => {
      ws.close();
    };
  }, []);

  return (
    <Chessboard position={position} boardWidth={400} />
  );
};

export default ChessGame;

