# Chess Replay – TODO

## 🧱 UI / Layout

- [ ] Implement a left-side navigation menu (Flexbox)
    - [ ] "Mode" section
        - [ ] Simple replay
        - [ ] Replay + analytics
    - [ ] "Openings" section
        - [ ] Ruy Lopez
        - [ ] Chigorin
        - [ ] Breyer
        - [ ] Arkhangelsk
    - [ ] "Analytics" section (placeholder)
        - [ ] Top openings
        - [ ] Winrate
        - [ ] Game length

- [ ] Connect menu to React state (active mode / active opening)
- [ ] Highlight selected menu item (active state)
- [ ] Make menu sections collapsible on mobile screens

---

## ♟️ Chess Replay (Frontend)

- [ ] Sync move list with the chessboard
    - [ ] Click on a move → jump to corresponding position
- [ ] Add automatic playback mode (play / pause)
- [ ] Handle replay speed (x1 / x2 / x4)
- [ ] Clean board reset when switching opening

---

## 🌐 Backend / Communication

- [x] Receive moves via WebSocket
- [ ] Split replay modes:
    - [ ] "Simple replay" mode (direct WebSocket)
    - [ ] "Kafka replay" mode (via backend)
- [ ] Prepare backend endpoint for Kafka-based replay

---

## 🔄 Kafka (Historical Replay)

- [ ] Kafka producer: send moves (1 message = 1 move)
- [ ] Topic `chess.moves`
- [ ] Backend consumer (Spring Boot)
- [ ] Forward moves to frontend via WebSocket
- [ ] Handle play / pause on backend side

---

## 📊 Analytics (Spark / Mongo)

- [x] Spark Streaming: opening statistics computation
- [x] Persist analytics data in MongoDB
- [ ] Expose analytics via REST API
- [ ] Display analytics in the frontend
- [ ] Link analytics with replay (opening detection)

---

## 🧼 Quality / Tech

- [ ] CSS cleanup (remove dead code)
- [ ] README documentation (global architecture)
- [ ] Architecture diagram (Kafka / Spark / WebSocket)
- [ ] Prepare a reproducible demo

