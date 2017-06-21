package andre.pt.projectoeseminario.Adapters.Clipboard;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import andre.pt.projectoeseminario.Adapters.ParentAdapter;
import andre.pt.projectoeseminario.R;

public class ClipboardCategoryDetailedAdapter extends ParentAdapter {

    public ClipboardCategoryDetailedAdapter(String[] categories){
        super(categories, R.layout.clipboard_item, (v) -> new ItemViewHolder((View)v));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView category_text;
        ImageView category_icon;
        ImageView action_switch;

        ItemViewHolder(View itemView) {
            super(itemView);
            category_text = (TextView) itemView.findViewById(R.id.category_text);
            category_icon = (ImageView) itemView.findViewById(R.id.category_icon);
            action_switch = (ImageView) itemView.findViewById(R.id.action_switch);
        }
    }

}
