import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import datamodel.GameData;
import ui.*;





public class PlayingClient {
    private final ServerFacade server;


    public PlayingClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        server.token = Main.tokens.authToken();
    }

    public void run() {
        System.out.println("Lets play! Type help to see what you can do");


        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (Main.state == State.LOGGEDIN){
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(EscapeSequences.BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET + "[PLAYING]>>> " + EscapeSequences.GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw(params);
                case "leave" -> leave(params);
                case "move" -> move(params);
                case "resign" -> resign(params);
                case "show" -> show(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String redraw(String... params) throws ResponseException {
       return "";
    }

    public String leave(String... params) throws ResponseException {
        return "";
    }

    public String move(String... params) throws ResponseException {
        return "";
    }

    public String resign(String... params) throws ResponseException {
        return "";
    }
    public String show(String... params) throws ResponseException {
        return "";
    }





    public String quit() throws ResponseException {
        Main.state = State.EXIT;
        server.clear();
        return "Goodbye!";
    }


    public String help() {
        return """
                - redraw
                - leave
                - move <Piece_location> <End_square>
                - resign
                - show <Piece_location>
                - quit
                - help
                """;
    }
}

