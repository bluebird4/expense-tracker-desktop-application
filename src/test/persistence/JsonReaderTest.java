package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Tests taken and repurposed from JsonSerializationDemo
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nonExistentFile.json");
        try {
            MonthlyRecord record = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // Expected, test passes
        }
    }

    @Test
    void testReaderEmptyRecord() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyRecord.json");
        try {
            MonthlyRecord record = reader.read();
            assertEquals(Month.OCTOBER, record.getMonth());
            assertEquals(2020, record.getYear());
            assertEquals(200000, record.getBudget());
            assertEquals(0, record.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralRecord() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralRecord.json");
        try {
            MonthlyRecord record = reader.read();

            assertEquals(Month.OCTOBER, record.getMonth());
            assertEquals(2020, record.getYear());
            assertEquals(200000, record.getBudget());

            List<Expense> expenses = record.getExpenses();
            assertEquals(2, expenses.size());

            Date d1 = new Date(2020, Month.OCTOBER, 12);
            Date d2 = new Date(2020, Month.OCTOBER, 21);
            checkExpense(1000, d1, "amazon purchase", Category.SHOPPING, expenses.get(0));
            checkExpense(1500, d2, "lunch", Category.FOOD, expenses.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
