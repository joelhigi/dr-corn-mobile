package com.jax.drcorn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MaizeDiseases extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String s1[], s2[], s3[];
    int images[] = {R.drawable.blight, R.drawable.comrust, R.drawable.grayspot};
    RecyclerView recyclerDis;
    DrawerLayout drawerDisLayout;
    NavigationView navigationDisView;
    Toolbar toolDisBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maize_diseases);
        s1 = getResources().getStringArray(R.array.diseases);
        s2 = getResources().getStringArray(R.array.shortdesc);
        recyclerDis = findViewById(R.id.recyclerDis);

        DiseaseAdapter diseaseAd = new DiseaseAdapter(this, s1, s2, images);
        recyclerDis.setAdapter(diseaseAd);
        recyclerDis.setLayoutManager(new LinearLayoutManager(this));

        drawerDisLayout = findViewById(R.id.drawDisLayout);
        navigationDisView = findViewById(R.id.navDisView);
        toolDisBar = findViewById(R.id.toolDisBar);
        setSupportActionBar(toolDisBar);

        navigationDisView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerDisLayout, toolDisBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerDisLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDisView.setNavigationItemSelectedListener(this);
        navigationDisView.setCheckedItem(R.id.diseases);
    }

    @Override
    public void onBackPressed() {
        if(drawerDisLayout.isDrawerOpen(GravityCompat.START)){
            drawerDisLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.camera:

                Intent intent1 = new Intent(MaizeDiseases.this,MainActivity.class);
                startActivity(intent1);
                break;

            case R.id.scans:
                Intent intent = new Intent(MaizeDiseases.this,PastRecords.class);
                startActivity(intent);
                break;

            case R.id.diseases:
                break;

        }
        drawerDisLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}