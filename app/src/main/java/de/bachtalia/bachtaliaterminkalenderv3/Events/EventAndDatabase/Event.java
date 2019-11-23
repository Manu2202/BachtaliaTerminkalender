package de.bachtalia.bachtaliaterminkalenderv3.Events.EventAndDatabase;

/*
 * Created by Manuel Lanzinger on 17. Dezember 2018.
 * For the project: Bachtalia Terminkalender.
 */

import java.util.Calendar;

public abstract class Event {

    /*
     * Define the variables of the superclass
     */
    private int eventType;          //Is the Event a Home or a Road Event (0 = Home, 1 = Road)
    private String name;
    private String location;        //Name of the location
    private String street;          //The adress (street) of the event
    private String city;            //The City of the RoadEvent

    private Calendar date;          //The Date of the event and the time of the apperance

    private String comment;         //for special comments for the event and which groups

    private String imageName;       //Image or Picture of the event if available

    /*
     * First Constructor if a Image is available
     */
    public Event(int eventType, String name, String location, String street, String city, Calendar date, String comment, String imageName) {
        this.eventType = eventType;
        this.name = name;
        this.location = location;
        this.street = street;
        this.city = city;
        this.date = date;
        this.comment = comment;
        this.imageName = imageName;
    }

    /*
     * Second Constructor if no Image is available
     */
    public Event(int eventType, String name, String location, String street, String city, Calendar date, String comment) {
        this.eventType = eventType;
        this.name = name;
        this.location = location;
        this.street = street;
        this.city = city;
        this.date = date;
        this.comment = comment;
        imageName = "default";
    }

    public int getEventType() {
        return eventType;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public Calendar getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public String getImageName() {
        return imageName;
    }
}
