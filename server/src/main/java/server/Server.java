package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SqlDataAccess;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;
import io.javalin.*;
import io.javalin.http.Context;
import service.GameService;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Javalin server;
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        var dataAccess = new SqlDataAccess();
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);
        server = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        server.delete("db", ctx -> userService.clear()); //clear
        server.post("user", ctx -> register(ctx)); //register
        server.post("session", ctx -> login(ctx)); //login
        server.delete("session", ctx -> logout(ctx)); //logout
        server.get("game", ctx -> listGames(ctx)); //list games
        server.post("game", ctx -> createGame(ctx)); //create game
        server.put("game", ctx -> joinGame(ctx)); //join game


    }

    private void register(Context ctx){
        try {
            var serilaizer = new Gson();
            String requestJson = ctx.body();
            var user = serilaizer.fromJson(requestJson, UserData.class);

            var authData = userService.register(user);

            ctx.result(serilaizer.toJson(authData));
        }
        catch (DataAccessException ex){
            var msg = String.format("{ \"message\": \"Error: bad request\" }", ex.getMessage());
            ctx.status(400).result(msg);
        }
        catch (Exception ex){
            var msg = String.format("{ \"message\": \"Error: already taken\" }", ex.getMessage());
            ctx.status(403).result(msg);
        }
        catch (Throwable ex) {
            ctx.status(500).result("{ \"message\": \"Error: server error\" }");
        }
    }
    private void login(Context ctx){
        try {
            var serilaizer = new Gson();
            String requestJson = ctx.body();
            var user = serilaizer.fromJson(requestJson, UserData.class);

            var authData = userService.login(user);
            ctx.result(serilaizer.toJson(authData));
        }
        catch (DataAccessException ex){
            var msg = String.format("{ \"message\": \"Error: bad request\" }", ex.getMessage());
            ctx.status(400).result(msg);
        }
        catch (Exception ex){
            var msg = String.format("{ \"message\": \"Error: unauthorized\" }", ex.getMessage());
            ctx.status(401).result(msg);
        }
        catch (Throwable ex) {
            ctx.status(500).result("{ \"message\": \"Error: server error\" }");
        }
    }
    private void logout(Context ctx) {
        try {
            String authHeader = ctx.header("Authorization");


            userService.logout(authHeader);
            ctx.status(200).result("{}");
        } catch (DataAccessException ex) {
            ctx.status(401).result("{ \"message\": \"Error: unauthorized\" }");
        } catch (Exception ex) {
            ctx.status(500).result("{ \"message\": \"Error: server error\" }");
        }
    }

    private void listGames(Context ctx){
        try {
            var serializer = new Gson();
            String authHeader = ctx.header("Authorization");
            Iterable<GameData> games = gameService.listGames(authHeader);
            HashMap<String, Iterable<GameData>> response = new HashMap<>();
            response.put("games", games);
            ctx.status(200).result(serializer.toJson(response));
        } catch (DataAccessException ex) {
            ctx.status(401).result("{ \"message\": \"Error: unauthorized\" }");
        } catch (Exception ex) {
            ctx.status(500).result("{ \"message\": \"Error: server error\" }");
        }

    }

    private void createGame(Context ctx){
        try {
            var serializer = new Gson();
            String auth = ctx.header("Authorization");
            var req = serializer.fromJson(ctx.body(), Map.class);
            String name = (String) req.get("gameName");

            var game = gameService.createGame(auth, name);
            ctx.status(200).result(serializer.toJson(Map.of("gameID", game.gameID())));
        }
        catch (DataAccessException ex){
            var msg = String.format("{ \"message\": \"Error: unauthorized\" }", ex.getMessage());
            ctx.status(401).result(msg);
        }
        catch (Exception ex){
            var msg = String.format("{ \"message\": \"Error: bad request\" }", ex.getMessage());
            ctx.status(400).result(msg);
        }
        catch (Throwable ex) {
            ctx.status(500).result("{ \"message\": \"Error: server error\" }");
        }

    }
    private void joinGame(Context ctx){
        try {
            var serializer = new Gson();
            String auth = ctx.header("Authorization");
            var req = serializer.fromJson(ctx.body(), Map.class);
            String color = (String) req.get("playerColor");
            int id = ((Double) req.get("gameID")).intValue();

            gameService.joinGame(auth, color, id);
            ctx.status(200).result("{}");
        }
        catch (DataAccessException ex){
            var msg = String.format("{ \"message\": \"Error: unauthorized\" }", ex.getMessage());
            ctx.status(401).result(msg);
        }
        catch (IllegalAccessException ex){
            var msg = String.format("{ \"message\": \"Error: already taken\" }", ex.getMessage());
            ctx.status(403).result(msg);
        }
        catch (Exception ex){
            var msg = String.format("{ \"message\": \"Error: bad request\" }", ex.getMessage());
            ctx.status(400).result(msg);
        }
        catch (Throwable ex) {
            ctx.status(500).result("{ \"message\": \"Error: server error\" }");
        }


    }


    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
