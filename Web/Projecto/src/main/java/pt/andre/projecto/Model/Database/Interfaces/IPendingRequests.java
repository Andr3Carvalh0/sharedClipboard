package pt.andre.projecto.Model.Database.Interfaces;

import java.util.List;

public interface IPendingRequests {
    List<String> getPendingRequests(String sub, String device);
    void addRequest(String sub, String device, String message);
    void removeAllRequests(String sub, String device);
}
