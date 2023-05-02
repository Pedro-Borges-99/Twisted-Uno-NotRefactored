import java.util.ArrayList;

public class Deck {
    private int size=80;
    String[] colors={"RED","BLUE","GREEN","YELLOW"};
    static ArrayList<String> deck=new ArrayList<>();
    String fight="F X";
    String fightall="X X";
    int numFight=100;
    int numFightAll=2;

    /** ALL THE ABOVE VALUES ARE OPEN FOR CHANGE. CHANGES MUST HAVE IN MIND TH FEELING OF THE GAME.
     * TOO MUCH FIGHTS CAN BE CAOTIC AND MAKE FIGHTS FEEL 'NOT SPECIAL', TOO LITTLE CAN BE BORING
     * WHEN A PLAYER PICKS A CARD it HAS A PROBABILITY OF IT BEEING ONE OF THE FOLLOWING TYPES:
     * -->NORMAL CARD, probability =40/deck.length
     * -->FIGH NEXT (F X), probability =numFight/deck.length
     * -->FIGHT ALL (X X) probability =numFightAll/deck.length                                   */

    public Deck(){
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < 10; j++) {
                deck.add(j+" "+colors[i]);
            }
        }
        /*for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < 10; j++) {
                deck.add(j+" "+colors[i]);
            }
        }*/
        for (int i = 0; i < numFight; i++) {
            deck.add(fight);
        }
        for (int i = 0; i < numFightAll; i++) {
            deck.add(fightall);
        }
    }

    public static String newCard(){
        return deck.get((int)(Math.random()*deck.size()));
    }

    public String firstCard(){return deck.get((int)(Math.random()*40));}


}
