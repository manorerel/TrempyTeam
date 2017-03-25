package com.example.hadar.trempyteam;


import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class NewTrempFragment extends Fragment {


    final int createNewTremp = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the fragment layout file
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.new_tremp, container, false);

        Button btnCreate = (Button) rootView.findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CreateNewTrempActivity.class);
                startActivity(intent);
            }
        });






        return rootView;
    }
}
