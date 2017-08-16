package com.hackathon.meetup.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by David Turk on 8/8/17.
 */
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column
    private int eventId;

    @OneToOne
    private User admin;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private  String location;

    @Column
    private String description;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Status status;

    public Event() {
    }

    public Event(User admin, String name, String location, String description, Date date, Status status) {
        this.admin = admin;
        this.name = name;
        this.location = location;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return eventId == event.eventId;
    }

    @Override
    public int hashCode() {
        return eventId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", admin=" + admin +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
