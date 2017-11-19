import java.util.ArrayList;
import java.util.Scanner;

public class Player {

    private char type;
    private char team;
    private String name;

    ArrayList<Card> hand = new ArrayList<>();
    ArrayList<Card> eligible = new ArrayList<>();

    Player(char t, char te, String n){
        type = t;
        team = te;
        name = n;
    }

    public String getName() {
        return name;
    }

    public char getType() {
        return type;
    }

    public void removeCard() {
        //printHand();
        //System.out.println("Which card would you like to discard?");
        //Scanner scan = new Scanner(System.in);
        //int choice = scan.nextInt();
        hand.remove(0);
    }

    public void addCard(Card faceUp) {
        hand.add(faceUp);
    }

    public Card playCard(char lead, int order, ArrayList<Card> trick, char trump, ArrayList<Card> played) throws InterruptedException {
        if(type == 'h'){
            return humanPlay(lead);
        }
        return computerPlay(lead, order, trick, trump, played);
    }

    private Card computerPlay(char lead, int order, ArrayList<Card> trick, char trump, ArrayList<Card> played) throws InterruptedException {
        eligible.clear();
        Card play;
        if(order == 0){
            eligible.addAll(hand);
            play = computerLeadOff(lead, trick, trump, played);
        } else if(order == 1){
            eligible = getEligible(hand, lead);
            play = computerSecondPlay(lead, trick, trump, played);
        } else if(order == 2){
            eligible = getEligible(hand, lead);
            play = computerThirdPlay(lead, trick, trump, played);
        } else{
            eligible = getEligible(hand, lead);
            play = computerFourthPlay(lead, trick, trump);
        }
        int index = 0;
        for(int i = 0; i < hand.size(); i++){
            if(hand.get(i) == play){
                index = i;
            }
        }
        Thread.sleep(1000);
        return hand.remove(index);
        
    }

    private Card computerFourthPlay(char lead, ArrayList<Card> trick, char trump) {
        if (canWin(lead, trick, trump)){
            return worstWinner(lead, trick, trump);
        }
        return getWorst(eligible, trump, lead);
        //play worst card that will win
    }

    private ArrayList<Card> getEligible(ArrayList<Card> hand, char lead) {
        ArrayList<Card> eligible = new ArrayList<>();
        for(Card c: hand){
            if(c.isLead(lead)){
                eligible.add(c);
            }
        }
        if(eligible.isEmpty()){
            eligible.addAll(hand);
        }
        return eligible;
    }


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

    private Card getWorst(ArrayList<Card> winners, char trump, char lead) {
        Card loser = winners.get(0);
        for(Card c : winners){
            if(!c.compareCards(loser, trump, lead)){
                loser = c;
            }
        }
        return loser;
    }

    private boolean canWin(char lead, ArrayList<Card> trick, char trump) {
        Card leader = getLeader(trick, trump, lead);
        for(Card c : eligible){
            if(c.compareCards(leader, trump, lead)) {
                return true;
            }
        }
        return false;
    }

    private Card getLeader(ArrayList<Card> trick, char trump, char lead) {
        Card leader = trick.get(0);
        for(Card c: trick){
            if(c.compareCards(leader, trump, lead)){
                leader = c;
            }
        }
        return leader;
    }

    private Card computerThirdPlay(char lead, ArrayList<Card> trick, char trump, ArrayList<Card> played) {
        if(!canWin(lead, trick, trump)){
            return getWorst(eligible, trump, lead);
        }
        return best(lead, trump);
    }

    private Card best(char lead, char trump) {
        return getLeader(eligible, trump, lead);
    }

    private Card computerSecondPlay(char lead, ArrayList<Card> trick, char trump, ArrayList<Card> played) {
        if(!canWin(lead, trick, trump)){
            return getWorst(eligible, trump, lead);
        }
        return worstWinner(lead, trick, trump);
    }

    private Card computerLeadOff(char lead, ArrayList<Card> trick, char trump, ArrayList<Card> played) {
        Card potential;
        potential = topper(trump, played);
        if(potential != null){
            return potential;
        }
        potential = aceOff(trump);
        if(potential != null){
            return potential;
        }
        return getWorst(hand, trump, lead);
    }

    private Card aceOff(char trump) {
        for(Card c: eligible){
            if(c.getRank() == 'A' && c.getSuit() != trump){
                return c;
            }
        }
        return null;
    }

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

    private boolean leftPlayed(ArrayList<Card> played, char trump) {
        for(Card c: played){
            if(c.isLeft(trump)){
                return true;
            }
        }
        return false;
    }

    private boolean rightPlayed(ArrayList<Card> played, char trump) {
        for(Card c: played) {
            if (c.isRight(trump)) {
                return true;
            }
        }
        return false;
    }


    private Card humanPlay(char lead) {
        eligible = getEligible(hand, lead);
        boolean valid = false;
        int choice = 0;
        while (!valid){
            System.out.println();
            printHand();
            System.out.println("Choose card to play: ");
            Scanner scan = new Scanner(System.in);
            choice = scan.nextInt() - 1;
            if (eligible.contains(hand.get(choice))){
                valid = true;
            } else {
                System.out.println("You must follow suit!");
            }
        }
        return hand.remove(choice);
    }

    public char getTeam(){
        return team;
    }

    public void printHand() {
        for (int i = 0; i < hand.size(); i++){
            System.out.print((i + 1) + ": ");
            System.out.print(hand.get(i).printRank() + " of ");
            System.out.println(hand.get(i).printSuit());
        }
        System.out.println();

    }

    public boolean trumpFirstRound(char suit) {
        if(scoreHand(suit) > 22){
            return true;
        }
        return false;
    }

    private int scoreHand(char trump) {
        int score = 0;
        for(Card c: hand){
            score += scoreCard(c, trump);
        }
        return score;
    }

    private int scoreCard(Card c, char trump) {
        if(c.isRight(trump)){
            return 12;
        }
        if(c.isLeft(trump)){
            return 10;
        }
        if(c.getSuit() == trump){
            return c.trumpRank();
        }
        if(c.isAceOff(trump)){
            return 7;
        }
        if(c.isKingOff(trump)){
            return 4;
        }
        return 0;
    }

    public char trumpSecondRound(char suit) {
        int spades = scoreHand('S');
        int hearts = scoreHand('H');
        int diamonds = scoreHand('D');
        int clubs = scoreHand('C');
        if(suit == 'S') {
            spades = 1;
        } else if(suit == 'H') {
            hearts = 1;
        } else if(suit == 'D') {
            diamonds = 1;
        } else{
            clubs = 1;
        }

        int options[] = new int[4];
        options[0] = spades;
        options[1] = hearts;
        options[2] = diamonds;
        options[3] = clubs;

        for(int i = 0; i < 4; i++){
            boolean call = true;
            if(options[i] < 20){
                call = false;
            }
            for(int j = i + 1; j < i + 4; j++) {
                if (options[i] < options[j % 4] * 2) {
                    call = false;
                }
            }
            if(call) {
                return call(i);
            }
        }

        return '\0';
    }

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
}
