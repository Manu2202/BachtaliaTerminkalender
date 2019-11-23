package de.bachtalia.bachtaliaterminkalenderv3.Events.ListAndDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.bachtalia.bachtaliaterminkalenderv3.Events.EventAndDatabase.Event;
import de.bachtalia.bachtaliaterminkalenderv3.Events.EventAndDatabase.EventDatabase;
import de.bachtalia.bachtaliaterminkalenderv3.R;

public class AllEventFragment extends ListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventDatabase eventDbAll = new EventDatabase();
        eventDbAll.generateEvents(getActivity().getApplicationContext(), R.raw.auftrittsplanall);

        ArrayList<Event> eventListAll = new ArrayList<>();
        Calendar[] eventDatesAll = eventDbAll.getEventdateList();
        for (int i = 0; i < eventDatesAll.length; i++) {
            eventListAll.add(eventDbAll.getEvent(eventDatesAll[i]));
        }

        EventAdapter eventAdapterAll = new EventAdapter(eventListAll);

        setListAdapter(eventAdapterAll);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("Fragment", "In All Event");



        // Set on Click Listener on the ListView
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsIntent = new Intent(getActivity(), EventDetailsActivity.class);
                detailsIntent.putExtra("position", position);
                startActivity(detailsIntent);
            }
        });


    }
}