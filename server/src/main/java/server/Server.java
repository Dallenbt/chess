package server;

import com.google.gson.Gson;
import datamodel.UserData;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin server;
    private final UserService userService;

    public Server() {
        userService = new UserService();
        server = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        server.delete("db", ctx -> ctx.result("{}"));
        server.post("user", ctx -> register(ctx));


    }

    private void register(Context ctx){
        var serilaizer = new Gson();
        String requestJson = ctx.body();
        var user = serilaizer.fromJson(requestJson, UserData.class);

        var authData = userService.register(user);

        ctx.result(serilaizer.toJson(authData));
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
