package io.github.jokoframework.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by joaquin on 23/08/17.
 *
 * @author joaquin
 * @author afeltes
 */
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String description;
    private Integer resourceIconId;
    private Integer iconMenu;
    private Class activity;

    public Event(Long id) {
        this.id = id;
    }

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
        return new ToStringBuilder(this)
                .append("id", id)
                .append("description", description)
                .append("resourceIconId", resourceIconId)
                .append("iconMenu", iconMenu)
                .append("activity", activity)
                .toString();
    }
}
