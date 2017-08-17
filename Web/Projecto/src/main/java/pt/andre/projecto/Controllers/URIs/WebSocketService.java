package pt.andre.projecto.Controllers.URIs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Controllers.Interfaces.INotify;
import pt.andre.projecto.WebSockets.WebSocketHandler;

public class WebSocketService implements INotify{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: Websocket ";

    @Autowired
    protected WebSocketHandler webSocketHandler;

    public WebSocketService(){
        logger.info(TAG + "CTOR" );
    }

    public boolean notify(String messageJSON, String... devices){


        return false;
    }
}
