import React, { useState } from "react";
import ChessGame from "./components/ChessGame";
import VariantsMenu from "./components/VariantsMenu";
import MovesList from "./components/MovesList";


function App() {
const [moves, setMoves] = useState([]);
const [resetKey, setResetKey] = useState(0);

const handleVariantSelect = () => {
    setMoves([]);
    setResetKey((k) => k + 1);
  };

  return (
    <div className="app">
      <h1 className="title">Partie en direct</h1>

      <div className="layout">
        <VariantsMenu />
        <main className="board">
          <ChessGame onMove={(move) => setMoves((prev) => [...prev, move])} />
          <MovesList moves={moves} />
        </main>
      </div>
    </div>
  );
}

export default App;