import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Round {

    ArrayList<Card> deck = new ArrayList<>();
    ArrayList<Card> trick = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    Card faceUp;
    Scanner scan = new Scanner(System.in);
    char trump;
    Player dealer;
    Player secondChair;
    Player thirdChair;
    Player fourthChair;
    char lead;
    char caller;
    int blueTricks = 0;
    int orangeTricks = 0;


    public Round(ArrayList<Player> list){
        assignPlayers(list);
    }

    public int playRound() {
        shuffleAndDeal();
        if(!firstRoundTrump()){
            secondRoundTrump();
        }
        playRound();
        for(int i = 0; i < 5; i++) {
            playTrick();
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

    private void playTrick() {
        for (int i = 0; i < 4; i++){
            lead = players.get(i).playCard().getSuit();
        }
        determineTrickWinner();
    }

    private void determineTrickWinner() {
        Card leader = trick.get(0);
        int lead = 0;
        for(int i = 1; i < 4; i++){
            if(leader.compareCards(trick.get(i), trump, this.lead)){
                leader = trick.get(i);
                lead = i;
            }
        }
        if(players.get(lead).getTeam() == 'b'){
            blueTricks++;
        } else{
            orangeTricks++;
        }
    }

    private void assignPlayers(ArrayList<Player> list) {
        dealer = list.get(0);
        secondChair = list.get(1);
        thirdChair = list.get(2);
        fourthChair = list.get(3);
        players.add(secondChair);
        players.add(thirdChair);
        players.add(fourthChair);
        players.add(dealer);
    }

    private void secondRoundTrump() {
        System.out.println("Everyone passed! You can now call any other suit as trump!");
        for(Player p: players){
            if(p.getType() == 'h') {
                playerTrumpSecondRound();
            } else{
                computerTrumpSecondRound();
            }
        }
    }

    private boolean computerTrumpSecondRound() {
        return false;
    }

    private boolean playerTrumpSecondRound() {
        System.out.println("Would you like to declare trump?");
        char response = scan.next().charAt(0);
        if(response == 'n'){
            return false;
        }
        System.out.println("What suit would you like to declare?");
        trump = scan.next().charAt(0);
        System.out.print("You declared: ");
        System.out.print(trump);
        System.out.println("as trump!");
        return true;
    }

    private boolean firstRoundTrump() {
        faceUp = deck.remove(0);
        System.out.print("Faceup Card is: " );
        System.out.print(faceUp.getSuit());
        System.out.println(faceUp.getRank());
        for(Player p: players){
            if(p.getType() == 'c'){
                  if(cpuTrumpFirstRound(p)) return true;
            } else{
                if(playerTrumpFirstRound(p)) return true;
            }
        }
        return false;

    }

    private boolean playerTrumpFirstRound(Player p) {
        System.out.println("Call trump? (Y/N)");
        if(scan.next().equals("y")){
            trump = faceUp.getSuit();
            dealer.addCard(faceUp);
            dealer.removeCard();
            System.out.print(trump);
            System.out.println(" is now trump!");
            return true;
        }
        return false;
    }

    private boolean cpuTrumpFirstRound(Player p){
        return false;
    }

    private void shuffleAndDeal() {
        deck.add(new Card('S', '2'));
        deck.add(new Card('S', '3'));
        deck.add(new Card('S', '4'));
        deck.add(new Card('S', '5'));
        deck.add(new Card('S', '6'));
        deck.add(new Card('S', '7'));
        deck.add(new Card('S', '8'));
        deck.add(new Card('S', '9'));
        deck.add(new Card('S', 'T'));
        deck.add(new Card('S', 'J'));
        deck.add(new Card('S', 'Q'));
        deck.add(new Card('S', 'K'));
        deck.add(new Card('S', 'A'));
        deck.add(new Card('H', '2'));
        deck.add(new Card('H', '3'));
        deck.add(new Card('H', '4'));
        deck.add(new Card('H', '5'));
        deck.add(new Card('H', '6'));
        deck.add(new Card('H', '7'));
        deck.add(new Card('H', '8'));
        deck.add(new Card('H', '9'));
        deck.add(new Card('H', 'T'));
        deck.add(new Card('H', 'J'));
        deck.add(new Card('H', 'Q'));
        deck.add(new Card('H', 'K'));
        deck.add(new Card('H', 'A'));
        deck.add(new Card('C', '2'));
        deck.add(new Card('C', '3'));
        deck.add(new Card('C', '4'));
        deck.add(new Card('C', '5'));
        deck.add( new Card('C', '6'));
        deck.add( new Card('C', '7'));
        deck.add( new Card('C', '8'));
        deck.add( new Card('C', '9'));
        deck.add( new Card('C', 'T'));
        deck.add( new Card('C', 'J'));
        deck.add( new Card('C', 'Q'));
        deck.add( new Card('C', 'K'));
        deck.add( new Card('C', 'A'));
        deck.add( new Card('D', '2'));
        deck.add( new Card('D', '3'));
        deck.add( new Card('D', '4'));
        deck.add( new Card('D', '5'));
        deck.add( new Card('D', '6'));
        deck.add( new Card('D', '7'));
        deck.add( new Card('D', '8'));
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
