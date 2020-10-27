package persistence;

import model.Category;
import model.Date;
import model.Expense;
import model.MonthlyRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Tests taken and repurposed from JsonSerializationDemo
public class JsonWriterTest extends JsonTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            MonthlyRecord record = new MonthlyRecord(10, 2020);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // Expected, test passes
        }
    }

    @Test
    public void testWriterEmptyRecord() {
        try {
            MonthlyRecord record = new MonthlyRecord(10, 2020);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyRecord.json");
            writer.open();
            writer.write(record);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyRecord.json");
            record = reader.read();
            assertEquals(10, record.getMonth());
            assertEquals(2020, record.getYear());
            assertEquals(0, record.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralRecord() {
        try {
            MonthlyRecord record = new MonthlyRecord(10, 2020);
            Date d1 = new Date(2020, 10, 20);
            Date d2 = new Date(2020, 10, 15);
            record.addExpense(new Expense(1000, d1, "expense 1", Category.CLOTHING));
            record.addExpense(new Expense(2000, d2, "expense 2", Category.TRANSPORTATION));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralRecord.json");
            writer.open();
            writer.write(record);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralRecord.json");
            record = reader.read();
            assertEquals(10, record.getMonth());
            assertEquals(2020, record.getYear());
            List<Expense> expenses = record.getExpenses();
            assertEquals(2, expenses.size());
            checkExpense(1000, d1, "expense 1", Category.CLOTHING, expenses.get(0));
            checkExpense(2000, d2, "expense 2", Category.TRANSPORTATION, expenses.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
