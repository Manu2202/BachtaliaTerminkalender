package de.bachtalia.bachtaliaapp.Events.EventAndDatabase;

/*
 * Created by Manuel Lanzinger on 17. Dezember 2018.
 * For the project: Bachtalia Terminkalender.
 */

import android.support.annotation.Nullable;

import java.util.Calendar;

public class RoadEvent extends Event {

    /*
     * Define the specific variables for the RoadEvent
     */
    private Calendar busDepature;     //Time when the bus is departing

    /*
     * First Constructor if a picture of the RoadEvent is available
     */

    public RoadEvent(int eventType, String name, String location, String street, String city, Calendar date,
                     String comment, String imageName, @Nullable Calendar busDepature) {
        super(eventType, name, location, street, city, date, comment, imageName);
        this.busDepature = busDepature;
    }

    /*
     * Second Constructor if no picture of the RoadEvent is available
     */
    public RoadEvent(int eventType, String name, String location, String street, String city, Calendar date,
                     String comment, @Nullable Calendar busDepature) {
        super(eventType, name, location, street, city, date, comment);
        this.busDepature = busDepature;
    }

    /*
     * Special Getter only for RoadEvents
     */
    public Calendar getBusDepature(){return busDepature;}

}


