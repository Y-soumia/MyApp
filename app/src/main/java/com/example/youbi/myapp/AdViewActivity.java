package com.example.youbi.myapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.youbi.myapp.model.Ad.Ad_view;
import com.example.youbi.myapp.model.Ad.CreatingAd;
import com.example.youbi.myapp.network.GetDataService;
import com.example.youbi.myapp.network.RetrofitClientInstance;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdViewActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    private static final String TAG = "AdViewActivity";
    private Context context;
    private JSONObject cities;
    private JSONArray categories;
    private Uri uri;
    private GetDataService service;
    private TextView adTitle, body, region, category;
    private ImageButton call_btn, sms_btn;
    private SimpleDraweeView cover;
    private Ad_view ad;
    private CreatingAd ad_with_phone;
    private SliderLayout slider;
    private int phoneHidden = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = getApplicationContext();
        call_btn = findViewById(R.id.call_btn);
        sms_btn = findViewById(R.id.sms_btn);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_view);
        Log.d(TAG, "onCreate : Started");


        cities = createJSONObjectFromFile("regions");
        categories = createJSONArrayFromFile("categorie");


        // Creating handle for the RetrofitInstance interface
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        ad = new Ad_view();
        ad_with_phone = new CreatingAd();

        Log.e(TAG, "ListId passed from the mainActivity : " + getIncomingIntents());

        get_ad();

        // PhoneHidden is the ad annoucer acccount phoneHidden
        if (phoneHidden == 0) {
            call_btn.setVisibility(View.VISIBLE);
            sms_btn.setVisibility(View.VISIBLE);
        }


    }

    //  Getting the ad with its listId
    private void get_ad() {
        retrofit2.Call<Ad_view> call = service.getAd(getIncomingIntents());
        call.enqueue(new Callback<Ad_view>() {
            @Override
            public void onResponse(retrofit2.Call<Ad_view> call, Response<Ad_view> response) {

                if (response.isSuccessful()) {
                    /*
                     * Got Successfully
                     */
                    Log.e(TAG, "succesful response : ");
                    Toast.makeText(AdViewActivity.this, "response succesful", Toast.LENGTH_SHORT).show();
                    if (response.body() == null) {
                        Log.e(TAG, "response.body() is null : ");
                    }
                    Log.e(TAG, "Ad title :" + response.body().getAd().getSubject());

                    ad = response.body();
                    get_phone(ad.getAd().getListId());
                    phoneHidden = ad.getAd().getPhoneHidden();
                    adDetail(ad);

                }
            }

            @Override
            public void onFailure(retrofit2.Call<Ad_view> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getStackTrace().toString());
                t.printStackTrace();

                Toast.makeText(AdViewActivity.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // setting the ad details in the layout
    private void adDetail(Ad_view ad) {
        adTitle = findViewById(R.id.ad_title);
        slider = (SliderLayout) findViewById(R.id.photos);
        body = findViewById(R.id.body);
        region = findViewById(R.id.region);
        category = findViewById(R.id.category);
        adTitle.setText(ad.getAd().getSubject());
        if (ad.getAd().getImages() != null) {

            HashMap<String, String> url_maps = new HashMap();

            for (int i = 0; i < ad.getAd().getImages().size(); i++) {

                url_maps.put(ad.getAd().getSubject() + " " + i, "https://services.avito.ma/images/" + ad.getAd().getImages().get(i).getPath());
                Log.e(TAG, ad.getAd().getImages().get(i).getPath());
            }
            for (String name : url_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);


                slider.addSlider(textSliderView);
            }
        }
        if (ad.getAd().getBody() != null) {
            body.setText(Html.fromHtml(ad.getAd().getBody()));
        }
        // setting the city and the category
        int region_id = ad.getAd().getRegion();
        int category_id = ad.getAd().getCategory();
        Log.e(TAG, "the ad category id is : " + category_id);
        try {
            JSONObject city = cities.getJSONObject(String.valueOf(region_id));
            region.setText(city.getString("name"));
            // getting the region of the city from params
            if (!ad.getAd().getParams().isEmpty()) {
                for (int i = 0; i < ad.getAd().getParams().size(); i++) {

                    if (ad.getAd().getParams().get(i).getName().equals("area")) {

                        JSONObject cities = city.getJSONObject("cities");
                        JSONObject sector = cities.getJSONObject((String.valueOf(ad.getAd().getParams().get(i).getValue())));
                        region.append(" " + sector.getString("name"));
                        break;
                    }
                }
            }
            for (int i = 0; i < categories.length(); i++) {
                JSONObject cat = categories.getJSONObject(i);

                if (category_id == cat.getInt("id")) {

                    Log.e(TAG, "Found the ad category ! " + cat.getInt("id") + " " + cat.getString("name"));
                    category.setText(cat.getString("name"));
                    break;
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "cast Failed ");
            e.printStackTrace();
        }
    }

    // method getting the incoming intents : the ad listId

    private int getIncomingIntents() {
        Log.d(TAG, "getIncomingintents : Getting incoming intents..");
        // if(getIntent().hasExtra("listId")){
        //     Log.d(TAG,"getIncomingintents :Found incoming intents");
        int listId = getIntent().getIntExtra("listId", 1);
        return listId;
    }

    // to get region JSON Object
    private JSONObject createJSONObjectFromFile(String file_name) {

        JSONObject result = null;

        try {
            // Read file into string builder
            InputStream inputStream = getResources().openRawResource(
                    getResources().getIdentifier(file_name,
                            "raw", getPackageName()));
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

    // get the ad phone
    public void get_phone(int listId) {
        Call<CreatingAd> call = service.get_ad_number(listId);
        call.enqueue(new Callback<CreatingAd>() {
            @Override
            public void onResponse(retrofit2.Call<CreatingAd> call, Response<CreatingAd> response) {

                Log.e(TAG, " Response code : " + response.code());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ad_with_phone.setPhone(response.body().getPhone());
                        Toast.makeText(context, "Got phone number succesfully ! ", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<CreatingAd> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getCause().toString());
                Log.e("test", t.getStackTrace().toString());
                t.printStackTrace();
                ;
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, " onFailure : Couldn't get phone number");
            }
        });
    }

    //to get the categories JSON Array
    private JSONArray createJSONArrayFromFile(String file_name) {

        JSONArray result = null;
        BufferedReader jsonReader = new BufferedReader(new InputStreamReader(
                this.getResources().openRawResource(
                        getResources().getIdentifier(file_name,
                                "raw", getPackageName()))));
        StringBuilder jsonBuilder = new StringBuilder();
        try {
            for (String line = null; (line = jsonReader.readLine()) != null; ) {
                jsonBuilder.append(line).append("\n");
            }
            result = new JSONArray(jsonBuilder.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    // calling the ad annoucer
    public void make_call(View view) {
        Log.e(TAG, "clicked on make a call");

        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + ad_with_phone.getPhone()));
        startActivity(dialIntent);
    }

    // Sending a text to the ad annoucer
    public void send_sms(View view) {
        Log.e(TAG, "clicked on send an sms");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", ad_with_phone.getPhone(), null));

        intent.putExtra("sms_body", "Bonjour, je vous contacte au sujet de l'annonce " + ad.getAd().getSubject() + ",");
        startActivity(intent);
    }
}

