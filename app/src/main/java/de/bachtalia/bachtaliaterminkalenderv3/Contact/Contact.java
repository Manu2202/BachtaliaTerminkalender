package de.bachtalia.bachtaliaterminkalenderv3.Contact;

/*
 * Created by Manuel Lanzinger on 27. Dezember 2018.
 * For the project: Bachtalia Terminkalender.
 */

public class Contact {

    private String name;
    private String position;
    private String mail;
    private String info;
    private String Image;

    /*
     * Some Infos over a person, which should be in the contact Activity
     */
    public Contact(String name, String position, String mail, String info, String image) {
        this.name = name;
        this.position = position;
        this.mail = mail;
        this.info = info;
        Image = image;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getMail() {
        return mail;
    }

    public String getInfo() {
        return info;
    }

    public String getImage() {
        return Image;
    }
}
