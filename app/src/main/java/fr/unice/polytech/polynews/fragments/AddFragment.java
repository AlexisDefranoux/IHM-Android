package fr.unice.polytech.polynews.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.InputStream;
import java.util.Date;

import fr.unice.polytech.polynews.Database;
import fr.unice.polytech.polynews.R;
import fr.unice.polytech.polynews.models.Mishap;
import fr.unice.polytech.polynews.models.User;

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
    private ImageView cameraView;
    private User user;

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
        TextView textCategory = (TextView) rootView.findViewById(R.id.textCategory);
        TextView textPlace = (TextView) rootView.findViewById(R.id.textPlace);
        TextView textPhoneNb = (TextView) rootView.findViewById(R.id.textPhoneNb);
        TextView textMandatory = (TextView) rootView.findViewById(R.id.textMandatory);
//        textView.setText("Add your mishap");
        textTitle.setText(R.string.add_title);
        textDescription.setText(R.string.add_description);
        textCategory.setText(R.string.add_category);
        textPlace.setText(R.string.add_place);
        textPhoneNb.setText(R.string.add_phone_number);
        textMandatory.setText(R.string.add_mandatory);

        //TODO user = ...
        user = new User(0, "test@gmail.com", "bon", "jean", "06", "mdp");

        Button buttonAdd = (Button) rootView.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(AddFragment.this);
        ImageButton imageView = (ImageButton) rootView.findViewById(R.id.image1);
        imageView.setOnClickListener(AddFragment.this);
        imageView = (ImageButton) rootView.findViewById(R.id.image2);
        imageView.setOnClickListener(AddFragment.this);
        imageView = (ImageButton) rootView.findViewById(R.id.image3);
        imageView.setOnClickListener(AddFragment.this);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean cameraViewUp = false;

        if (data == null) return;
        try {
            InputStream imageStream = getContext().getContentResolver().openInputStream(data.getData());
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            cameraView.setImageBitmap(selectedImage);
            cameraViewUp = true;
        } catch (Exception ignored) {
        }
        if (!cameraViewUp && data.getExtras() != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            cameraView.setImageBitmap(bitmap);
            cameraViewUp = true;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonAdd:
                onClickAdd(view);
                break;

            case R.id.image1:
                cameraView = rootView.findViewById(R.id.image1);
                onClickCamera();
                break;
            case R.id.image2:
                cameraView = rootView.findViewById(R.id.image2);
                onClickCamera();
                break;
            case R.id.image3:
                cameraView = rootView.findViewById(R.id.image3);
                onClickCamera();
                break;
        }
    }

    private void onClickAdd(View view) {
        EditText editCategory = (EditText) rootView.findViewById(R.id.editCategory);
        String category = editCategory.getText().toString();
        EditText editTitle = (EditText) rootView.findViewById(R.id.editTitle);
        String title = editTitle.getText().toString();
        EditText editPlace = (EditText) rootView.findViewById(R.id.editPlace);
        String place = editPlace.getText().toString();
        EditText editDescription = (EditText) rootView.findViewById(R.id.editDescription);
        String description = editDescription.getText().toString();
        EditText editPhone = (EditText) rootView.findViewById(R.id.editPhoneNb);
        String phone = editPhone.getText().toString();
        String state = "TO DO";
        String email = user.getEmail();

        String urgency = "High";
        //TODO ajouter radio urgency

        Database database = new Database(getContext());
        Mishap mishap = new Mishap(0, title, category, description, latitude, longitude, urgency,
                email, state, new Date().toString(), phone, place);

        database.addMishap(mishap);
    }

    private void onClickCamera() {
        Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intentGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooser = new Intent(Intent.createChooser(intentGalery, "Open with"));
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentTakePhoto});
        startActivityForResult(chooser,0);

        //startActivityForResult(intentTakePhoto, 0);
        //startActivityForResult(intentGalery, 1);
    }

    @Override
    public void onConnected(Bundle bundle) {
        TextView textLocation = (TextView) rootView.findViewById(R.id.location);
        textLocation.setText(R.string.add_location_cantfind);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; //Not enough permissions
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            textLocation.setText(getString(R.string.add_location_last, latitude, longitude));
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
            textLocation.setText(getString(R.string.add_location_now, latitude, longitude));
        }
    }
}