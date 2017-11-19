import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Round {

    ArrayList<Card> deck = new ArrayList<>();
    ArrayList<Card> trick = new ArrayList<>();
    ArrayList<Card> played = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    Card faceUp;
    Scanner scan = new Scanner(System.in);
    char trump;
    char lead = '\0';
    char caller;
    int blueTricks = 0;
    int orangeTricks = 0;


    public Round(ArrayList<Player> list) {
        players = list;
    }
    public int playRound() {
        shuffleAndDeal();
        if(!firstRoundTrump()){
            secondRoundTrump();
        }
        int starter = 0;
        for(int i = 0; i < 5; i++) {
           starter = playTrick(starter);
           trick.clear();
        }
        return determineRoundWinner();
    }


    private int determineRoundWinner() {
        if (blueTricks == 3 || blueTricks == 4) {
            if (caller == 'b') {
                return 1;
            }
            return 2;
        }
        if(blueTricks == 5){
            return 2;
        }
        if(blueTricks == 1 || blueTricks == 2){
            if(caller == 'b'){
                return -2;
            }
            return -1;
        }
        if(blueTricks == 0){
            return -2;
        }
        return 0;
    }

    private int playTrick(int starter) {
        for (int i = starter; i < starter + 4; i++){
            trick.add(players.get(i % 4).playCard(lead, i - starter, trick, trump, played));
            played.add(trick.get(trick.size()-1));
            this.lead = trick.get(0).getSuit();
            trick.get(trick.size()-1).printCard();
        }
        return determineTrickWinner(starter);
    }

    private int determineTrickWinner(int starter) {
        int leader = 0;
        for(int i = 1; i < 4; i++){
            if(trick.get(i).compareCards(trick.get(leader), trump, lead)){
                leader = i;
            }
        }
        int winner = (leader + starter) % 4;
        printWinner(winner);
        if(players.get(winner).getTeam() == 'b'){
            blueTricks++;
        } else{
            orangeTricks++;
        }
        return winner;
    }

    private void printWinner(int lead) {
        if(lead == 0){
            System.out.println("Second chair wins!");
        }
        if(lead == 1){
            System.out.println("Third chair wins!");
        }
        if(lead == 2){
            System.out.println("Fourth chair wins!");
        }
        if(lead == 3){
            System.out.println("Dealer wins!");
        }
    }


    private boolean secondRoundTrump() {
        System.out.println("Everyone passed! You can now call any other suit as trump!");
        for(Player p: players){
            if(p.getType() == 'h') {
                if(playerTrumpSecondRound(p)){
                    caller = p.getTeam();
                    return true;
                };
            } else{
                if(computerTrumpSecondRound(p)){
                 caller = p.getTeam();
                 return true;
                }
            }
        }
        return false;
    }

    private boolean computerTrumpSecondRound(Player p) {
        return false;
    }

    private boolean playerTrumpSecondRound(Player p) {
        System.out.println("Would you like to declare trump?");
        char response = scan.next().charAt(0);
        if(response == 'n'){
            return false;
        }
        System.out.println("What suit would you like to declare?");
        trump = scan.next().charAt(0);
        System.out.print("You declared: ");
        printTrump(trump);
        System.out.println(" as trump!");
        return true;
    }

    private void printTrump(char trump) {
        if(trump == 'H'){
            System.out.print("Hearts");
        }
        if(trump == 'S'){
            System.out.print("Spades");
        }
        if(trump == 'D'){
            System.out.print("Diamonds");
        }
        if(trump == 'C'){
            System.out.print("Clubs");
        }
    }

    private boolean firstRoundTrump() {
        faceUp = deck.remove(0);
        System.out.print("Faceup Card is: " );
        faceUp.printCard();
        for(Player p: players){
            if(p.getType() == 'c'){
                  if(cpuTrumpFirstRound(p)){
                      caller = p.getTeam();
                      return true;
                  }
            } else{
                if(playerTrumpFirstRound(p)){
                    caller = p.getTeam();
                    return true;
                }
            }
        }
        return false;

    }

    private boolean playerTrumpFirstRound(Player p) {
        System.out.println("Your hand:");
        p.printHand();
        System.out.println("Call trump? (Y/N)");
        if(scan.next().equals("y")){
            trump = faceUp.getSuit();
            players.get(3).addCard(faceUp);
            players.get(3).removeCard();
            printTrump(trump);
            System.out.println(" is now trump!");
            return true;
        }
        return false;
    }

    private boolean cpuTrumpFirstRound(Player p){
        return false;
    }

    private void shuffleAndDeal() {

        deck.add(new Card('S', '9'));
        deck.add(new Card('S', 'T'));
        deck.add(new Card('S', 'J'));
        deck.add(new Card('S', 'Q'));
        deck.add(new Card('S', 'K'));
        deck.add(new Card('S', 'A'));
        deck.add(new Card('H', '9'));
        deck.add(new Card('H', 'T'));
        deck.add(new Card('H', 'J'));
        deck.add(new Card('H', 'Q'));
        deck.add(new Card('H', 'K'));
        deck.add(new Card('H', 'A'));
        deck.add( new Card('C', '9'));
        deck.add( new Card('C', 'T'));
        deck.add( new Card('C', 'J'));
        deck.add( new Card('C', 'Q'));
        deck.add( new Card('C', 'K'));
        deck.add( new Card('C', 'A'));
        deck.add( new Card('D', '9'));
        deck.add( new Card('D', 'T'));
        deck.add( new Card('D', 'J'));
        deck.add( new Card('D', 'Q'));
        deck.add( new Card('D', 'K'));
        deck.add( new Card('D', 'A'));
        Collections.shuffle(deck);


        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                players.get(j).addCard(deck.remove(0));
            }
        }
    }


    public static void main(String[] args){


  }

}
