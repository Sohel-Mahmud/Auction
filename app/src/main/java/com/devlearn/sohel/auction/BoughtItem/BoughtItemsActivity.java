package com.devlearn.sohel.auction.BoughtItem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.devlearn.sohel.auction.CustomAdapters.CustomAdapterOnGoingBids;
import com.devlearn.sohel.auction.Model.ModelAuctionList;
import com.devlearn.sohel.auction.R;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;
import com.devlearn.sohel.auction.SharedPreference.UserSharedPref;
import com.devlearn.sohel.auction.SoldItem.SoldItemActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class BoughtItemsActivity extends AppCompatActivity {

    private List<ModelAuctionList> list = new ArrayList<>();
    private RecyclerView recyclerViewSold;
    private CustomAdapterOnGoingBids customAdapterOnGoingBids;
    private UserSharedPref usp4;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_items);

        usp4 = new UserSharedPref(BoughtItemsActivity.this);
        user_id = usp4.getUser_id();

        recyclerViewSold = findViewById(R.id.recyclerBoughtItems);
        get_auction_list("boughtitems",user_id);
    }

    private void get_auction_list(final String searchtype, int user_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        retrofit2.Call<List<ModelAuctionList>> call = apiInterface.get_auction_list(searchtype,user_id);
        call.enqueue(new Callback<List<ModelAuctionList>>() {
            @Override
            public void onResponse(retrofit2.Call<List<ModelAuctionList>> call, Response<List<ModelAuctionList>> response) {
                list = response.body();
                Log.d("ServerResponse", "onResponse:"+list.size());
                if(list.size()!=0)
                {
                     recyclerViewSold.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                     customAdapterOnGoingBids = new CustomAdapterOnGoingBids(BoughtItemsActivity.this,getApplicationContext(),list,searchtype);
                     recyclerViewSold.setAdapter(customAdapterOnGoingBids);


                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<ModelAuctionList>> call, Throwable t) {
                Toast.makeText(BoughtItemsActivity.this,"Please check your internet connection "+t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
}
