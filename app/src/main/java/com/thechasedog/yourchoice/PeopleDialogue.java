package com.thechasedog.yourchoice;

import java.util.List;

/**
 * Created by katha_000 on 1/24/2015.
 */
public class PeopleDialogue extends Dialogue {
    public NonPlayer person;

    public PeopleDialogue (String id, List<String> requirements, String text, NonPlayer person) {
        this.id = id;
        this.requirements = requirements;
        this.text = text;
        this.person = person;
        this.title = "???";
    }
}
