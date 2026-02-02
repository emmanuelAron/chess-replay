import { motion } from "framer-motion";
import MovesList from "./MovesList";

export default function VariantsList({
  title,
  variations,
  onSelectVariation
}) {
    console.log("VariantsList received variations:", variations);
  return (
    <div className="variants-panel">
      <h3>{title}</h3>

      {variations.map((variation) => (
        <motion.div
          key={variation.id}
          className="variant-card"
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.2 }}
        >
          {/* Variation title */}
          <div className="variant-title">
            {variation.name}
          </div>

          {/* Moves list */}
          <MovesList
            moves={variation.moves}
            compact
          />


          {/* Replay action (event-driven, explicit) */}
         {/*  variation.id = "CHIGORIN", "BREYER", etc. */}
          <button
            className="variant-button"
            onClick={() => onSelectVariation(
                {
                    openingId: variation.openingId,
                    variationId: variation.id
                   })
            }
          >
            ▶ Replay
          </button>
        </motion.div>
      ))}
    </div>
  );
}
