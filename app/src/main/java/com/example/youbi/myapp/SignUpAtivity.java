package com.example.youbi.myapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youbi.myapp.model.Account.AccStatus;
import com.example.youbi.myapp.model.Account.Account;
import com.example.youbi.myapp.network.GetDataService;
import com.example.youbi.myapp.network.RetrofitClientInstance;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpAtivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity : ";
    private Context context;
    private JSONObject citiesJson;
    private HashMap<String, Integer> map;
    private ArrayList<String> cities_list;
    private EditText name, email, phoneNmbr, password;
    private Switch hide_nmbr;
    private int hide_b = 0;
    private RadioButton pro, part;
    private RadioGroup acc_type;
    private Button signUpBtn;
    private Spinner cities_spinner;
    private AutoCompleteTextView cities_auto;
    private ImageView dropDownImage;
    private GetDataService service;
    private Account AccountCreated;
    private Account toCreateAcc;


    private Credentials login_credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_ativity);

        context = getApplicationContext();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phoneNmbr = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        hide_nmbr = findViewById(R.id.hide);
        pro = findViewById(R.id.pro);
        part = findViewById(R.id.particular);
        signUpBtn = findViewById(R.id.signup);
        cities_spinner = findViewById(R.id.cities);
        //cities_auto = findViewById(R.id.cities);
        //dropDownImage = findViewById(R.id.dropDownImage);
        citiesJson = createJSONObjectFromFile("regions");
        map = new HashMap<String, Integer>();
        cities_list = new ArrayList<>();
        //cities_auto.setThreshold(1);

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        getCities();

        /*dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cities_auto.showDropDown();
            }
        });*/

        hide_nmbr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hide_b = 1;
                } else {
                    hide_b = 0;
                }
            }
        });

        cities_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    public void onClick(View v) {
        if (!isValidEmail(email.getText().toString()) || !isValidPassword(password.getText().toString()) || name.getText().toString().isEmpty()) {
            if (!isValidEmail(email.getText().toString())) {
                email.setError("Email invalide");
                Log.e(TAG, "invalid email");
            }
            if (!isValidPassword(password.getText().toString())) {
                password.setError("mot de passe trop court");
                Log.e(TAG, "invalid password");
            }
            if (name.getText().toString().isEmpty()) {
                name.setError("Champ obligatoire");
                Log.e(TAG, "name is empty");
            }
        } else {
            checkAccountStatus(email.getText().toString());
        }
    }

    public void checkAccountStatus(final String email) {
        Call<AccStatus> call = service.checkEmail(email);
        call.enqueue(new Callback<AccStatus>() {
            @Override
            public void onResponse(retrofit2.Call<AccStatus> call, Response<AccStatus> response) {

                Log.e(TAG, " checking email response code : " + response.code());
                if (response.isSuccessful()) {

                    AccStatus accStatus = response.body();
                    Log.e(TAG, "this email is : " + accStatus.getAccountStatus());

                    if (accStatus.getAccountStatus().equals("ACCOUNT_AVAILABLE")) {

                        Log.e(TAG, "Creating email");
                        toCreateAcc.setName(name.getText().toString());
                        toCreateAcc.setEmail(email);
                        toCreateAcc.setPassword(password.getText().toString());
                        toCreateAcc.setLang("fr");
                        toCreateAcc.setPhoneHidden(hide_b);
                        toCreateAcc.setPhone(phoneNmbr.getText().toString());
                        toCreateAcc.setRegion(map.get(cities_spinner.getSelectedItem().toString()));
                        createAccount(toCreateAcc);
                    } else if (accStatus.getAccountStatus().equals("ACCOUNT_ACTIVE")) {
                        Toast.makeText(context, "Email existe deja, connectez vous ! ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Email not available ! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AccStatus> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getCause().toString());
                Log.e("test", t.getStackTrace().toString());
                t.printStackTrace();
                ;
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, " onFailure : Couldn't check the email");
            }
        });
    }

    public void createAccount(Account toCreateAcc) {

        Call<Account> call = service.createAccount(toCreateAcc);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(retrofit2.Call<Account> call, Response<Account> response) {

                Log.e(TAG, " Response code : " + response.code());
                if (response.isSuccessful()) {

                    AccountCreated = response.body();
                    Log.e(TAG, "new Account created : " + AccountCreated.getAccount().getAccountId());
                    Toast.makeText(context, "Response succesful ! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Account> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getCause().toString());
                Log.e("test", t.getStackTrace().toString());
                t.printStackTrace();
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, " onFailure :Couldn't create the Account");
            }
        });
    }

    // Loadinng cities into the spinner
    public void getCities() {
        try {
            for (int i = 1; i < citiesJson.length(); i++) {
                if (citiesJson.has(String.valueOf(i))) {
                    cities_list.add(citiesJson.getJSONObject(String.valueOf(i)).getString("name"));
                    //sorting the list
                    Collections.sort(cities_list);

                    map.put(citiesJson.getJSONObject(String.valueOf(i)).getString("name"), citiesJson.getJSONObject(String.valueOf(i)).getInt("id"));
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

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
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
}