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
import java.util.Random;

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

        for (Location loc : PersonalityActivity.game.availableLocations) {
            Button button = getButton(loc.name);
            button.setOnClickListener(clickListener);
            addButton(button);
        }

        final Button button = getButton("Wander");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                List<Location> locs = PersonalityActivity.game.locations;
                int count = 0;
                while(count < 100) {
                    int index = random.nextInt(locs.size());
                    if (!locs.get(index).isSub && !PersonalityActivity.game.availableLocations.contains(locs.get(index))) {
                        PersonalityActivity.game.availableLocations.add(locs.get(index));
                        Button btn = getButton(locs.get(index).name);
                        btn.setOnClickListener(clickListener);
                        addButton(btn);
                        button.setVisibility(View.GONE);
                        break;
                    }
                    count++;
                }
            }
        });
        addButton(button);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
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
    };



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

    public static class LocationNotFound extends Exception {

    }

    public static Location getLocation(List<Location> locations, String name) throws LocationNotFound {
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
