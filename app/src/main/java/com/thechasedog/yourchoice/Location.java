package com.thechasedog.yourchoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class Location {
    public String name;
    public List<Location> subLocations;

    public Location() {
        subLocations = new ArrayList<Location>();
    }

    @Override
    public String toString() {
        String subs = "";
        for (Location loc : subLocations) {
            subs += "\n"+loc.name;
        }
        return name + subs;
    }
}
