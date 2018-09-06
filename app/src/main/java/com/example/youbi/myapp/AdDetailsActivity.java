package com.example.youbi.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youbi.myapp.model.Account.Account;
import com.example.youbi.myapp.model.Ad.CreatingAd;
import com.example.youbi.myapp.model.Ad.Image;
import com.example.youbi.myapp.model.Ad.ImageForInsert;
import com.example.youbi.myapp.network.GetDataService;
import com.example.youbi.myapp.network.RetrofitClientInstance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdDetailsActivity extends AppCompatActivity {

    private static final String TAG = "Ad details : ";
    private Context context;
    private GetDataService service;
    private SharedPreferences sp;
    private ArrayList<String> categories_list, cities_list;
    private Spinner categories_spinner, cities_spinner;
    private JSONArray categories_JSON;
    private JSONObject cities_JSON;
    private HashMap<String, Integer> map_cities, map_categories;
    private EditText title, body;
    private Account account;
    private CreatingAd ad;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, " onCreate ..");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);

        context = getApplicationContext();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        categories_list = new ArrayList<>();
        cities_list = new ArrayList<>();
        categories_spinner = findViewById(R.id.categories);
        cities_spinner = findViewById(R.id.cities_spinner);
        categories_JSON = createJSONArrayFromFile("categorie");
        cities_JSON = createJSONObjectFromFile("regions");
        map_cities = new HashMap<String, Integer>();
        map_categories = new HashMap<String, Integer>();
        title = findViewById(R.id.title);
        body = findViewById(R.id.description);
        account = new Account();

        getCities();
        getCategories();

        /*if(getIntent().getParcelableExtra("Image")!=null){
            File filesDir = context.getFilesDir();
            bitmap = (Bitmap) getIntent().getParcelableExtra("Image");
        }*/

        Log.e(TAG, " sp.accountID :" + sp.getInt("accountID", 0));
        Log.e(TAG, " sp.token :" + sp.getString("token", ""));
        getAccount(sp.getInt("accountID", 0), sp.getString("token", ""));

        cities_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    public void onClick_1(View view) {
        Log.e(TAG, " Account info : accountID " + account.getAccountId() + " accountName : " + account.getName());

        if (isValidInput(title.getText().toString()) && isValidInput(body.getText().toString())) {
            ad = new CreatingAd(account.getName(), account.getPhone(), map_cities.get(cities_spinner.getSelectedItem()), map_categories.get(categories_spinner.getSelectedItem()), title.getText().toString(), body.getText().toString(), 0, account.getPhoneHidden());

            ArrayList<String> imageIDs = getArrayList("picsIDs");
            if (imageIDs.size() != 0) {
                ArrayList<ImageForInsert> images = new ArrayList<>();
                for (int i = 0; i < imageIDs.size(); i++) {
                    ImageForInsert img = new ImageForInsert();
                    img.setId(imageIDs.get(i));
                    img.setOrder(i);
                    if (i == 0) {
                        img.setDefault(1);
                    } else {
                        img.setDefault(0);
                    }
                    images.add(img);
                }
                ad.setImages(images);
            }

            Log.e(TAG, "ad category :" + ad.getCategory() + " ad subject " + ad.getSubject());
            createAd(sp.getString("token", ""), sp.getInt("accountID", 0), ad);
        } else {
            if (!isValidInput(title.getText().toString())) {
                title.setError("Champ obligatoire");
            } else {
                body.setError("Champ obligatoire");
            }
        }
    }

    //  Inserting the Ad
    public void createAd(String token, int acc_id, CreatingAd ad) {

        Call<ResponseBody> call = service.insert_ad("Bearer " + token, acc_id, ad);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e(TAG, " Response code : " + response.code());
                if (response.isSuccessful()) {

                    Log.e(TAG, "new Ad inserted : ");
                    Toast.makeText(context, "new Ad inserted ! ", Toast.LENGTH_SHORT).show();
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

    //  Getting the account
    public void getAccount(int acc_id, String token) {
        Log.e(TAG, "Got into getAccount method");
        Call<Account> call = service.get_account(acc_id, "Bearer " + token);

        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(retrofit2.Call<Account> call, Response<Account> response) {

                Log.e(TAG, " Response code : " + response.code());
                Log.e(TAG, "Got the account ! ");
                account = response.body().getAccount();
                Log.e(TAG, "account.accountID : " + account.getAccountId() + " account name : " + account.getName());

            }

            @Override
            public void onFailure(retrofit2.Call<Account> call, Throwable t) {
                Log.e("test", t.getMessage());
                t.printStackTrace();
                ;
                Log.e(TAG, " onFailure :Couldn't get the account");
            }
        });
    }

    // to validate title and description with retype
    private boolean isValidInput(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

    // Loadinng cities into the spinner
    public void getCities() {
        try {
            for (int i = 1; i < cities_JSON.length(); i++) {
                if (cities_JSON.has(String.valueOf(i))) {
                    cities_list.add(cities_JSON.getJSONObject(String.valueOf(i)).getString("name"));
                    //sorting the list
                    Collections.sort(cities_list);

                    map_cities.put(cities_JSON.getJSONObject(String.valueOf(i)).getString("name"), cities_JSON.getJSONObject(String.valueOf(i)).getInt("id"));
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, cities_list);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cities_spinner.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, "Couldn't load cities ");
            e.printStackTrace();
        }
    }

    //  Loadinng cities into the spinner
    public void getCategories() {
        try {
            for (int i = 1; i < categories_JSON.length(); i++) {
                categories_list.add(categories_JSON.getJSONObject(i).getString("name"));
                map_categories.put(categories_JSON.getJSONObject(i).getString("name"), categories_JSON.getJSONObject(i).getInt("id"));

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, categories_list);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categories_spinner.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, "Couldn't load cities ");
            e.printStackTrace();
        }
    }

    //  to get the categories JSON Array
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

    public ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    //  to get region JSON Object
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
}