package com.example.tourlogandroid.ui.gallery;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourlogandroid.R;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PictureItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyImageRecyclerViewAdapter extends RecyclerView.Adapter<MyImageRecyclerViewAdapter.MyViewHolde> {
    Context context;
    private ArrayList<String> imageData = new ArrayList<String>();


    public MyImageRecyclerViewAdapter(ArrayList<String> imageData, FragmentActivity activity) {
        this.imageData = imageData;
        this.context = activity;
    }

    @Override
    public MyImageRecyclerViewAdapter.MyViewHolde onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_gallery, parent, false);

        return new MyViewHolde(itemView);
    }

    @Override
    public void onBindViewHolder(MyImageRecyclerViewAdapter.MyViewHolde holder, int position) {
        String data = imageData.get(position);
        if (data != null){
            Glide.with(context).load(data).into(holder.singleImageView);

        }else {
            Toast.makeText(context, "Images Empty", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
        return imageData.size();
    }

    public class MyViewHolde extends RecyclerView.ViewHolder {
        ImageView singleImageView;

        public MyViewHolde(View itemView) {
            super(itemView);
            singleImageView = (ImageView) itemView.findViewById(R.id.image_view_content);
        }
    }
}