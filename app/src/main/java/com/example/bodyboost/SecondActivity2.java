package com.example.bodyboost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SecondActivity2 extends AppCompatActivity {


    int[] newArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second2);


        newArray = new int[]{


                R.id.bow_pose2, R.id.bridge_pose2, R.id.chair_pose2, R.id.child_pose2, R.id.cobbler_pose2, R.id.cow_pose2,
                R.id.playji_pose2, R.id.pauseji_pose2, R.id.plank_pose2, R.id.crunches_pose2,






        };
    }


    public void Imagebuttonclicked(View view) {



        for (int i=0;i< newArray.length;i++){


            if(view.getId() == newArray[i]) {
                int value = i+1;
                Log.i("FIRST", String.valueOf(value));
                Intent intent = new Intent(SecondActivity2.this, ThirdActivity2.class);
                intent.putExtra("value",String.valueOf(value));
                startActivity(intent);
            }
        }
    }
}