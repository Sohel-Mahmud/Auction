package com.devlearn.sohel.auction.CustomAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devlearn.sohel.auction.AdminPanel.ManagePostsActivity;
import com.devlearn.sohel.auction.AuctionDetailsActivity;
import com.devlearn.sohel.auction.Model.ModelAuctionList;
import com.devlearn.sohel.auction.Model.UserModel;
import com.devlearn.sohel.auction.R;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAdapterAdminManagePosts extends RecyclerView.Adapter<CustomAdapterAdminManagePosts.OnGoingBidsListHolder> {

    List<ModelAuctionList> list;
    Context context;
    Activity activity;
    OnGoingBidsListHolder holder;
    private long differenceDates;
    private String searchtype;
    private long diffHours;
    private int user_id;

    public CustomAdapterAdminManagePosts(Activity activity, Context context, List<ModelAuctionList> list, String searchtype, int user_id) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.searchtype = searchtype;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public OnGoingBidsListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.model_auction_list,viewGroup,false);

        return new CustomAdapterAdminManagePosts.OnGoingBidsListHolder(row);
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

        /*holder.auctionListCardview.setOnClickListener(new View.OnClickListener() {
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
        });*/
        final int auction_id = list.get(i).getAuction_id();
        holder.auctionListCardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                alertForDelete(auction_id);
                return true;
            }
        });

    }

    private void alertForDelete(final int auction_id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Do you want to delete this item?");

        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<UserModel> call = apiInterface.delete_auction(auction_id);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if(response.body()!=null)
                        {
                            if(response.body().getResponse().equals("ok"))
                            {
                                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, ManagePostsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                    /*            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                                retrofit2.Call<List<ModelAuctionList>> call2 = apiInterface.get_auction_list(searchtype,user_id);
                                call2.enqueue(new Callback<List<ModelAuctionList>>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<List<ModelAuctionList>> call, Response<List<ModelAuctionList>> response) {
                                        list = response.body();
                                        Log.d("ServerResponse", "onResponse:"+list.size());

                                    }

                                    @Override
                                    public void onFailure(retrofit2.Call<List<ModelAuctionList>> call, Throwable t) {
                                        Toast.makeText(activity,"Please check your internet connection "+t.getMessage(),Toast.LENGTH_LONG).show();

                                    }
                                });*/

                            }
                            else if(response.body().getResponse().equals("Error"))
                            {
                                Toast.makeText(activity, "Failed to delete", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(activity, "No response", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Toast.makeText(activity, "No internet "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
