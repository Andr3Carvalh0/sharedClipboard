package pt.andre.projecto.Model.DTOs;

import org.bson.Document;
import java.util.List;

public class PendingRequest {
    private String token;
    private List<Document> values;

    public PendingRequest(String token, List<Document> values) {
        this.token = token;
        this.values = values;
    }

    public String getToken() {
        return token;
    }

    public List<Document> getValues() {
        return values;
    }
}
