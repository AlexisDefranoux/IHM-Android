package fr.unice.polytech.polynews.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.unice.polytech.polynews.R;

/**
 * Created by Alex on 25/03/2018.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        Log.i("PlaceHolder", "message");
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        TextView textTitle = (TextView) rootView.findViewById(R.id.textTitle);
        TextView textDescription = (TextView) rootView.findViewById(R.id.textDescription);
        TextView textSomething = (TextView) rootView.findViewById(R.id.textSomething);
        TextView textSomethingElse = (TextView) rootView.findViewById(R.id.textSomethingElse);
        TextView textAnotherThing = (TextView) rootView.findViewById(R.id.textAnotherThing);
//        textView.setText("Add your mishap");
        textTitle.setText("Title : ");
        textDescription.setText("Description : ");
        textSomething.setText("Something : ");
        textSomethingElse.setText("SomethingElse : ");
        textAnotherThing.setText("AnotherThing : ");
        return rootView;
    }


}