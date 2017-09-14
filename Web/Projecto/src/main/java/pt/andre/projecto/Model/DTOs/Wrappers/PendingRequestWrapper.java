package pt.andre.projecto.Model.DTOs.Wrappers;

import org.bson.conversions.Bson;
import pt.andre.projecto.Model.DTOs.PendingRequest;

/*
* Used to represent a table, and its items creation filter
* */
public class PendingRequestWrapper {

    private final PendingRequest content;
    private final Bson accountFilter;

    public PendingRequestWrapper(PendingRequest content, Bson accountFilter) {
        this.content = content;
        this.accountFilter = accountFilter;
    }

    public PendingRequest getContent() {
        return content;
    }
    public Bson getAccountFilter() {
        return accountFilter;
    }
}
