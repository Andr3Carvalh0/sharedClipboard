package pt.andre.projecto.Model.DTOs.Wrappers;

import org.bson.conversions.Bson;
import pt.andre.projecto.Model.DTOs.Content;
import pt.andre.projecto.Model.DTOs.User;

/**
 * Created by André Carvalho on 28/06/2017.
 */
public class UserWrapper {

    private final User content;
    private final Bson accountFilter;

    public UserWrapper(User content, Bson accountFilter) {
        this.content = content;
        this.accountFilter = accountFilter;
    }

    public User getUser() {
        return content;
    }
    public Bson getAccountFilter() {
        return accountFilter;
    }
}
