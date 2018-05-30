package PJ4;

import java.util.*;


/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. One Pair: one pair of the same card
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */
/* This is the video poker game class.
 * It uses Decks and Card objects to implement video poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */
public class VideoPoker {

    // default constant values
    private static final int startingBalance = 100;
    private static final int numberOfCards = 5;

    // default constant payout value and playerHand types
    private static final int[] multipliers = {1, 2, 3, 5, 6, 10, 25, 50, 1000};
    private static final String[] goodHandTypes = {
        "One Pair", "Two Pairs", "Three of a Kind", "Straight", "Flush	",
        "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"};

    // must use only one deck
    private final Decks oneDeck;

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;
    private int[] frequencyOfCard;
    private int rankCounter;
    private int suitCounter;
    private List<Card> tempHand;// = new ArrayList<Card>();

    /**
     * default constructor, set balance = startingBalance
     */
    public VideoPoker() {
        this(startingBalance);
    }

    /**
     * constructor, set given balance
     */
    public VideoPoker(int balance) {
        this.playerBalance = balance;
        oneDeck = new Decks(1, false);
        frequencyOfCard = new int[14];
    }

    /**
     * This display the payout table based on multipliers and goodHandTypes
     * arrays
     */
    private void showPayoutTable() {
        System.out.println("\n\n");
        System.out.println("Payout Table   	      Multiplier   ");
        System.out.println("=======================================");
        int size = multipliers.length;
        for (int i = size - 1; i >= 0; i--) {
            System.out.println(goodHandTypes[i] + "\t|\t" + multipliers[i]);
        }
        System.out.println("\n\n");
    }

    /**
     * Check current playerHand using multipliers and goodHandTypes arrays Must
     * print yourHandType (default is "Sorry, you lost") at the end of function.
     * This can be checked by testCheckHands() and main() method.
     */
    private void checkHands() {
        // implement this method!
        //sort player hand
        tempHand = new ArrayList<Card>(playerHand);
        Collections.sort(tempHand, new CompareCards());
        for (int i = 0; i < tempHand.size(); i++) {
            frequencyOfCard[tempHand.get(i).getRank()]++;
        }
    }

    /**
     * ***********************************************
     * add new private methods here ....
     *
     ************************************************
     */
    //compare method for rank of cards
    class CompareCards implements Comparator<Card> {

        // overide compare() method
        public int compare(Card o1, Card o2) {
            return o1.getRank() - o2.getRank();
        }
    }

    /**
     * check for flush
     *
     */
    private boolean flush() {
        //compare suits of 1st and 2nd card
        int firstCardSuitNumber = tempHand.get(0).getSuit();
        if (tempHand.get(1).getSuit() == firstCardSuitNumber) {
            if (tempHand.get(2).getSuit() == firstCardSuitNumber) {
                if (tempHand.get(3).getSuit() == firstCardSuitNumber) {
                    if (tempHand.get(4).getSuit() == firstCardSuitNumber) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * check for straight with Ace high
     */
    private boolean aceHighStraight() {
        if (tempHand.get(0).getRank() == 1) {
            if (tempHand.get(1).getRank() == 10) {
                if (tempHand.get(2).getRank() == 11) {
                    if (tempHand.get(3).getRank() == 12) {
                        if (tempHand.get(4).getRank() == 13) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * check for straight
     */
    private boolean straight() {
        boolean checkStraight = true;
        for (int i = 0; i < 4 && checkStraight; i++) {
            if (tempHand.get(i).getRank() == tempHand.get(i + 1).getRank()) {
                checkStraight = false;
            }
        }
        return false;
    }

    /**
     * check for three-of-a-kind
     */
    private boolean threeOfAkind() {
        for (int i = 0; i < 3; i++) {
            if (tempHand.get(i).getRank() == tempHand.get(i + 1).getRank()
                    && tempHand.get(i + 1).getRank() == tempHand.get(i + 2).getRank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * check for four-of-a-kind
     */
    private boolean fourOfAkind() {
        for (int i = 0; i < 2; i++) {
            if (tempHand.get(i).getRank() == tempHand.get(i + 1).getRank()
                    && tempHand.get(i + 1).getRank() == tempHand.get(i + 2).getRank()
                    && tempHand.get(i + 2).getRank() == tempHand.get(i + 3).getRank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * check for royal flush
     */
    private boolean royalFlush() {
        if (aceHighStraight() && flush()) {
            return true;
        }
        return false;
    }

    /**
     * check for single pair
     */
    private boolean singlePair() {
        for (int i = 0; i < 4; i++) {
            if (tempHand.get(i).getRank() == tempHand.get(i + 1).getRank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * check for two pairs
     */
    private boolean twoPairs() {
        for (int i = 0; i < 4; i++) {
            if (tempHand.get(i).getRank() == tempHand.get(i + 1).getRank()
                    && tempHand.get(i + 2).getRank() == tempHand.get(i + 3).getRank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * check for full house
     */
    private boolean fullHouse() {
        for (int i = 0; i < 4; i++) {
            if (threeOfAkind() && twoPairs()) {
                return true;
            }
        }
        return false;
    }
    //end private methods

    public void play() {
        /**
         * The main algorithm for single player poker game
         *
         * Steps: showPayoutTable()
         *
         * ++ show balance, get bet verify bet value, update balance reset deck,
         * shuffle deck, deal cards and display cards ask for positions of cards
         * to replace get positions in one input line update cards check hands,
         * display proper messages update balance if there is a payout if
         * balance = O: end of program else ask if the player wants to play a
         * new game if the answer is "no" : end of program else :
         * showPayoutTable() if user wants to see it goto ++
         */

        // implement this method!
        //create scanner
        boolean retry = false;
        Scanner input = new Scanner(System.in);
        showPayoutTable();
        do {
            System.out.println("-----------------------------------");
            System.out.println("Balance: $" + playerBalance);
            do {
                System.out.print("Enter bet: $");
                playerBet = input.nextInt();
            } while (playerBet < 0 || playerBet > playerBalance);
            playerBalance -= playerBet;
            oneDeck.reset();
            oneDeck.shuffle();
            try {
                playerHand = new ArrayList<Card>(oneDeck.deal(numberOfCards));
                System.out.println("Hand:" + playerHand);
                System.out.print("Enter positions of cards to replace (e.g. 1 4 5 ): ");
                input = new Scanner(System.in);
                String line = input.nextLine();
                String[] strs = line.trim().split("\\s+");
                for (int i = 0; i < strs.length; i++) {
                    try {
                        int replaceMe = Integer.parseInt(strs[i]);
                        if (replaceMe > 0 && replaceMe < 6) {
                            playerHand.set(replaceMe - 1, oneDeck.deal(1).get(0));
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    } catch (PlayingCardException e) {
                        System.out.println("e");
                    }
                }
                System.out.println("Hand:" + playerHand);
                checkHands();
                playerBalance += playerBet;
                if (playerBalance <= 0) {
                    retry = false;
                } else {
                    char ans;
                    System.out.print("Your balance: $" + playerBalance + ", play again (y or n)? ");
                    ans = input.next().charAt(0);
                    if (ans == 'n') {
                        retry = false;
                    } else {
                        retry = true;
                        System.out.print("Show payout table (yes or no)? ");
                        ans = input.next().charAt(0);
                        if (ans == 'y') {
                            showPayoutTable();
                        }
                    }
                }
            } catch (PlayingCardException e) {
                System.out.println(e);
            }
        } while (retry);
        System.out.println("Sorry Game Over!");
    }

/**
 * ***********************************************
 * Do not modify methods below
 * /*************************************************
 *
 * /** testCheckHands() is used to test checkHands() method checkHands() should
 * print your current hand type
 */
public void testCheckHands() {
        try {
            playerHand = new ArrayList<Card>();

            // set Royal Flush
            playerHand.add(new Card(3, 1));
            playerHand.add(new Card(3, 10));
            playerHand.add(new Card(3, 12));
            playerHand.add(new Card(3, 11));
            playerHand.add(new Card(3, 13));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight Flush
            playerHand.set(0, new Card(3, 9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight
            playerHand.set(4, new Card(1, 8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Flush 
            playerHand.set(4, new Card(3, 5));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
            // "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };
            // set Four of a Kind
            playerHand.clear();
            playerHand.add(new Card(4, 8));
            playerHand.add(new Card(1, 8));
            playerHand.add(new Card(4, 12));
            playerHand.add(new Card(2, 8));
            playerHand.add(new Card(3, 8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Three of a Kind
            playerHand.set(4, new Card(4, 11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Full House
            playerHand.set(2, new Card(2, 11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Two Pairs
            playerHand.set(1, new Card(2, 9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set One Pair
            playerHand.set(0, new Card(2, 3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set One Pair
            playerHand.set(2, new Card(4, 3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set no Pair
            playerHand.set(2, new Card(4, 6));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) {
        VideoPoker pokergame = new VideoPoker();
        pokergame.testCheckHands();
    }
}
