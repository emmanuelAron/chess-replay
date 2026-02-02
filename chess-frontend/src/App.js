import React, { useState } from "react";
import ChessGame from "./components/ChessGame";
import OpeningsScreen from "./components/OpeningsScreen";
import MovesList from "./components/MovesList";
import SideMenu from "./components/SideMenu";


function App() {
// Global navigation state: selected opening category
  const [selectedCategory, setSelectedCategory] = useState("open_games");
  const [moves, setMoves] = useState([]);
  const [replayKey, setReplayKey] = useState(0);
  const [isWsReady, setIsWsReady] = useState(false);//Websocket state for managing concurrency problems


  // RESET LOGIC
  const handleReplay = () => {
    setMoves([]);                // reset move list
    setReplayKey((k) => k + 1);  // trigger chess reset (not remount)
  };


  return (
    <div className="app">
      <h1 className="title">Game Replay with Opening Insights</h1>
      <p className="subtitle">created by Emmanuel ARON-SAMUEL</p>

      <div className="layout">

         {/* Left side menu: global navigation (categories, mode, analytics) */}
                  <SideMenu
                    selectedCategory={selectedCategory}
                    onSelectCategory={setSelectedCategory}
                  />

        <main className="game-area">
        {/* Openings & variations (depends on selected category) */}
        <OpeningsScreen
            selectedCategory={selectedCategory}
            onReplayStart={handleReplay}
        />

          {/* wooden style Chess board */}
          <div className="board-frame">
            <div className="board-inner">
              <ChessGame
                resetKey={replayKey}
                onWsReady={setIsWsReady}
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