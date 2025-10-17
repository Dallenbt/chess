package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void register() throws Exception {
        DataAccess db = new MemoryDataAccess();
        var user = new UserData("Joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var authData = userService.register(user);
        assertNotNull(authData);
        assertEquals(user.username(), authData.username());
        assertTrue(!authData.authToken().isEmpty());
    }

    @Test
    void registerInvalidUserName() throws Exception {
        DataAccess db = new MemoryDataAccess();
        var user = new UserData(null, "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        assertThrows(Exception.class, () -> userService.register(user));

    }

    @Test
    void login() {
    }

    @Test
    void clear() {
        DataAccess db = new MemoryDataAccess();
        db.createUser(new UserData("Joe", "j@j.com", "toomanysecrets"));
        db.clear();
        assertNull(db.getUser("Joe"));
    }

    @Test
    void logout() {
    }
}