package pt.hidrogine.infinityedge.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import pt.hidrogine.infinityedge.R;


public class MainMenu extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);


        final Button dummyButton = (Button) findViewById(R.id.dummy_button);
        dummyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this,Game.class));
            }
        });


        ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(ACTIVITY_SERVICE);
        Log.i("Test", "Large Memory Limit " + activityManager.getLargeMemoryClass() + " MB");
        Log.i("Test", " Memory Limit  " + activityManager.getMemoryClass() + " MB");

   }



}
