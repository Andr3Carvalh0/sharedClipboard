package andre.pt.projectoeseminario.Adapters.Clipboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.function.Consumer;

import andre.pt.projectoeseminario.Adapters.ParentAdapter;
import andre.pt.projectoeseminario.R;

/**
 *   Shows the elements of the links
 */
public class ClipboardLinkDetailedAdapter extends ParentAdapter {

    private final Context ctx;
    private final Consumer<String> consumer;
    private final String FAVICON_LINK = "https://www.google.com/s2/favicons?domain=";

    public ClipboardLinkDetailedAdapter(Context ctx, String[] content, Consumer<String> consumer){
        super(content, R.layout.clipboard_item_link, (v) -> new ItemDetailedViewHolder((View)v));
        this.ctx = ctx;
        this.consumer = consumer;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemDetailedViewHolder)holder).category_text.setText((String)items[position]);
        ((ItemDetailedViewHolder)holder).category.setOnClickListener(v -> consumer.accept((String)items[position]));
        ((ItemDetailedViewHolder)holder).category_fav.setOnClickListener(v -> consumer.accept((String)items[position]));

        Picasso.with(ctx).load(FAVICON_LINK + items[position]).into(((ItemDetailedViewHolder)holder).category_fav);

        Animation animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
    }

    static class ItemDetailedViewHolder extends RecyclerView.ViewHolder {
        TextView category_text;
        RelativeLayout category;
        ImageView category_fav;

        ItemDetailedViewHolder(View itemView) {
            super(itemView);
            category_text = (TextView) itemView.findViewById(R.id.content_text);
            category = (RelativeLayout) itemView.findViewById(R.id.main);
            category_fav = (ImageView) itemView.findViewById(R.id.content_image);
        }
    }

}
