package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.*;

// Test taken and repurposed from JsonSerializationDemo
public class JsonTest {
    protected void checkExpense(int cost, Date date, String label, Category category, Expense expense) {
        assertEquals(cost, expense.getCost());

        Date expenseDate = expense.getDate();
        int day = date.getDay();
        int month = date.getMonth();
        int year = date.getYear();
        assertEquals(day, expenseDate.getDay());
        assertEquals(month, expenseDate.getMonth());
        assertEquals(year, expenseDate.getYear());

        assertEquals(label, expense.getLabel());
        assertEquals(category, expense.getCategory());
    }
}
