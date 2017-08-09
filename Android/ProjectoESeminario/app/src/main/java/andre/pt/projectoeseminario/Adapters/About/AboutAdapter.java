package andre.pt.projectoeseminario.Adapters.About;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.util.function.Function;

import andre.pt.projectoeseminario.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.Adapters.ParentAdapter;
import andre.pt.projectoeseminario.Adapters.Settings.PreferencesAdapter;
import andre.pt.projectoeseminario.R;

public class AboutAdapter extends ParentAdapter {
    public AboutAdapter(Object[] items, int layout, Function generateViewHolder) {
        super(items, layout, generateViewHolder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //((PreferencesAdapter.ViewHolder)holder).setting_title.setText(((Preference)items[position]).getTitle());
        //((PreferencesAdapter.ViewHolder)holder).setting_description.setText(((Preference)items[position]).getDescription());
        //((PreferencesAdapter.ViewHolder)holder).setting_switch.setChecked(((Preference)items[position]).getSwitchState());

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
