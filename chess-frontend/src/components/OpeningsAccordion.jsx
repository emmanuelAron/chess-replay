/**
 * Accordion component for a single opening.
 *
 * Responsibilities:
 * - Display opening label
 * - Show expand / collapse arrow
 * - Display variations when expanded
 *
 * IMPORTANT:
 * - This component is PURELY presentational
 * - It does NOT load data
 * - It does NOT own open/close logic
 */

export default function OpeningsAccordion({
  opening,
  isOpen,
  onToggle,
  variations,
  onSelectVariation
}) {
  return (
    <div className="mb-4">

      {/* Opening header */}
      <div
        className="cursor-pointer font-semibold flex items-center gap-2"
        onClick={onToggle}
      >
        <span>{isOpen ? "▾" : "▸"}</span>
        <span>{opening.label}</span>
      </div>

      {/* Variations */}
      {isOpen && variations && variations.length > 0 && (
        <div className="ml-6 mt-2 space-y-1">
          {variations.map((variation) => (
            <div
              key={variation.id}
              className="cursor-pointer text-sm text-gray-700 hover:underline flex items-center gap-2"
              onClick={() => onSelectVariation(variation.id)}
            >
              ▶ {variation.name}
            </div>
          ))}
        </div>
      )}

      {/* Optional empty state */}
      {isOpen && (!variations || variations.length === 0) && (
        <div className="ml-6 mt-2 text-sm text-gray-400">
          No variations available
        </div>
      )}

    </div>
  );
}
