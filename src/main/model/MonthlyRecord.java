package model;

import java.util.ArrayList;
import java.util.List;

// Represents a record of expenses tracked in a given calendar month with a budget
public class MonthlyRecord {
    public static final int DEFAULT_BUDGET = 200000;

    private List<Expense> record;
    private int budget;


    // REQUIRES: all expenses should be in the same month
    // EFFECTS:  creates a list of expenses in a given calendar month with default budget
    public MonthlyRecord() {
        record = new ArrayList<>();
        budget = DEFAULT_BUDGET;
    }

    // EFFECTS: returns the highest expense if list is not empty, otherwise returns null
    public Expense highestExpense() {
        Expense highest = new Expense(0, null, "", Category.MISCELLANEOUS);

        for (Expense e : record) {
            if (e.getCost() > highest.getCost()) {
                highest = e;
            }
        }

        if (highest.getCost() == 0) {
            return null;
        } else {
            return highest;
        }
    }

    // EFFECTS: returns the lowest expense if list is not empty, otherwise returns null
    public Expense lowestExpense() {
        Expense lowest = new Expense(2147483647, null, "", Category.MISCELLANEOUS);

        for (Expense e : record) {
            if (e.getCost() < lowest.getCost()) {
                lowest = e;
            }
        }

        if (lowest.getCost() == 2147483647) {
            return null;
        } else {
            return lowest;
        }
    }

    // EFFECTS:  returns a list of expenses with cost >= threshold
    public MonthlyRecord aboveThreshold(int threshold) {
        MonthlyRecord above = new MonthlyRecord();

        for (Expense e : record) {
            if (e.isAboveThreshold(threshold)) {
                above.addExpense(e);
            }
        }

        return above;
    }

    // EFFECTS:  returns a list of expenses with costs <= threshold
    public MonthlyRecord belowThreshold(int threshold) {
        MonthlyRecord below = new MonthlyRecord();

        for (Expense e : record) {
            if (e.isBelowThreshold(threshold)) {
                below.addExpense(e);
            }
        }

        return below;
    }

    // EFFECTS:  returns a list of expenses with the given category
    public MonthlyRecord filterCategory(Category c) {
        MonthlyRecord filtered = new MonthlyRecord();

        for (Expense e : record) {
            if (e.isOfCategory(c)) {
                filtered.addExpense(e);
            }
        }

        return filtered;
    }

    // EFFECTS:  returns a list of expenses with the given label
    public MonthlyRecord filterLabel(String l) {
        MonthlyRecord filtered = new MonthlyRecord();

        for (Expense e : record) {
            if (e.getLabel().equals(l)) {
                filtered.addExpense(e);
            }
        }

        return filtered;
    }

    // EFFECTS: prints the month and list of expenses in the record if list is not empty
    public String printRecord() {
        if (record.size() == 0) {
            return "There are no expenses tracked this month.\n";
        } else {
            StringBuilder s = new StringBuilder(new StringBuilder());
            int count = 1;
            for (Expense e : record) {
                String cost = " (" + e.toDollars() + ")";
                String counter = "[" + count + "] ";
                s.append(counter).append(e.getCategory()).append(": ").append(e.getLabel()).append(cost).append("\n");
                count++;
            }
            return s.toString();
        }
    }

    // EFFECTS: returns true if total sum of expenses is over budget, otherwise returns false
    public boolean isOverBudget() {
        return totalExpenses() > budget;
    }

    // EFFECTS: returns the total sum of expenses in the record
    public int totalExpenses() {
        int sum = 0;

        for (Expense e : record) {
            sum = sum + e.getCost();
        }

        return sum;
    }

    // MODIFIES: this
    // EFFECTS:  adds an expense to track to the record
    public void addExpense(Expense e) {
        record.add(e);
    }

    // MODIFIES: this
    // EFFECTS:  removes an expense being tracked on the record
    public void removeExpense(Expense e) {
        record.remove(e);
    }

    // EFFECTS: returns expense at the given position
    public Expense getExpense(int position) {
        return record.get(position);
    }

    // EFFECTS: returns the number of expenses in the record
    public int size() {
        int count = 0;

        for (Expense e : record) {
            count++;
        }

        return count;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int b) {
        budget = b;
    }

}
