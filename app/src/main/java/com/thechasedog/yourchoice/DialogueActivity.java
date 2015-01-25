package com.thechasedog.yourchoice;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class DialogueActivity extends Activity {
    private TextView speakerText;
    private TextView dialogueText;
    private LinearLayout choicesLayout;
    private Dialogue currentDialogue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogue);

        speakerText = (TextView)findViewById(R.id.speakerText);
        dialogueText = (TextView)findViewById(R.id.dialogueText);
        choicesLayout = (LinearLayout)findViewById(R.id.choicesLayout);
        setSpeakerText("Katie Keim");

        setDialogueText("Why don't you ask that lady over there where we can eat.");
        choicesLayout.addView(getButton("I don't know what you're doing, but I'm going somewhere without you"));
        choicesLayout.addView(getButton("I don't know what you're doing, but I'm going somewhere without you"));
        choicesLayout.addView(getButton("I don't know what you're doing, but yeah I hate you"));
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
}
