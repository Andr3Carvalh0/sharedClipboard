package andre.pt.projectoeseminario.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.function.Function;

/**
 * The parent of all the adapters
 */
public abstract class ParentAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final T[] items;
    protected final int layout;
    private final Function<View, RecyclerView.ViewHolder> generateViewHolder;

    public ParentAdapter(T[] items, int layout, Function<View, RecyclerView.ViewHolder> generateViewHolder){
        this.items = items;
        this.layout = layout;
        this.generateViewHolder = generateViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return generateViewHolder.apply(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Nothing to do here. The children should handle it.
    }

    @Override
    public int getItemCount() {
        return items.length;
    }
}
