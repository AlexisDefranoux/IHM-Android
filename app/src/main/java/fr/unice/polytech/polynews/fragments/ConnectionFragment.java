package fr.unice.polytech.polynews.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.unice.polytech.polynews.activities.ViewAndAddActivity;
import fr.unice.polytech.polynews.R;

/**
 * Created by Alex on 25/03/2018.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class ConnectionFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private View rootView;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public ConnectionFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ConnectionFragment newInstance() {
        ConnectionFragment fragment = new ConnectionFragment();
        Bundle args = new Bundle();
        Log.i("PlaceHolder", "message");
        args.putInt(ARG_SECTION_NUMBER, 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.connection, container, false);
        Button btnClickMe = rootView.findViewById(R.id.buttonConnection);
        btnClickMe.setOnClickListener(ConnectionFragment.this);

        TextView textID = rootView.findViewById(R.id.textID);
        textID.setText(R.string.username);
        TextView textMDP = rootView.findViewById(R.id.textMDP);
        textMDP.setText(R.string.password);




        return rootView;
    }

    @Override
    public void onClick(View view) {

        EditText editID = rootView.findViewById(R.id.editID);
        String ID = editID.getText().toString();
        EditText editMDP = rootView.findViewById(R.id.editMDP);
        String MDP = editMDP.getText().toString();

        if (MDP.equals("mdp") && (ID.equals("oscar@etu.unice.fr") || ID.equals("philippe@etu.unice.fr") || ID.equals("theresa@etu.unice.fr"))) {
            Intent intent = new Intent(getContext(), ViewAndAddActivity.class);
            intent.putExtra("email", ID);
            startActivityForResult(intent, 0);
        }
    }
}