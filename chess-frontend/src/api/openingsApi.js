const API_BASE = process.env.REACT_APP_API_BASE;

if (!API_BASE) {
  console.warn("REACT_APP_API_BASE is not defined");
}

export function replayEspagnole(variationId) {
  fetch(`${API_BASE}/replay/espagnole/${variationId}`)
    .catch((err) => {
      console.warn("Replay trigger failed (ignored):", err);
    });
}
