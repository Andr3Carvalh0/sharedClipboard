package andre.pt.projectoeseminario.Cache;

import java.util.List;

public interface ICache {
    void store(String value, String category);
    void store(String value);
    String[] pull(String category);
}
