package com.example.teacherapp;

public class classModel {

    private String name,batch,time,description, id ;

    public classModel() {
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
}
