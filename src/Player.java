import java.util.ArrayList;
import java.util.Scanner;

/**
 * Player class stores the team, player type, name , and curent hand of each player.
 * Contains methods for hand management, accessor functions for instance variables,
 * and AI functions for cpu to make decisions on card choices / trump calling
 */
public class Player {

    private char type;
    private char team;
    private String name;

    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Card> eligible = new ArrayList<>();

    /**
     * initializes player at start of game
     * @param t type of player (human or computer)
     * @param te team player is on (orange of blue)
     * @param n name of player
     */
    Player(char t, char te, String n){
        type = t;
        team = te;
        name = n;
    }

    /**
     * @getter
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * @getter
     * @return player type
     */
    public char getType() {
        return type;
    }

    /**
     * make player remove a card from their hand, called when the dealer picks up faceUp card,
     * as a result of a first round trump card
     */
    public void removeCard(char trump) {
        if(type == 'h'){
            humanRemoveCard();
        } else{
            cpuRemoveCard(trump);
        }
    }

    /**
     * make cpu discard a card after picking up faceUp card
     * @param trump the trump suit
     */
    private void cpuRemoveCard(char trump) {
        hand.remove(bestDiscard(trump));
    }

    /**
     * AI hub function for CPU to decide which card to discard
     * @param trump the trump suit
     * @return the card to discard
     */
    private Card bestDiscard(char trump) {
        Card worst = hand.get(0);
        for(Card c : hand){
            if(!c.discardCompare(worst, trump)){
                worst = c;
            }
        }
        return worst;
    }

    /**
     * makes human player remove card after picking up faceUp card
     */
    private void humanRemoveCard() {
        printHand();
        System.out.println("Which card would you like to discard?");
        boolean valid = false;
        int choice = 0;
        while (!valid){
            printHand();
            System.out.println("Choose card to play: ");
            Scanner scan = new Scanner(System.in);
            while (!scan.hasNextInt()){
                scan.next();
                System.out.println("enter a number!");
            }
            choice = scan.nextInt() - 1;
            if(choice < hand.size() && choice > -1) {
                hand.remove(choice);
                valid = true;
            } else{
                System.out.println("invalid number!");
            }
        }
    }

    /**
     * deal a card to a player
     * @param faceUp card that will be added to hand
     */
    public void addCard(Card faceUp) {
        hand.add(faceUp);
    }


    /**
     * hub function that determines what card a player will play.
     * Diverges into seperate functions for human and computer players
     * @param lead the lead suit
     * @param order what position player is playing from
     * @param trick cards that have been played earlier in the trick
     * @param trump the trump suit
     * @param played cards that have been played earlier in the round
     * @return the card that is being played
     * @throws InterruptedException
     */
    public Card playCard(char lead, int order, ArrayList<Card> trick, char trump, ArrayList<Card> played) throws InterruptedException {
        if(type == 'h'){
            return humanPlay(lead, trump);
        }
        return computerPlay(lead, order, trick, trump, played);
    }

    /**
     * AI method that determines which card CPU will choose to play
     * @param lead the lead suit
     * @param order what position player is playing from
     * @param trick cards that have been played earlier in the trick
     * @param trump the trump suit
     * @param played cards that have been played earlier in the round
     * @return the card to be played
     * @throws InterruptedException
     */
    private Card computerPlay(char lead, int order, ArrayList<Card> trick, char trump, ArrayList<Card> played) throws InterruptedException {
        eligible.clear();

        if(order == 0){
            eligible.addAll(hand);
        } else {
            eligible = getEligible(hand, lead, trump);
        }

        Card play;
        if(eligible.size() == 1){
            play = eligible.remove(0);
        } else {
            if (order == 0) {
                play = computerLeadOff(lead, trump, played);
            } else if (order == 1) {
                play = computerSecondPlay(lead, trick, trump, played);
            } else if (order == 2) {
                play = computerThirdPlay(lead, trick, trump, played);
            } else {
                play = computerFourthPlay(lead, trick, trump);
            }
        }

        int index = 0;
        for(int i = 0; i < hand.size(); i++){
            if(hand.get(i) == play){
                index = i;
            }
        }
        return hand.remove(index);

    }

    /**
     * method for CPU to decided which card to play as the last card of the trick
     * @param lead the lead suit
     * @param trick the cards that have been played so far
     * @param trump the trump suit
     * @return the card to be played
     */
    private Card computerFourthPlay(char lead, ArrayList<Card> trick, char trump) {
        if (!isWinning(lead, trick, trump) && canWin(lead, trick, trump)){
            return worstWinner(lead, trick, trump);
        }
        return getWorst(eligible, trump, lead);
    }

    /**
     * determines what card a CPU will play from the third spot
     * @param lead the lead suit
     * @param trick cards played so far
     * @param trump the trump suit
     * @param played cards played earlier in the round
     * @return the card to be played
     */
    private Card computerThirdPlay(char lead, ArrayList<Card> trick, char trump, ArrayList<Card> played) {
        // if you can't win, play worst card
        if(!canWin(lead, trick, trump)){
            return getWorst(eligible, trump, lead);
        }

        //if your partner is winning, top them only with:
        // 1. an off-suit Ace
        // 2. a 9/10/Q of trump
        if(isWinning(lead, trick, trump)){
            Card leadCard = getLeader(trick, trump, lead);
            if(leadCard.isLead(lead, trump) && !leadCard.isAceOff(trump) && aceLead(lead, trick, lead) != null){
                return aceLead(lead, trick, lead);
            }
            if(leadCard.isLead(lead, trump) && !leadCard.isAceOff(trump) && breakLead(lead, trick, trump) != null){
                char s = breakLead(lead, trick, trump).getRank();
                if(s == 'T' || s == '9' || s == 'Q') {
                    return breakLead(lead, trick, trump);
                }
            }
            return getWorst(eligible, trump, lead);
        }

        Card potential;

        //if partner is losing, there are 3 possible plays:
        // 1. Ace of Lead Suit
        // 2. Break Lead Suit with Trump
        // 3. Play the Toppper
        potential = aceLead(lead, trick, trump);
        if(potential != null){
            return potential;
        }

        potential = breakLead(lead, trick, trump);
        if(potential != null){
            return potential;
        }

        potential = topper(trump, played);
        if(potential!= null){
            return potential;
        }

        //if none of these options exist, play worst card
        return getWorst(eligible, trump, lead);
    }

    private Card breakLead(char lead, ArrayList<Card> trick, char trump) {
        ArrayList<Card> leadBreakers = new ArrayList<>();
        if(trick.get(0).isTrump(trump)){
            return null;
        }

        Card leader = getLeader(trick, trump, lead);
        for(Card c : eligible){
            if(c.isTrump(trump) && c.compareCards(leader, trump, lead)){
                leadBreakers.add(c);
            }
        }
        if(leadBreakers.isEmpty()){
            return null;
        }
        return getWorst(leadBreakers, trump, lead);
    }


    /**
     * determines what card the CPU will play from the second spot
     * @param lead the lead suit
     * @param trick cards played so far
     * @param trump the trump suit
     * @param played cards played earlier in the round
     * @return the card to be played
     */
    private Card computerSecondPlay(char lead, ArrayList<Card> trick, char trump, ArrayList<Card> played) {
        //if you can't win, play worst card
        if(!canWin(lead, trick, trump)){
            return getWorst(eligible, trump, lead);
        }
        Card potential;

        //if you can win, 3 option to consider:
        // 1. play ace of lead suit
        // 2. play low trump to break lead
        // 3. play topper
        potential = aceLead(lead, trick, trump);
        if(potential != null){
            return potential;
        }
        potential = breakLead(lead, trick, trump);
        if(potential != null){
            if(potential.getSuit() == 'T' || potential.getSuit() == '9' || potential.getSuit() == 'Q') {
                return potential;
            }
        }
        potential = topper(trump, played);
        if(potential!= null){
            return potential;
        }

        //if these options don't exist, play worst
        return getWorst(eligible, trump, lead);
    }


    /**
     * determines what card the CPU will play from the lead off spot
     * @param lead the lead suit
     * @param trump the trump suit
     * @param played cards played earlier in the round
     * @return the card to be played
     */
    private Card computerLeadOff(char lead, char trump, ArrayList<Card> played) {
        Card potential = topper(trump, played);
        if(potential != null){
            return potential;
        }
        potential = aceOff(trump);
        if(potential != null){
            return potential;
        }
        return getWorst(hand, trump, lead);
    }

    private Card aceLead(char lead, ArrayList<Card> trick, char trump) {
        Card ace = null;
        for(Card c: eligible){
            if(c.getRank() == 'A' && c.getSuit() == lead){
                ace = c;
            }
        }
        if(ace != null && ace.compareCards(getLeader(trick, trump, lead), trump, lead)){
            return ace;
        }
        return null;
    }

    /**
     * determines whether the current player's partner is leading the trick
     * @param lead the lead suit
     * @param trick the cards that have been played so far
     * @param trump the trump suit
     * @return true if the current player's partner is leading
     */
    private boolean isWinning(char lead, ArrayList<Card> trick, char trump) {

        Card leader = getLeader(trick, trump, lead);
        int i = 0;
        int order = 0;
        for(Card c : trick){
            if(c == leader){
                order = i;
            }
            i++;
        }

        if(trick.size() == 2){
            return order == 0;
        } else {
            return order == 1;
        }
    }

    /**
     * determines which cards in a player's hand are eligible to be played
     * a player must follow the lead suit if able
     * @param hand a player's hand
     * @param lead the lead suit
     * @return the cards that are eligible to be played
     */
    private ArrayList<Card> getEligible(ArrayList<Card> hand, char lead, char trump) {
        ArrayList<Card> eligible = new ArrayList<>();
        for(Card c: hand){
            if(c.isLead(lead, trump)){
                eligible.add(c);
            }
        }
        if(eligible.isEmpty()){
            eligible.addAll(hand);
        }
        return eligible;
    }

    /**
     * determines the worst card a player can play that would give them the lead in the trick
     * @param lead the lead suit
     * @param trick cards played so far
     * @param trump the trump suit
     * @return the worst card that can be played to take the lead
     */
    private Card worstWinner(char lead, ArrayList<Card> trick, char trump) {
        ArrayList<Card> winners = new ArrayList<>();
        Card leader = getLeader(trick, trump, lead);
        for(Card c: eligible){
            if(c.compareCards(leader, trump, lead)){
                winners.add(c);
            }
        }
        return getWorst(winners, trump, lead);
    }

    /**
     * determines the worst card out of a group of cards
     * @param options a group of cards
     * @param trump the trump suit
     * @param lead the lead suit
     * @return the worst card in winners
     */
    private Card getWorst(ArrayList<Card> options, char trump, char lead) {
        if(loner(options, trump) != null){
            return loner(options, trump);
        }

        Card loser = options.get(0);
        for(Card c : options){
            if(!c.compareCards(loser, trump, lead)){
                loser = c;
            }
        }
        return loser;
    }

    private Card loner(ArrayList<Card> eligible, char trump) {
        for(Card c: eligible){
            boolean pair = false;
            ArrayList<Card> others = new ArrayList<>();
            others.addAll(eligible);
            others.remove(c);
            for(Card o: others){
                if(c.isSameSuit(o, trump)){
                    pair = true;
                }
            }
            if(pair == false && c.getRank() != 'K' && c.getRank() == 'A' && !c.isTrump(trump)){
                return c;
            }
        }
        return null;
    }

    /**
     * determines whether or not it is possible for a player to play a card that would take the lead
     * @param lead the lead suit
     * @param trick cards played so far
     * @param trump the trump suit
     * @return true if the player has a card that can take the lead, else false
     */
    private boolean canWin(char lead, ArrayList<Card> trick, char trump) {
        Card leader = getLeader(trick, trump, lead);
        for(Card c : eligible){
            if(c.compareCards(leader, trump, lead)) {
                return true;
            }
        }
        return false;
    }

    /**
     * determines the card that is currently winning the trick
     * @param trick cards played so far
     * @param trump the trump suit
     * @param lead the lead suit
     * @return the card currently leading the trick
     */
    private Card getLeader(ArrayList<Card> trick, char trump, char lead) {
        Card leader = trick.get(0);
        for(Card c: trick){
            if(c.compareCards(leader, trump, lead)){
                leader = c;
            }
        }
        return leader;
    }




    /**
     * determines the best eligible card in a player's hand
     * @param lead the lead suit
     * @param trump the trump suit
     * @return the best eligible card in the player's hand
     */
    private Card best(char lead, char trump) {
        return getLeader(eligible, trump, lead);
    }



    /**
     * returns an ace off-suit card if the user has once
     * @param trump the trump suit
     * @return an ace off-suit card, or null if the user doesn't have one
     */
    private Card aceOff(char trump) {
        for(Card c: eligible){
            if(c.getRank() == 'A' && c.getSuit() != trump){
                return c;
            }
        }
        return null;
    }

    /**
     * deterimes if the player's hand contains the best card left in the round
     * @param trump the trump suit
     * @param played the cards that have been played in the round so far
     * @return the best card left in the round, or null if the player's hand doesn't contain it
     */
    private Card topper(char trump, ArrayList<Card> played) {
        boolean rightPlayed = rightPlayed(played, trump);
        boolean leftPlayed = leftPlayed(played, trump);

        for(Card c: eligible){
            if(c.isRight(trump) || (c.isLeft(trump) && rightPlayed) ||
                    (c.getRank() == 'A' && c.getSuit() == trump && leftPlayed)){
                return c;
            }
        }
        return null;
    }

    /**
     * deterimines whether the left bauer has been player in the round so far
     * @param played the cards that have been played in the round so far
     * @param trump the trump suit
     * @return true if the left bauer has been played
     */
    private boolean leftPlayed(ArrayList<Card> played, char trump) {
        for(Card c: played){
            if(c.isLeft(trump)){
                return true;
            }
        }
        return false;
    }

    /**
     * deterimines whether the right bauer has been player in the round so far
     * @param played the cards that have been played in the round so far
     * @param trump the trump suit
     * @return true if the right bauer has been played
     */
    private boolean rightPlayed(ArrayList<Card> played, char trump) {
        for(Card c: played) {
            if (c.isRight(trump)) {
                return true;
            }
        }
        return false;
    }

    /**
     * allows human player to select a card to play
     * @param lead the lead suit
     * @return the card to be played
     */
    private Card humanPlay(char lead, char trump) throws InterruptedException {
        eligible = getEligible(hand, lead, trump);
        boolean valid = false;
        int choice = 0;
        while (!valid){
            printHand();
            System.out.println("Choose card to play: ");
            Scanner scan = new Scanner(System.in);
            while (!scan.hasNextInt()){
                scan.next();
                System.out.println("enter a number!");
            }
            choice = scan.nextInt() - 1;
            if(choice < hand.size() && choice > -1) {
                if (eligible.contains(hand.get(choice))) {
                    valid = true;
                } else {
                    System.out.println("You must follow suit!");
                    Thread.sleep(1000);
                }
            } else{
                System.out.println("invalid number!");
                Thread.sleep(1000);
            }
        }
        System.out.println();
        return hand.remove(choice);
    }

    /**
     * @getter
     * @return the player team
     */
    public char getTeam(){
        return team;
    }

    /**
     * prints the player's current hand
     */
    public void printHand() {
        System.out.println("\n Your hand:");
        for (int i = 0; i < hand.size(); i++){
            System.out.print((i + 1) + ": ");
            System.out.print(hand.get(i).printRank() + " of ");
            System.out.println(hand.get(i).printSuit());
        }
        System.out.println();

    }

    /**
     * AI function for CPU player to decide whether to call trump on the first round
     * @param suit the facUp card's suit
     * @return true if CPU decides to call trump
     */
    public boolean trumpFirstRound(char suit) {
        if(scoreHand(suit) > 21 && handsDownTheBest('\0') == suit){
            return true;
        }
        return false;
    }

    /**
     * determines the overall value of a player's hand given a certain trump suit/
     * @param trump a given trump suit
     * @return numeric value of the hand given that trump suit
     */
    private int scoreHand(char trump) {
        int score = 0;
        for(Card c: hand){
            score += scoreCard(c, trump);
        }
        return score;
    }

    /**
     * scores the value of a card given a certain trump suit
     * @param c a card
     * @param trump a given trump suit
     * @return numeric value of the card
     */
    private int scoreCard(Card c, char trump) {
        if(c.isRight(trump)){
            return 12;
        }
        if(c.isLeft(trump)){
            return 9;
        }
        if(c.getSuit() == trump){
            return c.trumpRank();
        }
        if(c.isAceOff(trump)){
            return 7;
        }
        if(c.isKingOff(trump)){
            return 1;
        }
        return 0;
    }

    /**
     * determines if one trump suit would be way better than any other suit for this hand
     * @param suit the suit of the faceUp card
     * @return the suit being called trump, or the null character for a pass
     */
    public char handsDownTheBest(char suit) {
        int options[] = scoreSuits(suit);

        for(int i = 0; i < 4; i++){
            boolean call = true;
            for(int j = i + 1; j < i + 4; j++) {
                if (options[i] < options[j % 4] + 6) {
                    call = false;
                }
            }
            if(call) {
                return call(i);
            }
        }

        return '\0';
    }

    /**
     * AI method for CPU to decide whether to call trump on the second round
     * @param suit the suit of the faceUp card
     * @return the suit being called trump, or the null character for a pass
     */
    public char trumpSecondRound(char suit) {
        int options[] = scoreSuits(suit);

        if(allWorkWell(options)){
            return '\0';
        }

        for(int i = 0; i < 4; i++){
            boolean call = true;
            if(options[i] < 20){
                call = false;
            }
            for(int j = i + 1; j < i + 4; j++) {
                if (options[i] < options[j % 4]) {
                    call = false;
                }
            }
            if(options[i] < 22){
                call = false;
            }
            if(call) {
                return call(i);
            }
        }

        return '\0';
    }

    private boolean allWorkWell(int[] options) {
        int count = 0;
        for(int i: options) {
            if(i > 27){
                return false;
            }
            if(i >= 15){
                count++;
            }
        }
        if(count >= 3){
            return true;
        }
        return false;
    }

    /**
     * helper function used by trumpSecondRound
     * @param i which element in scoring array had the best score
     * @return suit to call
     */
    private char call(int i) {
        if (i == 0) {
            return 'S';
        } else if (i == 1) {
            return 'H';
        } else if (i == 2) {
            return 'D';
        } else {
            return 'C';
        }
    }


    /**
     * forces computer player to call trump
     * @param suit that cannot be called
     * @return suit to be called
     */
    public char cpuStickTheDealer(char suit) {
        int options[] = scoreSuits(suit);

        for(int i = 0; i < 4; i++){
            boolean call = true;
            for(int j = i + 1; j < i + 4; j++) {
                if (options[i] < options[j % 4]) {
                    call = false;
                }
            }
            if(call) {
                return call(i);
            }
        }
        return 'S';
    }

    /**
     * scores the value of player's hand against each suit
     * @param suit the suit that cannot be called
     * @return array of scores
     */
    private int[] scoreSuits(char suit) {
        int scores[] = new int[4];
        scores[0] = scoreHand('S');
        scores[1] = scoreHand('H');
        scores[2] = scoreHand('D');
        scores[3] = scoreHand('C');
        if(suit == 'S') {
            scores[0] = 1;
        } else if(suit == 'H') {
            scores[1] = 1;
        } else if(suit == 'D') {
            scores[2] = 1;
        } else{
            scores[3] = 1;
        }
        return scores;
    }

}
