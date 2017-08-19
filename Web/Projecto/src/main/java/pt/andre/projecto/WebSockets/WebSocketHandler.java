package pt.andre.projecto.WebSockets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pt.andre.projecto.Controllers.Interfaces.IAPI;
import pt.andre.projecto.WebSockets.Interfaces.IConnectionManager;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler implements IConnectionManager {

    @Autowired
    private IAPI api;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: WebSocketHandler ";

    //Map where the key is the user sub
    //Each value has the users deviceID as key, and the websocket object as value
    private ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>> connections;

    //Map where the key is the session id, and the value is the user sub
    private ConcurrentHashMap<String, InformationWrapper> connections_by_id;

    private Router router;

    public class InformationWrapper{
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
        router = new Router(this);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //Cleanup
        final InformationWrapper user_info = connections_by_id.get(session.getId());
        connections_by_id.remove(session.getId());

        final ConcurrentHashMap<String, WebSocketSession> map = connections.get(user_info.getSub());
        map.remove(user_info.getId());

        logger.info(TAG + "Closed connection with socket " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        JSONObject json = new JSONObject(jsonTextMessage.getPayload());
        router.route(json, session);

    }

    @Override
    public void connectWithDevice(String sub, String id, WebSocketSession session){
        connections.computeIfAbsent(sub, s -> new ConcurrentHashMap<>());
        connections.get(sub).putIfAbsent(id, session);

        connections_by_id.putIfAbsent(session.getId(), new InformationWrapper(sub, id));

        logger.info(TAG + "Connected with socket " + session.getId());
    }

    @Override
    public IAPI getAPI() {
        return api;
    }

    @Override
    public void notify(String sub, String device, String message) {
        logger.info(TAG, "Attempting to send message to device " + device);

        final WebSocketSession webSocketSession = connections.get(sub).get(device);

        try {
            webSocketSession.sendMessage(new TextMessage(message.getBytes()));
            logger.info(TAG + "Message sent to device " + device);
        } catch (IOException e) {
            logger.error(TAG + "Cannot send message to " + device);
            logger.info(TAG + "Removing device from our list");

            try {
                afterConnectionClosed(webSocketSession, CloseStatus.NO_STATUS_CODE);
            } catch (Exception e1) {
                //And at this moment I knew...I fucked up.
            }
        }
    }
}