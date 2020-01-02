package de.bachtalia.bachtaliaapp.Events.ListAndDetails;

/*
 * Created by Manuel Lanzinger on 17. Dezember 2018.
 * For the project: Bachtalia Terminkalender.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import de.bachtalia.bachtaliaapp.Contact.ContactsActivity;
import de.bachtalia.bachtaliaapp.PackinglistDatabase.PackingList;
import de.bachtalia.bachtaliaapp.R;

public class EventsListActivity extends AppCompatActivity {

    private AllEventFragment allEventFragment;
    private BottomNavigationView bottomNav;
    private Fragment lastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        setStatusBarColored(this);

        // Initialize Bottom Navigation Menu and Listener
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

       allEventFragment = new AllEventFragment();


       lastFragment = allEventFragment;


    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                lastFragment).commit();
    }

    /*
     * Methods for initializing the menu and select the choosen Item
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.itemPackingList){
            Intent packingIntent = new Intent(EventsListActivity.this, PackingList.class);
            startActivity(packingIntent);
            return true;
        }else if(id == R.id.itemContact){
            Intent contactIntent = new Intent(EventsListActivity.this, ContactsActivity.class);
            startActivity(contactIntent);
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

    /*
     * Private Attribute: Listener for the Bottom Navigation
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_allEvents:
                            selectedFragment = allEventFragment;
                            break;
                        case R.id.nav_big_events:
                            selectedFragment = new BigEventFragment();
                            break;
                        case R.id.nav_little_Events:
                            selectedFragment = new LittleEventFragment();
                            break;
                    }
                    lastFragment = selectedFragment;

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                            selectedFragment).commit();

                    return true;
                }
            };



    /*
     * Method to change the color of the statusbar in blue
     */
    private void setStatusBarColored(Activity activity){
        try{
            Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(activity,R.color.statusbarblue));
        }catch(Exception e){
            e.printStackTrace();
            Log.e("ChangeStatusbarColor", "Error while changing the color of the Statusbar");
        }
    }
}
