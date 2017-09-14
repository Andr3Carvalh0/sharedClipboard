package andre.pt.projectoeseminario.View.Adapters.Settings;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;

import andre.pt.projectoeseminario.R;
import andre.pt.projectoeseminario.View.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.View.Adapters.ParentAdapter;

/**
 * Represents one preference Item that will be shown by the RecyclerView
*/
public class PreferencesAdapter extends ParentAdapter {

    private final HashMap<String, CompoundButton.OnCheckedChangeListener> switchActions;

    public PreferencesAdapter(Preference[] preferences, HashMap<String, CompoundButton.OnCheckedChangeListener> switchActions){
        super(preferences, R.layout.preference_item, (v) -> new PreferencesAdapter.ViewHolder((View)v));
        this.switchActions = switchActions;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).setting_title.setText(((Preference)items[position]).getTitle());
        ((ViewHolder)holder).setting_description.setText(((Preference)items[position]).getDescription());
        ((ViewHolder)holder).setting_switch.setChecked(((Preference)items[position]).getSwitchState());

        ((ViewHolder)holder).setting_switch.setOnCheckedChangeListener(switchActions.get(((Preference)items[position]).getTitle()));

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        TextView setting_title;
        TextView setting_description;
        Switch setting_switch;

        ViewHolder(View itemView) {
            super(itemView);
            setting_title = (TextView) itemView.findViewById(R.id.action_title);
            setting_description = (TextView) itemView.findViewById(R.id.action_description);
            setting_switch = (Switch) itemView.findViewById(R.id.action_switch);
        }
    }
}
