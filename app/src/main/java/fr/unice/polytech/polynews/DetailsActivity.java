package fr.unice.polytech.polynews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import java.io.IOException;
import java.sql.SQLException;

import fr.unice.polytech.polynews.asyncTasks.LoadImages;
import fr.unice.polytech.polynews.models.Mishap;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        int position = Integer.parseInt(intent.getStringExtra("position"));

        setContentView(R.layout.activity_details);

        Mishap mishap = new Database(getApplicationContext()).getMishap(position+1);

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

        /*TextView phone = findViewById(R.id.phoneNumber);
        phone.setText(mishap.getPhone());*/

        TextView cat = findViewById(R.id.cat);
        cat.setText(mishap.getCategory());

        TextView desc = findViewById(R.id.description);
        desc.setText(mishap.getDescription());

        MapView mapView = findViewById(R.id.mapView);

    }

}
