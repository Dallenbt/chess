import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import datamodel.UserData;
import ui.*;





public class LoggedInClient {
    private final ServerFacade server;
    private final HashMap<String, String> emails = new HashMap<>();

    public LoggedInClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Alright time to get started! Type help to see what you can do");


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
        System.out.print("\n" + EscapeSequences.RESET + "[LOGGED_IN]>>> " + EscapeSequences.GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(params);
                case "create" -> create(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logout(String... params) throws ResponseException {
        Main.state = State.LOGGEDOUT;
        server.logout();
        return "Play again soon!";
    }

    public String create(String... params) throws ResponseException {
        try {
            if (params.length == 1) {
                var user = new UserData(params[0], params[1], params[2]);
                emails.put(params[0], params[2]);
                server.register(user);
                return String.format("Nice to meet you %s!", params[0]);
            }
        }catch (Exception ex) {
            return  "Expected: <Username> <Password> <Email>";
        }
        return "";
    }

    public String quit() throws ResponseException {
        Main.state = State.EXIT;
        return "Goodbye!";
    }


    public String help() {
        return """
                - create <GameName>
                - list
                - join <ID> [WHITE|BLACK]
                - observe <ID>
                - logout
                - quit
                - help
                """;
    }
}

