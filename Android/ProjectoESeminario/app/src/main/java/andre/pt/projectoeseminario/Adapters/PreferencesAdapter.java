package andre.pt.projectoeseminario.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;

import andre.pt.projectoeseminario.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.R;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder>{

    private final Preference[] preferences;
    private final HashMap<String, CompoundButton.OnCheckedChangeListener> switchActions;

    public PreferencesAdapter(Preference[] preferences, HashMap<String, CompoundButton.OnCheckedChangeListener> switchActions){
        this.preferences = preferences;
        this.switchActions = switchActions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.preference_item, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setting_title.setText(preferences[position].getTitle());
        holder.setting_description.setText(preferences[position].getDescription());
        holder.setting_switch.setChecked(preferences[position].getSwitchState());

        holder.setting_switch.setOnCheckedChangeListener(switchActions.get(preferences[position].getTitle()));

    }

    @Override
    public int getItemCount() {
        return preferences.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

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
