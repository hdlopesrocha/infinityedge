package pt.hidrogine.infinityedge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainMenu extends Activity {

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

   }



}
