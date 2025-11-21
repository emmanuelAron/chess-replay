/**
 * Tests unitaires du composant ChessGame (sans WebSocket)
 * Vérifie l'exécution locale des coups : e4, c5, Nf3
 */

import { render, screen, fireEvent } from "@testing-library/react";
import ChessGame from "../components/ChessGame";
import { Chess } from "chess.js";

// Mock du composant react-chessboard pour inspecter la FEN
jest.mock("react-chessboard", () => ({
  Chessboard: ({ position }) => (
    <div data-testid="mock-board">{position}</div>
  )
}));

describe("ChessGame (sans WebSocket)", () => {
  test("joue un coup (e4) et met à jour la FEN", () => {
    render(<ChessGame />);
    const button = screen.getByText("Jouer le prochain coup");

    const board = screen.getByTestId("mock-board");

    // FEN initiale
    const chess = new Chess();
    const initialFen = chess.fen();

    // Vérifie la FEN affichée initiale
    expect(board.textContent).toBe(initialFen);

    // Clique pour jouer e4
    fireEvent.click(button);

    // FEN attendue
    chess.move("e4");
    const expectedFen = chess.fen();

    expect(board.textContent).toBe(expectedFen);
  });

  test("joue 3 coups (e4, c5, Nf3) puis ne joue plus", () => {
    render(<ChessGame />);
    const button = screen.getByText("Jouer le prochain coup");

    const board = screen.getByTestId("mock-board");

    const moves = ["e4", "c5", "Nf3"];
    const expected = new Chess();

    // Joue les 3 coups
    moves.forEach((mv) => {
      fireEvent.click(button);
      expected.move(mv);
    });

    // Vérifie la FEN finale
    expect(board.textContent).toBe(expected.fen());

    // Clic supplémentaire → ne doit rien changer
    const fenBeforeExtra = board.textContent;

    fireEvent.click(button); // pas de 4e coup

    const fenAfterExtra = board.textContent;
    expect(fenAfterExtra).toBe(fenBeforeExtra);
  });

  test("réinitialise le plateau après quelques coups", () => {
    render(<ChessGame />);
    const play = screen.getByText("Jouer le prochain coup");
    const reset = screen.getByText("🔁 Réinitialiser");

    const board = screen.getByTestId("mock-board");

    const initialFen = new Chess().fen();

    // Joue deux coups
    fireEvent.click(play); // e4
    fireEvent.click(play); // c5

    // Reset
    fireEvent.click(reset);

    expect(board.textContent).toBe(initialFen);
  });
});
