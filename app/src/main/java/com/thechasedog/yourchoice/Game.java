package com.thechasedog.yourchoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class Game {
    public static enum TimeOfDay {MORNING, NOON, AFTERNOON, NIGHT}
    public List<Location> locations;
    public List<NonPlayer> people;
    public Player player;
    public List<Dialogue> allDialogues;
    public List<Option> options;
    public Location currentLocation;
    public List<Location> availableLocations;
    public TimeOfDay time;

    public Game()
    {
        availableLocations = new ArrayList<Location>();
        time = TimeOfDay.MORNING;
    }
}
