package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import datamodel.GameData;

import java.lang.module.FindException;
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
            throw new RuntimeException("Unauthorized");
        }
        return dataAccess.listGames();
    }

    public GameData createGame(String authToken, String gameName) throws Exception {
        var auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new RuntimeException("Unauthorized");
        }
        if (gameName == null){
            throw new Exception();
        }
        ChessGame board = new ChessGame();
        GameData game = new GameData(0, null, null, gameName, board);
        var madeGame = dataAccess.createGame(game);
        return madeGame;
    }

    public void joinGame(String authToken, String color, int id) throws Exception{
        var auth = dataAccess.getAuth(authToken);
        var game = dataAccess.getGame(id);
        if (auth == null) {
            throw new RuntimeException("Unauthorized");
        }
        if (game == null){
            throw new NullPointerException();
        }
        if (!color.equalsIgnoreCase("WHITE") && !color.equalsIgnoreCase("BLACK")){
            throw new NullPointerException();
        }
        if (game.whiteUsername() != null && game.blackUsername() != null) {
            throw new IllegalAccessException("Game is full");
        }
        if (color.equals("WHITE") && game.whiteUsername() != null) {
            throw new IllegalAccessException("White seat is already taken");
        }
        if (color.equals("BLACK") && game.blackUsername() != null) {
            throw new IllegalAccessException("Black seat is already taken");
        }
        if (color.equals("WHITE")){
            GameData update = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
            dataAccess.updateGame(update);
        }
        if (color.equals("BLACK")){
            GameData update = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
            dataAccess.updateGame(update);
        }

    }

}
