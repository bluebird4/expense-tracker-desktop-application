package model;

// Represents a tracked expense
public class Expense {
    private int cost;               // The cost of the expense in cents
    private Date date;              // The date the expense was made on
    private String label;           // A custom label the user may give to the expense
    private String category;        // The category of the expense


    // REQUIRES: p > 0
    // EFFECTS:  create a new expense to track with given price in cents made on given date
    public Expense(int p, Date d, String l, String c) {
        cost = p;
        date = d;
        label = l;
        category = c;
    }

    // EFFECTS: returns true if expense's price >= threshold, otherwise returns false
    public boolean isAboveThreshold(int threshold) {
        return cost >= threshold;
    }

    // EFFECTS: returns true if expense's price <= threshold, otherwise returns false
    public boolean isBelowThreshold(int threshold) {
        return cost <= threshold;
    }

    // EFFECTS: returns true if purchase was made before date, otherwise returns false
    public boolean isBefore(Date d) {
        return date.isBefore(d);
    }

    // EFFECTS: returns true if purchase was made after date, otherwise returns false
    public boolean isAfter(Date d) {
        return date.isAfter(d);
    }

    // EFFECTS: returns true if expense is of given category, otherwise returns false
    public boolean isOfCategory(String c) {
        return c.equals(category);
    }

    // EFFECTS: return value of expense in dollars as a string
    public String toDollars() {
        String dollars = String.valueOf(cost / 100);
        String cents = String.valueOf(cost % 100);

        if (cents.length() == 1) {
            cents = "0" + cents;
        }

        return "$" + dollars + "." + cents;
    }

    public void setCost(int c) {
        cost = c;
    }

    public void setDate(Date d) {
        date = d;
    }

    public void setLabel(String l) {
        label = l;
    }

    public void setCategory(String c) {
        category = c;
    }

    public int getCost() {
        return cost;
    }

    public Date getDate() {
        return date;
    }

    public String getLabel() {
        return label;
    }

    public String getCategory() {
        return category;
    }

}
