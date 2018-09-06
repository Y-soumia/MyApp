package com.example.youbi.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youbi.myapp.model.Account.Account;
import com.example.youbi.myapp.model.Account.Token;
import com.example.youbi.myapp.network.GetDataService;
import com.example.youbi.myapp.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity : ";
    private Context context;
    private GetDataService service;
    private EditText email, password;
    private Account account;
    private TextView signup;
    private Button btnLogin;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        account = new Account();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        sp = getSharedPreferences("login", MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, " Clicked on LoginActivity button ");
                account.setEmail(email.getText().toString());
                account.setPassword(password.getText().toString());
                service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                getToken(account);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, " Redirecting to sign up activity ");
                Intent intent = new Intent(context, SignUpAtivity.class);
                startActivity(intent);
            }
        });
    }

    public void getToken(Account account) {
        Call<Token> call = service.getToken(account);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(retrofit2.Call<Token> call, Response<Token> response) {

                Log.e(TAG, " Response code : " + response.code());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Token token = response.body();
                        Log.e(TAG, " Account ID : " + token.getAccountId());
                        Log.e(TAG, " xToken : " + token.getXToken());
                        Toast.makeText(context, "Logged in succesfully ! ", Toast.LENGTH_SHORT).show();
                        sp.edit().putString("token", token.getXToken()).commit();
                        sp.edit().putInt("accountID", token.getAccountId()).commit();
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(context, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(retrofit2.Call<Token> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getCause().toString());
                Log.e("test", t.getStackTrace().toString());
                t.printStackTrace();
                ;
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, " onFailure : Couldn't log in");
            }
        });
    }

}
