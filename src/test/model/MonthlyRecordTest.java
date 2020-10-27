package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonthlyRecordTest {
    MonthlyRecord r;
    Date d1;
    Date d2;
    Date d3;
    Expense e1;
    Expense e2;
    Expense e3;

    @BeforeEach
    public void runBefore() {
        r = new MonthlyRecord(10, 2020);
        d1 = new Date(2020, 4, 4);
        d2 = new Date(2020, 4, 12);
        d3 = new Date(2020, 4 ,24);
        e1 = new Expense(10000, d1, "birthday present", Category.GIFTS);
        e2 = new Expense(2500, d2, "concert ticket", Category.ENTERTAINMENT);
        e3 = new Expense(7500, d3, "weekly groceries", Category.FOOD);
        r.addExpense(e1);
        r.addExpense(e2);
        r.addExpense(e3);
    }

    @Test
    public void testConstructor() {
        assertEquals(3, r.size());
        assertEquals(MonthlyRecord.DEFAULT_BUDGET, r.getBudget());
        assertEquals(10, r.getMonth());
        assertEquals(2020, r.getYear());
    }

    @Test
    public void testHighestExpenseEmptyList() {
        r.removeExpense(e1);
        r.removeExpense(e2);
        r.removeExpense(e3);
        assertNull(r.highestExpense());
    }

    @Test
    public void testHighestExpenseNonEmptyList() {
        assertEquals(e1, r.highestExpense());
    }

    @Test
    public void testLowestExpenseEmptyList() {
        r.removeExpense(e1);
        r.removeExpense(e2);
        r.removeExpense(e3);
        assertNull(r.lowestExpense());
    }

    @Test
    public void testLowestExpenseNonEmptyList() {
        assertEquals(e2, r.lowestExpense());
    }

    @Test
    public void testAboveThresholdNone() {
        MonthlyRecord aboveThreshold = r.aboveThreshold(12000);
        assertEquals(0, aboveThreshold.size());
    }

    @Test
    public void testAboveThresholdOne() {
        MonthlyRecord aboveThreshold = r.aboveThreshold(10000);
        assertEquals(1, aboveThreshold.size());
    }

    @Test
    public void testAboveThresholdTwo() {
        MonthlyRecord aboveThreshold = r.aboveThreshold(5000);
        assertEquals(2, aboveThreshold.size());
    }

    @Test
    public void testBelowThresholdNone() {
        MonthlyRecord belowThreshold = r.belowThreshold(1000);
        assertEquals(0, belowThreshold.size());
    }

    @Test
    public void testBelowThresholdOne() {
        MonthlyRecord belowThreshold = r.belowThreshold(2500);
        assertEquals(1, belowThreshold.size());
    }

    @Test
    public void testBelowThresholdTwo() {
        MonthlyRecord belowThreshold = r.belowThreshold(7500);
        assertEquals(2, belowThreshold.size());
    }

    @Test
    public void testFilterCategoryNone() {
        MonthlyRecord filtered = r.filterCategory(Category.TRANSPORTATION);
        assertEquals(0, filtered.size());
    }

    @Test
    public void testFilterCategoryOne() {
        MonthlyRecord filtered = r.filterCategory(Category.ENTERTAINMENT);
        assertEquals(1, filtered.size());
    }

    @Test
    public void testFilterCategoryTwo() {
        Date d = new Date(2020, 4, 19);
        Expense e = new Expense(5000, d, "birthday present", Category.GIFTS);
        r.addExpense(e);
        MonthlyRecord filtered = r.filterCategory(Category.GIFTS);
        assertEquals(2, filtered.size());
    }

    @Test
    public void testFilterLabelNone() {
        MonthlyRecord filtered = r.filterLabel("take-out order");
        assertEquals(0, filtered.size());
    }

    @Test
    public void testFilterLabelOne() {
        MonthlyRecord filtered = r.filterLabel("concert ticket");
        assertEquals(1, filtered.size());
    }

    @Test
    public void testFilterLabelTwo() {
        Date d = new Date(2020, 4, 19);
        Expense e = new Expense(5000, d, "birthday present", Category.GIFTS);
        r.addExpense(e);
        MonthlyRecord filtered = r.filterLabel("birthday present");
        assertEquals(2, filtered.size());
    }

    @Test
    public void testPrintEmptyRecord() {
        r.removeExpense(e1);
        r.removeExpense(e2);
        r.removeExpense(e3);
        assertEquals("There are no expenses tracked this month.\n", r.printRecord());
    }

    @Test
    public void testPrintNonEmptyRecord() {
        String printFirst = "[1] GIFTS: birthday present ($100.00)\n";
        String printSecond = "[2] ENTERTAINMENT: concert ticket ($25.00)\n";
        String printThird = "[3] FOOD: weekly groceries ($75.00)\n";
        assertEquals(printFirst + printSecond + printThird, r.printRecord());
    }

    @Test
    public void testIsOverBudgetTypical() {
        Expense big = new Expense(MonthlyRecord.DEFAULT_BUDGET, d1, "large purchase", Category.MISCELLANEOUS);
        r.addExpense(big);
        assertTrue(r.isOverBudget());
    }

    @Test
    public void testIsOverBudgetBoundaryTrue() {
        r.removeExpense(e1);
        r.removeExpense(e2);
        r.removeExpense(e3);
        Expense big = new Expense(MonthlyRecord.DEFAULT_BUDGET + 1, d1, "large purchase", Category.MISCELLANEOUS);
        r.addExpense(big);
        assertTrue(r.isOverBudget());
    }

    @Test
    public void testIsOverBudgetBoundaryFalse() {
        r.removeExpense(e1);
        r.removeExpense(e2);
        r.removeExpense(e3);
        Expense big = new Expense(MonthlyRecord.DEFAULT_BUDGET, d1, "large purchase", Category.MISCELLANEOUS);
        r.addExpense(big);
        assertFalse(r.isOverBudget());
    }

    @Test
    public void testIsOverBudgetFalse() {
        assertFalse(r.isOverBudget());
    }

    @Test
    public void testSetBudget() {
        assertEquals(MonthlyRecord.DEFAULT_BUDGET, r.getBudget());
        r.setBudget(250000);
        assertEquals(250000, r.getBudget());
    }

    @Test
    public void testGetExpense() {
        Expense result = r.getExpense(1);
        assertEquals(e2, result);
    }

    @Test
    public void testGetExpenses() {
        List<Expense> list = r.getExpenses();
        Expense e1 = list.get(0);
        Expense e2 = list.get(1);
        Expense e3 = list.get(2);

        assertEquals(3, list.size());
        assertEquals("birthday present", e1.getLabel());
        assertEquals("concert ticket", e2.getLabel());
        assertEquals("weekly groceries", e3.getLabel());
    }

    @Test
    public void testGetYear() {
        assertEquals(2020, r.getYear());
    }

    @Test
    public void testGetMonth() {
        assertEquals(10, r.getMonth());
    }

}
