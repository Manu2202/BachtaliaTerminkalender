package de.bachtalia.bachtaliaapp.Events.ListAndDetails;

/*
 * Created by Manuel Lanzinger on 17. Dezember 2018.
 * For the project: Bachtalia Terminkalender.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.bachtalia.bachtaliaapp.Events.EventAndDatabase.Event;
import de.bachtalia.bachtaliaapp.R;

public class EventAdapter extends BaseAdapter {

    private List<Event> data;

    /*
     * Static final list of days of the week
     */
    private static final String[] DAYOFTHEWEEK = new String[]{
            "So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"
    };

    public EventAdapter(List<Event> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        Event event = data.get(position);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_list_view, null, false);
        }
        //Linking the Elements in the view with the variables in the class
        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        TextView tvTime = (TextView)convertView.findViewById(R.id.tvTime);
        TextView tvLocation = (TextView)convertView.findViewById(R.id.tvLocation);

        //To get the specific Image ID or for the default Image
        String stringImage = "pic_"+ event.getImageName();
        stringImage = stringImage.toLowerCase();
        int imageId = context.getResources().getIdentifier(stringImage, "drawable", context.getPackageName());

        //Setting the textviews and the imageview
        tvDate.setText(formatDate(event.getDate()));
        tvName.setText(event.getName());

        //More Info if phone is in Landscape Mode
        if (tvTime != null && tvLocation != null) {
            if(event.getDate().get(Calendar.HOUR_OF_DAY) == 2 && event.getDate().get(Calendar.MINUTE) == 59)
                tvTime.setText("Keine Zeit");
            else
                tvTime.setText(formatTime(event.getDate()));
            tvLocation.setText(event.getLocation() + ": " + event.getStreet() + " " + event.getCity());
        }
        return convertView;
    }

    /*
     * Convert the Calendar Date in a String to set the TextView for the Date
     */
    private static String formatDate(Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        sdf.setCalendar(date);
        String dateFormatted = sdf.format(date.getTime());

        // Getting and Setting the Dy of the week
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        dateFormatted = DAYOFTHEWEEK[dayOfWeek-1] + ": " + dateFormatted;

        return dateFormatted;
    }

    /*
     * Convert the time out of the calendar into a string
     */
    private static String formatTime(Calendar date){
        // Get the time out of the Calendar into a string
        int minute = date.get(Calendar.MINUTE);
        String sMinute;
        if (minute < 10)
            sMinute = "0" + minute;
        else
            sMinute = "" + minute;
        return date.get(Calendar.HOUR_OF_DAY) + ":" + sMinute + " Uhr";
    }
}
