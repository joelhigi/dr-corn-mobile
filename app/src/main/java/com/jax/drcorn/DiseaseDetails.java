package com.jax.drcorn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DiseaseDetails extends AppCompatActivity {

    ImageView mainImageView;
    TextView title,description,treatment;

    String data1,data2,data3;
    int myPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_details);

        mainImageView = findViewById(R.id.mainImageView);
        title = findViewById(R.id.titleText);
        description = findViewById(R.id.descText);
        treatment = findViewById(R.id.treatmentText);

        getData();
        setData();
    }

    private void getData(){
        if(getIntent().hasExtra("title")&&getIntent().hasExtra("desc")&&getIntent().hasExtra("image")){

            data1 = getIntent().getStringExtra("title");
            myPic = getIntent().getIntExtra("image", 1);


            if(data1.equals("Blight")){
                data2 = getResources().getString(R.string.blight_det);
                data3 = getResources().getString(R.string.blight_treat);
            }
            else if(data1.equals("Common Rust")){
                data2 = getResources().getString(R.string.rust_det);
                data3 = getResources().getString(R.string.rust_treat);
            }
            else if(data1.equals("Gray Leaf Spot")){
                data2 = getResources().getString(R.string.gls_det);
                data3 = getResources().getString(R.string.gls_treat);
            }


        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
    private void setData(){
        mainImageView.setBackgroundResource(myPic);
        title.setText(data1);
        description.setText(data2);
        treatment.setText(data3);

    }
}