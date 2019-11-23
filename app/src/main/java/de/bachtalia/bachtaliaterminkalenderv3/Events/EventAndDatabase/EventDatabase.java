package de.bachtalia.bachtaliaterminkalenderv3.Events.EventAndDatabase;
/*
 * Created by Manuel Lanzinger on 17. Dezember 2018.
 * For the project: Bachtalia Terminkalender.
 */

import android.content.Context;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bachtalia.bachtaliaterminkalenderv3.R;

public class EventDatabase {

    private ArrayList<Event> events;

    private final Map<Calendar, Event> EVENTS_MAP = new HashMap<>();

    private Calendar[] EVENTDATE_LIST;

    private void handleAndSortEvents() {
        for (Event e: events) {
            // Check if the Event more than one day in the past, if true skip this one
            // 8640000 = Amount of Milliseconds of a day
            if (e.getDate().getTimeInMillis() +86400000 > new GregorianCalendar().getTimeInMillis())
                EVENTS_MAP.put(e.getDate(), e);
        }
        EVENTDATE_LIST = new Calendar[EVENTS_MAP.size()];

        EVENTS_MAP.keySet().toArray(EVENTDATE_LIST);
        Arrays.sort(EVENTDATE_LIST);
    }

    /*
     * Generate the Events out of the csv Data
     */
    public void generateEvents(Context context, int fileID){
        InputStream inputStream = context.getResources().openRawResource(fileID);
        CSVFile csvFile = new CSVFile(inputStream);
        List<String[]> eventList = csvFile.read();

        events = new ArrayList<>();

        // Reading all events
        for(String[] event : eventList){
            String[] eventString = event[0].split(";");
            int eventTyp = Integer.valueOf(eventString[0]);
            String name = eventString[1];
            String location = eventString[2];
            String street = eventString[3];
            String city = eventString[4];
            int[] dateInt = convertStringArrayInIntegerArray(eventString[5].split("\\."));
            int[] timeInt = convertStringArrayInIntegerArray(eventString[6].split(":"));
            String comment = eventString[7];
            int[] busEntryInt = convertStringArrayInIntegerArray(eventString[8].split(":"));

            // Convert the int Arrays of the Dates in Date Datatypes: dateString, timeString, busEntryString
            GregorianCalendar date = new GregorianCalendar(dateInt[2], dateInt[1]-1,
                    dateInt[0], timeInt[0], timeInt[1]);
            GregorianCalendar busEntry = new GregorianCalendar(dateInt[2], dateInt[1]-1,
                    dateInt[0], busEntryInt[0], busEntryInt[1]);

            // Differentiation between Home- or Roadevent
            if(eventTyp == 0){
                events.add(new HomeEvent(eventTyp, name, location, street, city, date, comment, busEntry));
            }else if(eventTyp == 1){
                events.add(new RoadEvent(eventTyp, name, location, street, city, date, comment, busEntry));
            }

        }
        handleAndSortEvents();
    }

    /*
     * Get back the sorted array with all Dates with Events
     */
    public Calendar[] getEventdateList(){
        return EVENTDATE_LIST;
    }

    /*
     * Get Back an Event on a specific date
     */
    public Event getEvent(Calendar calendar){
        return EVENTS_MAP.get(calendar);
    }

    /*
     * Method to convert a String Array with int numbers in an Integer Array
     */
    private int[] convertStringArrayInIntegerArray(String[] stringArray){
        int[] intArray = new int[stringArray.length];
        for(int i=0;i<stringArray.length;i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }
}
