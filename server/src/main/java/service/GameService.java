package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import datamodel.GameData;

import java.util.HashMap;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public HashMap<Integer, GameData> listGames(String authToken) throws Exception {
        var auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("Unauthorized");
        }
        return dataAccess.listGames();
    }



}
