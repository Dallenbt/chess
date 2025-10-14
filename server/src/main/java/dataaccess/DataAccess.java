package dataaccess;

import datamodel.*;

public interface DataAccess {
    void clear();
    void createUser (UserData user);
    UserData getUser(String username);
    String createAuth(String Username);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
