package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void listGames() {
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
}