package ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;

import java.lang.reflect.Type;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    public String token;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clear() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null, null);
        sendRequest(request);
    }



    public AuthData register(UserData register) throws ResponseException {
        var request = buildRequest("POST", "/user", register, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(UserData login) throws ResponseException {
        var request = buildRequest("POST", "/session", login, null);
        var response = sendRequest(request);
        var data = handleResponse(response, AuthData.class);
        assert data != null;
        this.token = data.authToken();
        return data;
    }

    public void logout() throws ResponseException {
        var request = buildRequest("DELETE", "/session", null, token);
        sendRequest(request);
    }

    public HashMap<String, List<GameData>> listGames() throws ResponseException {
        var request = buildRequest("GET", "/game", null, token);
        var response = sendRequest(request);
        Type type = new TypeToken<HashMap<String, List<GameData>>>() {}.getType();
        return handleResponse(response, type);
    }


    public Map<String, Double> createGame(String gameName) throws ResponseException {
        record CreateGameRequest(String gameName) {}
        var requestBody = new CreateGameRequest(gameName);
        var request = buildRequest("POST", "/game", requestBody, token);
        var response = sendRequest(request);
        return (Map<String, Double>) handleResponse(response, Map.class);
    }



    public void joinGame(String playerColor, Double gameID) throws ResponseException {
        record JoinGameRequest(String playerColor, Double gameID) {}
        var requestBody = new JoinGameRequest(playerColor, gameID);
        var request = buildRequest("PUT", "/game", requestBody, token);
        var response = sendRequest(request);
        handleResponse(response, null);
    }






    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var builder = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));

        if (body != null) {
            builder.setHeader("Content-Type", "application/json");
        }

        if (authToken != null) {
            builder.setHeader("Authorization", authToken);
        }

        return builder.build();
    }


    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }
    private <T> T handleResponse(HttpResponse<String> response, java.lang.reflect.Type type) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        return new Gson().fromJson(response.body(), type);
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
