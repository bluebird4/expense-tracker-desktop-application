package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    Expense e;
    Date d;

    @BeforeEach
    public void runBefore() {
        d = new Date(2020, Month.OCTOBER, 1);
        e = new Expense(1000, d, "test purchase", Category.MISCELLANEOUS);
    }

    @Test
    public void testConstructor() {
        Date date = e.getDate();

        assertEquals(1000, e.getCost());
        assertEquals("test purchase", e.getLabel());
        assertEquals(Category.MISCELLANEOUS, e.getCategory());

        assertEquals(2020, date.getYear());
        assertEquals(Month.OCTOBER, date.getMonth());
        assertEquals(1, date.getDay());

    }

    @Test
    public void testIsAboveThresholdTypical() {
        assertTrue(e.isAboveThreshold(500));
    }

    @Test
    public void testIsAboveThresholdBoundary() {
        assertTrue(e.isAboveThreshold(1000));
    }

    @Test
    public void testIsAboveThresholdFalse() {
        assertFalse(e.isAboveThreshold(1500));
    }

    @Test
    public void testIsBelowThresholdTypical() {
        assertTrue(e.isBelowThreshold(1500));
    }

    @Test
    public void testIsBelowThresholdBoundary() {
        assertTrue(e.isBelowThreshold(1000));
    }

    @Test
    public void testIsBelowThresholdFalse() {
        assertFalse(e.isBelowThreshold(500));
    }

    @Test
    public void testIsBeforeTypical() {
        Date date = new Date(2020, Month.DECEMBER, 10);
        assertTrue(e.isBefore(date));
    }

    @Test
    public void testIsBeforeBoundaryTrue() {
        Date date = new Date(2020, Month.OCTOBER, 2);
        assertTrue(e.isBefore(date));
    }

    @Test
    public void testIsBeforeBoundaryFalse() {
        Date date = new Date(2020, Month.OCTOBER, 1);
        assertFalse(e.isBefore(date));
    }

    @Test
    public void testIsBeforeFalse() {
        Date date = new Date(2018, Month.OCTOBER, 25);
        assertFalse(e.isBefore(date));
    }

    @Test
    public void testIsAfterTypical() {
        Date date = new Date(2018, Month.OCTOBER, 25);
        assertTrue(e.isAfter(date));
    }

    @Test
    public void testIsAfterBoundaryTrue() {
        Date date = new Date(2020, Month.SEPTEMBER, 30);
        assertTrue(e.isAfter(date));
    }

    @Test
    public void testIsAfterBoundaryFalse() {
        Date date = new Date(2020, Month.OCTOBER, 1);
        assertFalse(e.isAfter(date));
    }

    @Test
    public void testIsAfterFalse() {
        Date date = new Date(2020, Month.DECEMBER, 25);
        assertFalse(e.isAfter(date));
    }

    @Test
    public void testIsOfCategoryTrue() {
        assertTrue(e.isOfCategory(Category.MISCELLANEOUS));
    }

    @Test
    public void testIsOfCategoryFalse() {
        assertFalse(e.isOfCategory(Category.ENTERTAINMENT));
    }

    @Test
    public void testSetCost() {
        e.setCost(5000);
        assertEquals(5000, e.getCost());
    }

    @Test
    public void testSetDate() {
        Date newDate = new Date(2020, Month.APRIL, 4);
        e.setDate(newDate);
        Date test = e.getDate();
        assertEquals(2020, test.getYear());
        assertEquals(Month.APRIL, test.getMonth());
        assertEquals(4, test.getDay());
    }

    @Test
    public void testSetLabel() {
        e.setLabel("new label");
        assertEquals("new label", e.getLabel());
    }

    @Test
    public void testSetCategory() {
        e.setCategory(Category.ENTERTAINMENT);
        assertEquals(Category.ENTERTAINMENT, e.getCategory());
    }

    @Test
    public void testToDollarsLeadingZeros() {
        assertEquals("$10.00", e.toDollars());
    }

    @Test
    public void testToDollarsNoLeadingZeros() {
        Expense e = new Expense(1025, d, "", Category.MISCELLANEOUS);
        assertEquals("$10.25", e.toDollars());
    }

}