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
      <h1 className="title">Game Replay with Opening Insights</h1>

      <div className="layout">
        <VariantsMenu />

        <main className="game-area">

          {/* wooden style */}
          <div className="board-frame">
            <div className="board-inner">
              <ChessGame
                onMove={(move) => setMoves((prev) => [...prev, move])}
              />
            </div>
          </div>

          {/* move list */}
          <div className="moves-panel">
            <MovesList moves={moves} />
          </div>

        </main>
      </div>
    </div>
  );





}

export default App;