package com.devlearn.sohel.auction.CustomAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devlearn.sohel.auction.Model.UserModel;
import com.devlearn.sohel.auction.R;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;

public class CustomAdapterAdminManageUsers extends RecyclerView.Adapter<CustomAdapterAdminManageUsers.UserProfileListHolder> {

    List<UserModel> list;
    Context context;
    Activity activity;
    UserProfileListHolder holder;
    private String searchtype;


    public CustomAdapterAdminManageUsers(Activity activity, Context context, List<UserModel> list, String searchtype) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.searchtype = searchtype;
    }

    @NonNull
    @Override
    public UserProfileListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.model_user_profile_list,viewGroup,false);

        return new CustomAdapterAdminManageUsers.UserProfileListHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileListHolder onGoingBidsListHolder, final int i) {
        holder = onGoingBidsListHolder;

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
        final int user_id = list.get(i).getUser_id();
        holder.txtUserName.setText(list.get(i).getUser_name());
        holder.txtUserEmailPhone.setText(list.get(i).getUser_email()+"   Phone: "+list.get(i).getUser_phone());
        holder.cardLayoutUserProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                alertForDelete(user_id);
                return true;
            }
        });

    }
    private void alertForDelete(int user_id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Do you want to Ban this user?");

        dialog.setPositiveButton("Ban User", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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

    public class UserProfileListHolder extends RecyclerView.ViewHolder {

        TextView txtUserName, txtUserEmailPhone;
        CardView cardLayoutUserProfile;

        public UserProfileListHolder(@NonNull View itemView) {
            super(itemView);
            cardLayoutUserProfile = itemView.findViewById(R.id.cardViewProfileList);
            txtUserName = itemView.findViewById(R.id.userName);
            txtUserEmailPhone = itemView.findViewById(R.id.userEmailPhone);

        }
    }
}
