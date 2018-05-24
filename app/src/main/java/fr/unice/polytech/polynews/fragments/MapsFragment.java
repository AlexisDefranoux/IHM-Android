package fr.unice.polytech.polynews.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.unice.polytech.polynews.Database;
import fr.unice.polytech.polynews.R;
import fr.unice.polytech.polynews.models.Mishap;

public class MapsFragment extends Fragment {

    private View rootView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    MapView mapView;
    GoogleMap map;
    LatLng latLng = new LatLng(43, 7);
    Circle circle;
    List<Mishap> mishaps;
    Location location = new Location("");
    Map<Double, Marker> markerMap = new HashMap<>();

    public static MapsFragment newInstance(int sectionNumber) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        Log.i("PlaceHolder", "message");
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        mishaps = new Database(getContext()).getAllMishaps();

        rootView = inflater.inflate(R.layout.map, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.print("Erreur manque des permissions");
        }
        map.setMyLocationEnabled(true);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        map.animateCamera(cameraUpdate);

        final TextView distance = rootView.findViewById(R.id.distance);

        SeekBar seekBar =(SeekBar) rootView.findViewById(R.id.seekBar);
        distance.setText(seekBar.getProgress() + " km");
        drawCircle(latLng, seekBar.getProgress());
        updateMarker(seekBar.getProgress());

        // perform seek bar change listener event used for getting the progress value
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 1;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                distance.setText(seekBar.getProgress() + " km");
                // Drawing circle on the map
                drawCircle(latLng, progressChangedValue);
                updateMarker(progressChangedValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) { }

            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        return rootView;
    }

    private void updateMarker(int size){
        Location targetLocation = new Location("");//provider name is unnecessary
        Marker marker;

        for (Mishap mishap: mishaps) {
            targetLocation.setLatitude(mishap.getLatitude());
            targetLocation.setLongitude(mishap.getLongitude());
            double distanceInMeters =  targetLocation.distanceTo(location);

            if(distanceInMeters <= size && !markerMap.containsKey(distanceInMeters)) {
                marker = map.addMarker(new MarkerOptions().position(new LatLng(mishap.getLatitude(), mishap.getLongitude())).title(mishap.getTitleMishap()));
                markerMap.put(distanceInMeters, marker);
            }

            if(distanceInMeters > size && markerMap.containsKey(distanceInMeters)){
                markerMap.remove(distanceInMeters);
            }
        }

    }

    private void drawCircle(LatLng point, int size){

        if(circle!=null){
            circle.remove();
        }

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(size);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(5);

        // Adding the circle to the GoogleMap
        circle = map.addCircle(circleOptions);

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}