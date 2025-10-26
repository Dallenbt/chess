package dataaccess;

import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;

import java.sql.SQLException;

public class SqlDataAccess implements DataAccess{

    public SqlDataAccess(){
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        }
        catch (Exception ex){

        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void createUser(UserData user) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public String createAuth(String username) {
        return "";
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void createGame(GameData game) {

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Iterable<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData game) {

    }
}
