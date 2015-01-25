package com.thechasedog.yourchoice;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Chase Dog on 1/24/2015.
 */
public class ReadInput {
    private static enum Mode {None,People, Locations, Options, PeopleDialogues, LocationDialogues};
    List<Location> locations;
    List<NonPlayer> people;
    Context context;

    public ReadInput(Context context) {
        people = new ArrayList<NonPlayer>(10);
        locations = new ArrayList<Location>(10);
        this.context = context;
    }

    public ArrayList<Option> getOptions() {
        Mode mode = Mode.None;
        Option option = new Option();
        option.requirements = new ArrayList<String>();
        option.modifiers = new ArrayList<String>();
        String command;
        String value;
        BufferedReader br;

        ArrayList<Option> options = new ArrayList<Option>();

        try {
            InputStream is = context.getResources().openRawResource(R.raw.options);
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
                    if (command.equals("OPTIONS")) {
                        mode = Mode.Options;
                    }
                    else if(command.equals("ENDOPTIONS"))
                    {
                        mode = Mode.None;
                    }
                }
                /*Id Leave1
                Requirements
                Modifiers LocMap
                Text Leave */
                else if (mode == Mode.Options) {
                    if (!value.equals("(none)")) {
                        if (command.equals("Id")) {
                            option.id = value;
                        } else if (command.equals("Requirements")) {
                            option.requirements = Arrays.asList(value.split(", "));
                        } else if (command.equals("Modifiers")) {
                            option.modifiers = Arrays.asList(value.split(", "));
                        } else if (command.equals("Text")) {
                            option.text = value;
                        } else if (command.equals("") && option.id != null) {
                            options.add(option);
                            option = new Option();
                            option.requirements = new ArrayList<String>();
                            option.modifiers = new ArrayList<String>();
                        }
                    }
                    else if (command.equals("") && option.id != null) {
                        options.add(option);
                        option = new Option();
                        option.requirements = new ArrayList<String>();
                        option.modifiers = new ArrayList<String>();
                    }
                }

            }
            br.close();
        }
        catch (IOException e) {

        }
        return options;
    }

    public ArrayList<Dialogue> getDialogues() {
        PeopleDialogue pd = new PeopleDialogue();
        LocationDialogue ld = new LocationDialogue();
        ArrayList<Dialogue> pds = new ArrayList<Dialogue>();
        Mode mode = Mode.None;

        String command;
        String value;
        BufferedReader br;


        try {
            InputStream is = context.getResources().openRawResource(R.raw.dialogues);
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
                    if (command.equals("PEOPLEDIALOGUES")) {
                        mode = Mode.PeopleDialogues;
                    }
                    else if (command.equals("LOCATIONDIALOGUES")) {
                        mode = Mode.LocationDialogues;
                    }
                }
                  /*Id Debra1
                    People Debra
                    Requirements
                    Text "What do we do now?"*/
                else if (mode == Mode.PeopleDialogues) {
                    if (command.equals("Id")) {
                        pd.id = value;
                    }
                    else if (command.equals("People")) {
                        try {
                            pd.person = getPerson(value);
                        }
                        catch (PersonNotFound ex) {
                            Log.e("PersonNotFound", value + " not found");
                        }
                    }
                    else if (command.equals("Requirements")) {
                        pd.requirements = Arrays.asList(value.split(", "));
                    }
                    else if (command.equals("Text")) {
                        pd.text = value;
                    }
                    else if (command.equals("") && pd.person != null) {
                        pd.title = pd.person.firstName;
                        pds.add(pd);
                        pd = new PeopleDialogue();
                    }
                }
                /*LOCATIONDIALOGUES
                Id BusStop1
                Location Bus Stop
                Text You are*/
                else if (mode == Mode.LocationDialogues) {
                    if (command.equals("Id")) {
                        ld.id = value;
                    }
                    else if (command.equals("Location")) {
                        try {
                            ld.location = getLocation(value);
                        }
                        catch (Exception ex) {
                            Log.e("ReadInput", value + " not found");
                        }
                    }
                    else if (command.equals("Text")) {
                        ld.text = value;
                    }
                    else if (command.equals("Requirements")) {
                        pd.requirements = Arrays.asList(value.split(", "));
                    }
                    else if (command.equals("") && ld.id != null) {
                        ld.title = ld.location.name;
                        pds.add(ld);
                        ld = new LocationDialogue();
                    }
                }
            }
            br.close();
        }
        catch (IOException e) {

        }
        return pds;
    }

    public Game getGame() {
        Game game = new Game();
        Mode mode = Mode.None;
        Random random = new Random();
        NonPlayer person = new NonPlayer(random.nextInt(41) - 20);
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
                /* this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.emotionMeter = initEmotionMeter;
        this.id = id;*/
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
                        person = new NonPlayer(random.nextInt(41) - 20);
                    }
                    else if (command.equals("ENDPEOPLE")) {
                        if (person.firstName != null) {
                            people.add(person);
                            person = new NonPlayer(random.nextInt(41) - 20);
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
                    else if (command.equals("IsSub")) {
                        location.isSub = Boolean.valueOf(value);
                    }
                    else if (command.equals("ReqText")) {
                        location.reqText = value;
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

    private class PersonNotFound extends Exception {

    }

    private NonPlayer getPerson(String name) throws PersonNotFound {
        int index = 0;
        for (NonPlayer p : people) {
            if (name.equals(p.firstName)) {
                return people.get(index);
            }
            index++;
        }
        throw new PersonNotFound();
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
