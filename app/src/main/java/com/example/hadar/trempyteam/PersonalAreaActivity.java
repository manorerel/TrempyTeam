package com.example.hadar.trempyteam;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.example.hadar.trempyteam.Model.ModelSql;

public class PersonalAreaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);
        this.setTitle("האיזור האישי");

        Button cretedTremps = (Button)findViewById(R.id.createdTremps);
        cretedTremps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ModelSql modelSql = ModelSql.getInstance();
                modelSql.getAllTremps(true);

            }
        });
    }

}
