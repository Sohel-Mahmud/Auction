package com.devlearn.sohel.auction.MyItems;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devlearn.sohel.auction.AddAuctionActivity;
import com.devlearn.sohel.auction.CustomAdapters.CustomAdapterOnGoingBids;
import com.devlearn.sohel.auction.Model.ModelAuctionList;
import com.devlearn.sohel.auction.R;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;
import com.devlearn.sohel.auction.SharedPreference.UserSharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MyItemsFragment extends Fragment {

    private List<ModelAuctionList> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapterOnGoingBids customAdapterOnGoingBids;
    private UserSharedPref usp;
    private int user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_my_items,container,false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddAuctionActivity.class);
                startActivity(intent);
            }
        });

        usp = new UserSharedPref(getContext());
        user_id = usp.getUser_id();

        recyclerView = view.findViewById(R.id.recyclerMyItems);
        get_auction_list("particular",user_id);

        return view;
    }

    private void get_auction_list(final String searchtype, int user_id) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        retrofit2.Call<List<ModelAuctionList>> call = apiInterface.get_auction_list(searchtype,user_id);
        call.enqueue(new Callback<List<ModelAuctionList>>() {
            @Override
            public void onResponse(retrofit2.Call<List<ModelAuctionList>> call, Response<List<ModelAuctionList>> response) {
                list = response.body();
                Log.d("ServerResponse", "onResponse:"+list.size());
                if(list!=null)
                {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    customAdapterOnGoingBids = new CustomAdapterOnGoingBids(getActivity(),getContext(),list,searchtype);
                    recyclerView.setAdapter(customAdapterOnGoingBids);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<ModelAuctionList>> call, Throwable t) {
                Toast.makeText(getContext(),"Please check your internet connection "+t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
}
