import { useEffect, useState } from "react";
import OpeningsAccordion from "./OpeningsAccordion";
import { replayEspagnole } from "../api/openingsApi";
//import { API_BASE } from "../api/apiConfig";
import { API_BASE } from "../api/openingsApi";


export default function OpeningsScreen({ selectedCategory, onReplayStart }) {

  // Holds the global openings index (navigation only)
  const [openingsIndex, setOpeningsIndex] = useState(null);

  // ID of the currently opened opening (accordion)
  const [selectedOpeningId, setSelectedOpeningId] = useState(null);

  // Full opening data loaded dynamically from opening.file
  const [openingData, setOpeningData] = useState(null);

  /* -----------------------------------------
     Load openings index once on mount
     ----------------------------------------- */
  useEffect(() => {
    fetch(`${API_BASE}/api/openings/index`)
      .then((res) => res.json())
      .then((data) => {
        setOpeningsIndex(data);
      });
  }, []);

  /* -----------------------------------------
     Reset state when category changes
     ----------------------------------------- */
  useEffect(() => {
    setSelectedOpeningId(null);
    setOpeningData(null);
  }, [selectedCategory]);

  /* -----------------------------------------
     Load opening file when an opening is opened
     ----------------------------------------- */
  useEffect(() => {
    if (!selectedOpeningId || !openingsIndex) return;

    const category = openingsIndex[selectedCategory];
    if (!category) return;

    const openingMeta = category.openings.find(
      (o) => o.id === selectedOpeningId
    );

    if (!openingMeta?.file) return;

    console.log("Loading opening file:", openingMeta.file);

    fetch(`${API_BASE}/api/openings/${openingMeta.file}`)
      .then((res) => res.json())
      .then((data) => {
          console.log("📘 Opening data loaded:", data);
        setOpeningData(data);
      });

  }, [selectedOpeningId, selectedCategory, openingsIndex]);

  /* -----------------------------------------
     Guards
     ----------------------------------------- */
  if (!openingsIndex) {
    return <div>Loading openings…</div>;
  }

  const category = openingsIndex[selectedCategory];
  if (!category) {
    return <div>No category selected</div>;
  }

  /* -----------------------------------------
     Render
     ----------------------------------------- */
  return (
    <div className="opening-panel">
        <h3 className="panel-title">Openings</h3>

      {category.openings.length === 0 && (
        <div>No openings available</div>
      )}

      {category.openings.map((opening) => {
        const isOpen = opening.id === selectedOpeningId;

        return (
          <OpeningsAccordion
            key={opening.id}
            opening={opening}
            isOpen={isOpen}
            onToggle={() =>
              setSelectedOpeningId(isOpen ? null : opening.id)
            }
            variations={
              isOpen && openingData
                ? openingData.variations
                : []
            }
            onSelectVariation={(variationId) =>{
              onReplayStart(); //Reset first
              replayEspagnole(opening.id, variationId)
            }}
          />
        );
      })}

    </div>
  );
}
