package com.example.tourlogandroid.ui.mapPage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.tourlogandroid.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements GoogleMap.OnCameraMoveListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // Google Maps Variables
    GoogleMap map;
    double currentLat = 0, currentLng = 0;
    FusedLocationProviderClient fusedLocationProviderClient;

    // Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    double markerLat = 0, markerLng = 0;
    public HashMap<String,dbModel> points = new HashMap<String,dbModel>();

    private Button favorites;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        // Start Maps on user location

        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location != null){
                        currentLat = location.getLatitude();
                        currentLng = location.getLongitude();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(currentLat,currentLng),15));
                    }
                }
            });
        }else {
            ActivityCompat.requestPermissions(getActivity()
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
        View v = inflater.inflate(R.layout.fragment_marker_info, container, false);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                SupportMapFragment supportMapFragment = (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.google_map);
                if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    Task<Location> task = fusedLocationProviderClient.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                currentLat = location.getLatitude();
                                currentLng = location.getLongitude();
                                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        // Initiate map on user position
                                        map = googleMap;
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(currentLat,currentLng),15));

                                        // Add Markers to map
                                        db.collection("locations")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                markerLat = document.getGeoPoint("position").getLatitude();
                                                                markerLng = document.getGeoPoint("position").getLongitude();
                                                                map.addMarker(new MarkerOptions().position(new LatLng(markerLat,markerLng)));
                                                            }
                                                        } else {
                                                            Log.w("Error", "Error getting documents.", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setOnCameraMoveListener(this);
        map.setOnMarkerClickListener(this);

        // Add Markers to map
        db.collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbModel tmp = new dbModel(document.getString("name"),
                                        document.getString("info"),
                                        document.getString("image"),
                                        document.getString("address"),
                                        document.getString("id"),
                                        document.getBoolean("fav"));
                                markerLat = document.getGeoPoint("position").getLatitude();
                                markerLng = document.getGeoPoint("position").getLongitude();
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(markerLat,markerLng))
                                        .title(document.getString("name")));
                                points.put(document.getString("name"),tmp);
                            }
                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onCameraMove() {

        // Actualize visible markers
        map.clear();
        points.clear();
        LatLng cameraPos = map.getCameraPosition().target;
        db.collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbModel tmp = new dbModel(document.getString("name"),
                                        document.getString("info"),
                                        document.getString("image"),
                                        document.getString("address"),
                                        document.getString("id"),
                                        document.getBoolean("fav"));
                                markerLat = document.getGeoPoint("position").getLatitude();
                                markerLng = document.getGeoPoint("position").getLongitude();
                                LatLng position = new LatLng(markerLat,markerLng);
                                double distance = CalculationByDistance(cameraPos,position);
                                if(distance<5){
                                    map.addMarker(new MarkerOptions()
                                            .position(new LatLng(markerLat,markerLng))
                                            .title(document.getString("name")));

                                    points.put(document.getString("name"),tmp);
                                }

                            }
                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        // Display dialog fragment on marker click
        AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
        View dialogView = LayoutInflater.from(getView().getContext()).inflate(R.layout.fragment_marker_info,null);
        dbModel tmp = points.get(marker.getTitle());
        Log.d("info",tmp.getInfo());
        TextView dialog_box_title;
        TextView dialog_box_info;
        ImageView dialog_box_image;
        Button dialog_box_button;

        dialog_box_title = dialogView.findViewById(R.id.markerLocation);
        dialog_box_info = dialogView.findViewById(R.id.markerDescription);
        dialog_box_image = dialogView.findViewById(R.id.markerImage);
        dialog_box_button = dialogView.findViewById(R.id.favButton);

        dialog_box_title.setText(tmp.getName());
        dialog_box_info.setText(tmp.getInfo());

        LoadImage loadImage = new LoadImage(dialog_box_image);
        loadImage.execute(tmp.getImage());

        if(tmp.isFav()) {
            dialog_box_button.setText("FAVORITE");
        }else{
            dialog_box_button.setText("ADD TO FAVORITES");
            dialog_box_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object> favorite = new HashMap<>();
                    favorite.put("name",tmp.getName());
                    favorite.put("address",tmp.getAddress());
                    db.collection("favorites")
                            .add(favorite)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("DEBUG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("WARNING", "Error adding document", e);
                                }
                            });
                    Map<String,Object> upDate = new HashMap<>();
                    upDate.put("fav",true);
                    tmp.setFav(true);
                    db.collection("locations").document(tmp.getiD()).update(upDate);
                    dialog_box_button.setText("FAVORITE");
                }
            });
        }

        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.show();
        return true;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private class LoadImage extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;
        public LoadImage(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                Log.e("Error",e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}