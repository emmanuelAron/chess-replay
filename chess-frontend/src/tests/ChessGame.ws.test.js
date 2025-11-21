/**
 * Tests WebSocket du composant ChessGame
 */

import { render, screen, act } from "@testing-library/react";
import ChessGame from "../components/ChessGame";
import { Chess } from "chess.js";

// ------- Mock WebSocket global ---------
global.WebSocket = class {
  constructor() {
    this.onmessage = null;
  }
  send() {}
  // permet d’envoyer un message simulé
  fakeMessage(msg) {
    this.onmessage?.({ data: msg });
  }
};

describe("ChessGame (avec WebSocket)", () => {
  test("joue e4 quand le WS envoie 'e4'", () => {
    const { container } = render(<ChessGame />);

    const mockSocket = new WebSocket();
    global.mockSocket = mockSocket;

    const board = container.querySelector("#test-board");

    const expected = new Chess();
    expected.move("e4");

    // simulate WS message
    act(() => {
      mockSocket.fakeMessage("e4");
    });

    expect(board.getAttribute("data-fen")).toBe(expected.fen());
  });

  test("enchaîne e4 puis c5", () => {
    const { container } = render(<ChessGame />);
    const mockSocket = new WebSocket();
    global.mockSocket = mockSocket;

    const board = container.querySelector("#test-board");

    const expected = new Chess();
    act(() => {
      mockSocket.fakeMessage("e4");
      expected.move("e4");

      mockSocket.fakeMessage("c5");
      expected.move("c5");
    });

    expect(board.getAttribute("data-fen")).toBe(expected.fen());
  });

  test("ignore un coup invalide", () => {
    const { container } = render(<ChessGame />);
    const mockSocket = new WebSocket();
    global.mockSocket = mockSocket;

    const board = container.querySelector("#test-board");
    const initialFen = board.getAttribute("data-fen");

    act(() => {
      mockSocket.fakeMessage("INVALID_MOVE");
    });

    expect(board.getAttribute("data-fen")).toBe(initialFen);
  });
});
