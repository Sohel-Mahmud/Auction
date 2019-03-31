package com.devlearn.sohel.auction.AdminPanel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.devlearn.sohel.auction.CustomAdapters.CustomAdapterAdminManagePosts;
import com.devlearn.sohel.auction.CustomAdapters.CustomAdapterOnGoingBids;
import com.devlearn.sohel.auction.Model.ModelAuctionList;
import com.devlearn.sohel.auction.R;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class ManageSoldProductsActivity extends AppCompatActivity {

    private List<ModelAuctionList> list = new ArrayList<>();
    private RecyclerView recyclerSoldProducts;
    private CustomAdapterAdminManagePosts customAdapterAdminManagePosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sold_products);
        
        recyclerSoldProducts = findViewById(R.id.recyclerAdminSoldProducts);
        get_auction_lis("revenue",-1);
    }

    private void get_auction_lis(final String searchtype, final int user_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        retrofit2.Call<List<ModelAuctionList>> call = apiInterface.get_auction_list(searchtype,user_id);
        call.enqueue(new Callback<List<ModelAuctionList>>() {
            @Override
            public void onResponse(retrofit2.Call<List<ModelAuctionList>> call, Response<List<ModelAuctionList>> response) {
                list = response.body();
                Log.d("ServerResponse", "onResponse:"+list.size());
                if(list!=null)
                {
                    recyclerSoldProducts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    customAdapterAdminManagePosts = new CustomAdapterAdminManagePosts(ManageSoldProductsActivity.this,getApplicationContext(),list,searchtype,user_id);
                    recyclerSoldProducts.setAdapter(customAdapterAdminManagePosts);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<ModelAuctionList>> call, Throwable t) {
                Toast.makeText(ManageSoldProductsActivity.this,"Please check your internet connection "+t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
}
