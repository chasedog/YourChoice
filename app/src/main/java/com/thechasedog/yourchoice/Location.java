package com.thechasedog.yourchoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class Location {
    public String name;
    public List<Location> subLocations;
    public boolean isSub;
    public String reqText;

    public Location() {
        subLocations = new ArrayList<Location>();
        isSub = false;
    }

    @Override
    public String toString() {
        String subs = "";
        for (Location loc : subLocations) {
            subs += "\n"+loc.name;
        }
        return name + subs;
    }

    @Override
    public boolean equals(Object o) {
        return name.equals(((Location)o).name);
    }
}
