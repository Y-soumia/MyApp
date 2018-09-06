package com.example.youbi.myapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youbi.myapp.AdViewActivity;
import com.example.youbi.myapp.R;
import com.example.youbi.myapp.model.Ad.Ad;
import com.example.youbi.myapp.model.Ad.AdList;
import com.example.youbi.myapp.network.GetDataService;
import com.example.youbi.myapp.network.RetrofitClientInstance;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by youbi on 11/07/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private static final String TAG = "CustomAdapter";
    private JSONObject cities;
    private AdList adList;
    private Context context;
    private Uri uri;
    private SharedPreferences sp;
    private GetDataService service;
    private Boolean fav = false;
    private Activity activity;
    private ArrayList<Integer> myFavs;

    public CustomAdapter(Context context, AdList adList, JSONObject cities, Boolean fav, Activity activity) {
        this.context = context;
        this.adList = adList;
        this.cities = cities;
        this.fav = fav;
        this.activity = activity;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sp = context.getSharedPreferences("login", MODE_PRIVATE);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_item_layout, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        final ArrayList<Ad> ad = adList.getAd();
        holder.adSubject.setText(ad.get(position).getSubject());

        if (ad.get(position).getSaved() == 1) {
            holder.fav_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fav_red));
        }

        if (ad.get(position).getPrice() != null) {
            holder.price.setText(ad.get(position).getPrice() + " DH");
        }

        int region_id = ad.get(position).getRegion();
        Log.e(TAG, "region_id : " + region_id);
        try {
            JSONObject city = cities.getJSONObject(String.valueOf(region_id));
            holder.city.setText(city.getString("name"));
        } catch (Exception e) {
            Log.e(TAG, "cast Failed ");
            e.printStackTrace();
        }

        // setting the cover Image
        holder.cover.getHierarchy().setPlaceholderImage(R.drawable.ic_launcher_background);

        if (ad.get(position).getImages() != null) {
            uri = Uri.parse("https://services.avito.ma/images/" + ad.get(position).getImages().get(0).getPath());
            holder.cover.setImageURI(uri);
        }
        holder.cover.getHierarchy().setFailureImage(R.drawable.ic_launcher_background);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked on an Ad" + ad.get(position));

                Intent intent = new Intent(context, AdViewActivity.class);
                intent.putExtra("listId", ad.get(position).getListId());
                context.startActivity(intent);

            }
        });

        holder.fav_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e(TAG, "Clicked on fav image");
                if (sp.getInt("accountID", 0) != 0 & !sp.getString("token", "").equals("")) {

                    Log.e(TAG, "account ID " + sp.getInt("accountID", 0));

                    if (ad.get(position).getSaved() == 0) {
                        add_to_fav(sp.getString("token", ""), sp.getInt("accountID", 0), ad.get(position));
                        ad.get(position).setSaved(1);
                        holder.fav_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fav_red));

                    } else {
                        holder.fav_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                        ad.get(position).setSaved(0);
                        delete_fav(sp.getString("token", ""), sp.getInt("accountID", 0), ad.get(position).getListId());
                    }
                    /*Intent i = new Intent(context,activity.getClass());
                    context.startActivity(i);*/
                } else {
                    Toast.makeText(context, "Veuillez-vous connecter d'abord ", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //  saving an ad
    public void add_to_fav(String token, int acc_id, Ad ad) {
        Call<ResponseBody> call = service.add_to_fav("Bearer " + token, acc_id, ad);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e(TAG, " Response code : " + response.code());
                if (response.isSuccessful()) {

                    Log.e(TAG, "ad added to favorites : ");
                    Toast.makeText(context, "ad saved ! ", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.e("test", t.getMessage());
                // Log.e("test",t.getStackTrace().toString());
                t.printStackTrace();
                ;
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, " onFailure :Couldn't create the Ad");
            }
        });
    }

    //  saving an ad
    public void delete_fav(String token, int acc_id, int listId) {
        Call<ResponseBody> call = service.delete_saved("Bearer " + token, acc_id, listId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e(TAG, " Response code : " + response.code());
                if (response.isSuccessful()) {

                    Log.e(TAG, "ad deleted from favorites : ");
                    Toast.makeText(context, "ad deleted from favs ! ", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.e("test", t.getMessage());
                // Log.e("test",t.getStackTrace().toString());
                t.printStackTrace();
                ;
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, " onFailure :Couldn't create the Ad");
            }
        });
    }

    @Override
    public int getItemCount() {
        return adList.getAd().size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View view;

        TextView adSubject, price, city;
        SimpleDraweeView cover;
        RelativeLayout parentLayout;
        ImageView fav_img;


        CustomViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            parentLayout = view.findViewById(R.id.AdLayout);
            fav_img = view.findViewById(R.id.fav_icon);
            adSubject = view.findViewById(R.id.title);
            cover = view.findViewById(R.id.coverImage);
            price = view.findViewById(R.id.price);
            city = view.findViewById(R.id.city);
        }
    }

}
