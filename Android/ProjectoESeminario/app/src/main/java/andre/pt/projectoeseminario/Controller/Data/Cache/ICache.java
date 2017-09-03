package andre.pt.projectoeseminario.Controller.Data.Cache;

public interface ICache {
    void store(String value, String category);
    void store(String value);
    String[] pull(String category);
}
