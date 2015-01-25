package com.thechasedog.yourchoice;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class ReadInput {
    private static enum Mode {None,People, Locations};
    List<Location> locations;
    List<Person> people;
    Context context;

    public ReadInput(Context context) {
        people = new ArrayList<Person>(10);
        locations = new ArrayList<Location>(10);
        this.context = context;
    }

    public Game getGame() {
        Game game = new Game();
        Mode mode = Mode.None;
        Person person = new Person();
        Location location = new Location();

        String command;
        String value;
        BufferedReader br;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.gameinfo);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            String[] toks;
            while ((line = br.readLine()) != null) {
                toks = line.split(" ", 2);
                if (toks.length > 0) {
                    command = toks[0];
                    if (toks.length > 1) {
                        value = toks[1];
                    }
                    else {
                        value = "(none)";
                    }
                }
                else {
                    command = "";
                    value = "(none)";
                }


                if (mode == Mode.None) {
                    if (command.equals("PEOPLE")) {
                        mode = Mode.People;
                    }
                    else if (command.equals("LOCATIONS")) {
                        mode = Mode.Locations;
                    }
                }
                else if (mode == Mode.People) {
                    if (command.equals("FirstName")) {
                        person.firstName = value;
                    }
                    else if (command.equals("LastName")) {
                        person.lastName = value;
                    }
                    else if (command.equals("Gender")) {
                        person.gender = value.equals("M") ? Person.Gender.MALE : Person.Gender.FEMALE;
                    }
                    else if (command.equals("Age")) {
                        person.age = Integer.parseInt(value);
                    }
                    else if (command.equals("")) {
                        people.add(person);
                        person = new Person();
                    }
                    else if (command.equals("ENDPEOPLE")) {
                        if (person.firstName != null) {
                            people.add(person);
                            person = new Person();
                        }
                        mode = Mode.None;
                    }
                }
                else if (mode == Mode.Locations) {
                    if (command.equals("Name")) {
                        location.name = value;
                    }
                    else if (command.equals("SubLocations")) {
                        String[] locs = value.split(", ");

                        for (String loc : locs) {
                            try {
                                location.subLocations.add(getLocation(loc));
                            }
                            catch (SubLocationNotFound ex) {
                                Log.e("ReadInput error", loc +" not found");
                            }
                        }
                    }
                    else if (command.equals("")) {
                        locations.add(location);
                        location = new Location();
                    }
                    else if (command.equals("ENDLOCATIONS")) {
                        if (location.name != null) {
                            locations.add(location);
                            location = new Location();
                        }
                        mode = Mode.None;
                    }
                }
            }
            br.close();
        }
        catch (IOException e) {

        }

        game.locations = locations;
        game.people = people;
        return game;
    }

    private class SubLocationNotFound extends Exception {

    }

    private Location getLocation(String name) throws SubLocationNotFound {
        int index = 0;
        for (Location loc : locations) {
            if (name.equals(loc.name)) {
                return locations.get(index);
            }
            index++;
        }
        throw new SubLocationNotFound();
    }
}
