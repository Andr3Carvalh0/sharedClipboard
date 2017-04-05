package pt.andre.projecto.Model.Database.Utils;

public class DatabaseOption {
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
