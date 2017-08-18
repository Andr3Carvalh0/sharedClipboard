package pt.andre.projecto.Controllers.URIs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Controllers.Interfaces.INotify;
import pt.andre.projecto.WebSockets.Interfaces.IConnectionManager;

import java.util.Arrays;

public class WebSocketService implements INotify{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: Websocket ";

    @Autowired
    protected IConnectionManager webSocketHandler;

    public WebSocketService(){
        logger.info(TAG + "CTOR" );
    }

    public void notify(String sub, String messageJSON, String... devices){
        Arrays.stream(devices)
                .forEach(s -> webSocketHandler.notify(sub, s, messageJSON));
    }
}
