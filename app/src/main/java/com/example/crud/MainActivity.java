package com.example.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the XML layout to be used for this activity
        setContentView(R.layout.activity_sign_in); // Make sure the XML file is named activity_main.xml or adjust the name accordingly.
    }

    // This method will be triggered when the button is clicked
    public void goToNextActivity(View view) {
        // Create an Intent to navigate to another activity (e.g., BlogActivity)
        Intent intent = new Intent(String.valueOf(MainActivity.this));
        startActivity(intent);
    }
}
