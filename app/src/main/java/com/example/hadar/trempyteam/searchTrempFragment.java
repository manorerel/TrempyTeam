package com.example.hadar.trempyteam;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class searchTrempFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the fragment layout file
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_tremp, container, false);
        try {
            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            setHasOptionsMenu(true);
        }
        catch (Exception e){
            Log.d("exception:" , "exception while trying to set action bar " + e.getMessage());
        }


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        Log.d("TAG", "handle action bar");
        inflater.inflate(R.menu.menu_buttons, menu);

        MenuItem personalArea =  menu.findItem(R.id.personalArea);
        personalArea.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.personalArea:{
//                getActivity().getFragmentManager().beginTransaction().replace(R.id.main_container, new NewStudentFragment()).addToBackStack(null).commit();
                Intent intent = new Intent(getActivity(), PersonalAreaActivity.class);
                startActivity(intent);
                return true;}
            default:
                return super.onOptionsItemSelected(item);
        }
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

