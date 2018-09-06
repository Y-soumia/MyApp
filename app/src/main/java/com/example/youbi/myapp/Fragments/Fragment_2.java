package com.example.youbi.myapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.youbi.myapp.LoginActivity;
import com.example.youbi.myapp.R;
import com.example.youbi.myapp.adapters.CustomAdapter;
import com.example.youbi.myapp.model.Ad.AdList;
import com.example.youbi.myapp.network.GetDataService;
import com.example.youbi.myapp.network.RetrofitClientInstance;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class Fragment_2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String ARG_PAGE = "ARG_PAGE";

    private Context context;

    private static final String TAG = "Fragment_2";
    private GetDataService service;
    private SharedPreferences sp;

    private JSONObject cities;
    private CustomAdapter adapter;
    private RecyclerView myAdsRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AdList adList;
    private ImageView fav_img;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(TAG, " onCreate");

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        context = getActivity();
        sp = context.getSharedPreferences("login", MODE_PRIVATE);

        load_saved_ads(service, sp.getInt("accountID", 0), sp.getString("token", ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        if (!sp.getString("token", "").equals("")) {
            view = inflater.inflate(R.layout.fragment_tab_2, container, false);

            myAdsRecycler = view.findViewById(R.id.myAdsRecycler);
            layoutManager = new GridLayoutManager(context, 2);

            swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setOnRefreshListener(this);
        } else {
            view = inflater.inflate(R.layout.fragment_empty, container, false);
        }

        return view;
    }

    // loading my favorites ads
    public void load_saved_ads(GetDataService service, int acc_id, String token) {
        retrofit2.Call<AdList> call = service.get_saved_ads("Bearer " + token, acc_id, 0);
        Log.e(TAG, "Loading MY favorite ads..");

        call.enqueue(new Callback<AdList>() {
            @Override
            public void onResponse(retrofit2.Call<AdList> call, Response<AdList> response) {

                Log.e(TAG, " onResponse");

                if (response.isSuccessful()) {
                    /*
                     * Got Successfully
                     */
                    adList = response.body();
                    generateDataList(adList);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, "Loaded my favorite ads ! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AdList> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getCause().toString());
                Log.e("test", t.getStackTrace().toString());
                t.printStackTrace();
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to generate List of data using RecyclerView with custom adapter
    private void generateDataList(AdList adList) {
        cities = createJSONObjectFromFile("regions");
        adapter = new CustomAdapter(context, adList, cities, true, getActivity());
        myAdsRecycler.setLayoutManager(layoutManager);
        myAdsRecycler.setItemAnimator(new DefaultItemAnimator());
        myAdsRecycler.setAdapter(adapter);
    }

    // to get region JSON Object
    private JSONObject createJSONObjectFromFile(String file_name) {

        JSONObject result = null;

        try {
            // Read file into string builder
            InputStream inputStream = getResources().openRawResource(
                    getResources().getIdentifier(file_name,
                            "raw", context.getPackageName()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            for (String line = null; (line = reader.readLine()) != null; ) {
                builder.append(line).append("\n");
            }

            // Parse into JSONObject
            String resultStr = builder.toString();
            JSONTokener tokener = new JSONTokener(resultStr);
            result = new JSONObject(tokener);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void login(View view) {
        Log.e(TAG, "clicked on login");
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        Log.e(TAG, "Refreshing the ads");
        load_saved_ads(service, sp.getInt("accountID", 0), sp.getString("token", ""));
    }

}
