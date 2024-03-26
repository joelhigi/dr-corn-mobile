package com.jax.drcorn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PastRecords extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerPastLayout;
    NavigationView navigationPastView;
    Toolbar toolPastBar;

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data;

    MySqliteHelper myDB;
    ArrayList<String> record_id, disease_name, pred_value, disease_date;

    RecordAdapter recordAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_records);

        drawerPastLayout = findViewById(R.id.drawPastLayout);
        navigationPastView = findViewById(R.id.navPastView);
        toolPastBar = findViewById(R.id.toolPastBar);

        setSupportActionBar(toolPastBar);

        navigationPastView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerPastLayout, toolPastBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerPastLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationPastView.setNavigationItemSelectedListener(this);
        navigationPastView.setCheckedItem(R.id.scans);

        recyclerView = findViewById(R.id.viewPastRecords);
        myDB = new MySqliteHelper(PastRecords.this);
        record_id = new ArrayList<>();
        disease_name = new ArrayList<>();
        pred_value = new ArrayList<>();
        disease_date = new ArrayList<>();

        storeInArrays();

        recordAdapter = new RecordAdapter(PastRecords.this, this, record_id, disease_name, pred_value, disease_date);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PastRecords.this));

        //Requesting for permission to read storage
        if(ContextCompat.checkSelfPermission(PastRecords.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PastRecords.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No data available",Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                record_id.add(cursor.getString(0));
                disease_name.add(cursor.getString(1));
                pred_value.add(cursor.getString(2));
                disease_date.add(cursor.getString(3));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerPastLayout.isDrawerOpen(GravityCompat.START)){
            drawerPastLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.camera:
                Intent intent1 = new Intent(PastRecords.this,MainActivity.class);
                startActivity(intent1);
                break;

            case R.id.scans:
                break;

            case R.id.diseases:
                Intent intent = new Intent(PastRecords.this,MaizeDiseases.class);
                startActivity(intent);
                break;

        }
        drawerPastLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}