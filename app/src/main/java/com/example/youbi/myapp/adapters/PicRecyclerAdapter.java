package com.example.youbi.myapp.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.youbi.myapp.AdInsert;
import com.example.youbi.myapp.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;

import static com.example.youbi.myapp.AdInsert.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.example.youbi.myapp.R.*;

public class PicRecyclerAdapter extends RecyclerView.Adapter<PicRecyclerAdapter.picViewHolder> {
    private static final String TAG = "PicRecyclerAdapter";
    private Context context;
    private ArrayList<Bitmap> photos;
    private Activity activity;
    private Bitmap btm_add_pic;
    private static final int REQUEST_CODE_CHOOSE = 23;

    public PicRecyclerAdapter(Context context, ArrayList<Bitmap> p, Activity act) {
        this.activity = act;
        this.context = context;
        this.photos = p;

        //photos.set(p.size()+1,btm_add_pic);

        for (int i = 0; i < 6; i++) {
            Bitmap btm_add_pic;
            if (i == p.size()) {
                btm_add_pic = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.add_image);
            } else {
                btm_add_pic = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.add_image);
            }
            photos.add(btm_add_pic);
        }
        //service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        //sp = context.getSharedPreferences("login",MODE_PRIVATE);
    }


    @NonNull
    @Override
    public PicRecyclerAdapter.picViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout.ad_picture, parent, false);
        return new picViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull picViewHolder holder, int position) {
        //holder.photo.setImageDrawable(context.getResources().getDrawable(drawable.add_image));

        Log.e("PicRc onBindViewHolder", "size :" + photos.size());
        Log.e("PicRc onBindViewHolder", "position :" + position);
        holder.photo.setImageBitmap(photos.get(position));

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Matisse.from(activity)
                            .choose(MimeType.ofImage(), false)
                            .countable(true)
                            .capture(true)
                            .captureStrategy(
                                    new CaptureStrategy(true, "com.example.youbi.myapp.fileprovider"))
                            .maxSelectable(6)
                            .theme(R.style.Matisse_Zhihu)
                            .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .countable(true)
                            .imageEngine(new PicassoEngine())
                            .showSingleMediaType(true)
                            .forResult(REQUEST_CODE_CHOOSE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG, "permissions are denied");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class picViewHolder extends RecyclerView.ViewHolder {

        public final View view;

        RecyclerView picRecycler;
        ImageView photo;

        picViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            picRecycler = view.findViewById(id.picsRecycler);
            photo = view.findViewById(id.photo);

        }
    }
}
