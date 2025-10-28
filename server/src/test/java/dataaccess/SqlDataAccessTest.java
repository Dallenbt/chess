package dataaccess;

import datamodel.UserData;
import org.junit.jupiter.api.Test;

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
    void listGames() {
    }

    @Test
    void updateGame() {
    }
}