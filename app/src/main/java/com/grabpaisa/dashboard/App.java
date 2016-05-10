package com.grabpaisa.dashboard;

/**
 * Created by tahir on 5/5/2016.
 */
public class App {

    private String name;

    private String dLink;

    public App(String name, String dLink) {
        this.name = name;
        this.dLink = dLink;
    }

    public String getName() {
        return name;
    }

    public String getDLink() {
        return dLink;
    }
}
