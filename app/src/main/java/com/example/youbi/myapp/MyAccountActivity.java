package com.example.youbi.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youbi.myapp.model.Account.Account;
import com.example.youbi.myapp.network.GetDataService;
import com.example.youbi.myapp.network.RetrofitClientInstance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountActivity extends AppCompatActivity {

    private Context context;
    private SharedPreferences sp;
    private static final String TAG = "MyAccountActivity : ";
    private GetDataService service;
    private Account account, edited_acc;
    private EditText name, phonenumber;
    private TextView email, log_out;
    private Switch hideSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        context = getApplicationContext();
        sp = getSharedPreferences("login", MODE_PRIVATE);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        account = new Account();
        edited_acc = new Account();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phonenumber = findViewById(R.id.phoneNumber);
        hideSwitch = findViewById(R.id.hide);
        log_out = findViewById(R.id.log_out);

        getAccount(sp.getInt("accountID", 0), sp.getString("token", ""));

        Log.e(TAG, " SP GET ACCOUNT ID :" + sp.getInt("accountID", 0));
        Log.e(TAG, " ACCOUNT NAME :" + account.getName());

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

                name.setText(account.getName(), TextView.BufferType.NORMAL);
                email.setText(account.getEmail());
                phonenumber.setText(account.getPhone(), TextView.BufferType.NORMAL);
                if (account.getPhoneHidden() == 1) {
                    hideSwitch.setChecked(true);
                }

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

    // clicking on the button
    public void edit_account(View v) {
        if (!isValid(phonenumber.getText().toString(), 10) || !isValid(name.getText().toString(), 3)) {

            if (!isValid(name.getText().toString(), 3)) {
                name.setError("Nom trop court");
                Log.e(TAG, "name is empty");
            }

            if (!isValid(phonenumber.getText().toString(), 10)) {
                phonenumber.setError("numero de telephone invalide");
                Log.e(TAG, "invalid phone number");
            }
        } else {
            Account acc = new Account();
            //acc.setAccountId(sp.getInt("accountID",0));
            acc.setName(name.getText().toString());

            acc.setPhoneHidden(0);
            if (hideSwitch.isChecked()) {
                acc.setPhoneHidden(1);
            }

            acc.setPhone(phonenumber.getText().toString());

            acc.setRegion(account.getRegion());

            Log.e(TAG, "account name :" + acc.getName() + "account region" + acc.getRegion());
            editAccount(sp.getString("token", ""), sp.getInt("accountID", 0), acc);
        }
    }

    //  Getting the account
    public void editAccount(String token, int acc_id, Account acc) {
        Log.e(TAG, "Got into editAccount method");
        Call<Account> call = service.edit_account("Bearer " + token, acc_id, acc);

        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(retrofit2.Call<Account> call, Response<Account> response) {

                Log.e(TAG, " Response code : " + response.code());
                Log.e(TAG, "Edited the account ! ");
                if (response.isSuccessful()) {

                    edited_acc = response.body().getAccount();
                    Log.e(TAG, "account.accountID : " + edited_acc.getAccountId() + " account name : " + edited_acc.getName()
                            + " account phone : " + edited_acc.getPhone() + " account PhoneHidden : " + edited_acc.getPhoneHidden());

                    name.setText(edited_acc.getName(), TextView.BufferType.NORMAL);
                    phonenumber.setText(edited_acc.getPhone(), TextView.BufferType.NORMAL);
                    if (edited_acc.getPhoneHidden() == 1) {
                        hideSwitch.setChecked(true);
                    }
                    Log.e(TAG, "Account updated succesfully !");
                    Toast.makeText(context, " Updated succesfully !", Toast.LENGTH_SHORT).show();
                }

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

    // validating password with retype password
    private boolean isValid(String text, int n) {
        if (text != null && text.length() >= n) {
            return true;
        }
        return false;
    }

    private void log_out(View view) {
        Intent intent = new Intent(context, MainActivity.class);
        log_out(sp.getString("token", ""));
        startActivity(intent);
    }

    // log out
    public void log_out(String token) {
        retrofit2.Call<ResponseBody> call = service.log_out(token);
        Log.e(TAG, "Loading MY favorite ads..");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e(TAG, " onResponse");

                if (response.isSuccessful()) {
                    /*
                     * Got Successfully
                     */
                    Log.e(TAG, "Logged out succesfully");
                    sp.edit().putString("token", "").apply();
                    Toast.makeText(context, "Logged out ! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getCause().toString());
                Log.e("test", t.getStackTrace().toString());
                t.printStackTrace();
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
