import java.util.ArrayList;

public class Euchre {


    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Player> order = new ArrayList<>();
    int blueScore = 0;
    int redScore = 0;

    Euchre(){
        Player p1 = new Player('h', 'b');
        Player p2 = new Player('c', 'o');
        Player p3 = new Player('c', 'b');
        Player p4 = new Player('c', 'o');
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
    }

    private void play() {
        int i = 0;
        while (redScore < 10 && blueScore < 10) {
            for (int j = 0; i < 4; i++) {
                order.add(players.get((i + j) % 4));
            }
            Round r = new Round(order);
            int score = r.playRound();
            if (score > 0) {
                blueScore += score;
            } else {
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

    public static void main(String[] args){
        Euchre game = new Euchre();
        game.play();
    }


}
