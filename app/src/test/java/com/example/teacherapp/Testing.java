package com.example.teacherapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

public class Testing {
    private EventRecyclerAdapter eventRecyclerAdapter;

    @BeforeEach
    public void setUp(){
        eventRecyclerAdapter = new EventRecyclerAdapter();
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
}
