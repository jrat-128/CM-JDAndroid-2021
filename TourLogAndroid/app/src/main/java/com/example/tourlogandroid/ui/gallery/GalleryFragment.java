package com.example.tourlogandroid.ui.gallery;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Picture;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlogandroid.R;
import com.example.tourlogandroid.ui.gallery.Picture.PictureContent;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private int cnt = 0;
    private int k = 0;
    static Context context;
    private static ArrayList<String> galleryImageUrls = new ArrayList<>();
    private static ArrayList<String> TourLogGalleryUrls = new ArrayList<>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GalleryFragment() {
    }

    public static GalleryFragment newInstance(int columnCount) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_list, container, false);
        View vv = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            cnt = fetchAllPictures();
            PictureContent PC = new PictureContent();
            for(int i = 0; i<k; i++)
            {
                PictureContent.PictureItem PCitem = new PictureContent.PictureItem(String.valueOf(i),TourLogGalleryUrls.get(i),TourLogGalleryUrls.get(i));
            }
            TextView txt_g = vv.findViewById(R.id.text_gallery);
            txt_g.setText(String.valueOf(cnt));
            recyclerView.setAdapter(new MyImageRecyclerViewAdapter(PC.ITEMS));
        }
        return view;
    }

    private int fetchAllPictures()
    {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID}; //get all columns of type images
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN; //order data by date
        k = 0;
        Cursor imagecursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");//get all data in Cursor by sorting in DESC order


        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);//get column index
            galleryImageUrls.add(imagecursor.getString(dataColumnIndex));//get Image from column index
        }

        // Filter out non-TourLog images
        for (int i = 0; i<galleryImageUrls.size(); i++) {
            if(galleryImageUrls.get(i).startsWith("TourLog_"))
            {
                TourLogGalleryUrls.add(k+1,galleryImageUrls.get(i));
                k++;
            }
        }

        Log.e("fatch in","images");
        return imagecursor.getCount();
    }
}