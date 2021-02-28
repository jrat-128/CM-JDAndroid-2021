package com.example.tourlogandroid.ui.favourites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlogandroid.R;
import com.example.tourlogandroid.ui.mapPage.dbModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FavouritesFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirestoreRecyclerAdapter adapter;

    private RecyclerView mFavoriteList;

    public HashMap<String, dbModelFavorites> list = new HashMap<String,dbModelFavorites>();

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


        db.collection("favorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbModelFavorites tmp = new dbModelFavorites(document.getString("name"),
                                        document.getString("address"),
                                        document.getString("iD"));
                                list.put(document.getString("name"),tmp);
                            }
                        } else {
                            Log.w("GET", "Error getting documents.", task.getException());
                        }
                    }
                });




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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                DocumentSnapshot snapshot = (DocumentSnapshot) adapter.getSnapshots().getSnapshot(position);
                db.collection("favorites")
                        .document(snapshot.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "onSuccess: Removed list item");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failure","on Failure:" +e.getLocalizedMessage());
                    }
                });
                Log.d("debug",snapshot.getString("name"));
                Map<String,Object> upDate = new HashMap<>();
                upDate.put("fav",false);
                dbModelFavorites tmp = list.get(snapshot.getString("name"));
                Log.d("ID",tmp.getName());
                Log.d("ID",tmp.getAddress());
                Log.d("ID",tmp.getiD());
                db.collection("locations").document(tmp.getiD()).update(upDate);
            }
        }).attachToRecyclerView(mFavoriteList);

        return view;
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder{

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
