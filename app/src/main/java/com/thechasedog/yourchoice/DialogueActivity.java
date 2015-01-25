package com.thechasedog.yourchoice;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class DialogueActivity extends Activity {
    private TextView speakerText;
    private TextView dialogueText;
    private LinearLayout choicesLayout;
    private Dialogue currentDialogue;
    private DialogueManager dialogueManager;
    private TextView locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogue);
        dialogueManager = new DialogueManager(PersonalityActivity.game.player);
        currentDialogue = dialogueManager.getNextDialogue();

        locationText = (TextView)findViewById(R.id.locationText);
        speakerText = (TextView)findViewById(R.id.speakerText);
        dialogueText = (TextView)findViewById(R.id.dialogueText);
        choicesLayout = (LinearLayout)findViewById(R.id.choicesLayout);
        setSpeakerText(currentDialogue.title);

        setDialogueText(currentDialogue.text);
        List<Option> options = dialogueManager.getOptions();
        for (Option option : options) {
            choicesLayout.addView(getButton(option.text));
        }
        setLocationText(PersonalityActivity.game.currentLocation);
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

    public void setSpeakerText(String text) {
        speakerText.setText(text + ":");
    }
    public void setSpeakerText(Person person) {
        setSpeakerText(person.firstName);
    }

    public void setDialogueText(String text) {
        dialogueText.setText(text);
    }
    public void setLocationText(Location loc) {locationText.setText(loc.name);}
}
