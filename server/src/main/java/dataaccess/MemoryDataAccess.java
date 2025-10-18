package dataaccess;

import chess.ChessGame;
import datamodel.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MemoryDataAccess implements DataAccess{
    private final HashMap<String, UserData> users = new HashMap<>();
    private final HashMap<String, AuthData> auths = new HashMap<>();
    private final HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
        auths.clear();
        games.clear();
    }

    @Override
    public void createUser(UserData user) {
        users.put(user.username(), user);

    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public String createAuth(String username) {
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(username, token);
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
    public void createGame(GameData game) {
        games.put(game.gameID(), game);

    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Iterable<GameData> listGames() {
        HashMap<Integer, GameData> gameInfo = new HashMap<>();
        for(GameData entry : games.values()){
            String white = entry.whiteUsername() == null ? "null" : entry.whiteUsername();
            String black = entry.blackUsername() == null ? "null" : entry.blackUsername();
            GameData info = new GameData(entry.gameID(), white, black, entry.gameName(), null);
            gameInfo.put(entry.gameID(), info);

        }
        return gameInfo.values();
    }

    @Override
    public void updateGame(GameData game) {
        games.replace(game.gameID(), game);

    }
}
