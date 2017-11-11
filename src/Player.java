import java.util.ArrayList;
import java.util.Scanner;

public class Player {

    private char type;
    private char team;

    ArrayList<Card> hand = new ArrayList<>();

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

    public Card playCard() {
        if(type == 'h'){
            return humanPlay();
        }
        return computerPlay();
    }

    private Card computerPlay() {
        return hand.remove(0);
    }

    private Card humanPlay() {
        printHand();
        System.out.println("Choose card to play: ");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt() - 1;
        return hand.remove(choice);
    }

    public char getTeam(){
        return team;
    }

    public void printHand() {
        for (int i = 0; i < hand.size(); i++){
            System.out.print((i + 1) + ": ");
            System.out.print(hand.get(i).getRank());
            System.out.println(hand.get(i).getSuit());
        }

    }
}
