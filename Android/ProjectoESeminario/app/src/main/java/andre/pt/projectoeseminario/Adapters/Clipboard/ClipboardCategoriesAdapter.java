package andre.pt.projectoeseminario.Adapters.Clipboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import andre.pt.projectoeseminario.Adapters.ParentAdapter;
import andre.pt.projectoeseminario.R;

public class ClipboardCategoriesAdapter extends ParentAdapter {
    private static final List<CategoryResources> categories = new LinkedList<>();
    private Context ctx;

    static {
        categories.add(new CategoryResources(R.string.Clipboard_Category_Text, R.drawable.text));
        categories.add(new CategoryResources(R.string.Clipboard_Category_Images, R.drawable.images));
        categories.add(new CategoryResources(R.string.Clipboard_Category_Contacts, R.drawable.contacts));
        categories.add(new CategoryResources(R.string.Clipboard_Category_Links, R.drawable.links));
    }


    private static class CategoryResources{
        private int stringResource;
        private int imageResource;

        CategoryResources(int message, int image){
            this.stringResource = message;
            this.imageResource = image;
        }

        int getStringResource() {
            return stringResource;
        }

        int getImageResource() {
            return imageResource;
        }
    }

    public ClipboardCategoriesAdapter(Context ctx){
        super(categories.toArray(), R.layout.clipboard_class_item, (v) -> new ClassViewHolder((View)v));
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClassViewHolder)holder).category_text.setText(ctx.getString(categories.get(position).getStringResource()));
        ((ClassViewHolder)holder).category_icon.setBackground(ctx.getDrawable(categories.get(position).getImageResource()));
   }


    private static class ClassViewHolder extends RecyclerView.ViewHolder {

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

}
