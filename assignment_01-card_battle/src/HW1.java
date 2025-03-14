/* HW1. Battle
 * This file contains two classes :
 * 		- Deck represents a pack of cards,
 * 		- Battle represents a battle game.
 */

import java.util.LinkedList;

class Deck { // represents a pack of cards

	LinkedList<Integer> cards;
	// The methods toString, hashCode, equals, and copy are used for
	// display and testing, you should not modify them.

	@Override
	public String toString() {
		return cards.toString();
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		Deck d = (Deck) o;
		return cards.equals(d.cards);
	}

	Deck copy() {
		Deck d = new Deck();
		for (Integer card : this.cards)
			d.cards.addLast(card);
		return d;
	}

	// constructor of an empty deck
	Deck() {
		cards = new LinkedList<Integer>();
	}

	// constructor from field
	Deck(LinkedList<Integer> cards) {
		this.cards = cards;
	}

	// constructor of a complete sorted deck of cards with nbVals values
	Deck(int nbVals) {
		cards = new LinkedList<Integer>();
		for (int j = 1; j <= nbVals; j++)
			for (int i = 0; i < 4; i++)
				cards.add(j);
	}

	// Question 1

	// takes a card from deck d to put it at the end of the current packet
	int pick(Deck d) {
		// throw new Error("Method pick(Deck d) to complete (Question 1)");
		if (!d.cards.isEmpty()) {
			int x = d.cards.removeFirst();
			cards.addLast(x);
			return x;
		} else {
			return -1;
		}
	}

	// takes all the cards from deck d to put them at the end of the current deck
	void pickAll(Deck d) {
		// throw new Error("Method pickAll(Deck d) to complete (Question 1)");
		while (!d.cards.isEmpty()) {
			pick(d);
		}
	}

	// checks if the current packet is valid
	boolean isValid(int nbVals) {
		// throw new Error("Method isValid(int nbVals) to complete (Question 1)");
		int[] numbers = new int[nbVals];
		for (Integer x : cards) {
			if (x < 1 || x > nbVals || numbers[x - 1] > 3)
				return false;
			numbers[x - 1]++;
		}
		return true;
	}

	// Question 2.1

	// chooses a position for the cut
	int cut() {
		// throw new Error("Method cut() to complete (Question 2.1)");
		int size = cards.size();
		int count = 0;

		for (int i = 0; i < size; i++) {
			if (Math.random() < 0.5) {
				count++;
			}
		}

		return count;
	}

	// cuts the current packet in two at the position given by cut()
	Deck split() {
		// throw new Error("Method split() to complete (Question 2.1)");
		int cutPosition = cut();
		Deck res = new Deck();

		if (cutPosition == 0 || cutPosition >= cards.size()) {
			return res;
		}

		// move the first cutPosition card to the result deck
		for (int i = 0; i < cutPosition; i++) {
			res.cards.addLast(cards.removeFirst());
		}
		return res;
	}

	// Question 2.2

	// mixes the current deck and the deck d
	void riffleWith(Deck d) {
		// throw new Error("Method riffleWith(Deck d) to complete (Question 2.2)");
		Deck res = new Deck();

		while (!d.cards.isEmpty() || !cards.isEmpty()) {
			// calculate probability
			double probFirstDeck = 0;
			if (!cards.isEmpty()) {
				probFirstDeck = (double) cards.size() / (cards.size() + d.cards.size());
			}

			// select a card from first or second deck based on probability
			// if probability less than P(first deck) , we will pick first deck
			if (!cards.isEmpty() && (d.cards.isEmpty() || Math.random() < probFirstDeck)) {
				res.pick(this);
			} else if (!d.cards.isEmpty()) {
				res.pick(d);
			}
		}

		this.cards = res.cards;
	}

	// Question 2.3

	// shuffles the current deck using the riffle shuffle
	void riffleShuffle(int m) {
		// throw new Error("Method riffleShuffle(int m) to complete (Question 2.3)");
		for (int i = 0; i < m; i++) {
			Deck splitDeck = split();
			riffleWith(splitDeck);
		}
	}
}

class Battle { // represents a battle game

	Deck player1;
	Deck player2;
	Deck trick;

	// constructor of a battle without cards
	Battle() {
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
	}

	// constructor from fields
	Battle(Deck player1, Deck player2, Deck trick) {
		this.player1 = player1;
		this.player2 = player2;
		this.trick = trick;
	}

	// copy the battle
	Battle copy() {
		Battle r = new Battle();
		r.player1 = this.player1.copy();
		r.player2 = this.player2.copy();
		r.trick = this.trick.copy();
		return r;
	}

	// string representing the battle
	@Override
	public String toString() {
		return "Player 1 : " + player1.toString() + "\n" + "Player 2 : " + player2.toString() + "\nPli "
				+ trick.toString();
	}

	// Question 3.1

	// constructor of a battle with a deck of cards of nbVals values
	Battle(int nbVals) {
		// throw new Error("Constructor Battle() to complete (Question 3.1)");
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();

		Deck fulldeck = new Deck(nbVals);
		fulldeck.riffleShuffle(7);

		// distribute this deck to the two players
		boolean player1Turn = true; // player1 get first
		while (!fulldeck.cards.isEmpty()) {
			if (player1Turn) {
				player1.pick(fulldeck);
			} else {
				player2.pick(fulldeck);
			}
			player1Turn = !player1Turn;
		}
	}

	// Question 3.2

	// test if the game is over
	boolean isOver() {
		// throw new Error("Method isOver() to complete (Question 3.2)");
		return player1.cards.isEmpty() || player2.cards.isEmpty();
	}

	// effectue un tour de jeu
	boolean oneRound() {
		// If either player has no cards, the game is over
		if (player1.cards.isEmpty() || player2.cards.isEmpty()) {
			return false;
		}
		
		// Each player draws a card for comparison
		int card1 = player1.cards.removeFirst();
		trick.cards.addLast(card1);
		
		if (player2.cards.isEmpty()) {
			player1.pickAll(trick);
			return true;
		}
		
		int card2 = player2.cards.removeFirst();
		trick.cards.addLast(card2);
		
		// Compare cards and handle battles
		while (card1 == card2) {
			// Both players need to place one card face down
			if (player1.cards.isEmpty() || player2.cards.isEmpty()) {
				// If either player can't continue the battle
				if (player1.cards.size() > player2.cards.size()) {
					player1.pickAll(trick);
				} else if (player2.cards.size() > player1.cards.size()) {
					player2.pickAll(trick);
				}
				return false;
			}
			
			// Place cards face down
			trick.cards.addLast(player1.cards.removeFirst());
			trick.cards.addLast(player2.cards.removeFirst());
			
			// Then place cards for comparison
			if (player1.cards.isEmpty() || player2.cards.isEmpty()) {
				// If either player can't continue
				if (player1.cards.isEmpty() && !player2.cards.isEmpty()) {
					player2.pickAll(trick);
				} else if (!player1.cards.isEmpty() && player2.cards.isEmpty()) {
					player1.pickAll(trick);
				}
				return false;
			}
			
			// Draw new cards for comparison
			card1 = player1.cards.removeFirst();
			trick.cards.addLast(card1);
			
			card2 = player2.cards.removeFirst();
			trick.cards.addLast(card2);
		}
		
		// Give trick to winner
		if (card1 > card2) {
			player1.pickAll(trick);
		} else {
			player2.pickAll(trick);
		}
		
		return true;
	}

	// Question 3.3

	// returns the winner
	int winner() {
		// throw new Error("Method winner() to complete (Question 3.3)");
		int p1CardSize = player1.cards.size();
		int p2CardSize = player2.cards.size();

		if (p1CardSize > p2CardSize) {
			return 1;
		} else if (p1CardSize < p2CardSize) {
			return 2;
		} else {
			return 0;
		}
	}

	// plays a game with a fixed maximum number of moves
	int game(int turns) {
		// throw new Error("Method game(int turns) to complete (Question 3.3)");
		for (int i = 0; i < turns; i++) {
			if(!oneRound()){ 
				break;
			}
		}

		return winner();
	}

	// Question 4.1

	// plays a game without limit of moves, but with detection of infinite games
	int game() {
		// throw new Error("Method game() to complete (Question 4.1)");
		// Create a clone for the "tortoise" game
		Battle tortoise = this.copy();
		
		int hareSteps = 0;
		int tortoiseSteps = 0;
		
		while (true) {
			// Move the hare two steps
			for (int i = 0; i < 2; i++) {
				if (!oneRound()) {
					return winner();
				}
				hareSteps+=1;
			}
			
			// move tortoise one step
			if (!tortoise.oneRound()) {
				return tortoise.winner();
			}
			tortoiseSteps+=1;
			
			// cycle detected
			if (this.toString().equals(tortoise.toString())) {
				return 3; 
			}
		}

	}

	// Question 4.2
	// performs statistics on the number of infinite games
	static void stats(int nbVals, int nbGames) {
		// throw new Error("Method stats(int bvVals, int nb_of_games) to complete (Question 4.2)");
		int player1Wins = 0;
		int player2Wins = 0;
		int draws = 0;
		int infiniteGames = 0;
		
		for (int i = 0; i < nbGames; i++) {
			Battle battle = new Battle(nbVals);
			int result = battle.game();
			
			switch (result) {
				case 0:
					draws++;
					break;
				case 1:
					player1Wins++;
					break;
				case 2:
					player2Wins++;
					break;
				case 3:
					infiniteGames++;
					break;
			}
		}
		
		System.out.println("Statistics for " + nbGames + " games with " + nbVals + " values:");
		System.out.println("Player 1 wins: " + player1Wins + " (" + (100.0 * player1Wins / nbGames) + "%)");
		System.out.println("Player 2 wins: " + player2Wins + " (" + (100.0 * player2Wins / nbGames) + "%)");
		System.out.println("Draws: " + draws + " (" + (100.0 * draws / nbGames) + "%)");
		System.out.println("Infinite games: " + infiniteGames + " (" + (100.0 * infiniteGames / nbGames) + "%)");
	}
}
