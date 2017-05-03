package pt.andre.projecto.Model.DTOs;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by Andre on 03/05/2017.
 */
public class Wrapper {

    private final Content content;
    //private final MongoCollection<Document> collection;
    private final Bson accountFilter;

    public Wrapper(Content content, //MongoCollection<Document> collection,
                   Bson accountFilter) {
        this.content = content;
        //this.collection = collection;
        this.accountFilter = accountFilter;
    }

    public Content getContent() {
        return content;
    }

  /*  public MongoCollection<Document> getCollection() {
        return collection;
    }*/

    public Bson getAccountFilter() {
        return accountFilter;
    }
}
