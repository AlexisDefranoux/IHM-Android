package fr.unice.polytech.polynews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.unice.polytech.polynews.asyncTasks.LoadImages;
import fr.unice.polytech.polynews.models.Mishap;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int position = Integer.parseInt(getIntent().getStringExtra("position"));

        setContentView(R.layout.activity_details);

        final Mishap mishap = new Database(getApplicationContext()).getMishap(position+1);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.placeholder);
        imageView.setImageBitmap(bitmap);
        LoadImages loadImages = new LoadImages(imageView);
        ImageView play = findViewById(R.id.play);

        TextView titre = findViewById(R.id.titre);
        titre.setText(mishap.getTitleMishap());

        TextView date = findViewById(R.id.date);
        date.setText(mishap.getDate());

        TextView autor = findViewById(R.id.autor);
        autor.setText(mishap.getEmail());

        TextView ur = findViewById(R.id.urgency);
        ur.setText(mishap.getUrgency());

        TextView cat = findViewById(R.id.cat);
        cat.setText(mishap.getCategory());

        TextView desc = findViewById(R.id.description);
        desc.setText(mishap.getDescription());

        TextView place = findViewById(R.id.place);
        place.setText(mishap.getPlace());

        TextView state = findViewById(R.id.state);
        state.setText(mishap.getState());

        LinearLayout phoneZone = findViewById(R.id.phoneZone);

        TextView phone = findViewById(R.id.phoneNumber);
        phone.setText(mishap.getPhone());

        Button smsButton = findViewById(R.id.smsButton);
        smsButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(View v) {
                String number = mishap.getPhone();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
            }
        });
        Button callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(mishap.getPhone()));
                startActivity(callIntent);
            }});

        if (mishap.getPhone().isEmpty()){
            phoneZone.setVisibility(View.GONE);
        }


        final Button gmaps = findViewById(R.id.gmaps);
        gmaps.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("latitude", mishap.getLatitude());
                intent.putExtra("longitude", mishap.getLongitude());
                startActivityForResult(intent, 0);
            }
        });

    }
}
