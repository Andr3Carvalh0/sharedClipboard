package pt.andre.projecto.WebSockets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import pt.andre.projecto.Controllers.Interfaces.IAPI;
import pt.andre.projecto.WebSockets.Interfaces.IConnectionManager;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class Router {

    private final IConnectionManager connectionManager;
    private final IAPI api;
    private HashMap<String, BiConsumer<JSONObject, WebSocketSession>> actions = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: Router ";

    public Router(IConnectionManager connectionManager, IAPI api){
        actions.put("push", this::handlePush);
        actions.put("pushMime", this::handlePushMIME);
        actions.put("register", this::handleRegister);
        this.connectionManager = connectionManager;
        this.api = api;
    }

    private void handleRegister(JSONObject json, WebSocketSession session) {
        logger.info(TAG + "calling handleRegister");
        connectionManager.connectWithDevice(json.getString("sub"), json.getString("id"), session);
    }

    private void handlePush(JSONObject json, WebSocketSession session){
        logger.info(TAG + "calling handlePush");
        api.push(json.getString("token"), json.getString("data"));
    }

    //side note the file on windows must come as string
    private void handlePushMIME(JSONObject json, WebSocketSession session) {
        logger.info(TAG + "calling handlePushMime");
        api.push(json.getString("token"), json.getString("file").getBytes(), json.getString("filename"));
    }

    void route(JSONObject json, WebSocketSession session) {
        try {
            final BiConsumer<JSONObject, WebSocketSession> action = actions.get(json.getString("action"));
            action.accept(json, session);
        }catch (Exception e){
            logger.error(TAG + "Something wrong with the request.Probably it didnt have an action field");
        }
    }
}
