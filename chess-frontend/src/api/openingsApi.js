export const API_BASE = process.env.REACT_APP_API_BASE || "http://localhost:8080";

if (!process.env.REACT_APP_API_BASE) {
  console.warn("REACT_APP_API_BASE is not defined, using localhost");
}


export function replayEspagnole(openingId , variationId) {
   const url = `${API_BASE}/replay/espagnole/${openingId}/${variationId}`;
   console.log("Trigger replay:", url);
  fetch(url)
    .catch((err) => {
      console.warn("Replay trigger failed (ignored):", err);
    });
}
