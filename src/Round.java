import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


/**
 * Round class contains the core game logic.
 * A new Round object is created and called in the Euchre class for each round.
 * Stores information about the current round being played, including the trump suit,
 * which team called trump, and number of tricks won by each team.
 */
public class Round {

    private ArrayList<Card> deck = new ArrayList<>();
    private ArrayList<Card> trick = new ArrayList<>();
    private ArrayList<Card> played = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private Card faceUp;
    private Scanner scan = new Scanner(System.in);
    private char trump;
    private char lead = '\0';
    private char caller;
    private int blueTricks = 0;
    private int orangeTricks = 0;


    /**
     *
     * @param list of players, in the order they will play this round, from second chair to dealer
     */
    public Round(ArrayList<Player> list) {
        players.addAll(list);
    }


    /**
     * hub function that is called by the Euchre class to play a round
     * @return the number of points that were scored this round (number will be negative if blue wins)
     * @throws InterruptedException
     */
    public int playRound() throws InterruptedException {
        shuffleAndDeal();
        if(!firstRoundTrump()){
            System.out.println();
            Thread.sleep(1000);
            secondRoundTrump();
        }
        Thread.sleep(1000);
        System.out.println();
        int starter = 0;
        for(int i = 0; i < 5; i++) {
            System.out.println("Trick " + (i+1) + ": \n");
            Thread.sleep(1000);
           starter = playTrick(starter);
           trick.clear();
        }
        return determineRoundWinner();
    }

    /**
     * runs at the end of a round, determines the winner based off number of tricks won
     * @return the number of points that were scored this round (number will be negative if blue wins)
     * @throws InterruptedException
     */
    private int determineRoundWinner() throws InterruptedException {
        System.out.println("Blue Team Tricks won: " + blueTricks);
        System.out.println("Orange Team Tricks won: " + orangeTricks);
        Thread.sleep(1000);
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

    /**
     * hub functino for each individual trick, runs 5 times per round
     * @param starter the order of the player who is leading off this trick
     * @return the order of the player who won the trick
     * @throws InterruptedException
     */
    private int playTrick(int starter) throws InterruptedException {
        lead = '\0';
        for (int i = starter; i < starter + 4; i++){
            trick.add(players.get(i % 4).playCard(lead, i - starter, trick, trump, played));
            played.add(trick.get(trick.size()-1));
            lead = trick.get(0).getSuit(trump);
            System.out.print(players.get(i%4).getName() + " played: ");
            trick.get(trick.size()-1).printCard();
            Thread.sleep(1200);
        }
        return determineTrickWinner(starter);
    }

    /**
     * runs at the end of each trick, determines who won the trick
     * @param starter the order of the player who lead the trick
     * @return theh order of the player who won the trick
     * @throws InterruptedException
     */
    private int determineTrickWinner(int starter) throws InterruptedException {
        Thread.sleep(1000);
        int leader = 0;
        for(int i = 1; i < 4; i++){
            if(trick.get(i).compareCards(trick.get(leader), trump, lead)){
                leader = i;
            }
        }
        int winner = (leader + starter) % 4;
        printWinner(winner);
        Thread.sleep(1000);
        if(players.get(winner).getTeam() == 'b'){
            blueTricks++;
        } else{
            orangeTricks++;
        }
        return winner;
    }

    /**
     * prints the winner
     * @param winner order of the player who won the trick
     */
    private void printWinner(int winner) {
       System.out.println("\n" + players.get(winner).getName() + " won! \n");
    }


    /**
     * hub function for the second round of trump calls
     * @return true (trump must be called on the second round)
     * @throws InterruptedException
     */
    private boolean secondRoundTrump() throws InterruptedException {
        System.out.println("Everyone passed! You can now call any other suit as trump! \n");
        Thread.sleep(1000);
        int i = 0;
        for(Player p: players){
            if(i == 3){
                return stickTheDealer(p);
            }
            Thread.sleep(1000);
            if(p.getType() == 'h') {
                char t = playerTrumpSecondRound(p);
                if(t != '\0'){
                    trump = t;
                    System.out.print("You declared ");
                    printTrump(trump);
                    System.out.println(" as trump!" +"\n");
                    caller = p.getTeam();
                    return true;
                } else{
                    System.out.println("You passed!");
                }
            } else{
                char t = computerTrumpSecondRound(p);
                if(t != '\0') {
                    trump = t;
                    System.out.print(p.getName() + " has called ");
                    printTrump(trump);
                    System.out.println(" as trump!" + "\n");
                    caller = p.getTeam();
                    return true;
                } else{
                    System.out.println(p.getName() + " passed!");
                }
            }
            i++;
        }
        return false;
    }

    /**
     * forces the dealer to call trump if everyone else has passed twice
     * @param p the dealer
     * @return true
     */
    private boolean stickTheDealer(Player p) throws InterruptedException {
        if(p.getType() == 'h') {
            trump = humanStickTheDealer(p);
        } else{
            trump = p.cpuStickTheDealer(faceUp.getSuit());
        }
        System.out.print(p.getName() + " declared ");
        printTrump(trump);
        System.out.println(" as trump!");
        Thread.sleep(1300);
        caller = p.getTeam();
        return true;
    }

    private char humanStickTheDealer(Player p) {
            System.out.println("What suit would you like to declare? You cannot pass.");
            while (true) {
                char t = scan.next().charAt(0);
                t = Character.toUpperCase(t);
                if (t != faceUp.getSuit() && (t == 'H' || t == 'S' || t == 'C' || t == 'D')) {
                    System.out.println("");
                    return t;
                }
                System.out.println("not a valid suit!");
                if(t == faceUp.getSuit()){
                    System.out.println("You cannot choose the suit of the face up card");
                }
            }
        }


    /**
     * calls the AI method in player for deciding whether to call trump on the second round
     * @param p the player who has to decide whether to call trump
     * @return the suit that has been called trump, or null character if player passes
     */
    private char computerTrumpSecondRound(Player p) {
        return p.trumpSecondRound(faceUp.getSuit());
    }

    /**
     * gives human player the opportunity to call trump or pass on second round
     * @param p the player deciding whether to call trump
     * @return the suit that has been called trump, or null character if player passes
     */
    private char playerTrumpSecondRound(Player p) {
        while (true) {
            p.printHand();
            System.out.println("Would you like to declare trump?");
            char response = scan.next().charAt(0);
            if (response == 'n') {
                return '\0';
            }
            if (response == 'y') {
                while (true) {
                    System.out.println("What suit would you like to declare? (H/S/C/D)");
                    char t = scan.next().charAt(0);
                    t = Character.toUpperCase(t);
                    if (t != faceUp.getSuit() && (t == 'H' || t == 'S' || t == 'C' || t == 'D')) {
                        System.out.println();
                        return t;
                    }
                    System.out.println("not a valid suit! enter H/S/C/D (capital)");
                    if(t == faceUp.getSuit()){
                        System.out.println("You cannot choose the suit of the face up card");
                    }
                }
            } else {
                System.out.println("invalid input! enter 'y' or 'n'!");
            }
        }
    }

    /**
     * prints out trump suit
     * @param trump the trump suit
     */
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

    /**
     * hub function for the first round of trump calls
     * @return true if trump was called, false if not
     * @throws InterruptedException
     */
    private boolean firstRoundTrump() throws InterruptedException {
        faceUp = deck.remove(0);
        System.out.print("Faceup Card is: " );
        faceUp.printCard();
        for(Player p: players){
            if(p.getType() == 'c'){
                  if(cpuTrumpFirstRound(p)){
                      caller = p.getTeam();
                      trump = faceUp.getSuit();
                      System.out.print(p.getName() + " calls! ");
                      printTrump(trump);
                      System.out.println(" is now trump! \n" );
                      Thread.sleep(1300);
                      players.get(3).addCard(faceUp);
                      players.get(3).removeCard(trump);
                      return true;
                  }
            } else{
                if(playerTrumpFirstRound(p)){
                    caller = p.getTeam();
                    trump = faceUp.getSuit();
                    players.get(3).addCard(faceUp);
                    players.get(3).removeCard(trump);
                    printTrump(trump);
                    System.out.println( " is now trump! \n");
                    Thread.sleep(1300);
                    return true;
                }
            }
            System.out.println(p.getName() + " passed!");
            Thread.sleep(1000);
        }
        return false;

    }

    /**
     * allows human player to decided whether to call trump on first round
     * @param p the player deciding whether to call trump
     * @return true if trump was called, else false
     */
    private boolean playerTrumpFirstRound(Player p) {
        p.printHand();
        while (true) {
            System.out.println("Call trump? (y/n)");
            String answer = scan.next();
            if (answer.equals("y")) {
                System.out.println();
                return true;
            } else if (answer.equals("n")){
                System.out.println();
                 return false;
            } else{
                System.out.println("Invalid input! Enter 'y' or 'n'!");
            }
        }
    }


    /**
     * calls AI function in Player class to decide whether cpu Calls trump
     * @param p the player deciding whether to call trump
     * @return true if trump was called, else false
     */
    private boolean cpuTrumpFirstRound(Player p){
       if(players.indexOf(p) == 1 || players.indexOf(p) == 3) {
           return p.trumpFirstRound(faceUp.getSuit(), faceUp);
       }
       return p.trumpFirstRound(faceUp.getSuit());
    }

    /**
     * runs at start of each round, shuffles deck and deals 5 cards to each player
     */
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


}
