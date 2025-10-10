import { render, screen } from '@testing-library/react';
import App from './App';

test('affiche tous les titres "Partie en direct"', () => {
  render(<App />);
  const titles = screen.getAllByText(/Partie en direct/i);
  expect(titles).toHaveLength(2); // h1 et h2
});

