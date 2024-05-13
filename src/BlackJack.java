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

    Random random = new Random();
    ArrayList<Card> deck, dealerHand, playerHand;
    Card card, hiddenCard;
    int borderWidth, borderHeight, dealerSum, playerSum, dealerAceCount, playerAceCount;

    int cardWidth = 110;
    int cardHeight = 154;

    JFrame frame;
    JPanel gamePanel, buttonPanel;
    JButton hitButton, stayButton;

    public BlackJack(int screenSize) {
        borderHeight = borderWidth = screenSize;
        startGame();
    }

    private void startGame() {
        buildWindow();
        buildDeck();
        shuffleDeck();
        startMatch();
        startWindow();

    }

    private void buildWindow() {
        frame = new JFrame("Black Jack");
        gamePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                try {
                    Image hiddenCardImg = new ImageIcon(getClass().getResource("./Cards/Backs/back_0.png")).getImage();
                    if (!stayButton.isEnabled()) {
                        hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                    }
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
                    if (!stayButton.isEnabled()) {
                        dealerSum = reduceDealerAce();
                        playerSum = reducePlayerAce();
                        String message = "";

                        if (playerSum > 21) {
                            message = "You Lose!";
                        } else if (dealerSum > 21 || playerSum > dealerSum) {
                            message = "You Win!";
                        } else if (playerSum == dealerSum) {
                            message = "Tie!";
                        } else {
                            message = "You Lose!";
                        }

                        g.setFont(new Font("Arial", Font.PLAIN, 30));
                        g.setColor(Color.WHITE);
                        g.drawString(message, 220, 250);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        buttonPanel = new JPanel();
        hitButton = new JButton("Hit");
        stayButton = new JButton("Stay");

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

    private void startMatch() {
        dealerHand = new ArrayList<Card>();
        dealerSum = dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount = hiddenCard.isAce() ? 1 : 0;

        card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        playerHand = new ArrayList<Card>();
        playerSum = playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }
    }

    private void startWindow() {
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
            public void actionPerformed(ActionEvent e) {
                card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);

                if (reducePlayerAce() > 21) {
                    hitButton.setEnabled(false);
                }
                gamePanel.repaint();
            }
        });
        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealerSum < 17) {
                    card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                }
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
    }

    private int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

    private int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }
}
