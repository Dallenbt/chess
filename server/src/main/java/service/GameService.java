package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import datamodel.GameData;

import java.util.HashMap;
import java.util.UUID;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public Iterable<GameData> listGames(String authToken) throws Exception {
        var auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("Unauthorized");
        }
        return dataAccess.listGames();
    }

    public GameData createGame(String authToken, String gameName) throws Exception {
        var auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("Unauthorized");
        }
        if (gameName == null){
            throw new Exception();
        }
        int id = Math.abs(UUID.randomUUID().hashCode());
        ChessGame board = new ChessGame();
        GameData game = new GameData(id, "", "", gameName,board);
        dataAccess.createGame(game);
        return game;
    }

    public void joinGame(String authToken, String color, int ID) throws Exception{
        var auth = dataAccess.getAuth(authToken);
        var game = dataAccess.getGame(ID);
        if (auth == null) {
            throw new DataAccessException("Unauthorized");
        }
        if (game == null){
            throw new Exception();
        }
        if (color != "WHITE" || color != "BLACK"){
            throw new Exception();
        }
        if (game.whiteUsername() != "" && game.blackUsername() != ""){
            throw new IllegalAccessException();
        }
        if (color == "WHITE"){
            GameData update = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
            dataAccess.updateGame(update);
        }
        if (color == "BLACK"){
            GameData update = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
            dataAccess.updateGame(update);
        }

    }




}
