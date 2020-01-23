package com.example.nrg_monitor.main.app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.example.nrg_monitor.DbRequestHandler;
import com.example.nrg_monitor.R;
import com.example.nrg_monitor.ui.accounts.RegisterActivity;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final Integer READ_STORAGE_PERMISSION_CODE = 1;
    private static final Integer WRITE_STORAGE_PERMISSION_CODE = 2;
    private EasyImage easyImage;
    private DrawerLayout drawer;
    private SharedPreferences mSharedPreferences;
    private String username;
    private View headerView;
    private ArrayList<MediaFile> photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Log.d("Dima", "In main on create: Logged in status " + mSharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.logged_in), false));

        easyImage = new EasyImage.Builder(this).setCopyImagesToPublicGalleryFolder(true).setFolderName("Images").build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        ImageView profileImage = (ImageView) headerView.findViewById(R.id.profile_img);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
                    easyImage.openGallery(MainActivity.this);

                } else {
                    requestStoragePermission();
                }


            }
        });

        if (!mSharedPreferences.getString(getApplicationContext().getResources().getString(R.string.logged_in_user_image_uri), "").equals("")) {
            // EasyImage library gives a cached image to the app , upon restart it disappears
            //might save it later and add its uri to sharedPreferences
            // setUserImageOnStartup();
        }


        TextView navigation_username_text = (TextView) headerView.findViewById(R.id.username_text);
        navigation_username_text.setText(username);
        TextView navigation_email_text = (TextView) headerView.findViewById(R.id.email_text);
        navigation_email_text.setText(mSharedPreferences.getString(getApplicationContext().getResources().getString(R.string.logged_in_user_email), ""));
        new GetUsernameFromDB().execute(navigation_email_text.getText().toString());

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this).
                    setTitle("Storage permission needed").
                    setMessage("Application needs access to the storage\nto choose an image").
                    setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_CODE);

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();


        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                easyImage.openGallery(MainActivity.this);


            } else {

                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(@NotNull MediaFile[] mediaFiles, @NotNull MediaSource mediaSource) {

                onPhotosReturned(mediaFiles);
            }

            @Override
            public void onCanceled(@NotNull MediaSource source) {
                super.onCanceled(source);
            }

            @Override
            public void onImagePickerError(@NotNull Throwable error, @NotNull MediaSource source) {
                super.onImagePickerError(error, source);
            }
        });

    }

    private void onPhotosReturned(@NonNull MediaFile[] returnedPhotos) {
        photos.addAll(Arrays.asList(returnedPhotos));
        ImageView img = headerView.findViewById(R.id.profile_img);

        //done like this tho keep the uri in sharedPreferences ... too much hustle to do web service calls  to keep image uri inside db
        String uriStr = photos.get(0).getFile().getPath();
        mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user_image_uri), uriStr).apply();

        Uri imageFileUri = Uri.parse(uriStr);
        File file = new File(imageFileUri.getPath());


        //Need write storage permission might make it work later
        //Need that commented to code to save imaged to a specified location so I cant get its uri and pass it ot
        //Shared preferences so I can reference is in MainActivity on create because profile image gets lost on restart
        //The library im using fetches cached images that disappear on restart so uri from them has no use

        /*
        String root = Environment.getExternalStorageDirectory().toString();
        File newDir = new File(root+"/profileImg");
        newDir.mkdirs();
        String fname = "ProfileImg_"+username;
        File newFile = new File(newDir,fname);
        if(newFile.exists()){
            newFile.delete();
        }


        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        try {
            FileOutputStream out = new FileOutputStream(newFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
            out.flush();
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

         */


        Picasso.get()
                .load(file)
                .fit()
                .centerCrop()
                .into(img);


        photos.clear();
    }

    private void setUserImageOnStartup() {

        ImageView img = headerView.findViewById(R.id.profile_img);

        // uri of a cached image returns nothing upon restart
        String uriStr = mSharedPreferences.getString(getApplicationContext().getResources().getString(R.string.logged_in_user_image_uri), "");
        Uri imageFileUri = Uri.parse(uriStr);
        File file = new File(Objects.requireNonNull(imageFileUri.getPath()));

        Picasso.get().load(file).fit().centerCrop().into(img);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navigation_username_text = (TextView) headerView.findViewById(R.id.username_text);
        navigation_username_text.setText(username);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_device_control:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DeviceControlFragment()).commit();

                break;
            case R.id.nav_log_out:
                mSharedPreferences.edit().putBoolean(getApplicationContext().getResources().getString(R.string.logged_in), false).apply();
                mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user), "").apply();
                mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user_email), "").apply();
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(intent);

                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private class GetUsernameFromDB extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            DbRequestHandler dbRequestHandler = new DbRequestHandler();

            String usernameReturned = dbRequestHandler.getUsernameFromDB(strings[0]);

            return usernameReturned;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            username = s;
            mSharedPreferences.edit().putString(getApplicationContext().getResources().getString(R.string.logged_in_user), username).apply();
            TextView username_view = headerView.findViewById(R.id.username_text);
            username_view.setText(username);


        }
    }


}
