package com.iyuba.camstory.vip;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.camstory.R;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IyubiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String SUBJECT = "爱语币";

    private interface Type {
        int NORMAL = 0;
        int END = 1;
    }

    private String[] prices;
    private int[] amounts;
    private int[] imageIds;

    private BuyIyubiListener mListener;

    public IyubiAdapter() {
        prices = new String[]{};
        amounts = new int[]{};
        imageIds = new int[]{};
    }

    public void setData(String[] prices, int[] amounts, int[] imageIds) {
        this.prices = prices;
        this.amounts = amounts;
        this.imageIds = imageIds;
        notifyDataSetChanged();
    }

    public void setBuyIyubiListener(BuyIyubiListener l) {
        this.mListener = l;
    }

    @Override
    public int getItemViewType(int position) {
        if (prices.length > 0) {
            return (position == prices.length) ? Type.END : Type.NORMAL;
        } else {
            return Type.NORMAL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Type.NORMAL:
                return new NormalHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_iyubi_normal, parent, false));
            case Type.END:
            default:
                return new EndHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_iyubi_end, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Type.NORMAL:
                ((NormalHolder) holder).setItem(prices[position], amounts[position], imageIds[position]);
                break;
            case Type.END:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (prices.length > 0) ? prices.length + 1 : 0;
    }

    class NormalHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_price)
        ImageView mPriceIv;
        @BindView(R.id.text_price)
        TextView mPriceTv;

        private String price;
        private int amount;

        public NormalHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setItem(String price, int amount, int imageId) {
            this.price = price;
            this.amount = amount;
            mPriceIv.setImageResource(imageId);
            mPriceTv.setText(itemView.getResources().getString(R.string.iyubi_price_info, price));
        }

        @OnClick(R.id.button_buy)
        void buy() {
            if (mListener != null) {
                String otn = Util.buildOutTradeNo();
                String body = "本次花费" + price + "元购买" + amount + SUBJECT;
                mListener.onBuy(otn, SUBJECT, body, amount, price);
            }
        }
    }

    class EndHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_copyright)
        TextView mCopyrightTv;

        public EndHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            int year = new Date().getYear() + 1900;
            mCopyrightTv.setText(itemView.getResources().getString(R.string.iyubi_charge_copyright, year));
        }
    }

    public interface BuyIyubiListener {
        void onBuy(String otn, String subject, String body, int amount, String price);
    }

}
