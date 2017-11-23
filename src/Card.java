/**
 * Each card in the game is represented as an object of this class
 */
public class Card {

    private char suit;
    private char rank;


    /**
     * constructor
     * @param s suit
     * @param r rank
     */
    public Card(char s, char r){
        this.suit = s;
        this.rank = r;
    }

    /**
     * @getter
     * @return card suit
     */
    public char getSuit(){
        return  suit;
    }

    /**
     * @getter
     * @return card rank
     */
    public char getRank() {
        return rank;
    }


    /**
     * determines which of two cards wins, given a trump suit and lead suit
     * @param other the card to compare to
     * @param trump trump suit
     * @param lead lead suit
     * @return true if this card beats other
     */
    public boolean compareCards(Card other, char trump, char lead){
        boolean b = this.getPower(trump, lead) > other.getPower(trump, lead);
        return b;
    }

    /**
     * determines the value of a card, given a trump suit and lead suit
     * @param trump the trump suit
     * @param lead the lead suit
     * @return numeric value of the card
     */
    public int getPower(char trump, char lead) {
        if(this.isTrump(trump)){
            return this.trumpRank(trump);
        }
        if(this.suit == lead){
            return this.leadRank();
        }
        return 0;
    }

    /**
     * determines the value of a card, given that it is of the lead suit
     * @return numeric score for the card
     */
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

    /**
     * determines the value of card, given that it is of the trump suit
     * @param trump the trump suit
     * @return numeric score for the card
     */
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

    /**
     * determines if a card is the left bauer
     * @param trump the trump suit
     * @return true if card is the left bauer
     */
    public boolean isLeft(char trump) {
        return this.oppositeSuit(trump) && rank == 'J';
    }

    /**
     * determines if card is the right bauer
     * @param trump the trump suit
     * @return true if the card is the right bauer
     */
    public boolean isRight(char trump) {
        return this.suit == trump && this.rank == 'J';
    }

    /**
     * deterines if the card is a trump card
     * @param trump the trump suit
     * @return true if the card is a trump card
     */
    public boolean isTrump(char trump) {
        return this.suit == trump || this.isLeft(trump) || trump == '\0';
    }

    /**
     * deterines if the card folows the lead suit
     * @param lead the lead suit
     * @return true if the card follows the lead suit
     */
    public boolean isLead(char lead) {
        return lead == '\0' || this.suit == lead || this.isLeft(lead);
    }

    /**
     * returns the opposite of the trump suit
     * @param trump the trump suit
     * @return the opposite of the trump suit
     */
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

    /**
     * prints a card's rank and suit
     */
    public void printCard(){
        System.out.println(printRank() + " of " + printSuit());
    }

    /**
     *
     * @return string version of a card's rank
     */
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

    /**
     *
     * @return string version of a card's suit
     */
    public String printSuit(){
        if(suit == 'S'){
            return  "Spades";
        }
        if(suit == 'H'){
            return "Hearts";
        }
        if(suit == 'C'){
            return "Clubs";
        }
        return "diamonds";
    }

    /**
     * deterimines if a card is ace off-suit
     * @param trump the trump suit
     * @return true if the card is ace off-suit
     */
    public boolean isAceOff(char trump) {
        if(trump != this.getSuit() && 'A' == this.getRank()){
            return true;
        }
        return false;
    }

    /**
     * deterimines if a card is king off-suit
     * @param trump the trump suit
     * @return true if the card is king off-suit
     */
    public boolean isKingOff(char trump) {
        if(trump != this.getSuit() && 'K' == this.getRank()){
            return true;
        }
        return false;
    }

    /**
     * returns the relative value of card if it is the trump suit, used solely by
     * CPU in deciding whether to call trump
     * @return numeric value
     */
    public int trumpRank() {
        if (rank == 'A'){
            return 7;
        }
        if (rank == 'K'){
            return 5;
        }
        if (rank == 'Q'){
            return 4;
        }
        return 3;
    }

    /**
     * decides which card is better to keep given a trump suit
     * @param worst card to compare to
     * @param trump the trump suit
     * @return true if this is the better card to keep
     */
    public boolean discardCompare(Card worst, char trump) {
        if (!this.isTrump(trump) && !worst.isTrump(trump)) {
            return this.rankScore() > worst.rankScore();
        } else if (this.isTrump(trump) && worst.isTrump(trump)){
            return this.trumpScore(trump) > worst.trumpScore(trump);
        } else if(this.isTrump(trump)){
            return true;
        } else{
            return false;
        }
    }

    private int trumpScore(char trump) {
        if(isRight(trump)){
            return 20;
        }
        if(isLeft(trump)){
            return 19;
        }
        if(rank == 'A'){
            return 18;
        }
        if(rank == 'K'){
            return 17;
        }
        if(rank == 'Q'){
            return 16;
        }
        if(rank == 'T'){
            return 15;
        }
        return 14;
    }

    private int rankScore() {
        if(rank == 'A'){
            return 10;
        }
        if(rank == 'K'){
            return 9;
        }
        if(rank == 'Q'){
            return 8;
        }
        if(rank == 'J'){
            return 7;
        }
        if(rank == 'T'){
            return 6;
        }
        return 5;
    }
}
