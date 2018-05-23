package fr.unice.polytech.polynews.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.unice.polytech.polynews.R;

public class MapsFragment extends Fragment {

    private View rootView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    MapView mapView;
    GoogleMap map;

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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-122.084, -37.422);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        // Updates the location and zoom of the MapView
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLatitude()), 10);
//        map.animateCamera(cameraUpdate);
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        return rootView;
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