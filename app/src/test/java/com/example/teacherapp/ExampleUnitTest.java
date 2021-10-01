package com.example.teacherapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
//import org.junit.jupiter.api.BeforeEach

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Attendance attendance;

    @Before
    public void setup(){
        attendance = new Attendance();
    }
    @Test
    public void addition_isCorrect() {
        Object ob = null;
        long result = attendance.calculateSummary(ob,20);
        assertEquals(20, result);
    }
}