package com.example.bodyboost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FoodActivity2 extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food2);


        String[] tstory = getResources().getStringArray(R.array.title_story);
        final String[] dstory = getResources().getStringArray(R.array.details_story);




        listView = findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row,R.id.rowtxt,tstory);
        listView.setAdapter(adapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private int position;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String t = dstory[position];
                Intent intent = new Intent(FoodActivity2.this,FitActivity.class);
                intent.putExtra("story",t);
                startActivity(intent);
            }
        });

    }

    public void foodgoback(View view) {



        Intent intent = new Intent(FoodActivity2.this,FoodActivityDeatils.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {




        Intent intent = new Intent(FoodActivity2.this,FoodActivityDeatils.class);
        startActivity(intent);
        finish();

    }
}