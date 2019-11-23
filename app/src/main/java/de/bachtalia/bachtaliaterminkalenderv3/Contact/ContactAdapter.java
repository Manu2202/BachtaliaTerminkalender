package de.bachtalia.bachtaliaterminkalenderv3.Contact;

/*
 * Created by Manuel Lanzinger on 23. Dezember 2018.
 * For the project: BachtaliaTerminkalenderV3.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.bachtalia.bachtaliaterminkalenderv3.R;

public class ContactAdapter extends BaseAdapter {

    private Contact[] contacts;

    public ContactAdapter(Contact[] contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.length;
    }

    @Override
    public Object getItem(int position) {
        return contacts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        Contact contact = contacts[position];

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_list_view, null, false);
        }

        //Linking the Elements on the ListView
        TextView tvContactName = (TextView)convertView.findViewById(R.id.tvContactName);
        TextView tvContactPosition = (TextView)convertView.findViewById(R.id.tvContactPosition);
        TextView tvContactMail = (TextView)convertView.findViewById(R.id.tvContactMail);
        TextView tvContactInfo = (TextView)convertView.findViewById(R.id.tvContactInfo);
        ImageView ivContactPic = (ImageView)convertView.findViewById(R.id.ivContactPic);

        //To get the specific Image ID or for the default Image
        String stringImage = "contact_pic_"+ contact.getImage();
        stringImage = stringImage.toLowerCase();
        int imageId = context.getResources().getIdentifier(stringImage, "drawable", context.getPackageName());

        // Setting the Infos into the View
        tvContactName.setText(contact.getName());
        tvContactPosition.setText(contact.getPosition());
        tvContactMail.setText(contact.getMail());
        tvContactInfo.setText(contact.getInfo());
        ivContactPic.setImageDrawable(context.getDrawable(imageId));

        return convertView;
    }
}
