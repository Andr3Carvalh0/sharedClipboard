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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class WebSocketHandler extends TextWebSocketHandler implements IConnectionManager {

    @Autowired
    private IAPI api;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: WebSocketHandler ";

    //Map where the key is the user sub
    //Each value has the users deviceID as key, and a list of websocket object as value, because there is a small possibility
    //that 2 devices have the same id.
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<WebSocketSession>>> connections;

    //Map where the key is the session id, and the value is the user sub
    private ConcurrentHashMap<String, InformationWrapper> connections_by_id;

    //Map where the key is the user sub, value is a map where key is device id, and value is a list of pending messages
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> pending_messages;

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
        pending_messages = new ConcurrentHashMap<>();
        router = new Router(this);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //Cleanup
        final InformationWrapper user_info = connections_by_id.get(session.getId());
        connections_by_id.remove(session.getId());

        final ConcurrentHashMap<String, ConcurrentLinkedQueue<WebSocketSession>> map = connections.get(user_info.getSub());
        map.remove(user_info.getId());

        logger.info(TAG + "Closed connection with socket " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage){
        try{
            JSONObject json = new JSONObject(jsonTextMessage.getPayload());
            router.route(json, session);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void connectWithDevice(String sub, String id, WebSocketSession session){
        connections.computeIfAbsent(sub, s -> new ConcurrentHashMap<>());
        connections.get(sub).putIfAbsent(id, new ConcurrentLinkedQueue<>());
        connections.get(sub).get(id).add(session);

        connections_by_id.putIfAbsent(session.getId(), new InformationWrapper(sub, id));

        logger.info(TAG + "Connected with socket " + session.getId());
        handlePendingMessages(sub, id, session);

    }

    @Override
    public IAPI getAPI() {
        return api;
    }

    @Override
    public void report(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message.getBytes()));
        } catch (IOException e) {
            logger.error(TAG + "Cannot report message");
        }
    }

    @Override
    public void notify(String sub, String device, String message) {
        logger.info(TAG, "Attempting to send message to device " + device);

        try {

            final ConcurrentLinkedQueue<WebSocketSession> webSocketSession = connections.get(sub).get(device);

            if (webSocketSession.size() == 0) {
                pending_messages.computeIfAbsent(sub, s -> new ConcurrentHashMap<>());
                pending_messages.get(sub).computeIfAbsent(device, s -> new ConcurrentLinkedQueue<>());
                pending_messages.get(sub).get(device).add(message);
                return;
            }

            webSocketSession
                    .forEach(s -> send(message, device, s));

        }catch (NullPointerException e){
            logger.info(TAG + "the user doesnt have any devices");
            pending_messages.computeIfAbsent(sub, s -> new ConcurrentHashMap<>());
            pending_messages.get(sub).computeIfAbsent(device, s -> new ConcurrentLinkedQueue<>());
            pending_messages.get(sub).get(device).add(message);

        }
    }

    private void handlePendingMessages(String sub, String device, WebSocketSession socket) {
        logger.info(TAG + "Attempting to send pending messages to device: " + device);
        final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> user_devices = pending_messages.get(sub);

        if(user_devices == null)
            return;

        final ConcurrentLinkedQueue<String> messages_list = user_devices.get(device);

        logger.info(TAG + "Pending messages to device: " + device + ": " + messages_list.size());

        messages_list.forEach(s -> send(s, device, socket));
    }

    private void send(String message, String device, WebSocketSession socket){
        try {
            socket.sendMessage(new TextMessage(message.getBytes()));
        } catch (IOException e) {
            logger.error(TAG + "Cannot send message to " + device);
            logger.info(TAG + "Removing device from our list");

            try {
                afterConnectionClosed(socket, CloseStatus.NO_STATUS_CODE);
            } catch (Exception e1) {
                //And at this moment we knew we fucked up...
            }
        }
    }
}