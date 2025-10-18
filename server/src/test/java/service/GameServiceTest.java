package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void listGames() throws Exception {
        DataAccess db = new MemoryDataAccess();
        var user = new UserData("Joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var gameService = new GameService(db);
        var authData = userService.register(user);
        var game = gameService.createGame(authData.authToken(), "dubbin");
        var listOfGames = gameService.listGames(authData.authToken());
        assertNotNull(listOfGames);
    }

    @Test
    void listGamesFail() throws Exception {
        DataAccess db = new MemoryDataAccess();
        var user = new UserData("Joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var gameService = new GameService(db);
        var authData = userService.register(user);
        var game = gameService.createGame(authData.authToken(), "dubbin");
        assertThrows(Exception.class, () ->gameService.listGames(authData.username()));

    }

    @Test
    void createGame() throws Exception {
        DataAccess db = new MemoryDataAccess();
        var user = new UserData("Joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var gameService = new GameService(db);
        var authData = userService.register(user);
        var game = gameService.createGame(authData.authToken(), "dubbin");
        assertEquals("dubbin", game.gameName());

    }
    @Test
    void createGameFail() throws Exception {
        DataAccess db = new MemoryDataAccess();
        var user = new UserData("Joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var gameService = new GameService(db);
        var authData = userService.register(user);
        assertThrows(Exception.class, () -> gameService.createGame(authData.authToken(), null));

    }

    @Test
    void joinGame() throws Exception {
        DataAccess db = new MemoryDataAccess();
        var user = new UserData("Joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var gameService = new GameService(db);
        var authData = userService.register(user);
        var game = gameService.createGame(authData.authToken(), "dubbin");
        gameService.joinGame(authData.authToken(), "WHITE", game.gameID());
        var newgame = db.getGame(game.gameID());
        assertEquals("Joe", newgame.whiteUsername());
    }

    @Test
    void joinGameFail() throws Exception {
        DataAccess db = new MemoryDataAccess();
        var user = new UserData("Joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var gameService = new GameService(db);
        var authData = userService.register(user);
        var game = gameService.createGame(authData.authToken(), "dubbin");
        assertThrows(Exception.class, () -> gameService.joinGame(authData.authToken(), "grey", game.gameID()));
    }
}