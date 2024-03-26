package com.jax.drcorn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.google.android.material.navigation.NavigationView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    Socket s;
    PrintWriter pw;
    String message = "NoOrder";
    ProgressDialog dialog,dialog1;
    AlertDialog.Builder conAlert;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolBar;
    ImageView cornpic;
    Button b, submitPic;
    Bitmap foof;
    int size;
    byte[] foofByte;
    private String Document_img1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conAlert = new AlertDialog.Builder(MainActivity.this);
        conAlert.setTitle("Server not found");
        conAlert.setMessage("The server was not found, check if this device or the server is online.");
        conAlert.setNegativeButton("Okay",null);
        conAlert.setIcon(android.R.drawable.ic_dialog_alert);


        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Sending Instruction, please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog1 = new ProgressDialog(MainActivity.this);
        dialog1.setMessage("Sending Picture, please wait...");
        dialog1.setIndeterminate(true);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        setContentView(R.layout.activity_main);

        cornpic = findViewById(R.id.CornPic);
        drawerLayout = findViewById(R.id.drawLayout);
        navigationView = findViewById(R.id.navView);
        toolBar = findViewById(R.id.toolBar);


        setSupportActionBar(toolBar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.camera);


        b=(Button)findViewById(R.id.btnSelectPhoto);
        submitPic=findViewById(R.id.btnSubmitPhoto);

        //Requesting for camera permission
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },100);
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        submitPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPic sendInst = new sendPic();

                sendInst.execute();
            }
        });
        cornpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                selectImage();
            }

        });
    }

    class sendPic extends AsyncTask<Void,Void,Void> {
        @NonNull
        @Override
        protected Void doInBackground(Void...params){
            try {
                s = new Socket("192.168.239.158", 8800);

                pw = new PrintWriter(s.getOutputStream());

                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                Scanner sc = new Scanner(s.getInputStream());
                pw.write(message);
                runOnUiThread(new Runnable(){
                    public void run(){
                        dialog1.show();
                    }
                });
                pw.flush();
                dos.writeInt(foofByte.length);
                dos.write(foofByte);
                dos.flush();
                //s.shutdownOutput();
                //dos.close();
                String pred = sc.nextLine();
                String fromServer = sc.next();
                Log.e("Value:",fromServer);
                runOnUiThread(new Runnable() {

                    public void run() {
                        Toast.makeText(MainActivity.this, fromServer, Toast.LENGTH_LONG).show();
                        if ((fromServer.equals("Blight")) || (fromServer.equals("Common_Rust")) || (fromServer.equals("Gray_Leaf_Spot")) || (fromServer.equals("Healthy"))) {
                            dialog.dismiss();
                            MySqliteHelper myDB = new MySqliteHelper(MainActivity.this);
                            long millis = System.currentTimeMillis();
                            java.sql.Date date = new java.sql.Date(millis);
                            String sDate = date.toString();
                            String dis="";
                            float realPredFloat = Float.parseFloat(pred);
                            realPredFloat = realPredFloat/100;
                            String finalPred = Float.toString(realPredFloat);
                            finalPred = finalPred+"%";
                            //String finalPred = "";
                            if(fromServer.equals("Blight")){
                                dis = "Blight";
                            }
                            else if(fromServer.equals("Common_Rust")){
                                dis = "Common Rust";
                            }
                            else if(fromServer.equals("Gray_Leaf_Spot")){
                                dis = "Gray Leaf Spot";
                            }
                            else if(fromServer.equals("Healthy")){
                                dis = "Healthy";
                            }
                            myDB.addRecord(dis,sDate,finalPred);
                            Intent diseaseIntent = new Intent(MainActivity.this, DisplayDisease.class);
                            diseaseIntent.putExtra("Disease", fromServer);
                            diseaseIntent.putExtra("Pred", finalPred);
                            startActivity(diseaseIntent);
                        }
                    }
                });
                    s.close();
                    dialog1.dismiss();

//                dialog.dismiss();


            } catch(UnknownHostException e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        conAlert.show();
                    }
                });
            } catch(IOException e){
                e.printStackTrace();
                dialog.dismiss();
            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return null;
        }
    }

    class takePic extends AsyncTask<Void,Void,Void> {
        @NonNull
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void...params){
            try {
                s = new Socket("192.168.0.100", 8800);

                pw = new PrintWriter(s.getOutputStream());

                pw.write(message);
                runOnUiThread(new Runnable() {

                    public void run() {
                        dialog.show();
                    }
                });

                pw.flush();
                Scanner sc = new Scanner(s.getInputStream());
                String pred = sc.nextLine();
                String fromServer = sc.next();
                Log.e("Value:",fromServer);
                runOnUiThread(new Runnable() {

                    public void run() {
                        Toast.makeText(MainActivity.this, fromServer, Toast.LENGTH_LONG).show();
                        if ((fromServer.equals("Blight")) || (fromServer.equals("Common_Rust")) || (fromServer.equals("Gray_Leaf_Spot")) || (fromServer.equals("Healthy"))) {
                            dialog.dismiss();
                            MySqliteHelper myDB = new MySqliteHelper(MainActivity.this);
                            long millis = System.currentTimeMillis();
                            java.sql.Date date = new java.sql.Date(millis);
                            String sDate = date.toString();
                            String dis="";
                            float realPredFloat = Float.parseFloat(pred);
                            realPredFloat = realPredFloat/100;
                            String finalPred = Float.toString(realPredFloat);
                            finalPred = finalPred+"%";
                            //String finalPred = "";
                            if(fromServer.equals("Blight")){
                                dis = "Blight";
                            }
                            else if(fromServer.equals("Common_Rust")){
                                dis = "Common Rust";
                            }
                            else if(fromServer.equals("Gray_Leaf_Spot")){
                                dis = "Gray Leaf Spot";
                            }
                            else if(fromServer.equals("Healthy")){
                                dis = "Healthy";
                            }
                            myDB.addRecord(dis,sDate,finalPred);
                            Intent diseaseIntent = new Intent(MainActivity.this, DisplayDisease.class);
                            diseaseIntent.putExtra("Disease", fromServer);
                            diseaseIntent.putExtra("Pred", finalPred);
                            startActivity(diseaseIntent);
                        }
                    }
                });
                Log.e("tag", fromServer);

                s.close();
                message = "NoOrder";
                dialog.dismiss();
            } catch (UnknownHostException e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        conAlert.show();
                    }
                });
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }


    private void selectImage() {
        final CharSequence[] options = { "Take Picture with Phone Camera", "Choose from Gallery","Take picture with PC Webcam","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Picture with Phone Camera"))
                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(MainActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider", f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Take picture with PC Webcam"))
                {
                    takePic RPiPic = new takePic();
                    message = "TakePic";
                    RPiPic.execute();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    bitmap = getResizedBitmap(bitmap,350);
                    cornpic.setImageBitmap(bitmap);
                    foof = bitmap;
                    ByteArrayOutputStream foo = new ByteArrayOutputStream();
                    foof.compress(Bitmap.CompressFormat.PNG, 100,foo);
                    foofByte = foo.toByteArray();
                    //foof.recycle();
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = getResizedBitmap(thumbnail, 350);
                Log.w("path of image ..*****..", picturePath+"");
                cornpic.setImageBitmap(thumbnail);
                foof = thumbnail;
                ByteArrayOutputStream foo = new ByteArrayOutputStream();
                foof.compress(Bitmap.CompressFormat.PNG, 100,foo);
                foofByte = foo.toByteArray();
            }
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.camera:
                break;

            case R.id.scans:
                Intent intent = new Intent(MainActivity.this,PastRecords.class);
                startActivity(intent);
                break;

            case R.id.diseases:
                Intent intent1 = new Intent(MainActivity.this,MaizeDiseases.class);
                startActivity(intent1);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}