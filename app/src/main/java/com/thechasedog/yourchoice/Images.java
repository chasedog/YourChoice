package com.thechasedog.yourchoice;

/**
 * Created by Chase Dog on 1/25/2015.
 */
public class Images {
    public static enum Type {Happy, Neutral, Mad, Punched}
    public static int getDebra(Type type) {
        switch (type) {
            case Happy:
                return R.drawable.debra_happy;
            case Neutral:
                return R.drawable.debra_neutral;
            case Mad:
                return R.drawable.debra_mad;
            case Punched:
                return  R.drawable.debra_punched;
        }
        return R.drawable.debra_neutral;
    }

    public static int getEsmerelda(Type type) {
        return R.drawable.esmerelda_neutral;
    }
}
