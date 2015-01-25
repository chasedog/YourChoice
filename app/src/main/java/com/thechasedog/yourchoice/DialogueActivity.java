package com.thechasedog.yourchoice;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class DialogueActivity extends Activity implements View.OnClickListener {
    private TextView speakerText;
    private TextView dialogueText;
    private LinearLayout choicesLayout;
    private Dialogue currentDialogue;
    private DialogueManager dialogueManager;
    private TextView locationText;
    private List<Option> options;

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
        if (options != null) {
            options.clear();
        }

        options = dialogueManager.getOptions();
        choicesLayout.removeAllViewsInLayout();
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
        button.setOnClickListener(this);
        return button;
    }

    public void updateScreen() {
        currentDialogue = dialogueManager.getNextDialogue();
        setDialogueText(currentDialogue.text);
        options.clear();
        options = dialogueManager.getOptions();
        choicesLayout.removeAllViewsInLayout();
        for (Option option : options) {
            choicesLayout.addView(getButton(option.text));
        }
        setLocationText(PersonalityActivity.game.currentLocation);
    }

    public void onClick (View view) {
        String text = ((Button)view).getText().toString();
        Option curOption = options.get(0);

        for (Option option : options) {
            if (option.text.equals(text)) {
                curOption = option;
                break;
            }
        }
        dialogueManager.selectOption(curOption);
        if (curOption.modifiers.contains("LocMap")) {
            Intent intent = new Intent(DialogueActivity.this, LocationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        else {
            updateScreen();
        }
    }

    public void setSpeakerText(String text) {
        speakerText.setText(text);
    }
    public void setSpeakerText(Person person) {
        setSpeakerText(person.firstName);
    }

    public void setDialogueText(String text) {
        dialogueText.setText(text);
    }
    public void setLocationText(Location loc) {locationText.setText(loc.name);}
}
