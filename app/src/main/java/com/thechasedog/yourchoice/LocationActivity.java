package com.thechasedog.yourchoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class LocationActivity extends Activity {
    LinearLayout choicesLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);

        choicesLayout = (LinearLayout)findViewById(R.id.choicesLayout);

        for (Location loc : PersonalityActivity.game.locations) {
            Button button = getButton(loc.name);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        PersonalityActivity.game.currentLocation = getLocation(PersonalityActivity.game.locations, ((Button) v).getText().toString());
                        startActivity(new Intent(LocationActivity.this, DialogueActivity.class));
                    }
                    catch (LocationNotFound ex) {
                        Log.e("LocationActivity", "location not found");
                    }
                }
            });
            addButton(button);
        }


    }

    private void addButton(Button button) {

        choicesLayout.addView(button);
    }

    public Button getButton(String text) {
        Button button = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 0.03f;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        button.setLayoutParams(params);
        button.setText(text);
        return button;
    }

    private class LocationNotFound extends Exception {

    }

    private Location getLocation(List<Location> locations, String name) throws LocationNotFound {
        int index = 0;
        for (Location loc : locations) {
            if (name.equals(loc.name)) {
                return locations.get(index);
            }
            index++;
        }
        throw new LocationNotFound();
    }
}
