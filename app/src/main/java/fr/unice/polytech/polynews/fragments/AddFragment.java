package fr.unice.polytech.polynews.fragments;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
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

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

import fr.unice.polytech.polynews.R;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class AddFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
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
        TextView textPhoneNb = (TextView) rootView.findViewById(R.id.textPhoneNb);
        TextView textMandatory = (TextView) rootView.findViewById(R.id.textMandatory);
//        textView.setText("Add your mishap");
        textTitle.setText("Title * : ");
        textDescription.setText("Description * : ");
        textSomething.setText("Something : ");
        textSomethingElse.setText("SomethingElse : ");
        textPhoneNb.setText("Phone number : ");
        textMandatory.setText("* Mandatory");

        Button btnClickMe = (Button) rootView.findViewById(R.id.buttonContinue);
        btnClickMe.setOnClickListener(AddFragment.this);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        return rootView;
    }

    @Override
    public void onClick(View view) {
        EditText editSomething = (EditText) rootView.findViewById(R.id.editSomething);
        String something = editSomething.getText().toString();
        EditText editTitle = (EditText) rootView.findViewById(R.id.editTitle);
        String title = editTitle.getText().toString();
        EditText editAnotherThing = (EditText) rootView.findViewById(R.id.editPhoneNb);
        String anotherThing = editAnotherThing.getText().toString();
        EditText editSomethingElse = (EditText) rootView.findViewById(R.id.editSomethingElse);
        String somethingElse = editSomethingElse.getText().toString();
        EditText editDescription = (EditText) rootView.findViewById(R.id.editText);
        String description = editDescription.getText().toString();
    }

    @Override
    public void onConnected(Bundle bundle) {
        TextView textLocation = (TextView) rootView.findViewById(R.id.location);
        textLocation.setText("Can't find location");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; //Not enough permissions
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            textLocation.setText("Your last location is latitude " + latitude + " and longitude " + longitude);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), 9000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            TextView textLocation = (TextView) rootView.findViewById(R.id.location);
            mLocation = location;
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
            textLocation.setText("Your location is latitude " + latitude + " and longitude " + longitude);
        }
    }
}