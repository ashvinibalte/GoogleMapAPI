package com.example.googlemapapi;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LatLng> points = new ArrayList<>(); // List to hold the points added on long click
    private Polyline polyline; // Polyline to display the points on the map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Setup map UI controls
        setupMapControls();

        // Initialize polyline options
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(0xFFFF0000) // Red line color
                .width(10); // Line width

        // Create polyline from options
        polyline = mMap.addPolyline(polylineOptions);

        // Setup a map long click listener
        mMap.setOnMapLongClickListener(latLng -> {
            // Add the latLng to the list of points
            points.add(latLng);

            // Optionally, add a marker on the map at the clicked location
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

            // Add all points to the polyline
            polyline.setPoints(points);

            // Automatically adjust the zoom to include all points
            updateCameraToBounds();
        });

        // Example marker to demonstrate map functionality
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
    }

    private void setupMapControls() {
        // Enable Zoom Controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable Compass
        mMap.getUiSettings().setCompassEnabled(true);

        // Enable Rotate Gestures
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        // Enable Tilt Gestures
        mMap.getUiSettings().setTiltGesturesEnabled(true);

        // Enable Scroll Gestures
        mMap.getUiSettings().setScrollGesturesEnabled(true);

        // Enable Zoom Gestures
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // Map Toolbar
        mMap.getUiSettings().setMapToolbarEnabled(true);
    }

    private void updateCameraToBounds() {
        if (points.isEmpty()) {
            return; // if no points, no need to adjust the camera
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();

        // Update the camera passing the bounds and padding for the zoom level
        int padding = 100; // padding around the edges of the visible map area
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }
}
