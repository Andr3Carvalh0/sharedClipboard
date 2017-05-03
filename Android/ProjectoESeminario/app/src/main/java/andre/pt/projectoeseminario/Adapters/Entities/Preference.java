package andre.pt.projectoeseminario.Adapters.Entities;


public class Preference {
    private final String title, description;
    private boolean switchState;

    public Preference(String title, String description, boolean switchState) {
        this.title = title;
        this.description = description;
        this.switchState = switchState;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getSwitchState() {
        return switchState;
    }
}
