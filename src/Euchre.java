import java.util.ArrayList;


/**
 * Drive class for the game, contains the main method.
 */
public class Euchre {


    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> order = new ArrayList<>();
    private int blueScore = 0;
    private int orangeScore = 0;

    /**
     * initializes players
     */
    Euchre(){
        Player p1 = new Player('h', 'b', "You");
        Player p2 = new Player('c', 'o', "Bob");
        Player p3 = new Player('c', 'b', "Alice");
        Player p4 = new Player('c', 'o', "Velma");
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
    }

    /**
     * play Euchre
     * @throws InterruptedException
     */
    private void play() throws InterruptedException {
        int i = 0;
        while (orangeScore < 10 && blueScore < 10) {
            order.clear();
            for (int j = 0; j < 4; j++) {
                order.add(players.get((i + j) % 4));
            }
            Round r = new Round(order);
            System.out.println("Round " + (i+1) + ": \n");
            int score = r.playRound();
            Thread.sleep(1300);
            String message = " points!";
            if(Math.abs(score) == 1){
                message = " point!";
            }
            if (score > 0) {
                System.out.println(" \nBlue wins " + score + message + "\n");
                blueScore += score;
            } else {
                System.out.println("\nOrange wins " + (-1 *score) + message + "\n");
                orangeScore -= score;
            }
            Thread.sleep(1500);
            System.out.println("Blue Score: " + blueScore);
            System.out.println("Orange Score: " + orangeScore + "\n");
            i++;
            Thread.sleep(1200);
        }
        if(blueScore > orangeScore){
            System.out.println("Blue wins!");
        } else {
            System.out.println("Orange wins!");
        }

    }

    /**
     * driver method
     * @param args
     * @throws InterruptedException
     */
     public static void main(String[] args) throws InterruptedException {
            Euchre game = new Euchre();
            game.play();
     }


    }






