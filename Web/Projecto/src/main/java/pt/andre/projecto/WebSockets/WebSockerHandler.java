package pt.andre.projecto.WebSockets;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class WebSockerHandler extends TextWebSocketHandler {

    private List<Session> sessions = new ArrayList<>();

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.forEach(s -> {
            if(s.getSocket().getId().equals(session.getId())){
                sessions.remove(s);
            }

        });
    }

    public void sendMessage(String token, String message) throws InterruptedException {
        sessions.forEach(s -> {

            if(s.getSocket().getId().equals(token))
                try {
                    s.getSocket().sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        System.out.println(("message received: " + jsonTextMessage.getPayload()));

        final boolean[] contains = {false};

        sessions.forEach(s -> {

            if(s.getSocket().getId().equals(session.getId()))
                contains[0] = true;
        });

        if(!contains[0])
            sessions.add(new Session(session, jsonTextMessage.getPayload()));
    }

}
