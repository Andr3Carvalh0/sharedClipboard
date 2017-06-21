package andre.pt.projectoeseminario.Adapters.Clipboard;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;

import andre.pt.projectoeseminario.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.Adapters.ParentAdapter;
import andre.pt.projectoeseminario.R;

public class ClipboardAdapter extends ParentAdapter {

    public ClipboardAdapter(Preference[] preferences, int layout){
        super(preferences, layout, (v) -> new ClassViewHolder((View)v));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    static class ClassViewHolder extends RecyclerView.ViewHolder {

        TextView category_text;
        ImageView category_icon;
        ImageView action_switch;

        ClassViewHolder(View itemView) {
            super(itemView);
            category_text = (TextView) itemView.findViewById(R.id.category_text);
            category_icon = (ImageView) itemView.findViewById(R.id.category_icon);
            action_switch = (ImageView) itemView.findViewById(R.id.action_switch);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView setting_title;
        TextView setting_description;
        Switch setting_switch;

        ItemViewHolder(View itemView) {
            super(itemView);
            setting_title = (TextView) itemView.findViewById(R.id.action_title);
            setting_description = (TextView) itemView.findViewById(R.id.action_description);
            setting_switch = (Switch) itemView.findViewById(R.id.action_switch);
        }
    }

}
