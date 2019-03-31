package com.devlearn.sohel.auction.Retrofit;

import com.devlearn.sohel.auction.Model.ModelAcceptBidNoti;
import com.devlearn.sohel.auction.Model.ModelAddAuction;
import com.devlearn.sohel.auction.Model.ModelAuctionDetails;
import com.devlearn.sohel.auction.Model.ModelAuctionList;
import com.devlearn.sohel.auction.Model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("register_user.php")
    Call<UserModel> set_profile(@Field("user_name") String user_name,
                                @Field("user_address") String user_address,
                                @Field("user_phone") String user_phone,
                                @Field("user_email") String user_email,
                                @Field("user_password") String user_password);

    @FormUrlEncoded
    @POST("login_user.php")
    Call<UserModel> user_login(@Field("user_email") String user_email,
                               @Field("user_password") String user_password,
                               @Field("user_role") String user_role,
                               @Field("user_token") String user_token);

    @FormUrlEncoded
    @POST("post_auction.php")
    Call<ModelAddAuction> upload_auction(@Field("user_id") int user_id,
                                         @Field("auction_title") String auction_title,
                                         @Field("auction_description") String auction_description,
                                         @Field("auction_pricelimit") double auction_pricelimit,
                                         @Field("auction_validity") String auction_validity,
                                         @Field("auction_image") String auction_image);

    @GET("get_auction_list.php")
    Call<List<ModelAuctionList>> get_auction_list(@Query("searchtype") String searchtype,
                                                  @Query("user_id") int user_id);

    @GET("get_auction_details.php")
    Call<ModelAuctionDetails> get_auction_details(@Query("auction_id") String auction_id);

    @FormUrlEncoded
    @POST("set_current_bid.php")
    Call<ModelAddAuction> set_current_bid(@Field("auction_bid_user_id") int auction_bid_user_id,
                                          @Field("auction_id") String auction_id,
                                          @Field("auction_bidprice") double auction_bidprice);

    @FormUrlEncoded
    @POST("api.php")
    Call<ModelAcceptBidNoti> accept_bid(@Field("auction_id") String auction_id,
                                        @Field("user_token") String user_token,
                                        @Field("auction_title") String auction_title);

    @GET("get_user_list.php")
    Call<List<UserModel>> get_user_list(@Query("searchtype") String searchtype);

    @GET("delete_auction.php")
    Call<UserModel> delete_auction(@Query("auction_id") int auction_id);

}
