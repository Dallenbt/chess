package dataaccess;

import datamodel.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {

    @Test
    void clear() {

    }

    @Test
    void createUser() {
        DataAccess db = new MemoryDataAccess();
        var user = new UserData("Joe", "j@j.com", "toomanysecrets");
        db.createUser(user);
        assertEquals(user, db.getUser(user.username()));
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
}