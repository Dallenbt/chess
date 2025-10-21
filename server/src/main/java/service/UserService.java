package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import datamodel.AuthData;
import datamodel.UserData;

public class UserService {
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData user) throws Exception{
        if (dataAccess.getUser(user.username()) != null){
            throw new Exception("Already Exists");
        }
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("Bad Request");
        }
        dataAccess.createUser(user);
        return new AuthData(user.username(), dataAccess.createAuth(user.username()));
    }

    public AuthData login(UserData user) throws Exception {
        if (user.username() == null || user.password() == null) {
            throw new DataAccessException("Bad Request");
        }

        var existingUser = dataAccess.getUser(user.username());
        if (existingUser == null) {
            throw new Exception("User does not exist");
        }

        if (!existingUser.password().equals(user.password())) {
            throw new Exception("Wrong Password");
        }

        var authToken = dataAccess.createAuth(user.username());
        return new AuthData(user.username(), authToken);
    }



    public void clear(){
        dataAccess.clear();
    }

    public void logout(String authToken) throws Exception {
        var auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new DataAccessException("Unauthorized");
        }
        dataAccess.deleteAuth(authToken);
    }



}
