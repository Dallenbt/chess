package dataaccess;

import chess.ChessGame;
import datamodel.GameData;
import datamodel.UserData;
import org.junit.jupiter.api.Test;

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
    void createGame() {
    }

    @Test
    void getGame() {
    }

    @Test
    void listGamesPositive() throws Exception {
        DataAccess db = new SqlDataAccess();
        var game1 = new GameData(1, "Training Match", "alice", "bob", new ChessGame());
        var game2 = new GameData(2, "Ranked Match", "carol", "dave", new ChessGame());

        db.createGame(game1);
        db.createGame(game2);


        var games = db.listGames();

        assertNotNull(games, "listGames() should not return null");
        var gameList = (List<GameData>) games;
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
        var game = new GameData(1, "white", "black", "old", new ChessGame());
        db.createGame(game);

        var updated = new GameData(1, "white", "black", "new", new ChessGame());
        db.updateGame(updated);

        var retrieved = db.getGame(1);
        assertEquals("new", retrieved.gameName());
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