package pt.andre.projecto.WebSockets.Interfaces;

import org.springframework.web.socket.WebSocketSession;
import pt.andre.projecto.Controllers.Interfaces.IAPI;

public interface IConnectionManager {
    void notify(String sub, String device, String message);
    void connectWithDevice(String sub, String id, WebSocketSession session);
    IAPI getAPI();
}
