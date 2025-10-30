package dataaccess;

import chess.ChessGame;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class SqlDataAccess implements DataAccess {

    private UUID UUID;
    private final Gson gson = new Gson();

    public SqlDataAccess() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        } catch (Exception ex) {

        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM authData");
            stmt.executeUpdate("DELETE FROM userData");
            stmt.executeUpdate("DELETE FROM gameData");
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing database", e);
        }
    }


    @Override
    public void createUser(UserData user) throws DataAccessException {
        var sql = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.username());
            ps.setString(2, user.password());  // Make sure this is a hashed password
            ps.setString(3, user.email());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating user", e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserData getUser(String username) throws DataAccessException {
        var sql = "SELECT username, password, email FROM userData WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserData(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error reading user", e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        var token = UUID.randomUUID().toString();
        var sql = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setString(1, token);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating auth token", e);
        }
        return token;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var sql = "SELECT authToken, username FROM authData WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setString(1, authToken);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(rs.getString("authToken"), rs.getString("username"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error reading auth", e);
        }
    }


    @Override
    public void deleteAuth(String authToken) throws Exception {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE authToken FROM authData");
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing database", e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        var sql = "INSERT INTO gameData (gameName, whiteUsername, blackUsername, gameJSON) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            String gameJson = gson.toJson(game.game());

            ps.setString(1, game.gameName());
            ps.setString(2, game.whiteUsername());
            ps.setString(3, game.blackUsername());
            ps.setString(4, gameJson);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating game", e);
        }
    }


    @Override
    public GameData getGame(int gameID) throws Exception {
        var sql = "SELECT * FROM gameData WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, gameID);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String gameJson = rs.getString("gameJSON");
                    ChessGame gameObj = gson.fromJson(gameJson, ChessGame.class);

                    return new GameData(
                            rs.getInt("gameID"),
                            rs.getString("gameName"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            gameObj
                    );
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error reading game from database", e);
        }
    }


    @Override
    public Iterable<GameData> listGames() throws DataAccessException {
        var sql = "SELECT * FROM gameData";
        var games = new ArrayList<GameData>();

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                String gameJson = rs.getString("gameJSON");
                ChessGame gameObj = gson.fromJson(gameJson, ChessGame.class);

                games.add(new GameData(
                        rs.getInt("gameID"),
                        rs.getString("gameName"),
                        rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),
                        gameObj
                ));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error reading games from database", e);
        }

        return games;
    }


    @Override
    public void updateGame(GameData game) throws DataAccessException {
        var sql = """
                UPDATE gameData
                SET gameName = ?, whiteUsername = ?, blackUsername = ?, gameJSON = ?
                WHERE gameID = ?
                """;

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {

            String gameJson = gson.toJson(game.game());

            ps.setString(1, game.gameName());
            ps.setString(2, game.whiteUsername());
            ps.setString(3, game.blackUsername());
            ps.setString(4, gameJson);
            ps.setInt(5, game.gameID());


        } catch (SQLException e) {
            throw new DataAccessException("Error updating game", e);
        }
    }
}
