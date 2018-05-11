package fr.unice.polytech.polynews.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import java.io.IOException;
import java.sql.SQLException;


import fr.unice.polytech.polynews.Database;
import fr.unice.polytech.polynews.ViewAndAddActivity;
import fr.unice.polytech.polynews.adapters.NewsCustomAdapter;
import fr.unice.polytech.polynews.R;

/**
 * Created by Alex on 25/03/2018.
 */
public class NewsGridFragment extends Fragment {

    public NewsGridFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NewsGridFragment newInstance(int sectionNumber) {
        NewsGridFragment fragment = new NewsGridFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_grid, container, false);

        //gridView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        NewsCustomAdapter list = new NewsCustomAdapter(this.getContext(), new Database(getContext()).getAllMishaps());
        GridView gridView = (GridView) getView().findViewById(R.id.id_grid);
        gridView.setAdapter(list);
    }
}
