package com.example.animalsheltertelegrambot.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Adopter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long chatId;
    private String username;
    private String name;
    private String address;
    private Integer age;
    private String telephoneNumber;

    @OneToMany(mappedBy = "adopter", fetch = FetchType.LAZY)
//    @JoinColumn
    @JsonBackReference
    private List<Animal> animals;

    @OneToMany(mappedBy = "adopter", fetch = FetchType.LAZY)
//    @JoinColumn
    @JsonBackReference
    private List<ProbationPeriod> probationPeriods;

    private Long currentReportId;

    public Adopter() {
    }

    public long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Long getCurrentReportId() {
        return currentReportId;
    }

    public int getAge() {
        return age;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<ProbationPeriod> getProbationPeriods() {
        return probationPeriods;
    }

    public String getUsername() {
        return username;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setCurrentReportId(Long currentReportId) {
        this.currentReportId = currentReportId;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public void setProbationPeriods(List<ProbationPeriod> probationPeriods) {
        this.probationPeriods = probationPeriods;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopter adopter = (Adopter) o;
        return id == adopter.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
