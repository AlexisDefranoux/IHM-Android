package fr.unice.polytech.polynews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;

import fr.unice.polytech.polynews.asyncTasks.LoadImages;
import fr.unice.polytech.polynews.models.Article;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        int position = Integer.parseInt(intent.getStringExtra("position"));

        setContentView(R.layout.activity_details);

        NewsDBHelper bd = new NewsDBHelper(getBaseContext());
        try {
            bd.openDataBase();
        }
        catch (IOException e){
            Log.d("NEWSGRIDFRAGMENT","Error : " + e);
        }
        catch (SQLException e){
            Log.d("NEWSGRIDFRAGMENT","Error SQL : " + e);
        }
        Article article = bd.getAllArticles().get(position);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.placeholder);
        imageView.setImageBitmap(bitmap);
        LoadImages loadImages = new LoadImages(imageView);
        ImageView play = findViewById(R.id.play);

        if(article.getType() == "photo"){
            loadImages.execute(article.getUrl());
        }else if (article.getType() == "vidéo"){
            play.setVisibility(ImageView.VISIBLE);
            String urlVideo = article.getUrl();
            int db = urlVideo.indexOf("=");
            String idVideo = urlVideo.substring(db+1);
            loadImages.execute("https://img.youtube.com/vi/"+idVideo+"/default.jpg");
        }


        TextView titre = findViewById(R.id.titre);
        titre.setText(article.getTitre());

        TextView date = findViewById(R.id.date);
        date.setText(article.getDate().toString());

        TextView cat = findViewById(R.id.cat);
        cat.setText(article.getCategorie());
    }

}
