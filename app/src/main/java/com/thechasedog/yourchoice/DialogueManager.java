package com.thechasedog.yourchoice;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

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
    static boolean firstDialogue = true;
    private Dialogue bedDialogue;
    private Option bedOption;
    private ImageView dialogueImage;

    public DialogueManager(Player player, ImageView dialogueImage) {
        //Get all dialogues
        allDialogues = PersonalityActivity.game.allDialogues;
        //Get all the options
        allOptions = PersonalityActivity.game.options;
        this.dialogueImage = dialogueImage;
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

        for (Dialogue dialogue : allDialogues) {
            if (dialogue.id.equals("Bedtime")) {
                bedDialogue = dialogue;
                break;
            }
        }

        for (Option option : allOptions) {
            if (option.id.equals("Bedtime")) {
                bedOption = option;
                break;
            }
        }

        tempRequirements.add("Free");
    }

    public void wipeTempRequirements() {
        tempRequirements.clear();
    }

    public Dialogue getBedDialogue() {
        wipeTempRequirements();
        tempRequirements.add("Bedtime");
        return bedDialogue;
    }

    public Option getBedOption() {
        return bedOption;
    }

    public void updateRequirements(List<String> newTempReqs, List<String> newPermReqs) {
        tempRequirements.addAll(newTempReqs);
        permRequirements.addAll(newPermReqs);
    }

    public Dialogue getNextDialogue() {
        if (firstDialogue) {
            firstDialogue = false;
        }

        int maxNumReqs = 0;
        Dialogue maxDialogue = allDialogues.get(0);
        int currNumReqs;
        boolean isInvalid;

        for (Dialogue dialogue : allDialogues) {
            currNumReqs = 0;
            isInvalid = false;
            if (dialogue.requirements != null) {
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

                        isInvalid = (name.isEmpty());
                    } else {
                        if (tempRequirements.contains(req) || permRequirements.contains(req)) {
                            currNumReqs++;
                        } else {
                            isInvalid = true;
                            break;
                        }
                    }
                }
            }

            if (!isInvalid && currNumReqs > maxNumReqs && !(tempRequirements.contains(dialogue.id))) {
                maxDialogue = dialogue;
                maxNumReqs = currNumReqs;
            }
        }

        tempRequirements.add(maxDialogue.id);

        if (tempRequirements.contains("PresentEsmerelda")) {
            tempRequirements.remove("PresentEsmerelda");
        }

        if (tempRequirements.contains("PresentDebra")) {
            tempRequirements.remove("PresentDebra");
        }
        if (permRequirements.contains("AtBusStop") && (PersonalityActivity.game.time == Game.TimeOfDay.MORNING || PersonalityActivity.game.time == Game.TimeOfDay.NOON)) {
            tempRequirements.add("PresentEsmerelda");
        }

        if (permRequirements.contains("AtBusStop") || permRequirements.contains("AtHome")) {
            tempRequirements.add("PresentDebra");
        }

        if (maxDialogue instanceof LocationDialogue && maxDialogue.requirements.contains("AtBusStop")) {
            dialogueImage.setImageResource(R.drawable.busstop);
        }


        return maxDialogue;
    }

    public void stopTalkingTo() {
        List<String> reqsToRemove = new ArrayList<String>();
        tempRequirements.add("Free");
        for (String tempReq : tempRequirements) {
            if (tempReq.contains("TalkingTo")) {
                reqsToRemove.add(tempReq);
            }
        }

        for (String req : reqsToRemove) {
            tempRequirements.remove(req);
        }
    }

    public void startTalkingTo(String name) {
        tempRequirements.add("TalkingTo" + name);
        if (tempRequirements.contains("Free")) {
            tempRequirements.remove("Free");
        }
    }

    public List<Option> getOptions () {
        List<Option> currOptions = new ArrayList<Option>();
        List<Option> wildCardOptions = new ArrayList<Option>();
        boolean isInvalid;
        boolean isWildCard;

        for (Option option : allOptions) {
            isInvalid = false;
            isWildCard = false;
            wildCardOptions.clear();

            if (option.requirements != null) {
                for (String req : option.requirements) {
                    if (req.contains("*")) {
                        isWildCard = true;
                        String firstPart = req.substring(0, req.indexOf('*'));
                        String name = "";
                        for (String tempReq : tempRequirements) {
                            if (tempReq.contains(firstPart)) {
                                name = tempReq.substring(firstPart.length());
                                wildCardOptions.add(new Option(option, name));
                            }
                        }

                        for (String tempReq : permRequirements) {
                            if (tempReq.contains(firstPart)) {
                                name = tempReq.substring(firstPart.length());
                                wildCardOptions.add(new Option(option, name));
                            }
                        }

                        isInvalid = (name.isEmpty());
                    } else {
                        if (!(tempRequirements.contains(req) || permRequirements.contains(req))) {
                            isInvalid = true;
                            break;
                        }
                    }
                }
            }

            if (!isInvalid) {
                if (isWildCard) {
                    currOptions.addAll(wildCardOptions);
                }
                else {
                    currOptions.add(option);
                }
            }
        }

        return currOptions;
    }

    public void selectOption (Option option) {
        if (option.modifiers != null) {
            if (option.modifiers.contains("StopTalking")) {
                stopTalkingTo();
            }

            if (option.modifiers.contains("TalkingTo*")) {
                startTalkingTo(option.text.substring(("Talk to ").length()));
            }

            for (String modifier : option.modifiers) {
                if (modifier.startsWith("Pic_")) {
                    String[] toks = modifier.split("_");
                    String person = toks[1];
                    Images.Type type = Images.Type.valueOf(toks[2]);
                    if (person.equals("Debra")) {
                        dialogueImage.setImageResource(Images.getDebra(type));
                    }
                    else if (person.equals("Esmerelda")) {
                        dialogueImage.setImageResource(Images.getEsmerelda(type));
                    }
                }
                else if (!(modifier.equals("TalkingTo*") || modifier.equals("StopTalking"))) {
                    tempRequirements.add(modifier);
                }
            }
        }
    }

    public void addPermReq(String perm) {
        for (String p : permRequirements) {
            if (p.equals(perm)) {
                break;
            }
        }
        permRequirements.add(perm);
    }

    public void removeLocation(Location location) {
        permRequirements.remove(location.reqText);
    }
}
