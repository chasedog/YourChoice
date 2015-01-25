package com.thechasedog.yourchoice;

import android.util.Log;

/**
 * Created by katha_000 on 1/24/2015.
 */
public class Player extends Person{
    Personality personality;

    public Player (String firstName, Gender gender, int id, Personality personality) {
        this.firstName = firstName;
        this.gender = gender;
        this.personality = personality;
        this.id = id;

        if (id == 1) {
            this.lastName = "Collins";
            this.age = 22;
        }
        else if (id == 2) {
            this.lastName = "Applewood";
            this.age = 32;
        }
        else {
            Log.d("Invalid ID", "Invalid player id " + id);
        }
    }
}
