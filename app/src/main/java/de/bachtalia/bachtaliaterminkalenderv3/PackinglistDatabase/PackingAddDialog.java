package de.bachtalia.bachtaliaterminkalenderv3.PackinglistDatabase;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import de.bachtalia.bachtaliaterminkalenderv3.R;


/*
 * Created by Manuel Lanzinger on 19. Dezember 2018.
 * For the project: BachtaliaTerminkalenderV3.
 */

public class PackingAddDialog extends Dialog {


    public EditText etObject;
    public Button btnAdd;
    public Button btnCancel;

    public PackingAddDialog(Activity activity){
        super(activity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_packing_add);

        etObject = findViewById(R.id.etObject);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
    }
}
