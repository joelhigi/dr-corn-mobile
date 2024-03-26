package com.jax.drcorn;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.jax.drcorn.databinding.ActivityPastRecordDetailsBinding;

public class PastRecordDetails extends AppCompatActivity {

    private ActivityPastRecordDetailsBinding binding;
    TextView miniT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPastRecordDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        //toolBarLayout.setTitle(getTitle());

        miniT = findViewById(R.id.miniText);
        Intent intent = getIntent();
        String dis = intent.getStringExtra("Disease");
        String pred = intent.getStringExtra("Pred");
        if(dis.equals("Blight")){
            miniT.setText(R.string.blight_text);
            toolBarLayout.setTitle("Blight"+" ("+pred+")");
        }
        else if(dis.equals("Common Rust")){
            miniT.setText(R.string.common_rust_text);
            toolBarLayout.setTitle("Common Rust"+" ("+pred+")");
        }
        else if(dis.equals("Gray Leaf Spot")){
            miniT.setText(R.string.gls_text);
            toolBarLayout.setTitle("Gray Leaf Spot"+" ("+pred+")");
        }
        else if(dis.equals("Healthy")){
            miniT.setText(R.string.healthy_text);
            toolBarLayout.setTitle("Healthy"+" ("+pred+")");
        }

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}