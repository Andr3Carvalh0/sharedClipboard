package pt.andre.projecto.Model.Database.Utils;


import com.mongodb.client.model.ValidationOptions;

public class Collection {

    private final String name;
    private final ValidationOptions options;

    public Collection(String name, ValidationOptions options){
        this.name = name;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public ValidationOptions getOptions() {
        return options;
    }


}
