package fr.unice.polytech.polynews.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.unice.polytech.polynews.R;
import fr.unice.polytech.polynews.ViewAndAddActivity;

/**
 * Created by Alex on 25/03/2018.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Button btnClickMe;
    private View rootView;

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
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
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

        btnClickMe = (Button) rootView.findViewById(R.id.buttonContinue);
        btnClickMe.setOnClickListener(PlaceholderFragment.this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        EditText editSomething = (EditText)rootView.findViewById(R.id.editSomething);
        String something = editSomething.getText().toString();
        EditText editTitle = (EditText)rootView.findViewById(R.id.editTitle);
        String title = editTitle.getText().toString();
        EditText editAnotherThing = (EditText)rootView.findViewById(R.id.editAnotherThing);
        String anotherThing = editAnotherThing.getText().toString();
        EditText editSomethingElse = (EditText)rootView.findViewById(R.id.editSomethingElse);
        String somethingElse = editSomethingElse.getText().toString();
        EditText editDescription = (EditText)rootView.findViewById(R.id.editText);
        String description = editDescription.getText().toString();
    }

}