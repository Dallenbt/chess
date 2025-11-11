package dataaccess;

import datamodel.AuthData;

import java.util.HashMap;
import java.util.UUID;


public class MemoryDataAccess implements DataAccess{
    private final HashMap<String, datamodel.UserData> users = new HashMap<>();
    private final HashMap<String, datamodel.AuthData> auths = new HashMap<>();
    private final HashMap<Integer, datamodel.GameData> games = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
        auths.clear();
        games.clear();
    }

    @Override
    public void createUser(datamodel.UserData user) {
        users.put(user.username(), user);

    }

    @Override
    public datamodel.UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public String createAuth(String username) {
        String token = UUID.randomUUID().toString();
        datamodel.AuthData auth = new datamodel.AuthData(username, token);
        auths.put(token, auth);
        return token;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public datamodel.GameData createGame(datamodel.GameData game) {
        games.put(game.gameID(), game);

        return game;
    }

    @Override
    public datamodel.GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Iterable<datamodel.GameData> listGames() {
        HashMap<Integer, datamodel.GameData> gameInfo = new HashMap<>();
        for(datamodel.GameData entry : games.values()){
            //String white = entry.whiteUsername() == null ? "null" : entry.whiteUsername();
            //String black = entry.blackUsername() == null ? "null" : entry.blackUsername();
            datamodel.GameData info = new datamodel.GameData(entry.gameID(), entry.whiteUsername(), entry.blackUsername(), entry.gameName(), null);
            gameInfo.put(entry.gameID(), info);

        }
        return gameInfo.values();
    }

    @Override
    public void updateGame(datamodel.GameData game) {
        games.replace(game.gameID(), game);

    }
}
