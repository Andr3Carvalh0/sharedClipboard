package pt.andre.projecto.Service.WebSockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.andre.projecto.Model.Database.Interfaces.IPendingRequests;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class PendingRequestsCache {

    @Autowired
    IPendingRequests database;

    //Map where the key is the user sub, value is a map where key is device id, and value is a list of pending messages
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> pending_messages;

    public PendingRequestsCache() {
        pending_messages = new ConcurrentHashMap<>();
    }

    public void addToPendingRequests(String user, String device, String message){
        addToLocalCache(user, device, message);
        database.addRequest(user, device, message);
    }

    private void addToLocalCache(String user, String device, String message){
        pending_messages.computeIfAbsent(user, s -> new ConcurrentHashMap<>());
        pending_messages.get(user).computeIfAbsent(device, s -> new ConcurrentLinkedQueue<>());
        pending_messages.get(user).get(device).add(message);
    }

    public ConcurrentLinkedQueue<String> getPendingMessages(String user, String device){
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> user_devices = pending_messages.get(user);

        if(user_devices == null || user_devices.get(device) == null){
            final List<String> pendingRequests = database.getPendingRequests(user, device);
            pendingRequests.stream()
                   .forEach(m -> addToLocalCache(user, device, m));

            user_devices = pending_messages.get(user);
        }

        database.removeAllRequests(user, device);
        return user_devices.get(device);

    }

}
