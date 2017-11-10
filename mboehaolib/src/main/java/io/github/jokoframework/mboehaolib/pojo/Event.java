package io.github.jokoframework.mboehaolib.pojo;

import java.io.Serializable;

/**
 * Created by joaquin on 23/08/17.
 */

public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String description;
    private Integer resourceIconId;
    private Integer iconMenu;

    public Event(Long id) {
        this.id = id;
    }
    private Class activity;

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }

    public Event(int idSelected) {
        setId(Long.valueOf(idSelected));
    }

    public Integer getIconMenu() {
        return iconMenu;
    }

    public void setIconMenu(Integer iconMenu) {
        this.iconMenu = iconMenu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getResourceIconId() {
        return resourceIconId;
    }

    public void setResourceIconId(Integer resourceIconId) {
        this.resourceIconId = resourceIconId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", resourceIconId=" + resourceIconId +
                ", iconMenu=" + iconMenu +
                '}';
    }
}
