package pt.andre.projecto.WebSockets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: WebSocketHandler ";

    //Map where the key is the user sub
    //Each value has the users deviceID as key, and the websocket object as value
    ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>> connections;

    //Map where the key is the session id, and the value is the user sub
    ConcurrentHashMap<String, InformationWrapper> connections_by_id;

    private class InformationWrapper{
        private String sub;
        private String id;

        public InformationWrapper(String sub, String id) {
            this.sub = sub;
            this.id = id;
        }

        public String getSub() {
            return sub;
        }

        public String getId() {
            return id;
        }
    }

    public WebSocketHandler(){
        connections = new ConcurrentHashMap<>();
        connections_by_id = new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //Cleanup
        final InformationWrapper user_info = connections_by_id.get(session.getId());
        connections_by_id.remove(session.getId());

        final ConcurrentHashMap<String, WebSocketSession> map = connections.get(user_info.getSub());
        map.remove(user_info.getId());

        logger.info(TAG, "Closed connection with socket " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        JSONObject json = new JSONObject(jsonTextMessage.getPayload());
        connections.computeIfAbsent(json.getString("sub"), s -> new ConcurrentHashMap<>());
        connections.get(json.getString("sub")).putIfAbsent(json.getString("id"), session);

        connections_by_id.computeIfAbsent(session.getId(), s -> new InformationWrapper(json.getString("sub"), json.getString("id")));

        logger.info(TAG, "Connected with socket " + session.getId());
    }
}