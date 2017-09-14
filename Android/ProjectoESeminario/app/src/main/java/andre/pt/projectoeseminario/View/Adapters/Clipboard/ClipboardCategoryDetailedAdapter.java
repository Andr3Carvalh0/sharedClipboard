package andre.pt.projectoeseminario.View.Adapters.Clipboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.function.Consumer;

import andre.pt.projectoeseminario.R;
import andre.pt.projectoeseminario.View.Adapters.ParentAdapter;

/**
 *   Shows the elements of the text/contacts
 */
public class ClipboardCategoryDetailedAdapter extends ParentAdapter {

    private final Context ctx;
    private final Consumer<String> consumer;

    public ClipboardCategoryDetailedAdapter(Context ctx, String[] content, Consumer<String> consumer){
        super(content, R.layout.clipboard_item, (v) -> new ItemDetailedViewHolder((View)v));
        this.ctx = ctx;
        this.consumer = consumer;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClipboardCategoryDetailedAdapter.ItemDetailedViewHolder)holder).category_text.setText((String)items[position]);
        ((ClipboardCategoryDetailedAdapter.ItemDetailedViewHolder)holder).category.setOnClickListener(v -> consumer.accept((String)items[position]));

        Animation animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
    }

    static class ItemDetailedViewHolder extends RecyclerView.ViewHolder {
        TextView category_text;
        RelativeLayout category;

        ItemDetailedViewHolder(View itemView) {
            super(itemView);
            category_text = (TextView) itemView.findViewById(R.id.content_text);
            category = (RelativeLayout) itemView.findViewById(R.id.main);
        }
    }

}
