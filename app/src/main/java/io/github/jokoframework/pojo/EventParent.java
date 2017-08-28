package io.github.jokoframework.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joaquin on 23/08/17.
 */

public class EventParent {


    private String title;

    private List<Event> events;

    private String iconId;

    private Event event;

    public EventParent(String title, List<Event> events) {
        this.title = title;
        this.events = events;
    }

    public EventParent(Event event) {
        this.title = event.getDescription();
        this.events = new ArrayList<>();
        this.event = event;
    }

    public String getTitle() {
        return this.title;
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public String getIconId() {
        return this.iconId;
    }

    public Event getEvent() {
        return this.event;
    }
}
