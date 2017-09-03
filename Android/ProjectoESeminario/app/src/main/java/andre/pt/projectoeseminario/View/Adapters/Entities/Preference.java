package andre.pt.projectoeseminario.View.Adapters.Entities;


/**
 * Represents one setting. We assume that all of the settings will be composed of a title, a description
 * and a switch to indicate/switch the state.
*/
public class Preference {
    //@param title: the prefence title
    //@param description: a brief description of what doesnt the preference do
    private final String title, description;
    // Its state
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
