package com.example.hadar.trempyteam;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;

import java.util.List;

public class PersonalAreaActivity extends Activity {
 List<Tremp> trempsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);
        this.setTitle("האיזור האישי");

        Button cretedTremps = (Button)findViewById(R.id.createdTremps);
        cretedTremps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ModelSql modelSql = ModelSql.getInstance();
                trempsList = modelSql.getAllTremps(true);
                Intent intent = new Intent(PersonalAreaActivity.this, ListTrempActivity.class);

                startActivity(intent);

            }
        });
    }

}
