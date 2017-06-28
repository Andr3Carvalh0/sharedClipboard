package pt.andre.projecto.Model.DTOs.Wrappers;

import org.bson.conversions.Bson;
import pt.andre.projecto.Model.DTOs.Content;

/*
* Used to represent a table, and its items creation filter
* */
public class ContentWrapper {

    private final Content content;
    private final Bson accountFilter;

    public ContentWrapper(Content content, Bson accountFilter) {
        this.content = content;
        this.accountFilter = accountFilter;
    }

    public Content getContent() {
        return content;
    }
    public Bson getAccountFilter() {
        return accountFilter;
    }
}
