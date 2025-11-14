import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import datamodel.UserData;
import ui.*;





public class LoggedOutClient {
    private final ServerFacade server;
    private HashMap<String, String> emails = new HashMap<>();

    public LoggedOutClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Hello! Ready to play chess? Type help to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (Main.state == State.LOGGEDOUT){
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
        System.out.print("\n" + EscapeSequences.RESET + ">>> " + EscapeSequences.GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> logIn(params);
                case "register" -> register(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logIn(String... params) throws ResponseException {
        if (params.length >= 1) {
            Main.state = State.LOGGEDIN;
            var user = new UserData(params[0], params[1], emails.get(params[0]));
            server.login(user);
            return String.format("You signed in as %s.", params[0]);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <Username> <Password>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 2) {
            var user = new UserData(params[0], params[1], params[2]);
            emails.put(params[0], params[2]);
            server.register(user);
            return String.format("Nice to meet you %s!", params[0]);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <Username> <Password> <Email>");
    }

    public String quit() throws ResponseException {
        Main.state = State.EXIT;
        return new String("Goodbye!");
    }


    public String help() {
        return """
                - register <Username> <Password> <Email>
                - login <Username> <Password>
                - quit
                - help
                """;
    }
}

