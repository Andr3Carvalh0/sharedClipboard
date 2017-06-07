package pt.andre.projecto.Model.Database.Utils;

/*
* Represents a object that is used to create a table
* */
public class DatabaseOption {

    //@param name: the table name
    //@param primaryKey: its primary key
    private final String name, primaryKey;

    public DatabaseOption(String name, String primaryKey) {
        this.name = name;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }
}
