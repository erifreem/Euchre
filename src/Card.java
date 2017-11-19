public class Card {

    private char suit;
    private char rank;


    public Card(char s, char r){
        this.suit = s;
        this.rank = r;
    }

    public char getSuit(){
        return  suit;
    }

    public char getRank() {
        return rank;
    }

    public boolean compareCards(Card other, char trump, char lead){
        boolean b = this.getPower(trump, lead) > other.getPower(trump, lead);
        return b;
    }

    public int getPower(char trump, char lead) {
        if(this.isTrump(trump)){
            return this.trumpRank(trump);
        }
        if(this.suit == lead){
            return this.leadRank();
        }
        return 0;
    }

    private int leadRank() {
        if(this.rank == 'A'){
            return 6;
        }
        if(this.rank == 'K'){
            return 5;
        }
        if(this.rank == 'Q'){
            return 4;
        }
        if(this.rank == 'J'){
            return 3;
        }
        if(this.rank == 'T'){
            return 2;
        }
        return 1;
    }

    private int trumpRank(char trump) {
        if(this.isRight(trump)){
            return 13;
        }
        if(this.isLeft(trump)){
            return 12;
        }
        if(this.rank == 'A'){
            return 11;
        }
        if(this.rank == 'K'){
            return 10;
        }
        if(this.rank == 'Q'){
            return 9;
        }
        if(this.rank == 'T'){
            return 8;
        }
        return 7;
    }

    public boolean isLeft(char trump) {
        return this.oppositeSuit(trump) && rank == 'J';
    }

    public boolean isRight(char trump) {
        return this.suit == trump && this.rank == 'J';
    }

    public boolean isTrump(char trump) {
        return this.suit == trump || this.isLeft(trump) || trump == '\0';
    }

    public boolean isLead(char lead) {
        return lead == '\0' || this.suit == lead || this.isLeft(lead);
    }

    private boolean oppositeSuit(char trump) {
        if(this.suit == 'S'){
            return trump == 'C';
        }
        if(this.suit == 'D'){
            return trump == 'H';
        }
        if(this.suit == 'H'){
            return  trump == 'D';
        }
        return trump == 'S';
    }

    public void printCard(){
        System.out.println(printRank() + " of " + printSuit());
    }

    public String printRank(){
        if(rank == 'T'){
            return "Ten";
        }
        if(rank == 'J'){
            return "Jack";
        }
        if(rank == 'Q'){
            return "Queen";
        }
        if(rank == 'K'){
            return "King";
        }
        if(rank == 'A'){
            return "Ace";
        }
        return "" + rank;
    }

    public String printSuit(){
        if(suit == 'S'){
            return  "Spades";
        }
        if(suit == 'H'){
            return "Heart";
        }
        if(suit == 'C'){
            return "Clubs";
        }
        return "diamonds";
    }
}
