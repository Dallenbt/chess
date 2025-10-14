package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import datamodel.AuthData;
import datamodel.UserData;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin server;
    private final UserService userService;

    public Server() {
        var dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        server = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        server.delete("db", ctx -> userService.clear()); //clear
        server.post("user", ctx -> register(ctx)); //register
        server.post("session", ctx -> login(ctx)); //login
        server.delete("session", ctx -> logout(ctx)); //logout
        server.get("game", ctx -> userService.clear()); //list games
        server.post("game", ctx -> userService.clear()); //create games
        server.put("game", ctx -> userService.clear()); //join games


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
    }
    private void logout(Context ctx) {
        try {
            String authHeader = ctx.header("Authorization");


            userService.logout(authHeader);
            ctx.status(200).result("{}"); // success
        } catch (DataAccessException ex) {
            ctx.status(401).result("{ \"message\": \"Error: unauthorized\" }");
        } catch (Exception ex) {
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
