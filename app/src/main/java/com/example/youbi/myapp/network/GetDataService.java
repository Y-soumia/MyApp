package com.example.youbi.myapp.network;

import com.example.youbi.myapp.model.Account.AccStatus;
import com.example.youbi.myapp.model.Account.Account;
import com.example.youbi.myapp.model.Ad.Ad;
import com.example.youbi.myapp.model.Ad.AdList;
import com.example.youbi.myapp.model.Ad.Ad_view;
import com.example.youbi.myapp.model.Account.Token;
import com.example.youbi.myapp.model.Ad.CreatingAd;
import com.example.youbi.myapp.model.Ad.Image;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by youbi on 11/07/2018.
 */

public interface GetDataService {
    @Headers({
            "Accept: application/json",
            "content-type: application/json",
            "lang: fr"
    })

    // Get Ads
    @GET("api/v1/ads")
    Call<AdList> getAllAds();

    @Headers({
            "Accept: application/json",
            "content-type: application/json",
            "lang: fr"
    })

    // Get Ads
    @GET("api/v1/ads")
    Call<AdList> getAllAds_w_token(@Header("x-token") String token);

    @Headers({
            "Accept: application/json",
            "content-type: application/json",
            "lang: fr"
    })

    // Get a specific Ad
    @GET("api/v1/ads/{listId}")
    Call<Ad_view> getAd(
            @Path("listId") int listId
    );

    @Headers({
            "Accept: application/json",
            "content-type: application/json",
            "lang: fr"
    })
    // Get Ads offset
    @GET("api/v1/ads")
    Call<AdList> getAllAds_o(
            @Query("o") int offset
    );

    @Headers({
            "Accept: application/json",
            "content-type: application/json",
            "lang: fr"
    })
    // creating a new account
    @POST("api/v1/accounts")
    Call<Account> createAccount(@Body Account accToCreate);

    @Headers({
            "Accept: application/json",
    })
    // Check if an email address is available
    @GET("api/v1/accounts/status")
    Call<AccStatus> checkEmail(
            @Query("email") String email
    );

    // Getting a token
    @Headers({
            "Accept: application/json",
            "content-type: application/json"
    })
    @POST("/api/v1/auth/token")
    Call<Token> getToken(@Body Account account);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
            // "x-token : Bearer {{Token}} ",
    })
    // Get my account
    @GET("/api/v1/accounts/{AccountId}")
    Call<Account> get_account(@Path("AccountId") int accountId, @Header("x-token") String token);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
            // "x-token : Bearer {{Token}} ",
    })
    // Insert a new Ad
    @POST("/api/v1/accounts/{AccountId}/ads")
    Call<ResponseBody> insert_ad(@Header("x-token") String token,
                                 @Path("AccountId") int accountId,
                                 @Body CreatingAd ad);


    @Headers({
            "Accept: application/json"
            // "x-token : Bearer {{Token}} ",
    })
    // Get My Ads
    @GET("/api/v1/accounts/{AccountId}/ads")
    Call<AdList> getMyAds(@Header("x-token") String token,
                          @Path("AccountId") int accountId,
                          @Query("o") int offset);

    @Headers({
            "Accept: application/json"
    })
    @Multipart
    @POST("/api/v1/ads/image")
    Call<Image> upload_image(@Part MultipartBody.Part upload);

    @Headers({
            "Accept: application/json"
            // "x-token : Bearer {{Token}} ",
    })
    // Get Saved Ads
    @GET("/api/v1/accounts/{AccountId}/ads/saved")
    Call<AdList> get_saved_ads(@Header("x-token") String token,
                          @Path("AccountId") int accountId,
                          @Query("o") int offset);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
            // "x-token : Bearer {{Token}} ",
    })
    // Save an ad
    @POST("/api/v1/accounts/{AccountId}/ads/saved")
    Call<ResponseBody> add_to_fav(@Header("x-token") String token,
                                 @Path("AccountId") int accountId,
                                 @Body Ad ad);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    // Delete saved ad
    @DELETE("/api/v1/accounts/{AccountId}/ads/saved/{listId}")
    Call<ResponseBody> delete_saved(@Header("x-token") String token,
                                    @Path("AccountId") int accountId,
                                    @Path("listId") int listId);

    // Get the phone number of an ad
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("/api/v1/ads/{listId}/phone")
    Call<CreatingAd> get_ad_number(@Path("listId") int listId);

    // Edit account
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "lang: fr"
    })
    @PUT("/api/v1/accounts/{AccountId}")
    Call<Account> edit_account(@Header("x-token") String token,
                               @Path("AccountId") int accountId,
                               @Body Account account);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    // Log out
    @DELETE("/api/v1/auth/token/{tokenId}")
    Call<ResponseBody> log_out(@Path ("tokenId") String token);
}

