package dataaccess;

import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class SqlDataAccess implements DataAccess{

    private UUID UUID;

    public SqlDataAccess(){
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        }
        catch (Exception ex){

        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM auth");
            stmt.executeUpdate("DELETE FROM user");
            stmt.executeUpdate("DELETE FROM game");
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing database", e);
        }
    }


    @Override
    public void createUser(UserData user) throws DataAccessException {
        var sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
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
        var sql = "SELECT username, password, email FROM user WHERE username=?";
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
                return null; // not found
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
        var sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
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
        var sql = "SELECT authToken, username FROM auth WHERE authToken=?";
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
    public void deleteAuth(String authToken) {

    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        var sql = "INSERT INTO game (gameName, whiteUsername, blackUsername, gameJSON) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setString(1, game.gameName());
            ps.setString(2, game.whiteUsername());
            ps.setString(3, game.blackUsername());
            ps.setString(4, game.game().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating game", e);
        }
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
