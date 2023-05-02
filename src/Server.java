

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    String fightImage;
    Thread fightPlayer=new Thread();
    Thread Fighting = new Thread();
    private int counterName = 0;
    public static boolean isGameOver;
    static volatile ArrayList<Player> allPlayers = new ArrayList<>();
    public static final int DEFAULT_PORT = 7777;
    private int maxPlayers;
    public static String tableCard;

    public volatile static boolean fightWinner;
    public volatile static boolean isImageUp;
    public static volatile ArrayList<Player> champion = new ArrayList<>();

    public Server(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getTableCard() {
        return tableCard;
    }

    public void serve() {

        // try-with-resources will auto close when the try block is exited
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {

            listen(serverSocket);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void listen(ServerSocket serverSocket) throws IOException, InterruptedException {


        for (int i = 0; i < maxPlayers; i++) {

            try {
                Socket connection = serverSocket.accept();
                PrintStream out = new PrintStream(connection.getOutputStream(), true);
                Prompt prompt = new Prompt(connection.getInputStream(), out);
                StringInputScanner askName = new StringInputScanner();
                askName.setMessage("What is your username?");
                String name = prompt.getUserInput(askName);
                Player player = new Player(name, connection);
                System.out.println(name + " has entered the room.");

                allPlayers.add(player);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        start();
    }

    public static void showTableCard(Player player) throws IOException {
        PrintWriter out = null;
        out = new PrintWriter(player.getConnection().getOutputStream(), true);
        out.println("This is the table card: " + tableCard);//Needs to be in the middle os screen


    }

    public void showMessageto(String string, Player player) throws IOException {
        PrintWriter out = new PrintWriter(player.getConnection().getOutputStream(), true);
        out.println("\n\n-->" + string);


    }


    public void showMessageToAll(String string) throws IOException {
        PrintWriter out = null;
        for (Player onePlayer : allPlayers) {
            out = new PrintWriter(onePlayer.getConnection().getOutputStream(), true);
            out.println("-->" + string);//Needs to be in the middle os screen

        }

    }


    public void showFirstCard(String string, Player player) throws IOException {
        tableCard = string;
        if (string.equals("F X")) {
            if (allPlayers.indexOf(player) + 1 == allPlayers.size()) {
                initFightMode(player, allPlayers.get(0));


            } else {

                initFightMode(player, allPlayers.get(allPlayers.indexOf(player) + 1));

            }
        }
        if (string.equals("X X")) {
            initFightAll();
        }
        PrintWriter out = null;
        for (Player onePlayer : allPlayers) {
            out = new PrintWriter(onePlayer.getConnection().getOutputStream(), true);
            out.println("           " + player.getName() + " " + tableCard);//Needs to be in the middle os screen

        }

    }

    public static void showFirstCard(String string) throws IOException {
        tableCard = string;
        PrintWriter out = null;
        for (Player onePlayer : allPlayers) {
            out = new PrintWriter(onePlayer.getConnection().getOutputStream(), true);
            out.println("       First card: " +
                    " " + string);//Needs to be in the middle os screen

        }

    }

    public void showPlayerDeck(Player player) throws IOException {
        int localCounter = 1;
        PrintWriter out = new PrintWriter(player.getConnection().getOutputStream(), true);
        for (String card : player.playerDeck) {
            out.println(localCounter + " - " + card);
            localCounter++;
        }


    }


    public void start() throws IOException, InterruptedException {
        Deck deck = new Deck();
        dealCards(7);
        tableCard = deck.firstCard();
        showFirstCard(tableCard);
        while (!isGameOver) {

            for (Player player : allPlayers) {
                for (int i = 0; i < allPlayers.size(); i++) {
                    if (allPlayers.get(i).getName().equals(player.getName())) {
                        player.setMyTurn(true);
                    } else {
                        allPlayers.get(i).playCard(); //playCard only blocks the Thread if myTurn is set to to true;
                    }

                }
                if (Fighting.isAlive()) Fighting.join();
                if (fightPlayer.isAlive()) fightPlayer.join();
                player.playCard();
                //if (Fighting.isAlive()) Fighting.join();

            }

        }
    }


    public void dealCards(int numOfCards) {
        ArrayList<String> playerDeck = new ArrayList<>();
        for (Player x : allPlayers) {
            for (int j = 0; j < numOfCards; j++) {
                playerDeck.add(Deck.newCard());
                System.out.println(Deck.newCard());
            }
            x.addCards(playerDeck);
            playerDeck = new ArrayList<>();

        }
    }

    public void initFightMode(Player one, Player two) throws IOException {
        fightWinner=false;
        ArrayList<String> extraCards = new ArrayList<>();
        extraCards.add(Deck.newCard());
        extraCards.add(Deck.newCard());
        one.fightMode();
        two.fightMode();
        //String fightImage = "POW";
        char [] abc= "abcdefghijklmnopqrstuvwxyz".toCharArray();
        fightImage = ""+abc[(int)(Math.random()* abc.length)]+""+(int)(Math.random()*9);
        System.out.println(fightImage);
        /*String[] randomShowImage = new String[100];
        for (int i = 0; i < randomShowImage.length; i++) {
          randomShowImage[i]="blank";
        }
        randomShowImage[(int) (Math.random() * randomShowImage.length)] = "fight";

        String overBoard = "----------------------------------------------";
        for (String d: randomShowImage) {
            System.out.println(d);
        }


        String fightImage = "POW";
        StringBuilder stringBuilder = new StringBuilder(overBoard+"\n");
        int counter = 1;

        for (String s : randomShowImage) {
            //System.out.println("filling String builder");
            if (s.equals("blank")) {
                if (counter == 25) {
                    stringBuilder.append("\n" + " ");
                    counter=1;
                }else{stringBuilder.append(" ");}

            } else {
                System.out.println("filling String builder");

                stringBuilder.append(fightImage);
            }
            counter++;


        }
        stringBuilder.append("\n"+overBoard);
        //System.out.println(stringBuilder);*/


        //String finalFightImage = fightImage;
        class InitFight implements Runnable {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000 + ((int) (Math.random() * 5000)));
                    showMessageToAll(fightImage);//maybe bug
                    isImageUp = true;
                    while (!fightWinner) {
                        continue;
                    }
                    System.out.println(champion.get(0).getName());
                    showMessageToAll("\n\n\nThe winner is--> " + champion.get(0).getName());
                    fightPlayer.interrupt();
                    if (champion.get(0).getName().equals(one.getName())) {
                        two.addCards(extraCards);
                        //showPlayerDeck(two);
                    } else {
                        one.addCards(extraCards);
                        //showPlayerDeck(one);
                    }


                    champion = new ArrayList<>();
                    isImageUp = false;

                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        Fighting = new Thread(new InitFight());
        Fighting.start();


    }

    public void initFightAll() throws IOException {
        char [] abc= "abcdefghijklmnopqrstuvwxyz".toCharArray();
        fightImage = ""+abc[(int)(Math.random()* abc.length)]+""+(int)(Math.random()*9);
        fightWinner = false;
        for (Player player : allPlayers) {
            player.fightMode();
        }

        //String fightImage = "POW";
        class InitFight implements Runnable {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000 + ((int) (Math.random() * 5000)));
                    isImageUp = true;

                    showMessageToAll(fightImage);
                    //isImageUp = true;
                    while (!fightWinner) {
                        continue;
                    }
                    showMessageToAll("\n\n\nThe winner is--> " + champion.get(0).getName());
                    System.out.println(champion.get(0).getName());
                    ArrayList<String> playerDeck = new ArrayList<>();
                    for (Player x : allPlayers) {
                        for (int j = 0; j < 3; j++) {
                            playerDeck.add(Deck.newCard());
                        }
                        if (!champion.get(0).getName().equals(x.getName())) x.addCards(playerDeck);
                        playerDeck = new ArrayList<>();

                    }
                    champion = new ArrayList<>();

                    isImageUp = false;


                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        Fighting = new Thread(new InitFight());
        Fighting.start();


    }

    private class Player {


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

        private ArrayList<String> playerDeck = new ArrayList<>();

        public void addCards(ArrayList<String> playerDeck) {
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
            in.setMessage("Select your card:" + "\n" + "If you need a card, ENTER 1 ");
            if (!myTurn) showPlayerDeck(this);

            if (myTurn) {
                showTableCard(this);
                int card = prompt.getUserInput(in);
                if (!isPlayable(card)) {

                    System.out.println("You cant play that card.");
                    showMessageto("Invalid card selection, choose another", this);
                    playCard();
                    return;
                } else if (card == 1) {
                    playerDeck.add(Deck.newCard());
                    showMessageToAll(name + " draw a card");
                    myTurn = false;
                } else {
                    //(playerDeck.get(card - 1).contains("X"))
                    showFirstCard(playerDeck.get(card - 1), this);
                    playerDeck.remove(playerDeck.get(card - 1));
                    myTurn = false;

                    if (playerDeck.size() <= 1) {
                        Server.isGameOver = true;
                        showMessageToAll(name + " has won the game!!!! :)");
                    }
                }
            }
        }

        public boolean isPlayable(int card) {
            String[] selectedCard = playerDeck.get(card - 1).split(" ");
            System.out.println(Server.tableCard + "  --->  " + selectedCard[1] + "  --  " + selectedCard[0]);

            if (Server.tableCard.contains(selectedCard[1])
                    || (Server.tableCard.contains(selectedCard[0])) || Server.tableCard.contains("X")
                    || card == 1 || playerDeck.get(card - 1).contains("X") || Server.tableCard.contains("POW")) {
                return true;
            }
            return false;
        }


        public void fightMode() throws IOException {

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            class Fight implements Runnable {

                @Override
                public void run() {
                    String playerResponse="";

                    while (!fightWinner) {
                        System.out.println("in while fightmode");
                        if (isImageUp) {

                            try {
                                System.out.println("in fighting mode");
                                playerResponse=in.readLine();
                                if(fightImage.equals(playerResponse)){;
                                champion.add(Player.this);
                                fightWinner = true;}
                            } catch (IOException e) {
                                System.out.println("interrupted");
                                throw new RuntimeException(e);
                            }
                        }

                    }


                }
            }
            fightPlayer = new Thread(new Fight());
            fightPlayer.start();
        }


    }


}
