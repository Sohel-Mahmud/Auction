package com.devlearn.sohel.auction.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devlearn.sohel.auction.AuctionDetailsActivity;
import com.devlearn.sohel.auction.Model.ModelAddAuction;
import com.devlearn.sohel.auction.Model.ModelAuctionList;
import com.devlearn.sohel.auction.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomAdapterOnGoingBids extends RecyclerView.Adapter<CustomAdapterOnGoingBids.OnGoingBidsListHolder> {

    List<ModelAuctionList> list;
    Context context;
    Activity activity;
    OnGoingBidsListHolder holder;
    private long differenceDates;
    private String searchtype;
    private long diffHours;

    public CustomAdapterOnGoingBids(Activity activity, Context context, List<ModelAuctionList> list, String searchtype) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.searchtype = searchtype;
    }

    @NonNull
    @Override
    public OnGoingBidsListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.model_auction_list,viewGroup,false);

        return new CustomAdapterOnGoingBids.OnGoingBidsListHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull OnGoingBidsListHolder onGoingBidsListHolder, final int i) {
        holder = onGoingBidsListHolder;

        Picasso.with(context)
                .load("http://roommatebd.com/" + list.get(i).getAuction_image()).fit()
                .into(holder.imgAuctionImage);

        holder.txtauctionTitle.setText(list.get(i).getAuction_title());
        holder.txtPostTime.setText("post date: "+list.get(i).getAuction_date());

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        String PostTime = dates.format(date);
        String Validity = list.get(i).getAuction_validity();

        Date date1;
        Date date2;


        //Setting dates
        try {
            date1 = dates.parse(Validity);
            date2 = dates.parse(PostTime);
            //Comparing dates
            long difference = date1.getTime() - date2.getTime();
            if(difference>0)
            {
                differenceDates = difference / (24 * 60 * 60 * 1000);
                diffHours = difference / (60 * 60 * 1000) % 24;
                holder.txtauctionRemainingTime.setText(differenceDates+" Days "+diffHours+" Hours Remaining");
            }else{
                holder.txtauctionRemainingTime.setText("Time Over");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtAuctionCurrentPrice.setText(String.valueOf(list.get(i).getAuction_bidprice())+"৳");
        holder.txtauctionNumBids.setText(String.valueOf(list.get(i).getNum_of_bids())+" bids");
        holder.txtauctionStatus.setText(list.get(i).getAuction_status());

        holder.auctionListCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AuctionDetailsActivity.class);
                intent.putExtra("auction_id",String.valueOf(list.get(i).getAuction_id()));
                intent.putExtra("user_id",String.valueOf(list.get(i).getUser_id()));
                intent.putExtra("auction_title",list.get(i).getAuction_title());
                intent.putExtra("auction_description",list.get(i).getAuction_description());
                intent.putExtra("auction_image",list.get(i).getAuction_image());
                intent.putExtra("auction_date",list.get(i).getAuction_date());
                intent.putExtra("auction_validity",list.get(i).getAuction_validity());
                intent.putExtra("auction_pricelimit",String.valueOf(list.get(i).getAuction_pricelimit()));
                intent.putExtra("auction_bidprice",String.valueOf(list.get(i).getAuction_bidprice()));
                intent.putExtra("auction_bid_user_id",String.valueOf(list.get(i).getAuction_bid_user_id()));
                intent.putExtra("num_of_bids",String.valueOf(list.get(i).getNum_of_bids()));
                intent.putExtra("searchtype",searchtype);
                intent.putExtra("auction_status",String.valueOf(list.get(i).getAuction_status()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OnGoingBidsListHolder extends RecyclerView.ViewHolder {

        ImageView imgAuctionImage;
        TextView txtauctionTitle, txtPostTime, txtauctionRemainingTime, txtAuctionCurrentPrice, txtauctionNumBids, txtauctionStatus;
        CardView auctionListCardview;

        public OnGoingBidsListHolder(@NonNull View itemView) {
            super(itemView);

            imgAuctionImage = itemView.findViewById(R.id.auctionImage);
            txtauctionTitle= itemView.findViewById(R.id.auctionTitle);
            txtPostTime = itemView.findViewById(R.id.auctionPostTime);
            txtauctionRemainingTime = itemView.findViewById(R.id.auctionRemainingTime);
            txtAuctionCurrentPrice = itemView.findViewById(R.id.auctionCurrentPrice);
            txtauctionNumBids = itemView.findViewById(R.id.auctionNumBids);
            txtauctionStatus = itemView.findViewById(R.id.auctionStatus);
            auctionListCardview = itemView.findViewById(R.id.auctionlistCardView);
        }
    }
}
