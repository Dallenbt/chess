package dataaccess;

import datamodel.AuthData;

public interface DataAccess {
    void clear() throws DataAccessException;
     void createUser(datamodel.UserData user) throws Exception;
     datamodel.UserData getUser(String username) throws DataAccessException;
    String createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws  Exception;
    datamodel.GameData createGame (datamodel.GameData game) throws DataAccessException;
    datamodel.GameData getGame(int gameID) throws Exception;
    Iterable<datamodel.GameData> listGames() throws Exception;
    void updateGame(datamodel.GameData game) throws Exception;
}
