public class Main {

    public static State state = State.LOGGEDOUT;

    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client ♕");
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        while (state != State.EXIT) {
            if(state == State.LOGGEDOUT) {
                try {
                    new LoggedOutClient(serverUrl).run();
                }
                catch (Throwable ex) {
                    System.out.printf("Unable to start server: %s%n", ex.getMessage());
                }
            }
        }
    }
}