package com.example.tourlogandroid.ui.gallery;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourlogandroid.R;
import com.example.tourlogandroid.ui.gallery.Picture.PictureContent.PictureItem;
import com.example.tourlogandroid.ui.gallery.Picture.PictureContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PictureItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyImageRecyclerViewAdapter extends RecyclerView.Adapter<MyImageRecyclerViewAdapter.ViewHolder> {

    private final List<PictureItem> mValues;

    public MyImageRecyclerViewAdapter(List<PictureContent.PictureItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        // holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final ImageView mContentView;
        public PictureItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (ImageView) view.findViewById(R.id.image_view_content);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}