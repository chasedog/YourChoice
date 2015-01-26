package com.thechasedog.yourchoice;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class DialogueActivity extends Activity implements View.OnClickListener {
    private TextView speakerText;
    private TextView dialogueText;
    private TextView timeText;
    private LinearLayout choicesLayout;
    private Dialogue currentDialogue;
    private DialogueManager dialogueManager;
    private TextView locationText;
    private List<Option> options;
    private static int numClicksPerDay = 0;
    private ImageView dialogueImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogue);
        dialogueImage = (ImageView)findViewById(R.id.dialoguePic);
        dialogueManager = new DialogueManager(PersonalityActivity.game.player, dialogueImage);
        locationText = (TextView)findViewById(R.id.locationText);
        speakerText = (TextView)findViewById(R.id.speakerText);
        dialogueText = (TextView)findViewById(R.id.dialogueText);
        choicesLayout = (LinearLayout)findViewById(R.id.choicesLayout);
        timeText = (TextView)findViewById(R.id.timeText);
        dialogueManager.addPermReq(PersonalityActivity.game.currentLocation.reqText);
        updateScreen(false);
    }

    public Button getButton(String text) {
        Button button = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 0.03f;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        button.setLayoutParams(params);
        button.setText(text);
        button.setOnClickListener(this);
        return button;
    }

    public void updateScreen(boolean hasImage) {
        currentDialogue = dialogueManager.getNextDialogue(hasImage);

        setSpeakerText(currentDialogue.title);
        setDialogueText(currentDialogue.text);
        if (options != null) {
            options.clear();
        }
        options = dialogueManager.getOptions();
        choicesLayout.removeAllViewsInLayout();
        for (Option option : options) {
            choicesLayout.addView(getButton(option.text));
        }
        setLocationText(PersonalityActivity.game.currentLocation);

        if (numClicksPerDay > 0 && numClicksPerDay % 5 == 0) {
            if (PersonalityActivity.game.time == Game.TimeOfDay.NIGHT) {
                numClicksPerDay = 0;
                goToBed();
            }
            else {
                PersonalityActivity.game.time = Game.TimeOfDay.values()[(PersonalityActivity.game.time.ordinal() + 1) % 4];
            }
        }
        setTimeText(convertTimeToString(PersonalityActivity.game.time));
    }

    public static String convertTimeToString(Game.TimeOfDay time) {
        switch (time) {
            case MORNING :
                return "Morning";
            case NOON :
                return "Noon";
            case AFTERNOON:
                return "Afternoon";
            case NIGHT:
                return "Night";
        }

        return "Day";
    }

    public void onClick (View view) {
        boolean hasImage = false;
        numClicksPerDay++;
        String text = ((Button)view).getText().toString();
        Option curOption = options.get(0);
        for (Option option : options) {
            if (option.text.equals(text)) {
                curOption = option;
                break;
            }
        }

        if (curOption.text.startsWith("Talk to")) {
            String person = curOption.text.substring(("Talk to ").length());
            if (person.equals("Debra")) {
                dialogueImage.setImageResource(Images.getDebra(Images.Type.Neutral));
            }
            else if (person.equals("Esmerelda")) {
                dialogueImage.setImageResource(Images.getEsmerelda(Images.Type.Neutral));
            }
        }

        for (String modifier : curOption.modifiers) {
            if (modifier.startsWith("Avail")) {
                String loc = modifier.split(" ", 2)[1];
                try {
                    if (loc.equals("AllLocs")) {
                        for (Location l : PersonalityActivity.game.locations) {
                            if (!l.isSub && !PersonalityActivity.game.availableLocations.contains(l)) {
                                PersonalityActivity.game.availableLocations.add(l);
                            }
                        }
                    }
                    else {
                        Location location = LocationActivity.getLocation(PersonalityActivity.game.locations, loc);

                        PersonalityActivity.game.availableLocations.add(location);
                    }

                }
                catch (Exception ex) {
                    Log.e("DialogueActivity", loc + " not found");
                }
            }
        }

        hasImage = dialogueManager.selectOption(curOption);
        if (curOption.modifiers.contains("LocMap")) {
            dialogueManager.removeLocation(PersonalityActivity.game.currentLocation);
            Intent intent = new Intent(DialogueActivity.this, LocationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        else {
            updateScreen(hasImage);
        }
    }

    public void goToBed() {
        currentDialogue = dialogueManager.getBedDialogue();
        setSpeakerText("");
        setDialogueText("It's late. Time to go to bed.");
        if (options != null) {
            options.clear();
        }
        options = new ArrayList<Option>();
        options.add(dialogueManager.getBedOption());
        choicesLayout.removeAllViewsInLayout();
        for (Option option : options) {
            choicesLayout.addView(getButton(option.text));
        }
        setLocationText(PersonalityActivity.game.currentLocation);
    }

    public void setSpeakerText(String text) {
        speakerText.setText(text);
    }

    public void setTimeText(String text) {
        timeText.setText(text);
    }

    public void setSpeakerText(Person person) {
        setSpeakerText(person.firstName);
    }

    public void setDialogueText(String text) {
        dialogueText.setText(text);
    }

    public void setLocationText(Location loc) {locationText.setText(loc.name);}
}
