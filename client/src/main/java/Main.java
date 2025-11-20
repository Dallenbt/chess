import datamodel.AuthData;

public class Main {

    public static State state = State.LOGGEDOUT;
    public static AuthData tokens = new AuthData(null, null);

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
            if(state == State.LOGGEDIN){
                try{
                    new LoggedInClient(serverUrl).run();
                } catch (Exception ex) {
                    System.out.printf("Had trouble logging you in: %s%n", ex.getMessage());
                }
            }
            if(state == State.PLAYING){
                try{
                    new PlayingClient(serverUrl).run();
                } catch (Exception ex) {
                    System.out.printf("Had trouble playing a game: %s%n", ex.getMessage());
                }
            }
        }
    }
}