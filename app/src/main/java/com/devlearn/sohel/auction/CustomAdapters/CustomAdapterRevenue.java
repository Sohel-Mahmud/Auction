package com.devlearn.sohel.auction.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devlearn.sohel.auction.Model.ModelAuctionList;
import com.devlearn.sohel.auction.R;

import java.util.List;

public class CustomAdapterRevenue extends RecyclerView.Adapter<CustomAdapterRevenue.CustomAdapterRevenueListHolder> {

    List<ModelAuctionList> list;
    Context context;
    Activity activity;
    CustomAdapterRevenueListHolder holder;
    private String searchtype;
    double value = 0;

    public CustomAdapterRevenue(Activity activity, Context context,List<ModelAuctionList> list,String searchtype ) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.searchtype = searchtype;
    }

    @NonNull
    @Override
    public CustomAdapterRevenueListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_revenue_list,viewGroup,false);

        return new CustomAdapterRevenue.CustomAdapterRevenueListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterRevenueListHolder customAdapterRevenueListHolder, int i) {
        holder = customAdapterRevenueListHolder;
        holder.txtProductTitle.setText(list.get(i).getAuction_title());
        holder.txtProductPrice.setText(String.valueOf(list.get(i).getAuction_bidprice())+"৳");
        holder.txtNumOfBids.setText(String.valueOf(list.get(i).getNum_of_bids()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomAdapterRevenueListHolder extends RecyclerView.ViewHolder{

        TextView txtProductTitle, txtNumOfBids, txtProductPrice;

        public CustomAdapterRevenueListHolder(@NonNull View itemView) {
            super(itemView);

            txtProductTitle = itemView.findViewById(R.id.txtProductTitle);
            txtNumOfBids = itemView.findViewById(R.id.txtNumOfBids);
            txtProductPrice = itemView.findViewById(R.id.txtSellPrice);
        }
    }
}
