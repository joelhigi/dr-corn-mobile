package com.jax.drcorn;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jax.drcorn.databinding.ActivityDisplayDiseaseBinding;

public class DisplayDisease extends AppCompatActivity {

private ActivityDisplayDiseaseBinding binding;
TextView superT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityDisplayDiseaseBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        //setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        //toolBarLayout.setTitle(getTitle());
        superT = findViewById(R.id.superText);
        Intent intent = getIntent();
        String dis = intent.getStringExtra("Disease");
        String pred = intent.getStringExtra("Pred");
        if(dis.equals("Blight")){
            superT.setText(R.string.blight_text);
            toolBarLayout.setTitle("Blight"+" ("+pred+")");
        }
        else if(dis.equals("Common_Rust")){
            superT.setText(R.string.common_rust_text);
            toolBarLayout.setTitle("Common Rust"+" ("+pred+")");
        }
        else if(dis.equals("Gray_Leaf_Spot")){
            superT.setText(R.string.gls_text);
            toolBarLayout.setTitle("Gray Leaf Spot"+" ("+pred+")");
        }
        else if(dis.equals("Healthy")){
            superT.setText(R.string.healthy_text);
            toolBarLayout.setTitle("Healthy"+" ("+pred+")");
        }


        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}