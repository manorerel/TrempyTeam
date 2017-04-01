package com.example.hadar.trempyteam;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.Tremp;

import java.util.List;

public class searchTrempFragment extends Fragment {

    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the fragment layout file
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_tremp, container, false);


        Button btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GOOD
              //  Intent intent = new Intent(getActivity(), ListTrempActivity.class);
              //  startActivity(intent);

                /*EditText phone = (EditText)rootView.findViewById(R.id.editTextPhone);
                EditText source = (EditText)rootView.findViewById(R.id.exitfrom);
                EditText dest = (EditText)rootView.findViewById(R.id.dest);
                EditText seatsText = (EditText)rootView.findViewById(R.id.avaliable_seats);
                DateEditText date = (DateEditText)rootView.findViewById(R.id.date);
                TimeEditText time = (TimeEditText)rootView.findViewById(R.id.time);
                EditText carModel = (EditText)rootView.findViewById(R.id.car_model);

                List<Tremp> trempsList;
                ModelFirebase fbModel = new ModelFirebase();
                fbModel.getAllTremps(new Model.GetAllTrempListener() {
                    @Override
                    public void onComplete(List<Tremp> tremps) {
                        for (Tremp tr : tremps)
                        {
                            Log.d("tremp user phone: ", tr.getPhoneNumber());
                        }
                    }
                });*/


            }
        });
        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}

