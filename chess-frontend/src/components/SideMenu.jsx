export default function SideMenu({ selectedCategory, onSelectCategory }) {
  return (
    <aside className="side-menu">

      {/* Mode section */}
      <div className="menu-section">
        <h3 className="menu-title">Mode</h3>
        <button className="menu-item active">Replay</button>
        <button className="menu-item" disabled>Replay + Stats</button>
      </div>

      {/* Openings categories */}
      <div className="menu-section">
        <h3 className="menu-title">Openings</h3>

        <button
          className={`menu-item ${selectedCategory === "open_games" ? "active" : ""}`}
          onClick={() => onSelectCategory("open_games")}
        >
          Open Games
        </button>

        <button
          className={`menu-item ${selectedCategory === "semi_open_games" ? "active" : ""}`}
          onClick={() => onSelectCategory("semi_open_games")}
        >
          Semi-Open Games
        </button>

        <button
          className={`menu-item ${selectedCategory === "closed_games" ? "active" : ""}`}
          onClick={() => onSelectCategory("closed_games")}
        >
          Closed Games
        </button>

        <button
          className={`menu-item ${selectedCategory === "indian_games" ? "active" : ""}`}
          onClick={() => onSelectCategory("indian_games")}
        >
          Indian Defenses
        </button>

        <button
          className={`menu-item ${selectedCategory === "other_games" ? "active" : ""}`}
          onClick={() => onSelectCategory("other_games")}
        >
          Other Openings
        </button>
      </div>

      {/* Analytics */}
      <div className="menu-section">
        <h3 className="menu-title">Analytics</h3>
        <button className="menu-item" disabled>Top openings</button>
        <button className="menu-item" disabled>Winrate</button>
      </div>

    </aside>
  );
}
