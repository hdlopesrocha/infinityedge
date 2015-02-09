package pt.ist.bombraider.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import pt.ist.bombraider.R;


public class Levels extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.levels);

        // Levels
        {
            LinearLayout table = (LinearLayout) findViewById(R.id.singleplayer_levels);
            Button btn = new Button(this);
            {
                btn.setText("play");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Levels.this, Game.class);
                        Levels.this.startActivity(intent);
                    }
                });
            }


            table.addView(btn);
        }

    }


}

