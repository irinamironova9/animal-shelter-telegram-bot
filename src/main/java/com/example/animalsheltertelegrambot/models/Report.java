package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pengrad.telegrambot.model.File;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Report {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    @JsonBackReference
    private ProbationPeriod probationPeriod;

    private LocalDate date;
    private String entry;
    private String photoId;

    public Report() {
    }

    public Long getId() {
        return id;
    }

    public ProbationPeriod getProbationPeriod() {
        return probationPeriod;
    }

    public String getEntry() {
        return entry;
    }

    public String getPhotoId() {
        return photoId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProbationPeriod(ProbationPeriod probationPeriod) {
        this.probationPeriod = probationPeriod;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}