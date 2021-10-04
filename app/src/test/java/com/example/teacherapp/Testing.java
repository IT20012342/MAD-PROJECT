package com.example.teacherapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import javax.annotation.Nullable;

public class Testing {
    private EventRecyclerAdapter eventRecyclerAdapter;

    private AverageCal averagecal;

    private SummaryCalculation attendance;

    private GPAcal gpAcal;


    @BeforeEach
    public void setUp(){
        eventRecyclerAdapter = new EventRecyclerAdapter();

        //averagecal = new AverageCal();

        attendance = new SummaryCalculation();

        //gpAcal = new GPAcal();
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



    // IT20012342
    @Test
    public  void testTotal() {
        int result = averagecal.getTotal(70,80,90);
        assertEquals(240, result);
    }

    @Test
    public  void testAverage() {
        double result = averagecal.getAverage(70,80,90);
        assertEquals(80.0, result);
    }
  
    //IT20188054
    @Test
    public void summaryCalculation_isCorrect() {

        Object ob = 100L;  //Value retrieve  from the database  {Use object datatype since retrieve value from DB must be object}

        long result = attendance.calculateSummary(ob,20);

        Assert.assertEquals(120, result);
    }

    //IT20211332
    @Test
    public void checkCounter() throws Exception {
        //assertEquals(4.0, GPAcal.getCounter(3,4.0));
        double result = gpAcal.getCounter(3.0, 4.0);
        assertEquals(12.0, result);
    }
}
