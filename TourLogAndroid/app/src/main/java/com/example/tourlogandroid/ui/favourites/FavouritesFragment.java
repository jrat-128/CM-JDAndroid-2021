package com.example.tourlogandroid.ui.favourites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlogandroid.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FavouritesFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirestoreRecyclerAdapter adapter;

    private RecyclerView mFavoriteList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        mFavoriteList = (RecyclerView) view.findViewById(R.id.favoritesList);

        //Query
        Query query = db.collection("favorites");

        //RecyclerOptions
        FirestoreRecyclerOptions<dbModelFavorites> options = new FirestoreRecyclerOptions.Builder<dbModelFavorites>()
                .setQuery(query, dbModelFavorites.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<dbModelFavorites, FavoritesViewHolder>(options) {
            @NonNull
            @Override
            public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item,parent,false);
                return new FavoritesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FavoritesViewHolder holder, int i, @NonNull dbModelFavorites dbModelFavorites) {
                holder.mTitle.setText(dbModelFavorites.getName());
                holder.mAddress.setText(dbModelFavorites.getAddress());
            }
        };

        mFavoriteList.setHasFixedSize(true);
        mFavoriteList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFavoriteList.setAdapter(adapter);

        return view;
    }
    private  class FavoritesViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitle;
        private TextView mAddress;

        public FavoritesViewHolder(@NonNull View itemView){
            super(itemView);
            this.mTitle = itemView.findViewById(R.id.favTitle);
            this.mAddress = itemView.findViewById(R.id.favAddress);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
