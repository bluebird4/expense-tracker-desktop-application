package model;

import model.exception.RecordNonexistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrackedRecordsTest {
    TrackedRecords tr;
    MonthlyRecord r1;
    MonthlyRecord r2;
    MonthlyRecord r3;

    @BeforeEach
    public void runBefore() {
        tr = new TrackedRecords();
        r1 = new MonthlyRecord(Month.APRIL, 2020);
        r2 = new MonthlyRecord(Month.JUNE, 2020);
        r3 = new MonthlyRecord(Month.AUGUST, 2020);
        try {
            tr.addRecord(r1);
            tr.addRecord(r2);
            tr.addRecord(r3);
        } catch (Exception e) {
            fail("MonthlyRecords were not added to TrackedRecords, tests could not be run");
        }
    }

    @Test
    public void testConstructor() {
        List<MonthlyRecord> records = tr.getRecords();
        assertEquals(3, tr.size());
        assertEquals(r1, records.get(0));
        assertEquals(r2, records.get(1));
        assertEquals(r3, records.get(2));
    }

    @Test
    public void testRecordExistsTrue() {
        assertTrue(tr.recordExists(Month.JUNE, 2020));
    }

    @Test
    public void testRecordExistsFalse() {
        assertFalse(tr.recordExists(Month.JANUARY, 2020));
    }

    @Test
    public void testRecordExistsFileExists() {
        assertTrue(tr.recordExists(Month.NOVEMBER, 2020));
    }

    @Test
    public void testRecordExistsWrongYear() {
        assertFalse(tr.recordExists(Month.JUNE, 2018));
    }

    @Test
    public void testAddRecordSuccessful() {
        try {
            assertEquals(3, tr.size());
            tr.addRecord(new MonthlyRecord(Month.FEBRUARY, 2020));
            assertEquals(4, tr.size());
        } catch (Exception e) {
            fail("Exception was thrown when it should not have been");
        }
    }

    @Test
    public void testAddRecordUnsuccessful() {
        try {
            assertEquals(3, tr.size());
            tr.addRecord(r1);
            assertEquals(3, tr.size());
            fail("Exception should have been thrown");
        } catch (Exception e) {
            // Expected result, test passes
        }
    }

    @Test
    public void testFindRecordSuccessful() {
        try {
            MonthlyRecord found = tr.findRecord(Month.APRIL, 2020);
            assertEquals(found, r1);
        } catch (RecordNonexistentException e) {
            fail("RecordNonexistentException was unexpectedly thrown");
        }
    }

    @Test
    public void testFindRecordRightMonthWrongYear() {
        try {
            MonthlyRecord found = tr.findRecord(Month.APRIL, 2019);
            fail("RecordNonexistentException was expected but not thrown");
        } catch (RecordNonexistentException e) {
            // Expected result, test passes
        }
    }

    @Test
    public void testFindRecordWrongMonthRightYear() {
        try {
            MonthlyRecord found = tr.findRecord(Month.SEPTEMBER, 2020);
            fail("RecordNonexistentException was expected but not thrown");
        } catch (RecordNonexistentException e) {
            // Expected result, test passes
        }
    }

    @Test
    public void testFindRecordWrongMonthWrongYear() {
        try {
            MonthlyRecord found = tr.findRecord(Month.MARCH, 2019);
            fail("RecordNonexistentException was expected but not thrown");
        } catch (RecordNonexistentException e) {
            // Expected result, test passes
        }
    }

    @Test
    public void testGenerateFilePath() {
        assertEquals("./data/APRIL2020.json", tr.generateFilePath(Month.APRIL, 2020));
    }

}
