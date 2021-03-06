package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultContainter {
    String section;
    ArrayList<Results> results;

    public ResultContainter(String section, ArrayList<Results> results) {
        this.section = section;
        this.results = results;
    }

    public ResultContainter() {
    }

    public ArrayList<Results> getResults() {
        return results;
    }

    public void setResults(ArrayList<Results> results) {
        this.results = results;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "ResultContainter{" +
                "results=" + results +
                '}';
    }
}
