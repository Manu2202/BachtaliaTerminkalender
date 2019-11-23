package de.bachtalia.bachtaliaterminkalenderv3.Events.ListAndDetails;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.bachtalia.bachtaliaterminkalenderv3.Events.EventAndDatabase.Event;
import de.bachtalia.bachtaliaterminkalenderv3.Events.EventAndDatabase.EventDatabase;
import de.bachtalia.bachtaliaterminkalenderv3.Events.EventAndDatabase.HomeEvent;
import de.bachtalia.bachtaliaterminkalenderv3.Events.EventAndDatabase.RoadEvent;
import de.bachtalia.bachtaliaterminkalenderv3.R;

/*
 * Created by Manuel Lanzinger on 21. Dezember 2018.
 * For the project: BachtaliaTerminkalenderV3.
 */

public class EventDetailsActivity extends AppCompatActivity {

    /*
     * Initializing the Textviews and the Imageview in the Activity
     */
    private TextView tvEventName;
    private TextView tvCalendarDate;
    private TextView tvTime;
    private TextView tvEntryBus;
    private TextView tvEntryBusTime;
    private TextView tvLocationName;
    private TextView tvStreet;
    private TextView tvCity;
    private TextView tvComment;
    private ImageView ivEventImage;

    /*
     * Initializing the other class variables
     */
    private EventDatabase eDB;
    private Event event;
    private Calendar eventDate;
    private ShareActionProvider shareActionProvider;

    /*
     * Static final list of days of the week
     */
    private static final String[] DAYOFTHEWEEK = new String[]{
            "So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        setStatusBarColored(this);

        //Get the intent
        Intent intent = getIntent();

        //Get the event
        int position = (int) intent.getSerializableExtra("position");
        int fileID = (int) intent.getSerializableExtra("fileID");
        eDB = new EventDatabase();

        // Generate the events
        eDB.generateEvents(this, fileID);

        eventDate = eDB.getEventdateList()[position];
        event = eDB.getEvent(eventDate);

        // Create a new Thread for the UI

        //Linking the variables with the views in the activity
        tvEventName = findViewById(R.id.tvEventName);
        tvCalendarDate = findViewById(R.id.tvCalendarDate);
        tvTime = findViewById(R.id.tvTime);
        tvEntryBus = findViewById(R.id.tvEntryBus);
        tvEntryBusTime = findViewById(R.id.tvEntryBusTime);
        tvLocationName = findViewById(R.id.tvLocationName);
        tvStreet = findViewById(R.id.tvStreet);
        tvCity = findViewById(R.id.tvCity);
        tvComment = findViewById(R.id.tvComment);
        ivEventImage = findViewById(R.id.ivEventImage);

        // Set the textviews belong to the type of the event
        if (getEventType(eDB.getEvent(eventDate)))
            setRoadEventSpecial((RoadEvent) event);
        else
            setHomeEventSpecial((HomeEvent) event);

        // Setting the Infos out of the event into the activity
        setInfos();
    }

    /*
     * Method to get the SpecialType of the Event
     */
    private boolean getEventType(Event event) {
        if (event.getEventType() == 1)
            return true;                //= RoadEvent
        else
            return false;               //= HomeEvent
    }

    /*
     * Two Methods to setting the textviews according to the type of event
     */
    private void setRoadEventSpecial(RoadEvent event) {
        tvEntryBus.setText("Bus Abfahrt: ");
        if (event.getBusDepature() != null)
            tvEntryBusTime.setText(formatTime(event.getBusDepature()));
        else
            tvEntryBusTime.setText("Abfahrt Bus ist noch nicht bekannt.");
    }

    private void setHomeEventSpecial(HomeEvent event) {
        tvEntryBus.setText("Einlass: ");
        tvEntryBusTime.setText(formatTime(event.getEntryTime()));
    }

    /*
     * Method for filling in the data out of the event into the textviews
     */
    private void setInfos() {
        tvEventName.setText(event.getName());
        tvCalendarDate.setText(formatDate(event.getDate()));
        tvTime.setText(formatTime(event.getDate()));
        tvLocationName.setText(event.getLocation());
        tvStreet.setText(event.getStreet());
        tvCity.setText(event.getCity());
        tvComment.setText(event.getComment());

        // Generating image ID to get the image out of the "drawable" folder
        String stringImage = event.getImageName() + "_image";
        stringImage = stringImage.toLowerCase();

        int imageID = this.getResources().getIdentifier(stringImage, "drawable", this.getPackageName());
        // Setting the image
        ivEventImage.setImageDrawable(this.getDrawable(imageID));
    }

    /*
     * Convert the Calendar Date in a String to set the TextView for the Date
     */
    private static String formatDate(Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        sdf.setCalendar(date);
        String dateFormatted = sdf.format(date.getTime());

        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

        dateFormatted = DAYOFTHEWEEK[dayOfWeek - 1] + ": " + dateFormatted;
        return dateFormatted;
    }

    /*
     * Convert the time out of the calendar into a string
     */
    private static String formatTime(Calendar date) {
        // Get the time out of the Calendar into a string
        int minute = date.get(Calendar.MINUTE);
        String sMinute;
        if (minute < 10)
            sMinute = "0" + minute;
        else
            sMinute = "" + minute;
        return date.get(Calendar.HOUR_OF_DAY) + ":" + sMinute + " Uhr";
    }

    /*
     * On Click of one of the Location Statements in the details:
     * Get the adress and open the location on Google Maps
     */
    public void onClickLocation(View view) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0´?q=" + event.getStreet() + " " + event.getCity()));
        startActivity(mapIntent);
    }

    /*
     * On Click on the Time or on the Date to save the Event as an Calendar Entry
     *
     * A default length for all events is used: 6 hours
     */
    public void onClickDate(View view) {
        Intent calendarIntent;
        if (Build.VERSION.SDK_INT >= 14) {
            calendarIntent = new Intent(Intent.ACTION_INSERT)
                    .setData(Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getDate().getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                            event.getDate().getTimeInMillis() + 1000 * 60 * 60 * 6)
                    .putExtra(Events.TITLE, event.getName())
                    .putExtra(Events.DESCRIPTION, event.getComment())
                    .putExtra(Events.EVENT_LOCATION, event.getStreet() + " " + event.getCity())
                    .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
        } else {
            calendarIntent = new Intent(Intent.ACTION_EDIT);
            calendarIntent.setType("vnd.android.cursor.item/event");
            calendarIntent.putExtra("beginTime", event.getDate().getTimeInMillis());
            calendarIntent.putExtra("endTime",
                    event.getDate().getTimeInMillis() + 1000 * 60 * 60 * 6);
            calendarIntent.putExtra("title", event.getName());
        }
        startActivity(calendarIntent);
    }

    /*
     * Initializing and setting the Share Button at the top of the activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_details_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        //Set the Text which should be shared with others
        String shareText = "Die Bachtalia tritt auf beim " + event.getName() + ". Der Auftritt ist am "
                + formatDate(event.getDate()) + " um " + formatTime(event.getDate()) + " in der "
                + event.getStreet() + " " + event.getCity() + ". Wir sehen uns dort ;)";
        setShareText(shareText);
        return true;
    }

    /*
     * Set the text which should be shared
     */
    private void setShareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (text != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        shareActionProvider.setShareIntent(shareIntent);
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
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.navy));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ChangeStatusbarColor", "Error while changing the color of the Statusbar");
        }
    }
}
