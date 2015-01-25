package com.thechasedog.yourchoice;

import java.util.List;

/**
 * Created by katha_000 on 1/24/2015.
 */
public class Option {
    public String id;
    public List<String> requirements;
    public List<String> modifiers;
    public String text;

    public Option() {}
    public Option(Option option, String name) {
        this.id = option.id;
        this.requirements = option.requirements;
        this.modifiers = option.modifiers;
        this.text = option.text.replace("*", name);
    }
}
