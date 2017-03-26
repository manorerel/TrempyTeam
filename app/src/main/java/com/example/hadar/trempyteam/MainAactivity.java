package com.example.hadar.trempyteam;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainAactivity extends Activity {
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //tran
        fragmentManager = getFragmentManager();

        searchTrempFragment frg = new searchTrempFragment();
        NewTrempFragment frg2 = new NewTrempFragment();
        FragmentTransaction  transaction=fragmentManager.beginTransaction();//create an instance of Fragment-transaction

        transaction.add(R.id.My_Container_1_ID, frg, "Frag_Top_tag");
        transaction.add(R.id.My_Container_2_ID, frg2, "Frag_Middle_tag");


        transaction.commit();

    }
}
