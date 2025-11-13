package client;

import chess.ChessGame;
import datamodel.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ResponseException;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    public static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void clearData() throws ResponseException {facade.clear();}

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void clear() throws Exception {
        var user = new UserData("player1", "password", "p1@email.com");
        facade.register(user);
        facade.clear();
        assertThrows(Exception.class, () -> facade.login(user));
    }


    @Test
    void register() throws Exception {
        var user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerFail(){
        var user = new UserData("player1", null, "p1@email.com");
         assertThrows(Exception.class, () -> facade.register(user));
    }

    @Test
    void login() throws Exception {
        var user = new UserData("player1", "password", "p1@email.com");
        facade.register(user);
        var authData = facade.login(user);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginFail() throws Exception {
        var user = new UserData("player1", "password", "p1@email.com");
        facade.register(user);
        var badUser = new UserData("player1", null, "p1@email.com");
        assertThrows(Exception.class, () -> facade.login(badUser));
    }
}
