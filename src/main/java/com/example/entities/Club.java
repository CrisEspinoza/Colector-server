package com.example.entities;


import java.util.List;
import java.util.Set;


public class Club  {


    private Long id;

    private String name;

    private Set<Keyword> keywords;


    public Long getId() {
        return id;
    }

    public Set<Keyword> getKeywords() {
        return keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setId(Long id) {
        this.id = id;

    }



    public Club() {

    }


}
