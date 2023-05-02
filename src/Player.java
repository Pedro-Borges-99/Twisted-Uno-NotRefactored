/*
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class Player {


    private String name;

    public String getName() {
        return name;
    }

    private Socket connection;

    public Socket getConnection() {
        return connection;
    }

    private boolean myTurn = false;

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    private ArrayList<String> playerDeck=new ArrayList<>();

    public void setPlayerDeck(ArrayList<String> playerDeck) {
        this.playerDeck.addAll(playerDeck);
    }

    public Player(String name, Socket connection) {
        playerDeck.add("DRAW CARD");
        this.name = name;
        this.connection = connection;
    }

    public void playCard() throws IOException {
        String[] deck = playerDeck.toArray(new String[0]);
        PrintStream out = new PrintStream(connection.getOutputStream(), true);
        Prompt prompt = new Prompt(connection.getInputStream(), out);
        MenuInputScanner in = new MenuInputScanner(deck);
        in.setMessage("Select your card:"+"\n"+ "If you need a card, ENTER 1 ");
        Server.showDeck(in);

        if (myTurn) {
            int card = prompt.getUserInput(in);
            if (!isPlayable(card)) {
                System.out.println("You cant play that card.");
                Server.showTableCard(this);
                playCard();
            } else if(card==1){
                playerDeck.add(Deck.newCard());
                myTurn=false;
            }else{
                //(playerDeck.get(card - 1).contains("X"))
                Server.showCard(playerDeck.get(card - 1),this);
                playerDeck.remove(playerDeck.get(card - 1));
                myTurn=false;

                if(playerDeck.size()<=1) {Server.isGameOver=true;
                System.out.println(name+" has won the game!!!! :)");}
            }
        }
    }

    public boolean isPlayable(int card) {
        String[] selectedCard = playerDeck.get(card - 1).split(" ");
        System.out.println(Server.tableCard+"  --->  "+selectedCard[1]+"  1--2  "+selectedCard[0]);

        if (Server.tableCard.contains(selectedCard[1])
                || (Server.tableCard.contains(selectedCard[0])) || Server.tableCard.contains("X")
                || card==1 || playerDeck.get(card - 1).contains("X") ||Server.tableCard.contains("POW")) {
            return true;
        }
        return false;
    }


    public void fightMode() throws IOException {
        BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        class Fight implements Runnable{

            @Override
            public void run() {

                while(!Server.fightWinner){
                    System.out.println("in while fightmode");
                    if(Server.isImageUp) {

                        try {
                            System.out.println("in fighting mode");
                            in.readLine();
                            Server.champion.add(Player.this);
                            Server.fightWinner=true;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }


            }
        }
        Thread thread = new Thread(new Fight());
        thread.start();
    }



}

*/
