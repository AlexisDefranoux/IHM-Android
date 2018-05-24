package fr.unice.polytech.polynews.activities;

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

import fr.unice.polytech.polynews.Database;
import fr.unice.polytech.polynews.R;
import fr.unice.polytech.polynews.models.Mishap;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int position = Integer.parseInt(getIntent().getStringExtra("position"));

        setContentView(R.layout.activity_details);

        final Mishap mishap = new Database(getApplicationContext()).getMishap(position+1);

        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.placeholder);
        imageView.setImageBitmap(bitmap);

        byte[] image1 = mishap.getImage1(), image2 = mishap.getImage2(), image3 = mishap.getImage3();
        if (image1 != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(image1, 0, image1.length));
            imageView = (ImageView) findViewById(R.id.imageView2);
        }
        if (image2 != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(image2, 0, image2.length));
            imageView = (ImageView) findViewById(R.id.imageView3);
        }
        if (image3 != null)
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(image3, 0, image3.length));

        TextView titre = findViewById(R.id.titre);
        titre.setText(mishap.getTitleMishap());

        TextView date = findViewById(R.id.date);
        date.setText(mishap.getDate());

        TextView autor = findViewById(R.id.autor);
        autor.setText(mishap.getEmail());

        TextView ur = findViewById(R.id.urgency);
        if (mishap.getUrgency())
            ur.setText("Urgent");
        else
            ur.setText("non urgent");

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
            public void onClick(View v) {
                String number = mishap.getPhone();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
            }
        });
        Button callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mishap.getPhone(), null));
                startActivity(callIntent);
            }});

        if (mishap.getPhone().isEmpty()){
            phoneZone.setVisibility(View.GONE);
        }

        Button mailButton = findViewById(R.id.mailButton);
        mailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + mishap.getEmail() + "?subject=" + "[PolyIncident]"+mishap.getTitleMishap() + "&body=" + "");
                emailIntent.setData(data);
                startActivity(emailIntent);


            }
        });

        final Button gmaps = findViewById(R.id.gmaps);
        gmaps.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("latitude", mishap.getLatitude());
                intent.putExtra("longitude", mishap.getLongitude());
                if(mishap.getPlace() == null || mishap.getPlace().equals(""))
                    intent.putExtra("titre", mishap.getTitleMishap());
                else
                    intent.putExtra("titre", mishap.getPlace());

                startActivityForResult(intent, 0);
            }
        });

    }
}
