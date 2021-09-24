package com.example.teacherapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class classModel {

    private String name;
    private String description;
    private String batch;
    private String time;
    private String id;

    public classModel() {
        this.name = "";
        this.description = "";
        this.batch = " ";
        this.time = "";
        this.id = " ";
    }

    public classModel(String name,String description, String batch, String time, String id) {
        this.name = name;
        this.description = description;
        this.batch = batch;
        this.time = time;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",name);
        result.put("description",description);
        result.put("batch",batch);
        result.put("time",time);
        //result.put("id",id);

        return result;
    }
}
