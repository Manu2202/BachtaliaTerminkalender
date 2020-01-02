package de.bachtalia.bachtaliaapp.Events.ListAndDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Calendar;

import de.bachtalia.bachtaliaapp.Events.EventAndDatabase.Event;
import de.bachtalia.bachtaliaapp.Events.EventAndDatabase.EventDatabase;
import de.bachtalia.bachtaliaapp.R;

public class LittleEventFragment extends ListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventDatabase eventDbLittle = new EventDatabase();
        eventDbLittle.generateEvents(getActivity().getApplicationContext(), R.raw.auftrittsplankh);

        ArrayList<Event> eventListLittle = new ArrayList<>();
        Calendar[] eventDatesLittle = eventDbLittle.getEventdateList();
        for (int i = 0; i < eventDatesLittle.length; i++) {
            eventListLittle.add(eventDbLittle.getEvent(eventDatesLittle[i]));
        }

        EventAdapter eventAdapterLittle = new EventAdapter(eventListLittle);

        setListAdapter(eventAdapterLittle);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set on Click Listener on the ListView
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsIntent = new Intent(getActivity(), EventDetailsActivity.class);
                detailsIntent.putExtra("position", position);
                detailsIntent.putExtra("fileID", R.raw.auftrittsplankh);
                startActivity(detailsIntent);
            }
        });


    }
}
