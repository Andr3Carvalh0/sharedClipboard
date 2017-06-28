package pt.andre.projecto.WebSockets;

import org.springframework.web.socket.WebSocketSession;

/**
 * Created by André Carvalho on 28/06/2017.
 */
public class Session {
    private final WebSocketSession socket;
    private final String token;


    public Session(WebSocketSession socket, String token) {
        this.socket = socket;
        this.token = token;
    }

    public WebSocketSession getSocket() {
        return socket;
    }

    public String getToken() {
        return token;
    }
}
