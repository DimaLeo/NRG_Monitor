package temp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.nrg_monitor.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if(savedInstanceState==null){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HubFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_hub);

        }




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_hub:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HubFragment()).commit();

                break;
            case R.id.nav_suggestions:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SuggestionsFragment()).commit();

                break;
            case R.id.nav_about:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();

                break;
            case R.id.nav_device_control:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DeviceControlFragment()).commit();
                break;

            case R.id.nav_log_out:
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_other:
                Toast.makeText(this, "Something", Toast.LENGTH_SHORT).show();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}