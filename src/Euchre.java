import java.util.ArrayList;



public class Euchre {


    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Player> order = new ArrayList<>();
    int blueScore = 0;
    int redScore = 0;

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

    private void play() throws InterruptedException {
        int i = 0;
        while (redScore < 10 && blueScore < 10) {
            order.clear();
            for (int j = 0; j < 4; j++) {
                order.add(players.get((i + j) % 4));
            }
            Round r = new Round(order);
            System.out.println("Round " + (i+1) + ": \n");
            int score = r.playRound();
            if (score > 0) {
                System.out.println(" \n Blue wins " + score + " points! \n");
                blueScore += score;
            } else {
                System.out.println("Orange wins " + (-1 *score) + " points!");
                redScore -= score;
            }
            i++;
        }
        if(blueScore > redScore){
            System.out.println("Blue wins!");
        } else {
            System.out.println("Red wins!");
        }

    }

     public static void main(String[] args) throws InterruptedException {
            Euchre game = new Euchre();
            game.play();
     }


    }






