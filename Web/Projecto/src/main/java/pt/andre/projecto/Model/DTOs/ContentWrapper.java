package pt.andre.projecto.Model.DTOs;

import org.bson.conversions.Bson;

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
