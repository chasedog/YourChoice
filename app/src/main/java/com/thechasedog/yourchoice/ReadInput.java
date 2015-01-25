package com.thechasedog.yourchoice;

import android.content.Context;

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
    private static enum Mode {None,People, Person, Locations, Location};

    public ReadInput(String file, Context context) {
        List<Person> people = new ArrayList<Person>(10);
        Mode mode = Mode.None;
        Person person = new Person();
        String command;
        String value;
        BufferedReader br;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.gameinfo);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                command = line.split(" ", 1)[0];
                value = line.split(" ", 1)[1];
                if (mode == Mode.None) {
                    if (command.equals("PEOPLE")) {
                        mode = Mode.People;
                    }
                }
                else if (mode == Mode.People) {
                    if (command.equals(("PERSON"))) {
                        mode = Mode.Person;
                        person = new Person();
                    }
                }
                else if (mode == Mode.Person) {
                    if (command.equals("FirstName") && person != null) {
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
                }
            }
            br.close();
        }
        catch (IOException e) {

        }
    }
}
