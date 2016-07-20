package com.theironyard.entities;


import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @GeneratedValue
    @Id
    int id;

    @Column(nullable = false)
    String username;

    @Column(nullable = false)
    String password;

//    @Column(nullable = false)
//    int daysVisited;
//
//    @Column(nullable = false)
//    int lastVisited;

    @Column
    String language;

    @Column
    Boolean technology;

    @Column
    Boolean sports;

    @Column
    Boolean business;

    @Column
    Boolean politics;

    @Column
    Boolean arts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public int getDaysVisited() {
//        return daysVisited;
//    }
//
//    public void setDaysVisited(int daysVisited) {
//        this.daysVisited = daysVisited;
//    }
//
//    public int getLastVisited() {
//        return lastVisited;
//    }
//
//    public void setLastVisited(int lastVisited) {
//        this.lastVisited = lastVisited;
//    }
}
