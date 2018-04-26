package com.devglan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "Userrr")
public class User {

    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    private String username;
    @Column
    private String title;

    public long getId() {
        return id;
    }


    public User(String username, String title) {
        this.username = username;
        this.title = title;
    }

    public User() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title =  title;
    }
}
