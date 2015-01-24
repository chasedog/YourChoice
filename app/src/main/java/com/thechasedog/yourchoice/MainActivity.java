package com.thechasedog.yourchoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class MainActivity extends Activity {
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;

    private Button startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);
        startButton = (Button)findViewById(R.id.btn_start);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(PLAYER1);
            }
        });
    }

    private void startGame(int player) {
        Intent intent = new Intent(this,PersonalityActivity.class);
        startActivity(intent);
    }
}
