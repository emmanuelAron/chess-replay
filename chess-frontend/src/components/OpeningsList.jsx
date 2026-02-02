import { motion } from "framer-motion";

export default function OpeningsList({
  openings,
  selectedOpening,
  onSelectOpening
}) {
  // Safety guard: component renders nothing if data is not ready yet
  if (!openings || openings.length === 0) {
    return <div className="mt-6 text-gray-400">No openings available</div>;
  }

  return (
    <motion.ul
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.3 }}
      className="space-y-4 mt-6"
    >
      {openings.map((opening) => (
        <motion.li
          key={opening.id}
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.96 }}
          className={`p-4 rounded-xl cursor-pointer ${
            selectedOpening === opening.id
              ? "bg-amber-200"
              : "bg-gray-100"
          }`}
          onClick={() => onSelectOpening(opening.id)}
        >
          <div className="font-semibold">{opening.label}</div>

          {opening.eco && (
            <div className="text-sm text-gray-500">
              ECO {opening.eco.join("–")}
            </div>
          )}
        </motion.li>
      ))}
    </motion.ul>
  );
}
