/**
 * Tests WebSocket pour ChessGame.jsx
 */

import { render, screen, act } from "@testing-library/react";
import ChessGame from "../components/ChessGame";
import { Chess } from "chess.js";

class MockWebSocket {
  constructor() {
    MockWebSocket.instances.push(this);
    this.onopen = null;
    this.onmessage = null;
  }
  send() {}
  close() {}
}
MockWebSocket.instances = [];
global.WebSocket = MockWebSocket;

jest.useFakeTimers();

describe("ChessGame (WebSocket)", () => {
  test("reçoit un coup via WS et met à jour la FEN", () => {
    const expected = new Chess();
    expected.move("e4");
    const expectedFen = expected.fen();

    render(<ChessGame />);

    const ws = MockWebSocket.instances[0];

    act(() => {
      ws.onopen && ws.onopen();
      ws.onmessage &&
        ws.onmessage({ data: JSON.stringify({ move: "e4" }) });
    });

    jest.advanceTimersByTime(20);

    expect(screen.getByTestId("fen").textContent).toBe(expectedFen);
  });

  test("reçoit e4, c5, Nf3", () => {
    const expected = new Chess();
    const seq = ["e4", "c5", "Nf3"];
    seq.forEach((mv) => expected.move(mv));
    const expectedFen = expected.fen();

    render(<ChessGame />);

    const ws = MockWebSocket.instances[MockWebSocket.instances.length - 1];

    act(() => {
      ws.onopen && ws.onopen();
      seq.forEach((mv) => {
        ws.onmessage &&
          ws.onmessage({ data: JSON.stringify({ move: mv }) });
      });
    });

    jest.advanceTimersByTime(50);

    expect(screen.getByTestId("fen").textContent).toBe(expectedFen);

    const list = screen.getByTestId("ws-messages");
    expect(list.children.length).toBe(1 + seq.length); // WS connecté + 3 coups
  });
});
