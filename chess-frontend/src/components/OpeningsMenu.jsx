import { motion } from "framer-motion";
import { useSwipeable } from "react-swipeable";
import { useState } from "react";

export default function OpeningsMenu({ data, onSelect }) {
  const categories = Object.entries(data);
  const [index, setIndex] = useState(0);

  const handlers = useSwipeable({
    onSwipedLeft: () => setIndex(i => Math.min(i + 1, categories.length - 1)),
    onSwipedRight: () => setIndex(i => Math.max(i - 1, 0)),
    trackMouse: true
  });

  return (
    <div {...handlers} className="overflow-hidden">
      <motion.div
        className="flex"
        animate={{ x: `-${index * 100}%` }}
        transition={{ type: "spring", stiffness: 260, damping: 25 }}
      >
        {categories.map(([key, cat]) => (
          <div
            key={key}
            className="min-w-full flex justify-center items-center cursor-pointer"
            onClick={() => onSelect(key)}
          >
            <h2 className="text-2xl font-bold">{cat.label}</h2>
          </div>
        ))}
      </motion.div>
    </div>
  );
}
