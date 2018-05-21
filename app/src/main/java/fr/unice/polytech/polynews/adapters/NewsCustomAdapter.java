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
import fr.unice.polytech.polynews.models.Mishap;

/**
 * Created by Alex on 26/03/2018.
 */

public class NewsCustomAdapter extends ArrayAdapter<Mishap> {

    public NewsCustomAdapter(Context context, List<Mishap> mishaps) {
        super(context, 0, mishaps);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = inflater.inflate(R.layout.element, null);

        Mishap mishap = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.placeholder);
        imageView.setImageBitmap(bitmap);

        TextView titre = convertView.findViewById(R.id.titre);
        titre.setText(mishap.getTitleMishap());

        TextView date = convertView.findViewById(R.id.date);
        date.setText(mishap.getDate());

        TextView cat = convertView.findViewById(R.id.cat);
        cat.setText(mishap.getCategory());

        convertView.findViewById(R.id.tablelayout).setTag(position);

        byte[] image1 = mishap.getImage1(), image2 = mishap.getImage2(), image3 = mishap.getImage3();
        if (image1 != null)
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(image1, 0, image1.length));
        else if (image2 != null)
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(image2, 0, image2.length));
        else if (image3 != null)
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(image3, 0, image3.length));

        return convertView;
    }
}
