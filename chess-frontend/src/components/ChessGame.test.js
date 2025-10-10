import React from "react";
import { render, screen, act , waitFor} from "@testing-library/react";
import ChessGame from "./ChessGame";

/**
 * un test unitaire ChessGame.test.js fonctionnel avec un faux WebSocket (FakeWebSocket).
 *  Il vérifie qu’un message reçu met bien à jour les noms des joueurs dans l’interface (Kasparov vs Karpov).
✅ Il utilise act() pour encapsuler les changements d’état et éviter les erreurs React.
 * 
 */

// === MOCK WEBSOCKET ===
class FakeWebSocket {
  static instances = [];
  constructor() {
    this.onopen = null;
    this.onmessage = null;
    this.onerror = null;
    this.onclose = null;
    FakeWebSocket.instances.push(this);

    // simulate connection opened
    setTimeout(() => {
      this.onopen && this.onopen();
    }, 0);
  }

  send() {}
  close() {}

  triggerMessage(data) {
    this.onmessage && this.onmessage({ data: JSON.stringify(data) });
  }
}

global.WebSocket = FakeWebSocket;

test("affiche les noms des joueurs après un message", async () => {
  render(<ChessGame />);

  const fakeMessage = {
    gameId: "game-0",
    move: "e4",
    moveIndex: 0,
    white: "Kasparov",
    black: "Karpov",
  };

  // simulate WebSocket message inside React's act()
  await act(async () => {
    const socketInstance = FakeWebSocket.instances[0];
    socketInstance.triggerMessage(fakeMessage);
  });

  await waitFor(() => {
    expect(screen.getByText(/Kasparov vs Karpov/i)).toBeInTheDocument();
});
});

test("met à jour l’échiquier après réception d’un coup via WebSocket", async () => {
  render(<ChessGame />);

  // Attendre que WebSocket soit connecté
  await act(async () => {
    await new Promise((resolve) => setTimeout(resolve, 50));
  });

  const fakeMove = {
    gameId: "game-0",
    move: "e4",
    moveIndex: 0,
    white: "Kasparov",
    black: "Karpov",
  };

  await act(async () => {
    const socketInstance = FakeWebSocket.instances[0];
    socketInstance.triggerMessage(fakeMove);
  });

  await waitFor(() => {
    expect(screen.getByText(/Kasparov/i)).toBeInTheDocument();
    expect(screen.getByText(/Karpov/i)).toBeInTheDocument();
  });
});
