import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {

    /**
     * Card
     */
    private class Card {
    
        String value;
        String type;

        public Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String toString(){
            return type + "_card_" + value;

        }
    }

    ArrayList<Card> deck;

    public BlackJack() {
        startGame();
    }

    private void startGame() {
        buildDeck();
    }

    private void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13"};
        String[] types = {"Clubs", "Diamonds", "Hearts", "Spades"};

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("Deck:");
        System.out.println(deck);
    }
}
