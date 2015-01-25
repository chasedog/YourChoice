package com.thechasedog.yourchoice;

import android.graphics.BitmapFactory;

import java.util.List;

/**
 * Created by katha_000 on 1/24/2015.
 */
public class DialogueManager {
    private List<Dialogue> allDialogues;
    private List<String> tempRequirements;
    private List<String> permRequirements;

    public DialogueManager() {
        //Get all dialogues
    }

    public void wipeTempRequirements() {
        tempRequirements.clear();
    }

    public void updateRequirements(List<String> newTempReqs, List<String> newPermReqs) {
        tempRequirements.addAll(newTempReqs);
        permRequirements.addAll(newPermReqs);
    }

    public Dialogue getNextDialogue() {
        int maxNumReqs = 0;
        Dialogue maxDialogue = allDialogues.get(0);
        int currNumReqs;
        boolean isInvalid;

        for (Dialogue dialogue : allDialogues) {
            currNumReqs = 0;
            isInvalid = false;
            for (String req : dialogue.requirements) {
                if (tempRequirements.contains(req) || permRequirements.contains(req)) {
                    currNumReqs++;
                }
                else {
                    isInvalid = true;
                    break;
                }
            }

            if (!isInvalid && currNumReqs > maxNumReqs) {
                maxDialogue = dialogue;
                maxNumReqs = currNumReqs;
            }
        }

        if (maxDialogue instanceof PeopleDialogue) {
            tempRequirements.add("TalkingTo" + ((PeopleDialogue)maxDialogue).person.firstName);
            tempRequirements.add(maxDialogue.id);
        }
        else {
            tempRequirements.add("Free");
            for (String tempReq : tempRequirements) {
                if (tempReq.contains("TalkingTo")) {
                    tempRequirements.remove(tempReq);
                }
            }
        }

        return maxDialogue;
    }
}
