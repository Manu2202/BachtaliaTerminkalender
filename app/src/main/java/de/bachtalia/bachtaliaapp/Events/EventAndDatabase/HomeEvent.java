package de.bachtalia.bachtaliaapp.Events.EventAndDatabase;

/*
 * Created by Manuel Lanzinger on 17. Dezember 2018.
 * For the project: Bachtalia Terminkalender.
 */

import java.util.Calendar;

public class HomeEvent extends Event {

    /*
     * Define specific variables for HomeEvent
     */
    Calendar entryTime;

    /*
     * First Constructor if a Image for the HomeEvent is available
     */
    public HomeEvent(int eventType, String name, String location, String street, String city, Calendar date,
                     String comment, String imageName, Calendar entryTime) {
        super(eventType, name, location, street, city, date, comment, imageName);
        this.entryTime = entryTime;
    }

    /*
     * Second Constructor if no Image for the HomeEvent is available
     */
    public HomeEvent(int eventType, String name, String location, String street, String city, Calendar date,
                     String comment, Calendar entryTime) {
        super(eventType, name, location, street, city, date, comment);
        this.entryTime = entryTime;
    }

    /*
     * Special Getter only for Home Events
     */
    public Calendar getEntryTime() {
        return entryTime;
    }
}
