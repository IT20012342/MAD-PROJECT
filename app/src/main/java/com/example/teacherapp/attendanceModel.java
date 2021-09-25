package com.example.teacherapp;

public class attendanceModel {

    private int Count;
    public  attendanceModel(){
        this.Count = 0;
    }


    public attendanceModel(int count) {
        this.Count = count;
    }


    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        this.Count = count;
    }
}
