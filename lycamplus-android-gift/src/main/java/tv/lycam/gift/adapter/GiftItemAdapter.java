package tv.lycam.gift.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import tv.lycam.gift.R;
import tv.lycam.gift.entity.GiftItem;

public class GiftItemAdapter extends BaseAdapter {

    private List<GiftItem> data;

    private LayoutInflater inflater;

    private int size = 0;

    public GiftItemAdapter(Context context, List<GiftItem> list) {
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        if (data != null)
            this.size = list.size();
    }

    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_gift, parent, false);
            holder = new BaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        GiftItem model = data.get(position);
        String imageUrl = model.getImageUrl();

        ImageView view = holder.getView(R.id.gift_icon);
        Glide.with(parent.getContext())
                .load(imageUrl)
                .into(view);
        holder.setText(R.id.gift_name, model.getDisplayName());
        holder.setText(R.id.gift_price, "宝石: " + model.getPrice());
        return convertView;
    }

}