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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);
        this.setTitle("האיזור האישי");

        Button cretedTremps = (Button)findViewById(R.id.createdTremps);
        cretedTremps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), ListTrempActivity.class);
                intent.putExtra("cameFrom","personalArea");
                intent.putExtra("isCreated", "true");

                startActivity(intent);

            }
        });

        Button joinedTremps = (Button)findViewById(R.id.joinedTremps);
        joinedTremps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), ListTrempActivity.class);
                intent.putExtra("cameFrom","personalArea");
                intent.putExtra("isCreated", "false");

                startActivity(intent);

            }
        });
    }

}
