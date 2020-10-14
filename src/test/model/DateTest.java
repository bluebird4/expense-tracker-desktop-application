package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DateTest {
    Date d;

    @BeforeEach
    public void runBefore() {
        d = new Date(2020, 10, 2);
    }

    @Test
    public void testConstructor() {
        assertEquals(2020, d.getYear());
        assertEquals(10, d.getMonth());
        assertEquals(2, d.getDay());
    }

    @Test
    public void testIsBeforeYear() {
        Date test = new Date(2021, 12, 25);
        assertTrue(d.isBefore(test));
    }

    @Test
    public void testIsBeforeMonth() {
        Date test = new Date (2020, 12, 25);
        assertTrue(d.isBefore(test));
    }

    @Test
    public void testIsBeforeBoundaryTrue() {
        Date test = new Date(2020, 10, 3);
        assertTrue(d.isBefore(test));
    }

    @Test
    public void testIsBeforeBoundaryFalse() {
        Date test = new Date(2020, 10, 2);
        assertFalse(d.isBefore(test));
    }

    @Test
    public void testIsBeforeYearFalse() {
        Date test = new Date(2018, 10, 1);
        assertFalse(d.isBefore(test));
    }

    @Test
    public void testIsBeforeMonthFalse() {
        Date test = new Date(2020, 4, 4);
        assertFalse(d.isBefore(test));
    }

    @Test
    public void testIsAfterYear() {
        Date test = new Date(2018, 10, 1);
        assertTrue(d.isAfter(test));
    }

    @Test
    public void testIsAfterMonth() {
        Date test = new Date(2020, 4, 4);
        assertTrue(d.isAfter(test));
    }

    @Test
    public void testIsAfterBoundaryTrue() {
        Date test = new Date(2020, 10, 1);
        assertTrue(d.isAfter(test));
    }

    @Test
    public void testIsAfterBoundaryFalse() {
        Date test = new Date(2020, 10, 2);
        assertFalse(d.isAfter(test));
    }

    @Test
    public void testIsAfterYearFalse() {
        Date test = new Date (2021, 12, 25);
        assertFalse(d.isAfter(test));
    }

    @Test
    public void testIsAfterMonthFalse() {
        Date test = new Date(2020, 12, 25);
        assertFalse(d.isAfter(test));
    }

    @Test
    public void testSetYear() {
        d.setYear(2018);
        assertEquals(2018, d.getYear());
    }

    @Test
    public void testSetMonth() {
        d.setMonth(4);
        assertEquals(4, d.getMonth());
    }

    @Test
    public void testSetDay() {
        d.setDay(4);
        assertEquals(4, d.getDay());
    }

}
