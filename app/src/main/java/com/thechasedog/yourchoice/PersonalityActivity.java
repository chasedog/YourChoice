package com.thechasedog.yourchoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class PersonalityActivity extends Activity implements CompoundButton.OnCheckedChangeListener{
    public Button startGame;
    public ToggleButton alcoholicBtn;
    public ToggleButton aggressiveBtn;
    public ToggleButton braveBtn;
    public ToggleButton childishBtn;
    public ToggleButton competitiveBtn;
    public ToggleButton humbleBtn;
    public ToggleButton mischievousBtn;
    public ToggleButton romanticBtn;
    public ToggleButton shyBtn;
    public ToggleButton smartBtn;
    public ToggleButton talkativeBtn;

    private List<CompoundButton> selectedBtns;
    private int numSelections = 0;

    private int checkedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedBtns = new ArrayList<CompoundButton>();

        setContentView(R.layout.personality_layout);
        startGame = (Button)findViewById(R.id.startGame);

        alcoholicBtn = (ToggleButton)findViewById(R.id.personality_alcoholic);
        aggressiveBtn = (ToggleButton)findViewById(R.id.personality_aggressive);
        braveBtn = (ToggleButton)findViewById(R.id.personality_brave);
        childishBtn = (ToggleButton)findViewById(R.id.personality_childish);
        competitiveBtn = (ToggleButton)findViewById(R.id.personality_competitive);
        humbleBtn = (ToggleButton)findViewById(R.id.personality_humble);
        mischievousBtn = (ToggleButton)findViewById(R.id.personality_mischievous);
        romanticBtn = (ToggleButton)findViewById(R.id.personality_romantic);
        shyBtn = (ToggleButton)findViewById(R.id.personality_shy);
        smartBtn = (ToggleButton)findViewById(R.id.personality_smart);
        talkativeBtn = (ToggleButton)findViewById(R.id.personality_talkative);

        alcoholicBtn.setOnCheckedChangeListener(this);
        aggressiveBtn.setOnCheckedChangeListener(this);
        braveBtn.setOnCheckedChangeListener(this);
        childishBtn.setOnCheckedChangeListener(this);
        competitiveBtn.setOnCheckedChangeListener(this);
        humbleBtn.setOnCheckedChangeListener(this);
        mischievousBtn.setOnCheckedChangeListener(this);
        romanticBtn.setOnCheckedChangeListener(this);
        shyBtn.setOnCheckedChangeListener(this);
        smartBtn.setOnCheckedChangeListener(this);
        talkativeBtn.setOnCheckedChangeListener(this);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Personality personality = getPersonality();
                startActivity(new Intent(PersonalityActivity.this, DialogueActivity.class));
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && !selectedBtns.contains(buttonView)) {
            if (numSelections >= 3) {
                selectedBtns.get(numSelections % 3).setChecked(false);
                selectedBtns.set(numSelections % 3, buttonView);
            }
            else {
                selectedBtns.add(numSelections, buttonView);
            }

            numSelections++;
        }

        switch(buttonView.getId()) {
            case R.id.personality_aggressive:
                if (isChecked) {
                    humbleBtn.setEnabled(false);
                }
                else {
                    humbleBtn.setEnabled(true);
                }
                break;
            case R.id.personality_shy:
                if (isChecked) {
                    talkativeBtn.setEnabled(false);
                    braveBtn.setEnabled(false);
                }
                else {
                    braveBtn.setEnabled(true);
                    talkativeBtn.setEnabled(true);
                }
                break;
            case R.id.personality_talkative:
                if (isChecked) {
                    shyBtn.setEnabled(false);
                }
                else if (!braveBtn.isChecked()){
                    shyBtn.setEnabled(true);
                }
                break;
            case R.id.personality_brave:
                if (isChecked) {
                    shyBtn.setEnabled(false);
                }
                else if (!talkativeBtn.isChecked()){
                    shyBtn.setEnabled(true);
                }
                break;
            case R.id.personality_humble:
                if (isChecked) {
                    aggressiveBtn.setEnabled(false);
                }
                else {
                    aggressiveBtn.setEnabled(true);
                }
                break;
        }
    }

    private Personality getPersonality() {
        Personality personality = new Personality();
        personality.alcoholic = alcoholicBtn.isChecked();
        personality.aggressive = aggressiveBtn.isChecked();
        personality.brave = braveBtn.isChecked();
        personality.childish = childishBtn.isChecked();
        personality.competitive = competitiveBtn.isChecked();
        personality.humble = humbleBtn.isChecked();
        personality.mischievous = mischievousBtn.isChecked();
        personality.romantic = romanticBtn.isChecked();
        personality.shy = shyBtn.isChecked();
        personality.smart = smartBtn.isChecked();
        personality.talkative = talkativeBtn.isChecked();
        return personality;
    }
}
