package dataaccess;

import chess.ChessGame;
import datamodel.GameData;
import datamodel.UserData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqlDataAccessTest {

    @Test
    void clear() throws Exception {
        DataAccess db = new SqlDataAccess();
        db.clear();
        var user = new UserData("bob", "bob@email.com","hashedpw" );
        db.createUser(user);
        db.clear();
        assertEquals(null, db.getUser(user.username()));
    }

    @Test
    public void createUserPositive() throws Exception {
        DataAccess db = new SqlDataAccess();
        db.clear();
        var user = new UserData("bob", "bob@email.com","hashedpw" );
        db.createUser(user);
        var found = db.getUser("bob");
        assertEquals("bob", found.username());
    }

    @Test
    void getUser() {
    }

    @Test
    void createAuth() {
    }

    @Test
    void getAuth() {
    }

    @Test
    void deleteAuth() {
    }

    @Test
    void createGamePositive() throws Exception {
        var db = new SqlDataAccess();
        db.clear();

        db.createUser(new UserData("bob", "hashedpw", "bob@email.com"));
        db.createUser(new UserData("alice", "hashedpw", "alice@email.com"));
        var chessGame = new ChessGame();


        var game = new GameData(
                0,
                "bob",
                "alice",
                "Test Match",
                chessGame
        );


        db.createGame(game);
        var games = db.listGames();
        assertNotNull(games);


        var found = games.iterator().next();
        assertEquals("Test Match", found.gameName());
        assertNotNull(found.game());
    }


    @Test
    void createGameNegative() throws Exception {
        var db = new SqlDataAccess();
        db.clear();


        var invalidGame = new GameData(0, null, "whitePlayer", "blackPlayer", new ChessGame());

        assertThrows(DataAccessException.class, () -> {
            db.createGame(invalidGame);
        });
    }



    @Test
    void getGame() {
    }

    @Test
    void listGamesPositive() throws Exception {
        DataAccess db = new SqlDataAccess();
        db.clear();


        db.createUser(new UserData("carol", "hashedpw", "carol@email.com"));
        db.createUser(new UserData("alice", "hashedpw", "alice@email.com"));
        var game1 = new GameData(0, "carol", "alice", "bob", new ChessGame());
        var game2 = new GameData(0, "alice", "carol", "dave", new ChessGame());


        db.createGame(game1);
        db.createGame(game2);
        var games = db.listGames();
        assertNotNull(games, "listGames() should not return null");


        var gameList = new ArrayList<GameData>();
        games.forEach(gameList::add);

        assertEquals(2, gameList.size(), "Should return 2 games");
    }


    @Test
    void listGamesEmpty() throws Exception {
        DataAccess db = new SqlDataAccess();
        db.clear();

        // Act
        var games = db.listGames();

        // Assert
        assertNotNull(games, "listGames() should not return null");
        assertFalse(((List<GameData>) games).iterator().hasNext(), "Should return empty list");
    }


    @Test
    void updateGamePositive() throws Exception {
        DataAccess db = new SqlDataAccess();
        db.clear();

        db.createUser(new UserData("white", "hashedpw", "white@email.com"));
        db.createUser(new UserData("black", "hashedpw", "black@email.com"));
        var game = new GameData(0, "white", "black", "old", new ChessGame());
        db.createGame(game);


        var insertedGame = db.listGames().iterator().next();
        int gameID = insertedGame.gameID();
        var updated = new GameData(gameID, "white", "black", "new", new ChessGame());
        db.updateGame(updated);

        var retrieved = db.getGame(gameID);
        assertEquals("new", retrieved.gameName());
        assertEquals("white", retrieved.whiteUsername());
        assertEquals("black", retrieved.blackUsername());
    }

    @Test
    void updateGameNegative() throws Exception {
        DataAccess db = new SqlDataAccess();
        db.clear();

        var nonExistentGame = new GameData(
                999,
                "Phantom Match",
                "nobody",
                "ghost",
                new ChessGame()
        );


        assertThrows(DataAccessException.class, () -> {
            db.updateGame(nonExistentGame);
        });
    }


}