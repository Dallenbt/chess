package dataaccess;

import datamodel.*;

import java.util.HashMap;

public interface DataAccess {
    void clear();
    void createUser (UserData user);
    UserData getUser(String username);
    String createAuth(String username);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
    void createGame (GameData game);
    GameData getGame(int gameID);
    Iterable<GameData> listGames();
    void updateGame(GameData game);
}
