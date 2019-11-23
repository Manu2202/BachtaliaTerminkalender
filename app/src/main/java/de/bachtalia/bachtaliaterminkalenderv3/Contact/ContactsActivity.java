package de.bachtalia.bachtaliaterminkalenderv3.Contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import de.bachtalia.bachtaliaterminkalenderv3.R;

/*
 * Created by Manuel Lanzinger on 23. Dezember 2018.
 * For the project: BachtaliaTerminkalenderV3.
 */
public class ContactsActivity extends AppCompatActivity {

    private final static Contact[] CONTACTS = new Contact[]{
            new Contact("Michaela Kinzler", "Vorstand Medien", "medien@bachtalia.de",
                    "Zuständig für Organisation und allgemeine Bereiche.", "default"),
            new Contact("Ernst Heiser", "Vorstand Gastronomie", "gastronomie@bachtalia.de",
                    "Zuständig für Gastronomie Fragen und Angelegenheiten.", "default"),
            new Contact("Bernd Danner", "Terminkoordinator großer Hofstaat", "termine@bachtalia.de",
                    "Zuständig für Termine und Terminkoordination großer Hofstaat.", "default"),
            new Contact("Fabian Hanf", "Administrator", "admin@bachtalia.de",
                    "Zuständig für Web Auftritt und allgemein Weborganisation.", "default"),
            new Contact("Manuel Lanzinger", "2.Administrator und App Entwickler", "app@bachtalia.de",
                    "Zuständig für Web Auftritt und Bachtalia Terminkalender App.", "default")

    };

    int clickPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setStatusBarColored(this);

        Thread uiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Linking the Listview into the class
                ListView lvContacts = (ListView)findViewById(R.id.lvContacts);

                // Set the adapter on the List View
                ContactAdapter contactAdapter = new ContactAdapter(CONTACTS);
                lvContacts.setAdapter(contactAdapter);

                // Set the onClickListener on the ListView to get the position
                lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        clickPos = position;
                    }
                });
            }
        });

        uiThread.start();
    }

    /*
     * On Click on the Table Row with the mail, open the mail client which is installed
     * on the phone to send an email to this the person
     */
    public void onClickMail(View view){
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setType("message/rfc822");
        String[] receiver = new String[]{CONTACTS[clickPos].getMail()};
        mailIntent.putExtra(Intent.EXTRA_EMAIL, receiver);

        //Try to start an email activity
        try{
            startActivity(Intent.createChooser(mailIntent, "Sende Mail..."));
        }
        // Catch the Exception if no Client is installed and show an Info to the User
        catch (android.content.ActivityNotFoundException e){
            e.printStackTrace();
            Log.e("SendMailError", "Error while starting an mail activity outside the App");
            Toast.makeText(ContactsActivity.this, "Es ist kein Mail Client installiert!" +
                    " Deshalb kann keine Mail an diesen Kontakt versendet werden!", Toast.LENGTH_LONG);
        }
    }

    /*
     * Method to change the color of the statusbar in blue
     */
    private static void setStatusBarColored(Activity activity){
        try{
            Window window = activity.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(activity,R.color.navy));
        }catch(Exception e){
            e.printStackTrace();
            Log.e("ChangeStatusbarColor", "Error while changing the color of the Statusbar");
        }
    }
}
