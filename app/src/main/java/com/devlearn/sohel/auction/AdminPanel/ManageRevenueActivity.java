package com.devlearn.sohel.auction.AdminPanel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.devlearn.sohel.auction.CustomAdapters.CustomAdapterOnGoingBids;
import com.devlearn.sohel.auction.CustomAdapters.CustomAdapterRevenue;
import com.devlearn.sohel.auction.Model.ModelAuctionList;
import com.devlearn.sohel.auction.R;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;
import com.devlearn.sohel.auction.SharedPreference.UserSharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageRevenueActivity extends AppCompatActivity {

    private List<ModelAuctionList> list = new ArrayList<>();
    private RecyclerView recyclerViewManageRevenue;
    private CustomAdapterRevenue customAdapterRevenue;
    double value = 0;
    private TextView txtTotalPrice;
    private TextView txtTotalRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_revenue);
        
        recyclerViewManageRevenue = findViewById(R.id.recyclerRevenue);
        txtTotalRevenue = findViewById(R.id.totalRevenue);
        txtTotalPrice = findViewById(R.id.totalAmount);

        get_revenue_lis("revenue",-1);
    }

    private void get_revenue_lis(final String searchtype, int user_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelAuctionList>> call = apiInterface.get_auction_list(searchtype, user_id);
        call.enqueue(new Callback<List<ModelAuctionList>>() {
            @Override
            public void onResponse(Call<List<ModelAuctionList>> call, Response<List<ModelAuctionList>> response) {
                list = response.body();
                if(list!=null)
                {
                    recyclerViewManageRevenue.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    customAdapterRevenue = new CustomAdapterRevenue(ManageRevenueActivity.this,getApplicationContext(),list,searchtype);
                    recyclerViewManageRevenue.setAdapter(customAdapterRevenue);

                    for (int i=0; i<list.size(); i++)
                    {
                        value+=list.get(i).getAuction_bidprice();
                    }
                    txtTotalPrice.setText(String.valueOf(value));
                    double revenue = (value*0.2);
                    txtTotalRevenue.setText(String.valueOf(revenue));
                }
            }

            @Override
            public void onFailure(Call<List<ModelAuctionList>> call, Throwable t) {
                Toast.makeText(ManageRevenueActivity.this, "Please check your internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
