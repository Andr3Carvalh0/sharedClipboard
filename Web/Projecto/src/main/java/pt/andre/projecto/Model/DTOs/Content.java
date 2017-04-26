package pt.andre.projecto.Model.DTOs;

/**
 * Created by Andre on 26/04/2017.
 */
public class Content {
    private long token;
    private String content;

    public Content(long token, String content) {
        this.token = token;
        this.content = content;
    }

    public long getToken() {
        return token;
    }

    public String getContent() {
        return content;
    }
}
