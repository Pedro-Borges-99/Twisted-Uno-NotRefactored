public class Main {
    public static void main(String[] args) {

        Server chatServer = new Server(2);
        chatServer.serve();
    }
}
