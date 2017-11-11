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
        return this.getPower(trump, lead) > other.getPower(trump, lead);
    }

    private int getPower(char trump, char lead) {
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

    private boolean isLeft(char trump) {
        return this.oppositeSuit(trump) && rank == 'J';
    }

    private boolean isRight(char trump) {
        return this.suit == trump && this.rank == 'J';
    }

    private boolean isTrump(char trump) {
        return this.suit == trump || this.isLeft(trump);
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
}
