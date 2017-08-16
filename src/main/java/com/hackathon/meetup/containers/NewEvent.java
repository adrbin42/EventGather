package com.hackathon.meetup.containers;

import com.hackathon.meetup.exceptions.BadRequestException;

/**
 * Created by David Turk on 8/10/17.
 */
public class NewEvent {

    private String name;

    private String location;

    private String description;

    public NewEvent() {
    }

    public NewEvent(String name, String location, String description) throws BadRequestException{
        this.name = name;
        this.location = location;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
