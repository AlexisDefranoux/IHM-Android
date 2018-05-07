package fr.unice.polytech.polynews.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.unice.polytech.polynews.R;
import fr.unice.polytech.polynews.asyncTasks.LoadImages;
import fr.unice.polytech.polynews.models.Article;

/**
 * Created by Alex on 26/03/2018.
 */

public class NewsCustomAdapter extends ArrayAdapter<Article> {

    public NewsCustomAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = inflater.inflate(R.layout.element, null);

        Article article = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.placeholder);
        imageView.setImageBitmap(bitmap);
        LoadImages loadImages = new LoadImages(imageView);
        ImageView play = convertView.findViewById(R.id.play);

        if(article.getType() == "photo"){
            loadImages.execute(article.getUrl());
        }else if (article.getType() == "vidéo"){
            play.setVisibility(ImageView.VISIBLE);
            String urlVideo = article.getUrl();
            int db = urlVideo.indexOf("=");
            String idVideo = urlVideo.substring(db+1);
            loadImages.execute("https://img.youtube.com/vi/"+idVideo+"/default.jpg");
        }


        TextView titre = convertView.findViewById(R.id.titre);
        titre.setText(article.getTitre());

        TextView date = convertView.findViewById(R.id.date);
        date.setText(article.getDate().toString());

        TextView cat = convertView.findViewById(R.id.cat);
        cat.setText(article.getCategorie());

        convertView.findViewById(R.id.tablelayout).setTag(position);

        return convertView;
    }
}
