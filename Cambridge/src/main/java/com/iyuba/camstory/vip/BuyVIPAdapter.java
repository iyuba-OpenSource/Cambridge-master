package com.iyuba.camstory.vip;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.iyuba.camstory.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class BuyVIPAdapter extends RecyclerView.Adapter<BuyVIPAdapter.BuyHolder> {

    private List<BuyVIPItem> mData;

    public BuyVIPItem mSelectedItem;

    public BuyVIPAdapter() {
        mData = new ArrayList<>();
    }

    public void setData(List<BuyVIPItem> data) {
        if (mData != data) {
            mData = data;
            notifyDataSetChanged();
        }
    }

    @Nullable
    public BuyVIPItem getSelectedItem() {
        return mSelectedItem;
    }

    @NonNull
    @Override
    public BuyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BuyHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_buy_vip, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuyHolder holder, int position) {
        holder.setItem(mData.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class BuyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.check_box)
        CheckBox mCheckBox;
        @BindView(R.id.text_description)
        TextView mDescriptionTv;
        @BindView(R.id.text_price)
        TextView mPriceTv;


        int appVipProductId = 10;

        BuyVIPItem item;

        public BuyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setItem(BuyVIPItem item, int position) {
            this.item = item;
            Resources resources = itemView.getResources();

            if (mSelectedItem==null&&position==0){
                mSelectedItem=item;
            }

            mCheckBox.setChecked(item == mSelectedItem);
            mPriceTv.setText(resources.getString(R.string.vip_price_info, item.price));
            String description;
            if (item.productId == appVipProductId) {
                description = item.name;
            } else {
                description = item.name ;//+ resources.getString(R.string.vip_time_info, item.month)
            }
            mDescriptionTv.setText(description);
        }

        @OnClick(R.id.linear_container)
        void click() {
            if (this.item == mSelectedItem) {
                Timber.i("current buy item is already selected");
            } else {
                mSelectedItem = this.item;
                notifyDataSetChanged();
            }
        }
    }
}
