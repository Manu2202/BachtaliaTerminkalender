package de.bachtalia.bachtaliaterminkalenderv3.LoginAndMain;

/*
 * Created by Manuel Lanzinger on 17. Dezember 2018.
 * For the project: Bachtalia Terminkalender.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.bachtalia.bachtaliaterminkalenderv3.R;

public class LoginActivity extends AppCompatActivity {

    EditText etPassword;
    private static final String PASSWORD = "0b44b8b99138a70606e2280cfb894fbf";
    String inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Call the Method to color the status bar
        setStatusBarColored(this);

        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    /*
     * If the app gets closed:
     * Safe the actual tipped in Password
     * ->You only have to tip in the correct Password once
     */
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        inputPassword = etPassword.getText().toString();

        editor.putString("Password", inputPassword);

        editor.apply();
    }

    /*
     *If the app gets restarted:
     * Restore the last tipped in Password
     * If you have tipped in the correct Password you don't have to do it again
     */
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        inputPassword = prefs.getString("Password", "");

        etPassword.setText(inputPassword);

        //If the App gets restarted, check the last tipped in Password to skip Login Activity
        onBtnLoginClicked(findViewById(android.R.id.content));
    }

    public void onBtnLoginClicked(View view) {
        inputPassword = etPassword.getText().toString();

        try{
            if (PASSWORD.equals(passwordToHash(inputPassword))){
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }else{
                Toast.makeText(getApplicationContext(), "Falsches Passwort!!!",
                        Toast.LENGTH_LONG).show();
            }
        }catch(Exception npe){
            Log.e("BachtaliaTerminkalender", "No Password was filled in the password field");
            npe.printStackTrace();
            Toast.makeText(getApplicationContext(), "Bitte gib ein Passwort ein!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String passwordToHash(String password){
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal formatDate;
            //Convert it to hexadecimal formatDate
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex formatDate
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /*
     * Method to change the color of the statusbar in blue
     */
    private void setStatusBarColored(Activity activity){
        try{
            Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(activity,R.color.navy));
        }catch(Exception e){
            e.printStackTrace();
            Log.e("ChangeStatusbarColor", "Error while changing the color of the Statusbar");
        }
    }
}