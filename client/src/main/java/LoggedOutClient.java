import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;


import datamodel.UserData;
import ui.*;





public class LoggedOutClient {
    private final ServerFacade server;
    private final HashMap<String, String> emails = new HashMap<>();

    public LoggedOutClient(String serverUrl){
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
                System.out.print(EscapeSequences.RED + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET + "[LOGGED_OUT]>>> " + EscapeSequences.GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        try {
            if (params.length == 2) {
                var user = new UserData(params[0], params[1], emails.get(params[0]));
                Main.tokens = server.login(user);
                Main.state = State.LOGGEDIN;
                return String.format("You signed in as %s.", params[0]);
            }
            else{
                throw new ArrayIndexOutOfBoundsException();
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            return "Expected: <Username> <Password>";
        }
        catch (Exception e) {
            return "Incorrect Username or Password";
        }
    }

    public String register(String... params) throws ResponseException {
        try {
            if (params.length == 3) {
                var user = new UserData(params[0], params[1], params[2]);
                emails.put(params[0], params[2]);
                Main.tokens = server.register(user);
                Main.state = State.LOGGEDIN;
                return String.format("Nice to meet you %s!", params[0]);
            }
            else{
                throw new RuntimeException();
            }
        }catch (NullPointerException ex) {
            return  "Sorry that Username is taken, try a different one!";
       }
        catch (Exception ex){
            return "Expected: <Username> <Password> <Email>";
        }
    }

    public String quit() throws ResponseException {
        Main.state = State.EXIT;
        server.clear();
        return "Goodbye!";
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

