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
        tempRequirements.add("GameStart");
    }

    public void wipeTempRequirements() {
        tempRequirements.clear();
    }

    public Dialogue getBedDialogue() {
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
        int maxNumReqs = 0;
        Dialogue maxDialogue = allDialogues.get(0);
        int currNumReqs;
        boolean isInvalid;
        boolean notReq;

        if (tempRequirements.contains("PresentEsmerelda")) {
            tempRequirements.remove("PresentEsmerelda");
        }

        if (tempRequirements.contains("PresentDebra")) {
            tempRequirements.remove("PresentDebra");
        }

        if (tempRequirements.contains("PresentPenelope")) {
            tempRequirements.remove("PresentPenelope");
        }

        if (tempRequirements.contains("PresentTim")) {
            tempRequirements.remove("PresentTim");
        }

        if (permRequirements.contains("AtBusStop") && (PersonalityActivity.game.time == Game.TimeOfDay.MORNING || PersonalityActivity.game.time == Game.TimeOfDay.NOON) && !(tempRequirements.contains(("PunchedEsmerelda")))) {
            tempRequirements.add("PresentEsmerelda");
        }

        if (!tempRequirements.contains("PunchedDebra") && !tempRequirements.contains("PunchedEsmerelda")) {
            tempRequirements.add("PresentDebra");
        }

        if (permRequirements.contains("AtMo'sDiner") && PersonalityActivity.game.time == Game.TimeOfDay.NOON) {
            tempRequirements.add("PresentTim");
        }

        if (permRequirements.contains("AtMo'sDiner")) {
            tempRequirements.add("PresentPenelope");
        }

        for (Dialogue dialogue : allDialogues) {
            currNumReqs = 0;
            isInvalid = false;
            notReq = false;
            if (dialogue.requirements != null) {
                for (String req : dialogue.requirements) {
                    notReq = req.contains("Not ");
                    if (notReq) {
                        req = req.substring(("Not ").length());
                        if (tempRequirements.contains(req) || permRequirements.contains(req)) {
                            isInvalid = true;
                        }
                    }
                    else if (req.contains("*")) {
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

            if (!isInvalid && currNumReqs > maxNumReqs && (!(dialogue instanceof PeopleDialogue) || !(tempRequirements.contains(dialogue.id)))) {
                maxDialogue = dialogue;
                maxNumReqs = currNumReqs;
            }
        }


        if (maxDialogue instanceof PeopleDialogue) {
            tempRequirements.add(maxDialogue.id);
        }



        if (maxDialogue instanceof LocationDialogue && maxDialogue.requirements.contains("AtBusStop")) {
            dialogueImage.setImageResource(R.drawable.busstop);
        }

        if (tempRequirements.contains("GameStart")) {
            tempRequirements.remove("GameStart");
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
        boolean notReq;

        for (Option option : allOptions) {
            isInvalid = false;
            isWildCard = false;
            notReq = false;
            wildCardOptions.clear();
            if (option.requirements != null) {
                for (String req : option.requirements) {
                    notReq = req.contains("Not ");
                    if (notReq) {
                        req = req.substring(("Not ").length());
                    }
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

                        if (notReq) {
                            isInvalid = !(name.isEmpty());
                        }
                        else {
                            isInvalid = (name.isEmpty());
                        }
                    } else {
                        if (!(tempRequirements.contains(req) || permRequirements.contains(req)) && !notReq) {
                            isInvalid = true;
                            break;
                        }
                        else if ((tempRequirements.contains(req) || permRequirements.contains(req)) && notReq) {
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

    public boolean selectOption (Option option) {
        boolean hasImage = false;
        if (option.modifiers != null) {
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
                        hasImage = true;
                    }
                    else if (person.equals("Esmerelda")) {
                        dialogueImage.setImageResource(Images.getEsmerelda(type));
                        hasImage = true;
                    }
                }
                else if (modifier.equals("Punched*")) {
                    tempRequirements.add("Punched" + option.text.substring(("Punch ").length()));
                    for (String t : tempRequirements) {
                        if (t.startsWith("TalkingTo")) {
                            String person = t.substring("TalkingTo".length());
                            if (person.equals("Debra") && option.text.substring(("Punch ").length()).equals("Debra")) {
                                dialogueImage.setImageResource(Images.getDebra(Images.Type.Punched));
                                hasImage = true;
                                break;
                            }
                            else if (person.equals("Esmerelda") && option.text.substring(("Punch ").length()).equals("Esmerelda")) {
                                dialogueImage.setImageResource(Images.getEsmerelda(Images.Type.Punched));
                                hasImage = true;
                                break;
                            }
                        }
                    }
                }
                else if (modifier.equals("StopTalking")) {
                    stopTalkingTo();
                }
                else if (!(modifier.equals("TalkingTo*") || modifier.equals("StopTalking"))) {
                    tempRequirements.add(modifier);
                }
            }
        }
        return hasImage;
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
