package pt.andre.projecto.WebSockets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;
import pt.andre.projecto.Controllers.Interfaces.IAPI;
import pt.andre.projecto.Controllers.URIs.API;
import pt.andre.projecto.WebSockets.Interfaces.IConnectionManager;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class Router {

    private IConnectionManager connectionManager;
    private HashMap<String, BiConsumer<JSONObject, WebSocketSession>> actions = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: Router ";

    public Router(IConnectionManager connectionManager){
        actions.put("push", this::handlePush);
        actions.put("pushMime", this::handlePushMIME);
        actions.put("register", this::handleRegister);
        this.connectionManager = connectionManager;
    }

    private void handleRegister(JSONObject json, WebSocketSession session) {
        logger.info(TAG + "calling handleRegister");
        connectionManager.connectWithDevice(json.getString("sub"), json.getString("id"), session);
    }

    private void handlePush(JSONObject json, WebSocketSession session){
        logger.info(TAG + "calling handlePush");
        connectionManager.getAPI().push(json.getString("sub"), json.getString("data"));
    }

    //side note the file on windows must come as string
    private void handlePushMIME(JSONObject json, WebSocketSession session) {
        logger.info(TAG + "calling handlePushMime");
        connectionManager.getAPI().push(json.getString("sub"),  Base64.getDecoder().decode(json.getString("data")), json.getString("filename"));
    }

    void route(JSONObject json, WebSocketSession session) {
        try {
            final BiConsumer<JSONObject, WebSocketSession> action = actions.get(json.getString("action"));
            action.accept(json, session);
        }catch (Exception e){
            logger.error(TAG + "Something wrong with the request.Probably it didnt have an action field");
            try {
                session.close();
            } catch (IOException e1) {

            }
        }
    }
}
