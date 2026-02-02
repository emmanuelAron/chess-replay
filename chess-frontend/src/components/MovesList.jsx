import React from "react";

export default function MovesList({ moves, compact = false }) {
  // Group moves by move number (1. e4 e5)
  const rows = [];

  for (let i = 0; i < moves.length; i += 2) {
    rows.push({
      moveNumber: i / 2 + 1,
      white: moves[i],
      black: moves[i + 1] || "",
    });
  }

 return (
     <div className={compact ? "moves compact" : "moves"}>
       {rows.map((row) => (
         <div key={row.moveNumber} className="move-row">
           <span className="move-number">{row.moveNumber}.</span>
           <span className="move white">{row.white}</span>
           <span className="move black">{row.black}</span>
         </div>
       ))}
     </div>
   );
}
