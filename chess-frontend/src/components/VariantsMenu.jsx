import { replayEspagnole } from "../api/openingsApi";

export default function VariantsMenu() {
  return (
    <aside className="side-menu">

      {/* MODE SECTION */}
      <div className="menu-section">
        <h3 className="menu-title">Mode</h3>
        <button className="menu-item active">
          Replay
        </button>
        <button className="menu-item" disabled>
          Replay + Stats
        </button>
      </div>

      {/* OPENINGS SECTION */}
      <div className="menu-section">
        <h3 className="menu-title">Ruy Lopez</h3>

        <button
          className="menu-item"
          onClick={() => replayEspagnole("CHIGORIN")}
        >
          Chigorin
        </button>

        <button
          className="menu-item"
          onClick={() => replayEspagnole("BREYER")}
        >
          Breyer
        </button>

        <button
          className="menu-item"
          onClick={() => replayEspagnole("ARKHANGELSK")}
        >
          Arkhangelsk
        </button>
      </div>

      {/* ANALYTICS SECTION (future) */}
      <div className="menu-section">
        <h3 className="menu-title">Analytics</h3>
        <button className="menu-item" disabled>
          Top openings
        </button>
        <button className="menu-item" disabled>
          Winrate
        </button>
      </div>

    </aside>
  );
}
