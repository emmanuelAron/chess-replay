import { replayEspagnole } from "../api/openingsApi";

export default function VariantsMenu() {
  return (
    <aside className="sidebar">
      <h3>Ruy Lopez</h3>
      <ul>
        <li onClick={() => replayEspagnole("CHIGORIN")}>Chigorin</li>
        <li onClick={() => replayEspagnole("BREYER")}>Breyer</li>
        <li onClick={() => replayEspagnole("ARKHANGELSK")}>Arkhangelsk</li>
      </ul>
    </aside>
  );
}
