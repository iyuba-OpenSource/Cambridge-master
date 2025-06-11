package tv.lycam.gift.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by lycam on 16/3/3.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> items;

    public BaseViewHolder(View itemView) {
        super(itemView);
        items = new SparseArray<>();
    }

    public <T extends View> T getView(int id) {
        View item = items.get(id);
        if (null == item) {
            item = itemView.findViewById(id);
            items.put(id, item);
        }
        return (T) item;
    }

    public void setText(int id, String description) {
        ((TextView) getView(id)).setText(description);
    }
}
