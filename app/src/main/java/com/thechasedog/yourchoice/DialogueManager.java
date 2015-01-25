package com.thechasedog.yourchoice;

import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by katha_000 on 1/24/2015.
 */
public class DialogueManager {
    private List<Dialogue> allDialogues;
    private List<Option> allOptions;
    private List<String> tempRequirements;
    private List<String> permRequirements;

    public DialogueManager(Player player) {
        //Get all dialogues
        allDialogues = PersonalityActivity.game.allDialogues;
        //Get all the options
        allOptions = PersonalityActivity.game.options;

        permRequirements = new ArrayList<String>();
        tempRequirements = new ArrayList<String>();

        if (player.personality.aggressive) {
            permRequirements.add("Aggressive");
        }

        if (player.personality.alcoholic) {
            permRequirements.add("Alcoholic");
        }

        if (player.personality.brave) {
            permRequirements.add("Brave");
        }

        if (player.personality.childish) {
            permRequirements.add("Childish");
        }

        if (player.personality.competitive) {
            permRequirements.add("Competitive");
        }

        if (player.personality.humble) {
            permRequirements.add("Humble");
        }

        if (player.personality.mischievous) {
            permRequirements.add("Mischievous");
        }

        if (player.personality.romantic) {
            permRequirements.add("Romantic");
        }

        if (player.personality.shy) {
            permRequirements.add("Shy");
        }

        if (player.personality.smart) {
            permRequirements.add("Smart");
        }

        if (player.personality.talkative) {
            permRequirements.add("Talkative");
        }
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
                if (req.contains("*")) {
                    String firstPart = req.substring(0, req.indexOf('*'));
                    String name = "";
                    for (String tempReq : tempRequirements) {
                        if (tempReq.contains(firstPart)) {
                            name = tempReq.substring(firstPart.length());
                            currNumReqs++;
                        }
                    }

                    isInvalid = (name == "");
                }
                else {
                    if (tempRequirements.contains(req) || permRequirements.contains(req)) {
                        currNumReqs++;
                    } else {
                        isInvalid = true;
                        break;
                    }
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

    public List<Option> getOptions () {
        List<Option> currOptions = new ArrayList<Option>();
        boolean isInvalid;

        for (Option option : allOptions) {
            isInvalid = false;

            for (String req : option.requirements) {
                if (req.contains("*")) {
                    String firstPart = req.substring(0, req.indexOf('*'));
                    String name = "";
                    for (String tempReq : tempRequirements) {
                        if (tempReq.contains(firstPart)) {
                            name = tempReq.substring(firstPart.length());
                            currOptions.add(new Option(option, name));
                        }
                    }

                    isInvalid = (name == "");
                }
                else {
                    if (!(tempRequirements.contains(req) || permRequirements.contains(req))) {
                        isInvalid = true;
                        break;
                    }
                }
            }

            if (!isInvalid) {
                currOptions.add(option);
            }
        }

        return currOptions;
    }
}
