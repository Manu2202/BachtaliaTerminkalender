package de.bachtalia.bachtaliaapp.PackinglistDatabase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import de.bachtalia.bachtaliaapp.R;

public class PackingList extends AppCompatActivity {

    private DBHelper dbHelper;
    private PackingAdapter pa;
    private HashMap<String, Boolean> packingList;
    private ArrayList<String> keySetList;

    private PackingAddDialog dialog;

    private ListView lvPacking;
    private Button btnDeleteAll;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list);
        setStatusBarColored(this);

        dbHelper = new DBHelper(this);
        packingList = readDB();
        pa = new PackingAdapter(packingList);

        keySetList = new ArrayList<>();
        keySetList.addAll(packingList.keySet());

        Thread uiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                lvPacking = findViewById(R.id.lvPacking);
                btnAdd = findViewById(R.id.btnAdd);
                btnDeleteAll = findViewById(R.id.btnDeleteAll);

                lvPacking.setAdapter(pa);

                // OnClickListener for the ListView to update the checkBox
                lvPacking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        updateDB(position);
                    }
                });

                // OnClickListener for the Button to add a Item to start the Dialog
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new PackingAddDialog(PackingList.this);
                        dialog.show();
                        // Only set the OnClickListener, if the dialog is shown
                        // OnClickListener for the Button Add in the Dialog
                        dialog.btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String newPackingItem = dialog.etObject.getText().toString();
                                addDB(newPackingItem);
                                dialog.dismiss();
                                refreshActivity();
                            }
                        });

                        // OnClickListener for the Button Cancel in the Dialog
                        dialog.btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Set the text empty and close the dialog
                                dialog.etObject.setText("");
                                dialog.dismiss();
                            }
                        });
                    }
                });

                // OnClickListener for the Button DeleteAll to delete all entries in the Database
                btnDeleteAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try {
                            db.execSQL("DELETE FROM " + dbHelper.TABLE_PACKINGLIST);
                            db.close();

                            Log.d("myTag", "try Block von item Delete");
                            //Refresh the Activity
                            refreshActivity();

                        } catch (Exception e) {
                            Log.d("DeleteEntriesDB", "Error while deleting all entries in the database");
                            e.printStackTrace();
                        }
                    }
                });


                }

        });

        // Start the Thread
        uiThread.start();
    }

    /*
     * Add a new entry in the Database
     */
    private void addDB(String object) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_ITEM, object);
            values.put(dbHelper.COL_CHECKED, 0);

            db.insertOrThrow(dbHelper.TABLE_PACKINGLIST, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("Add DB", "Error while Add a new object in the Database");
            e.printStackTrace();
        }

        db.endTransaction();

        packingList.put(object, false);
        pa = new PackingAdapter(packingList);
        keySetList.clear();
        keySetList.addAll(packingList.keySet());
    }

    /*
     * Method to update the Database
     * Change the CheckBox if clicked
     */
    private void updateDB(int position) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        int isChecked = 0;      //0=false, 1=true
        if (packingList.get(packingList.get(keySetList.get(position))))
            isChecked = 1;

        try {
            ContentValues values = new ContentValues();
            values.put(dbHelper.COL_CHECKED, isChecked);

            db.update(dbHelper.TABLE_PACKINGLIST, values,
                    dbHelper.COL_ITEM + " = '" + keySetList.get(position)
                            + "'", null);
        } catch (Exception e) {
            Log.d("Update DB", "Error while Updating the Database");
            e.printStackTrace();
        }

        db.endTransaction();
    }

    /*
     * Method to read out the Data in the Database
     */
    private HashMap<String, Boolean> readDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        HashMap<String, Boolean> hmDB = new HashMap<>();
        db.beginTransaction();
        Cursor cursor = db.query(dbHelper.TABLE_PACKINGLIST,
                new String[]{dbHelper.COL_ITEM, dbHelper.COL_CHECKED},
                null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int i = cursor.getInt(cursor.getColumnIndex(dbHelper.COL_CHECKED));
                    hmDB.put(cursor.getString(cursor.getColumnIndex(dbHelper.COL_ITEM)), i == 1);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("Read DB", "Error while reading the Database");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        db.endTransaction();

        return hmDB;
    }

    /*
     * Refresh the activity
     * But only if the dialog is not shown
     */
    private void refreshActivity(){
        Log.d("myTag", "refreshActivity wurde aufgerufen");
        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
        overridePendingTransition(0,0);

    }

    /*
     * Method to change the color of the statusbar in blue
     */
    public void setStatusBarColored(Activity activity){
        try{
            Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(activity,R.color.statusbarblue));
        }catch(Exception e){
            e.printStackTrace();
            Log.e("ChangeStatusbarColor", "Error while changing the color of the Statusbar");
        }
    }

    /*
     * Inner Class for the Special Adapter of the List View with items in the packing list
     */
    class PackingAdapter extends BaseAdapter {
        private HashMap<String, Boolean> packingObjects;
        private ArrayList<String> keySetList;
        private TextView tvObject;
        private CheckBox checkbox;

        public PackingAdapter(HashMap<String, Boolean> packingObjects) {
            this.packingObjects = packingObjects;
            keySetList = new ArrayList<>();
            keySetList.addAll(packingObjects.keySet());
            }

        @Override
        public int getCount() {
            return packingObjects.keySet().size();
        }

        @Override
        public Object getItem(int position) {
            return keySetList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Context context = parent.getContext();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.packinglist_list_view, null, false);
            }

            //Linking the Objects in the layout
            tvObject = convertView.findViewById(R.id.tvObject);
            checkbox = convertView.findViewById(R.id.checkBox);

            if(position < keySetList.size()) {
                tvObject.setText(keySetList.get(position));
                checkbox.setChecked(packingObjects.get(keySetList.get(position)));
            }

            checkbox.setOnClickListener(new ownOnClickListener(convertView, position));
            return convertView;
        }

        /*
         * Own class as a onclicklistener
         */
        private class ownOnClickListener implements View.OnClickListener {
            View view;
            int position;

            public ownOnClickListener(View view, int position) {
                this.position = position;
                this.view = view;
            }

            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                String object = keySetList.get(position);
                packingObjects.put(object, cb.isChecked());
                view.callOnClick();
            }
        }
    }
}
