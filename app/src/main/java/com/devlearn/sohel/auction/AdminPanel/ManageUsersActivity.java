package com.devlearn.sohel.auction.AdminPanel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.devlearn.sohel.auction.CustomAdapters.CustomAdapterAdminManageUsers;
import com.devlearn.sohel.auction.Model.UserModel;
import com.devlearn.sohel.auction.R;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageUsersActivity extends AppCompatActivity {

    private List<UserModel> list2 = new ArrayList<>();
    private RecyclerView recyclerProfileView;
    private CustomAdapterAdminManageUsers customAdapterAdminManageUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        recyclerProfileView = findViewById(R.id.recyclerProfileView);

        get_user_list("allusers");
    }

    private void get_user_list(final String searchtype) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<UserModel>> call = apiInterface.get_user_list(searchtype);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                list2 = response.body();
                if(list2!=null){
                    recyclerProfileView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    customAdapterAdminManageUsers = new CustomAdapterAdminManageUsers(ManageUsersActivity.this, getApplicationContext(), list2, searchtype);
                    recyclerProfileView.setAdapter(customAdapterAdminManageUsers);
                }else
                {
                    Toast.makeText(ManageUsersActivity.this, "No response", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(ManageUsersActivity.this, "No internet "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
