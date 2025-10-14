package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import datamodel.AuthData;
import datamodel.UserData;

import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData user) throws Exception{
        if (dataAccess.getUser(user.username()) != null){
            throw new Exception("Already exists");
        }
        if (user.username() == null || user.password() == null || user.email() == null)
            throw new DataAccessException("Bad Request");
        dataAccess.createUser(user);
        return new AuthData(user.username(),generateAuthToken());
    }

    public void clear(){
        dataAccess.clear();
    }


    private String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
