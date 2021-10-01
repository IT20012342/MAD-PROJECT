package com.example.teacherapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

public class Testing {
    private EventRecyclerAdapter eventRecyclerAdapter;

    private Attendance attendance;

    @BeforeEach
    public void setUp(){
        eventRecyclerAdapter = new EventRecyclerAdapter();
    }

    @BeforeEach
    public void setup(){
        attendance = new Attendance();
    }



    @Test
    public void testConvertStringToDate(){
        Date Result = eventRecyclerAdapter.ConvertStringToDate("Thu Jan 01 07:00:00 GMT+05:30 2021");
        assertEquals(2021-01-01,Result);
    }

    @Test
    public void testConverStringToTime(){
        Date Result = eventRecyclerAdapter.ConvertStringToTime("Thu Jan 01 07:00:00 GMT+05:30 2021");
        assertEquals("07:00",Result);
    }


    @Test
    public void summaryCalculation_isCorrect() {
        Object ob = null;
        long result = attendance.calculateSummary(ob,20);
        Assert.assertEquals(20, result);
    }



}
