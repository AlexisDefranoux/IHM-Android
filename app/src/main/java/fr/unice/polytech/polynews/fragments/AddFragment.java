package fr.unice.polytech.polynews.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.unice.polytech.polynews.Database;
import fr.unice.polytech.polynews.R;
import fr.unice.polytech.polynews.activities.ViewAndAddActivity;
import fr.unice.polytech.polynews.models.Mishap;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class AddFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemSelectedListener {
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
    private boolean putLocation;
    static private String email;
    private boolean urgency;
    private String fragmentNumber = "0";
    private String category;
    private Uri imageUri;
    private boolean [] addImage = new boolean[3];
    private ImageView cameraView;
    private int imageN;

    public AddFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AddFragment newInstance(int sectionNumber, String email) {
        AddFragment.email = email;
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
        TextView textTitle = rootView.findViewById(R.id.textTitle);
        TextView textDescription = rootView.findViewById(R.id.textDescription);
        TextView textCategory = rootView.findViewById(R.id.textCategory);
        TextView textPlace = rootView.findViewById(R.id.textPlace);
        TextView textMandatory = rootView.findViewById(R.id.textMandatory);
        textTitle.setText(R.string.add_title);
        textDescription.setText(R.string.add_description);
        textCategory.setText(R.string.add_category);
        textPlace.setText(R.string.add_place_mandatory);
        textMandatory.setText(R.string.add_mandatory);

        Spinner editCategory = rootView.findViewById(R.id.editCategory);
        String [] categories = new String[] {"Autre", "Informatique","Eléctrique","Plomberie", "Propreté"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        editCategory.setAdapter(dataAdapter);
        editCategory.setOnItemSelectedListener(this);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            CheckBox addLocation = rootView.findViewById(R.id.addLocation);
            addLocation.setText(R.string.add_location_checkbox);
            addLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                TextView textPlace = rootView.findViewById(R.id.textPlace);
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    putLocation = b;
                    if (b)
                        textPlace.setText(R.string.add_place);
                    else
                        textPlace.setText(R.string.add_place_mandatory);
                }
            });
        }

        CheckBox addManyCheck = rootView.findViewById(R.id.addMany);
        addManyCheck.setText(R.string.addMany);
        addManyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    fragmentNumber = "1";
                else
                    fragmentNumber = "0";
            }
        });

        urgency = false;
        CheckBox checkBox = rootView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                urgency = b;
            }
        });


        Button buttonAdd = rootView.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(AddFragment.this);
        ImageButton imageView = rootView.findViewById(R.id.image1);
        imageView.setOnClickListener(AddFragment.this);
        imageView = rootView.findViewById(R.id.image2);
        imageView.setOnClickListener(AddFragment.this);
        imageView = rootView.findViewById(R.id.image3);
        imageView.setOnClickListener(AddFragment.this);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;
        try {
            Bitmap selectedImage;
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                Uri uri = data.getData();
                try { //Case 1 : permission + case gallery
                    selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (Exception e) { //Case 2 : permission + case take photo
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().managedQuery(imageUri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    selectedImage = BitmapFactory.decodeFile(cursor.getString(column_index));
                }
            } else { //case 3 : no permission + case take photo
                InputStream imageStream = getContext().getContentResolver().openInputStream(data.getData());
                selectedImage = BitmapFactory.decodeStream(imageStream);
            }
            if (selectedImage != null) {
                cameraView.setImageBitmap(selectedImage);
                addImage[imageN - 1] = true;
            }
        } catch (Exception e) {
            if (data.getExtras() != null) { //case 4 : no permission + case gallery
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                cameraView.setImageBitmap(bitmap);
                addImage[imageN-1] = true;
            }
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        category = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        category = "Autre";
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonAdd:
                onClickAdd(view);
                break;

            case R.id.image1:
                cameraView = rootView.findViewById(R.id.image1);
                imageN = 1;
                onClickCamera();
                break;
            case R.id.image2:
                cameraView = rootView.findViewById(R.id.image2);
                imageN = 2;
                onClickCamera();
                break;
            case R.id.image3:
                cameraView = rootView.findViewById(R.id.image3);
                imageN = 3;
                onClickCamera();
                break;
        }
    }

    private Bitmap getResizedBitmap(Bitmap bm, int value) {
        int newWidth = 0, newHeight = 0;
        int width = bm.getWidth();
        int height = bm.getHeight();
        //if (width < value && height < value) return bm;
        if (width > height) newWidth = value;
        else newHeight = value;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        if(scaleHeight == 0) matrix.postScale(scaleWidth, scaleWidth);
        else matrix.postScale(scaleHeight, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }

    private byte[] imageViewToByte(ImageView imageView) {
        Bitmap bitmap = getResizedBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap(), 1000);
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }
        return null;
    }

    private void onClickAdd(View view) {
        EditText editTitle = rootView.findViewById(R.id.editTitle);
        String title = editTitle.getText().toString();
        EditText editPlace = rootView.findViewById(R.id.editPlace);
        String place = editPlace.getText().toString();
        EditText editDescription = rootView.findViewById(R.id.editDescription);
        String description = editDescription.getText().toString();

        double lati = 0, longi = 0;
        if (putLocation) {
            lati = latitude;
            longi = longitude;
        }

        byte[] image1 = null, image2 = null, image3 = null;
        if (addImage[0]) {
            ImageView imageView = rootView.findViewById(R.id.image1);
            image1 = imageViewToByte(imageView);
        }
        if (addImage[1]) {
            ImageView imageView = rootView.findViewById(R.id.image2);
            image2 = imageViewToByte(imageView);
        }
        if (addImage[2]) {
            ImageView imageView = rootView.findViewById(R.id.image3);
            image3 = imageViewToByte(imageView);
        }

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        Database database = new Database(getContext());
        Mishap mishap = new Mishap(0, title, category, description, lati, longi, urgency,
                email, "Non traité", date, "0664035799", place, image1, image2, image3);

        long res = database.addMishap(mishap);
        if (res != -1) {
            Intent intent = new Intent(getContext(), ViewAndAddActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("fragmentNumber", fragmentNumber);
            startActivityForResult(intent, 0);
            getActivity().finish(); //Si on appuie sur la touche retour on revient sur la connexion
        }

    }

    private void onClickCamera() {
        Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Nouvelle photo");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Depuis l'appareil photo");
            imageUri = getContext().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //can write on external storage : save photo for a better quality
        }

        Intent intentGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooser = new Intent(Intent.createChooser(intentGalery, "Ouvrir avec"));
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentTakePhoto});
        startActivityForResult(chooser,0);
    }

    @Override
    public void onConnected(Bundle bundle) {
        CheckBox addLocation = rootView.findViewById(R.id.addLocation);
        addLocation.setVisibility(View.INVISIBLE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; //Not enough permissions
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            addLocation.setVisibility(View.VISIBLE);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            CheckBox addLocation = rootView.findViewById(R.id.addLocation);
            addLocation.setVisibility(View.VISIBLE);
            mLocation = location;
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }
    }
}