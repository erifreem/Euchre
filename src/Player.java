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
        for (int i = 0; i < 5; i++){
            System.out.print((i + 1) + ": ");
            System.out.print(hand.get(i).getRank());
            System.out.println(hand.get(i).getSuit());
        }
        System.out.println("Which card would you like to discard?");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        hand.remove(choice - 1);
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
        for(int i = 0; i < hand.size(); i++){
            int n = i+1;
            System.out.println(n + ": " + hand.get(i).getSuit() + hand.get(i).getRank());
        }
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        return hand.remove(choice);
    }

    public char getTeam(){
        return team;
    }
}
