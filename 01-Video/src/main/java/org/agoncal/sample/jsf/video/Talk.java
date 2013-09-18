package org.agoncal.sample.jsf.video;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@Table(name = "JSFTalk")
public class Talk implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    @GeneratedValue
    private Long id;
    @Version
    private int version;
    @NotNull
    private String title;
    private String description;
    @NotNull
    private String room;
    private String speaker;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Talk() {
    }

    public Talk(String title, String description, String room) {
        this.title = title;
        this.description = description;
        this.room = room;
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
}