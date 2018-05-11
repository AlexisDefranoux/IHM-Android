package fr.unice.polytech.polynews.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import fr.unice.polytech.polynews.R;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * Created by Alex on 25/03/2018.
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class AddFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Button btnClickMe;
    private View rootView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    private double latitude;
    private double longitude;

    public AddFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AddFragment newInstance(int sectionNumber) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        Log.i("PlaceHolder", "message");
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        TextView textTitle = (TextView) rootView.findViewById(R.id.textTitle);
        TextView textDescription = (TextView) rootView.findViewById(R.id.textDescription);
        TextView textSomething = (TextView) rootView.findViewById(R.id.textSomething);
        TextView textSomethingElse = (TextView) rootView.findViewById(R.id.textSomethingElse);
        TextView textAnotherThing = (TextView) rootView.findViewById(R.id.textAnotherThing);
//        textView.setText("Add your mishap");
        textTitle.setText("Title : ");
        textDescription.setText("Description : ");
        textSomething.setText("Something : ");
        textSomethingElse.setText("SomethingElse : ");
        textAnotherThing.setText("AnotherThing : ");

        btnClickMe = (Button) rootView.findViewById(R.id.buttonContinue);
        btnClickMe.setOnClickListener(AddFragment.this);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        EditText editSomething = (EditText) rootView.findViewById(R.id.editSomething);
        String something = editSomething.getText().toString();
        EditText editTitle = (EditText) rootView.findViewById(R.id.editTitle);
        String title = editTitle.getText().toString();
        EditText editAnotherThing = (EditText) rootView.findViewById(R.id.editAnotherThing);
        String anotherThing = editAnotherThing.getText().toString();
        EditText editSomethingElse = (EditText) rootView.findViewById(R.id.editSomethingElse);
        String somethingElse = editSomethingElse.getText().toString();
        EditText editDescription = (EditText) rootView.findViewById(R.id.editText);
        String description = editDescription.getText().toString();
    }

    @Override
    public void onConnected(Bundle bundle) {
        TextView textLocation = (TextView) rootView.findViewById(R.id.location);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            textLocation.setText("Can't find location");
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            //startLocationUpdates();
            textLocation.setText("Can't find location");
            return;
        }
        //if (mLocation != null) {
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();
        textLocation.setText("Location is latitude " + latitude + " and longitude " + longitude);
        //}
    }

    /*protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }*/

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onLocationChanged(Location location) {

    }
}