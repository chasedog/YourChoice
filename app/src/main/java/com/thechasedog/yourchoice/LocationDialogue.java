package com.thechasedog.yourchoice;

import java.util.List;

/**
 * Created by katha_000 on 1/24/2015.
 */
public class LocationDialogue extends Dialogue{
    Location location;

    public LocationDialogue (String id, List<String> requirements, String text, Location location) {
        this.id = id;
        this.requirements = requirements;
        this.text = text;
        this.location = location;
        this.title = location.toString();
    }

    public LocationDialogue() {}
}
