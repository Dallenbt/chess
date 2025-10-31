package dataaccess;

import datamodel.*;

public interface DataAccess {
    void clear() throws DataAccessException;
     void createUser(UserData user) throws Exception;
     UserData getUser(String username) throws DataAccessException;
    String createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws  Exception;
    GameData createGame (GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws Exception;
    Iterable<GameData> listGames() throws Exception;
    void updateGame(GameData game) throws Exception;
}
