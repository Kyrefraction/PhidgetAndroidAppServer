package uk.ac.mmu.a15072935.phidgetvisualizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String[] PhidgetsList = {"Slider", "Force"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);
        ListView PhidgetListView = (ListView) findViewById(R.id.PhidgetListView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,PhidgetsList);
        PhidgetListView.setAdapter(arrayAdapter);

        PhidgetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "You have selected: " + PhidgetsList[i], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DisplayData.class);
                String intExt = PhidgetsList[i];
                intent.putExtra("STRING_I_NEED", intExt);
                startActivity(intent);
            }
        });
    }
}
