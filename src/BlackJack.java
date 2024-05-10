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

        public String toString() {
            return type + "_card_" + value;

        }

        public int getValue() {
            if (value.equals("1")) {
                return 11;
            } else if (value.equals("11") || value.equals("12") || value.equals("13")) {
                return 10;
            }
            return Integer.parseInt(value);
        }

        public boolean isAce() {
            return value.equals("1");
        }

        public String getImagePath() {
            return "./Cards/" + type + "/" + toString() + ".png";
        }
    }

    Card card;
    ArrayList<Card> deck;
    Random random = new Random();

    ArrayList<Card> dealerHand;
    Card hiddenCard;
    int dealerSum;
    int dealerAceCount;

    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    int borderWidth = 600;
    int borderHeight = borderWidth;

    int cardWidth = 110;
    int cardHeight = 154;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./Cards/Backs/back_0.png")).getImage();
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                for (int i = 0; i < dealerHand.size(); i++) {
                    card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 25 + cardWidth + (cardWidth + 5) * i, 20, cardWidth, cardHeight, null);
                }

                for (int i = 0; i < playerHand.size(); i++) {
                    card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5) * i, borderHeight - cardHeight - 90, cardWidth,
                            cardHeight, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");

    public BlackJack() {
        startGame();

        frame.setSize(borderWidth, borderHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
    }

    private void startGame() {
        buildDeck();
        shuffleDeck();

        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount = hiddenCard.isAce() ? 1 : 0;

        card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }

    }

    private void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13" };
        String[] types = { "Clubs", "Diamonds", "Hearts", "Spades" };

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }
    }

    private void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currentCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currentCard);
        }
    }
}
