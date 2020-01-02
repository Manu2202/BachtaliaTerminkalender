package de.bachtalia.bachtaliaapp.LoginAndMain;

/*
 * Created by Manuel Lanzinger on 17. Dezember 2018.
 * For the project: Bachtalia Terminkalender.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.List;

import de.bachtalia.bachtaliaapp.Contact.ContactsActivity;
import de.bachtalia.bachtaliaapp.Events.ListAndDetails.EventsListActivity;
import de.bachtalia.bachtaliaapp.PackinglistDatabase.PackingList;
import de.bachtalia.bachtaliaapp.R;

public class MainActivity extends AppCompatActivity {

    ImageButton imEvents;
    ImageButton imFacebook;
    ImageButton imWebsite;
    ImageButton imInstagram;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColored(this);

        //Initializing the content on the activity
        imEvents = (ImageButton) findViewById(R.id.imEvents);
        imFacebook = (ImageButton) findViewById(R.id.imFacebook);
        imWebsite = (ImageButton) findViewById(R.id.imWebsite);
        imInstagram = (ImageButton) findViewById(R.id.imInstagram);

        // Inner Class with On Click Listener for the EventButton
        imEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventIntent = new Intent(MainActivity.this, EventsListActivity.class);
                startActivity(eventIntent);
            }
        });

        // Inner Class on Click Listener for the FacebookButton
        imFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebook();
            }
        });

        // inner Class on Click Listener for Instagram Button
        imInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagram();
            }
        });

        // Inner Class with On Click Listener for the WebsiteButton
        imWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bachtalia.de"));
                startActivity(intent);
            }
        });
    }

    /*
     * Method to check, if the Facebook App is installed
     * If yes, open the URL in the App
     * else open in Browser
     */
    private void openFacebook(){
        //Creating the URLs
        final String urlFb = "fb://page/410503759011075";
        final String urlBrowser = "https://facebook.com/pages/410503759011075";

        //Start Facebook in App or Browser
        Intent fbIntent = new Intent(Intent.ACTION_VIEW);

        fbIntent.setData(Uri.parse(urlFb));
        final PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(fbIntent, pm.MATCH_DEFAULT_ONLY);

        //Check if Facebook App is installed, if not set the link of the browser
        if (list.size() == 0)
            fbIntent.setData(Uri.parse(urlBrowser));

        startActivity(fbIntent);
    }

    /*
     * Method to check, if the Instagram App is installed
     * If yes, open the URL in the App
     * else open in Browser
     */
    private void openInstagram(){
        // The Uri's for Instagram, one for the app, one for the browser
        String urlInsta = "https://instagram.com/_u/bachtalia";
        String urlBrowser = "https://instagram.com/bachtalia";

        Intent instaIntent = new Intent(Intent.ACTION_VIEW);
        instaIntent.setData(Uri.parse(urlInsta));
        instaIntent.setPackage("com.instagram.android");

        final PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(instaIntent, pm.MATCH_DEFAULT_ONLY);

        if (list.size() == 0)
            instaIntent.setData(Uri.parse(urlBrowser));

        // start the activity
        startActivity(instaIntent);
    }

    /*
     * To close the app when you push the back Button on the Main Activity
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /*
     * Methods for initializing the menu and select the choosen Item
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemPackingList) {
            Intent packingIntent = new Intent(MainActivity.this, PackingList.class);
            startActivity(packingIntent);
            return true;
        } else if (id == R.id.itemContact) {
            Intent contactIntent = new Intent(MainActivity.this, ContactsActivity.class);
            startActivity(contactIntent);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    /*
     * Method to change the color of the statusbar in blue
     */
    private void setStatusBarColored(Activity activity) {
        try {
            Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.statusbarblue));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ChangeStatusbarColor", "Error while changing the color of the Statusbar");
        }
    }
}
