package com.example.hadar.trempyteam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

public class MainAactivity extends Activity {
    FragmentManager fragmentManager;
    public static final int  REQUEST_CODE_ASK_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main_activity);

        fragmentManager = getFragmentManager();

        searchTrempFragment frg = new searchTrempFragment();
        NewTrempFragment frg2 = new NewTrempFragment();
        FragmentTransaction  transaction=fragmentManager.beginTransaction();//create an instance of Fragment-transaction

        transaction.add(R.id.My_Container_1_ID, frg, "Frag_Top_tag");
        transaction.add(R.id.My_Container_2_ID, frg2, "Frag_Middle_tag");

        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finish();
                }

                }
                return;
            }
        }


    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        Log.d("TAG", "handle action bar");
        inflater.inflate(R.menu.menu_buttons, menu);

        MenuItem add =  menu.findItem(R.id.personalArea);
        add.setVisible(true);
    }
}
