import java.util.ArrayList;
import java.util.Scanner;

public class Player {

    private char type;
    private char team;

    ArrayList<Card> hand = new ArrayList<>();
    ArrayList<Card> eligible = new ArrayList<>();

    Player(char t, char te){
        type = t;
        team = te;
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

    public Card playCard(char lead, int order, ArrayList<Card> trick, char trump, ArrayList<Card> played) {
        if(type == 'h'){
            return humanPlay(lead);
        }
        return computerPlay(lead, order, trick, trump, played);
    }

    private Card computerPlay(char lead, int order, ArrayList<Card> trick, char trump, ArrayList<Card> played) {
        eligible.clear();
        Card play;
        if(order == 0){
            eligible = hand;
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
        boolean valid = false;
        int choice = 0;
        while (!valid){
            printHand();
            System.out.println("Choose card to play: ");
            Scanner scan = new Scanner(System.in);
            choice = scan.nextInt() - 1;
            if (hand.get(choice).isLead(lead)) {
                valid = true;
            } else {
                System.out.print("You must follow suit!");
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

    }
}
