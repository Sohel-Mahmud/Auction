package com.devlearn.sohel.auction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devlearn.sohel.auction.LoginRegisterPanel.LoginRegisterActivity;
import com.devlearn.sohel.auction.Model.ModelAcceptBidNoti;
import com.devlearn.sohel.auction.Model.ModelAddAuction;
import com.devlearn.sohel.auction.Model.ModelAuctionDetails;
import com.devlearn.sohel.auction.Retrofit.ApiClient;
import com.devlearn.sohel.auction.Retrofit.ApiInterface;
import com.devlearn.sohel.auction.SharedPreference.UserSharedPref;
import com.google.android.gms.common.api.Api;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionDetailsActivity extends AppCompatActivity {
    private String user_id,auction_id,auction_title,auction_description,auction_image,auction_date,auction_validity, auction_status,
            auction_pricelimit,auction_bidprice,auction_bid_user_id,num_of_bids,searchtype;

    private TextView txtNumOfbids, txtuser_name,txtauction_post_time,txtitem_title, txtitem_time_remain,txtitem_description;
    private ImageView imgOffer;
    private EditText edtBidPrice;
    private Button btnApplyBid, btnBidAccept;
    private TextView txtuser_latest_name,txtuser_offered_price;
    private UserSharedPref usp;

    private CardView cardDetailsLayout;
    private LinearLayout accept_reject_layout;
    private RelativeLayout enterBidLayout;
    private CountDownTimer countDownTimer;
    private long difference;

    private ProgressDialog progressDialog;
    
    private int current_user_id;
    private double bidHighestPrice=9999999999.99;
    private String user_token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_details);

        usp= new UserSharedPref(AuctionDetailsActivity.this);
        current_user_id = usp.getUser_id();

        auction_id = getIntent().getStringExtra("auction_id");
        user_id = getIntent().getStringExtra("user_id");
        auction_title = getIntent().getStringExtra("auction_title");
        auction_description = getIntent().getStringExtra("auction_description");
        auction_image = getIntent().getStringExtra("auction_image");
        auction_date = getIntent().getStringExtra("auction_date");
        auction_validity = getIntent().getStringExtra("auction_validity");
        auction_pricelimit = getIntent().getStringExtra("auction_pricelimit");
        auction_bidprice = getIntent().getStringExtra("auction_bidprice");
        auction_bid_user_id = getIntent().getStringExtra("auction_bid_user_id");
        num_of_bids = getIntent().getStringExtra("num_of_bids");
        searchtype = getIntent().getStringExtra("searchtype");
        auction_status = getIntent().getStringExtra("auction_status");

        txtuser_name = findViewById(R.id.user_name);
        txtauction_post_time = findViewById(R.id.auction_post_time);
        txtitem_title = findViewById(R.id.item_title);
        txtitem_time_remain = findViewById(R.id.item_tiem_remain);
        imgOffer = findViewById(R.id.imageOffer);
        txtitem_description = findViewById(R.id.item_description);
        edtBidPrice = findViewById(R.id.edtBidPrice);
        btnApplyBid = findViewById(R.id.btnApplyBid);
        txtuser_latest_name = findViewById(R.id.user_latest_name);
        txtuser_offered_price = findViewById(R.id.user_offered_price);
        btnBidAccept = findViewById(R.id.btnBidAccept);
        txtNumOfbids = findViewById(R.id.num_of_bids);
        accept_reject_layout = findViewById(R.id.accept_reject_layout);
        cardDetailsLayout = findViewById(R.id.cardCurrentbid);
        enterBidLayout = findViewById(R.id.enterReferCodelayout);

        if((searchtype.equals("all") || searchtype.equals("particular") || searchtype.equals("mybids") || searchtype.equals("solditems")) && user_id.equals(String.valueOf(current_user_id)))
        {
            enterBidLayout.setVisibility(View.GONE);
        }
        if((searchtype.equals("all") || searchtype.equals("particular") || searchtype.equals("mybids")) && auction_bid_user_id.equals("-1"))
        {
            cardDetailsLayout.setVisibility(View.GONE);
            accept_reject_layout.setVisibility(View.GONE);

        }
        if(searchtype.equals("all") || searchtype.equals("mybids") || searchtype.equals("solditems") || searchtype.equals("boughtitems"))
        {
            accept_reject_layout.setVisibility(View.GONE);
        }
        if(searchtype.equals("boughtitems"))
        {
            enterBidLayout.setVisibility(View.GONE);
        }
        if(auction_status.equals("sold"))
        {
            enterBidLayout.setVisibility(View.GONE);
        }

        //putting the intent vlues
        txtitem_title.setText(auction_title);
        txtitem_description.setText(auction_description);
        Picasso.with(AuctionDetailsActivity.this)
                .load("http://roommatebd.com/" +auction_image).fit()
                .into(imgOffer);
        txtauction_post_time.setText("Posted On: "+auction_date);

        getAuctionDetails(auction_id);
        
        btnApplyBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                double edtTextgetBidPrice = Double.valueOf(String.valueOf(edtBidPrice.getText()));
                
                if(edtTextgetBidPrice>bidHighestPrice)
                {
                    startBidding(current_user_id,auction_id, edtTextgetBidPrice);
                }
                else
                {
                    Toast.makeText(AuctionDetailsActivity.this, "Your price is lower than the Current bid price", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        
        btnBidAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user_token.isEmpty())
                {
                    progressDialog = new ProgressDialog(AuctionDetailsActivity.this);
                    progressDialog.setMessage("In progress...");
                    progressDialog.show();
                    
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<ModelAcceptBidNoti> call = apiInterface.accept_bid(auction_id,user_token,auction_title);
                    call.enqueue(new Callback<ModelAcceptBidNoti>() {
                        @Override
                        public void onResponse(Call<ModelAcceptBidNoti> call, Response<ModelAcceptBidNoti> response) {
                            if(response.body().getResponse().equals("ok"))
                            {
                                progressDialog.dismiss();
                                btnBidAccept.setEnabled(false);
                                Toast.makeText(AuctionDetailsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(AuctionDetailsActivity.this, "No response", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelAcceptBidNoti> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(AuctionDetailsActivity.this, "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    
                }
                else{
                    Toast.makeText(AuctionDetailsActivity.this, "Token didn't loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startBidding(int current_user_id, final String auction_id, double edtTextgetBidPrice) {
        progressDialog = new ProgressDialog(AuctionDetailsActivity.this);
        progressDialog.setMessage("Biding in progress...");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<ModelAddAuction> call = apiInterface.set_current_bid(current_user_id, auction_id,edtTextgetBidPrice);
        call.enqueue(new Callback<ModelAddAuction>() {
            @Override
            public void onResponse(Call<ModelAddAuction> call, Response<ModelAddAuction> response) {
                if(response.body()!=null)
                {
                    if(response.body().getResponse().equals("ok"))
                    {
                        Toast.makeText(AuctionDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        edtBidPrice.setText("");
                        Intent intent = new Intent(AuctionDetailsActivity.this,AuctionDetailsActivity.class);
                        intent.putExtra("auction_id",auction_id);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("auction_title",auction_title);
                        intent.putExtra("auction_description",auction_description);
                        intent.putExtra("auction_image",auction_image);
                        intent.putExtra("auction_date",auction_date);
                        intent.putExtra("auction_validity",auction_validity);
                        intent.putExtra("auction_pricelimit",auction_pricelimit);
                        intent.putExtra("auction_bidprice",auction_bidprice);
                        intent.putExtra("auction_bid_user_id",auction_bid_user_id);
                        intent.putExtra("num_of_bids",num_of_bids);
                        intent.putExtra("searchtype",searchtype);
                        intent.putExtra("auction_status",auction_status);
                        startActivity(intent);
                        finish();
                    }
                    else if(response.body().getResponse().equals("error")){
                        Toast.makeText(AuctionDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(AuctionDetailsActivity.this, "No response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelAddAuction> call, Throwable t) {
                Log.d("pricebug", "onFailure:"+t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(AuctionDetailsActivity.this, "No internet "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAuctionDetails(String auction_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<ModelAuctionDetails> call = apiInterface.get_auction_details(auction_id);
        call.enqueue(new Callback<ModelAuctionDetails>() {
            @Override
            public void onResponse(Call<ModelAuctionDetails> call, Response<ModelAuctionDetails> response) {
                if(response.body()!=null)
                {
                    if(searchtype.equals("boughtitems"))
                    {
                        txtuser_name.setText(response.body().getUser_name()+" phone:"+response.body().getUser_phone());
                    }else
                    {
                        txtuser_name.setText(response.body().getUser_name());
                    }
                    txtuser_latest_name.setText(response.body().getLatest_user_name());
                    user_token = response.body().getUser_token();
                    auction_status = response.body().getAuction_status();
                    if(auction_status.equals("sold"))
                    {
                        btnBidAccept.setEnabled(false);
                        btnBidAccept.setText("sold");
                    }

                    bidHighestPrice = response.body().getAuction_bidprice();
                    txtuser_offered_price.setText(String.valueOf(bidHighestPrice));
                    txtNumOfbids.setText(num_of_bids+" Bids Highest Offered Price: "+ bidHighestPrice +"৳");
                    countRemainigTime(response.body().getAuction_validity());
                }else{
                    Toast.makeText(AuctionDetailsActivity.this, "Something went wrong No response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelAuctionDetails> call, Throwable t) {
                Toast.makeText(AuctionDetailsActivity.this, "Check your internet "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void countRemainigTime(String auction_validity) {
        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        String PostTime = dates.format(date);
        String Validity = auction_validity;

        Date date1;
        Date date2;


        //Setting dates
        try {
            date1 = dates.parse(Validity);
            date2 = dates.parse(PostTime);
            //Comparing dates
            difference = date1.getTime() - date2.getTime();

            if(difference >0)
            {
                countDownTimer = new CountDownTimer(difference,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String text = String.format(Locale.getDefault(), "Time Remaining %02d days %02d hour: %02d min: %02d sec",
                                TimeUnit.MILLISECONDS.toDays(millisUntilFinished),TimeUnit.MILLISECONDS.toHours(millisUntilFinished)%60,TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                        txtitem_time_remain.setText(text);

                    }

                    @Override
                    public void onFinish() {
                        txtitem_time_remain.setText("Time Over");
                    }
                }.start();
            }
            else{
                txtitem_time_remain.setText("Time Expired");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
