package com.example.animalsheltertelegrambot.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GeneralInfo {
    private long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
