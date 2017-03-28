package com.example.hadar.trempyteam;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;

import java.util.Date;

public class CreateNewTrempActivity extends Activity {

    //check hadariiiii eliyahu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_tremp);
        final ModelFirebase fbModel = new ModelFirebase();
        Button saveBtn = (Button) findViewById(R.id.btnSave);
        Button cancleBtn = (Button) findViewById(R.id.btnCancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText phone = (EditText)findViewById(R.id.editTextPhone);
                EditText source = (EditText)findViewById(R.id.exitfrom);
                EditText dest = (EditText)findViewById(R.id.dest);
                EditText seetsText = (EditText)findViewById(R.id.num_of_seats);
                DateEditText dateText = (DateEditText)findViewById(R.id.date);
                TimeEditText time = (TimeEditText)findViewById(R.id.time);

                //int seets = int.class.cast(seetsText.getText());
                int seets = 3;
                Date date = new Date(dateText.getYear(), dateText.getMonth(), dateText.getDay());
              Tremp newTremp = new Tremp(seets, "dd", date, source.getText().toString(), dest.getText().toString(), "dd");
//                fbModel.addTremp(newTremp);
                //ModelSql sqlLight = new Mod5elSql();
                //sqlLight.addTremp(newTremp);
                Log.d("TAG", "Create new tremp and save to db");
                finish();
            }
        });

    }
}
