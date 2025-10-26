package dataaccess;

import datamodel.*;

import java.util.HashMap;

public interface DataAccess {
    void clear() throws DataAccessException;
     void createUser(UserData user) throws Exception;
     UserData getUser(String username) throws DataAccessException;
    String createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken);
    void createGame (GameData game) throws DataAccessException;
    GameData getGame(int gameID);
    Iterable<GameData> listGames();
    void updateGame(GameData game);
}
