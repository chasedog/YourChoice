package com.thechasedog.yourchoice;

/**
 * Created by katha_000 on 1/24/2015.
 */
public class NonPlayer extends Person{
    private int emotionMeter;
    private double[] responseMultipliers;

    public NonPlayer (String firstName, String lastName, Gender gender, int age, int initEmotionMeter, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.emotionMeter = initEmotionMeter;
        this.id = id;

        for (Personality.Trait trait:Personality.Trait.values()) {
            this.responseMultipliers[trait.ordinal()] = 10 * Math.random() - 10 * Math.random();
        }
    }
}
