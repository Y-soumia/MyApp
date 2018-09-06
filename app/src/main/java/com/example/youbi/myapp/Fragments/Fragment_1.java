package com.example.youbi.myapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.example.youbi.myapp.AdInsert;
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


public class Fragment_1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String ARG_PAGE = "ARG_PAGE";

    private Context context;

    private static final String TAG = "Fragment 1 : ";
    private SharedPreferences sp;
    private static final int limit = 35;
    private int last_Id = 0;
    private int offset = 0;
    private boolean itShouldLoadMore = false;
    private boolean isLoading = false;
    private JSONObject cities;
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private FloatingActionButton fab;
    private RecyclerView.LayoutManager layoutManager;
    private AdList adList;
    private GetDataService service;
    public static final String EXTRA_listId = "listId";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView fav_img;


    /*public static Fragment_1 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Fragment_1 fragment = new Fragment_1();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, " onCreate");
        // Creating handle for the RetrofitInstance interface

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        context = getActivity();
        sp = context.getSharedPreferences("login", MODE_PRIVATE);

        Log.e(TAG, sp.getString("token", ""));

        // first load of ads
        first_load(service, sp.getString("token", ""));

        if (isLoading) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading Data.. Please wait...");
            progressDialog.show();
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    // Inflating the fragment layout defined above for this fragment
    // RecyclerView Listner
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_1, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        recyclerView = view.findViewById(R.id.customRecyclerView);
        fab = view.findViewById(R.id.fab);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(context, 2);
        fav_img = view.findViewById(R.id.fav_icon);

        // adding the recyclerView listener to load more ads when scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Recycle view scrolling downwards...
                    // this if statement detects when user reaches the end of recyclerView, this is only time we should load more
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        // allowed to load more
                        // checking if itShouldLoadMore variable is true
                        if (itShouldLoadMore) {
                            loadMore();
                        }

                    }

                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

        // Scheme colors for animation
        /*swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );*/


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, " Clicked on Fab button ");
                Intent intent;
                if (!sp.getString("token", "").equals("")) {
                    intent = new Intent(context, AdInsert.class);
                } else {
                    intent = new Intent(context, LoginActivity.class);
                    Toast.makeText(context, " Veuillez vous connecter d'abord ", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        });

        return view;
    }

    // first load of the ads
    public void first_load(GetDataService service, String token) {
        retrofit2.Call<AdList> call;
        if (token.equals("")) {
            call = service.getAllAds();
        } else {
            call = service.getAllAds_w_token(token);
        }

        Log.e("test", "First load of ads..");

        // Enqueue Callback will be called when we get the response...
        itShouldLoadMore = false;
        isLoading = true;
        call.enqueue(new Callback<AdList>() {
            @Override
            public void onResponse(retrofit2.Call<AdList> call, Response<AdList> response) {
                //Dismiss Dialog
                progressDialog.dismiss();
                itShouldLoadMore = true;
                isLoading = false;
                Log.e(TAG, " onResponse");

                if (response.isSuccessful()) {
                    /*
                     * Got Successfully
                     */
                    adList = response.body();
                    last_Id = adList.getAd().get(adList.getAd().size() - 1).getListId();
                    generateDataList(adList);
                    Toast.makeText(context, "Response succesful ! ", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AdList> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getCause().toString());
                Log.e("test", t.getStackTrace().toString());
                itShouldLoadMore = true;
                isLoading = false;
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, " onFailure : Couldn't load more");
            }
        });
    }

    public void loadMore() {

        itShouldLoadMore = false;
        offset += limit;

        retrofit2.Call<AdList> call = service.getAllAds_o(offset);
        Log.e(TAG, " Loading More ..");
        /* Enqueue Callback will be called when we get the response...
         */
        call.enqueue(new Callback<AdList>() {
            @Override
            public void onResponse(retrofit2.Call<AdList> call, Response<AdList> response) {
                //Dismiss Dialog
                progressDialog.dismiss();
                itShouldLoadMore = true;
                Toast.makeText(context, "OnResponse", Toast.LENGTH_SHORT).show();

                if (response.isSuccessful()) {
                    /*
                     * loaded more Successfully
                     */
                    Log.e(TAG, " Response succesful ");
                    adList.getAd().addAll(response.body().getAd());
                    last_Id = adList.getAd().get(adList.getAd().size() - 1).getListId();
                    Log.e(TAG, "last ad id : " + last_Id);
                    //generateDataList(adList);
                    //Notifying the adapter of the update
                    adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                    Toast.makeText(context, " Loaded more ! ", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<AdList> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getCause().toString());
                Log.e("test", t.getStackTrace().toString());
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(context, "Couldn't load more ! ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method to generate List of data using RecyclerView with custom adapter
    private void generateDataList(AdList adList) {
        cities = createJSONObjectFromFile("regions");
        adapter = new CustomAdapter(context, adList, cities, false, getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onRefresh() {
        Log.e(TAG, "Refreshing the ads");
        first_load(service, sp.getString("token", ""));
    }


}
