package com.example.youbi.myapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.youbi.myapp.adapters.PicRecyclerAdapter;
import com.example.youbi.myapp.model.Ad.CreatingAd;
import com.example.youbi.myapp.model.Ad.Image;
import com.example.youbi.myapp.network.GetDataService;
import com.example.youbi.myapp.network.RetrofitClientInstance;
import com.google.gson.Gson;
import com.mindorks.paracamera.Camera;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdInsert extends AppCompatActivity {

    private static final String TAG = "AdInsert : ";
    private Context context;
    private SharedPreferences sp;
    private Button btn;
    private Intent intentToMain;
    private Intent intentToAdDetails;
    private GetDataService service;
    private File file;
    private ArrayList<String> bitmap_listIDs;
    private RecyclerView photosRecycler;
    private GridLayoutManager layoutManager;
    private ArrayList<Bitmap> photos;
    private PicRecyclerAdapter adapter;
    private static final int REQUEST_CODE_CHOOSE = 23;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_insert);

        context = getApplicationContext();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        sp = getSharedPreferences("login", MODE_PRIVATE);
        bitmap_listIDs = new ArrayList<String>();
        intentToMain = new Intent(context, MainActivity.class);
        intentToAdDetails = new Intent(context, AdDetailsActivity.class);
        context = getApplicationContext();
        //picFrame = (ImageView) findViewById(R.id.picFrame);

        photos = new ArrayList<>(6);

        photosRecycler = findViewById(R.id.picsRecycler);
        layoutManager = new GridLayoutManager(context, 3);
        adapter = new PicRecyclerAdapter(context, photos, this);
        photosRecycler.setHasFixedSize(true);
        photosRecycler.setLayoutManager(layoutManager);
        photosRecycler.setAdapter(adapter);

        btn = findViewById(R.id.add_photos);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
            Log.d("Matisse", "Paths: " + Matisse.obtainPathResult(data));
            try {
                File filesDir = context.getFilesDir();

                for (int i = 0; i < Matisse.obtainResult(data).size(); i++) {
                    Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(Matisse.obtainResult(data).get(i)));
                    photos.set(i, bmp);
                    file = persistImage(bmp, "img :" + i, filesDir);
                    uploadImage(file);

                }
                adapter = new PicRecyclerAdapter(context, photos, this);
                photosRecycler.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private File persistImage(Bitmap bitmap, String name, File filesDir) {

        //create a file to write bitmap data
        File f = new File(context.getCacheDir(), "filename");

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    //  Uploading Image
    public void uploadImage(File file) {

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        Call<Image> call = service.upload_image(body);
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(retrofit2.Call<Image> call, Response<Image> response) {

                Log.e(TAG, " Response code : " + response.code());
                if (response.isSuccessful()) {

                    Log.e(TAG, "Ad image uploaded succesfully " + response.body().getFile());
                    Toast.makeText(context, "Ad image succesfully uploaded ! ", Toast.LENGTH_SHORT).show();

                    bitmap_listIDs.add(response.body().getFile());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Image> call, Throwable t) {
                Log.e("test", t.getMessage());
                Log.e("test", t.getCause().toString());
                Log.e("test", t.getStackTrace().toString());
                t.printStackTrace();
                Toast.makeText(context, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, " onFailure :Couldn't upload image ");
            }
        });
    }

    // clicking on the button
    public void onClick_1(View v) {
        saveArrayList(bitmap_listIDs, "picsIDs");
        startActivity(intentToAdDetails);
    }

    public void saveArrayList(ArrayList<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /*public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.myDialog);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }*/

}
