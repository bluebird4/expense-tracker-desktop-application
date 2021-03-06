package model;

// Represents a calendar date
public class Date {
    private int year;
    private Month month;
    private int day;


    // REQUIRES: date must be valid (year, month, and day must all be given valid values)
    // EFFECTS:  create a date of the given year, month, and day
    public Date(int y, Month m, int d) {
        year = y;
        month = m;
        day = d;
    }

    // EFFECTS: returns true if the current date is before the given date, otherwise returns false
    public boolean isBefore(Date d) {
        if (year < d.getYear()) {
            return true;
        } else if (year == d.getYear()) {
            if (isMonthBefore(d.getMonth())) {
                return true;
            } else if (isMonthSame(d.getMonth())) {
                return day < d.getDay();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // EFFECTS: returns true if the current date is after the given date, otherwise returns false
    public boolean isAfter(Date d) {
        if (year > d.getYear()) {
            return true;
        } else if (year == d.getYear()) {
            if (isMonthAfter(d.getMonth())) {
                return true;
            } else if (isMonthSame(d.getMonth())) {
                return day > d.getDay();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setYear(int y) {
        year = y;
    }

    public void setMonth(Month m) {
        month = m;
    }

    public void setDay(int d) {
        day = d;
    }

    public int getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    // EFFECTS: returns true if current month is before given month, otherwise returns false
    public boolean isMonthBefore(Month month) {
        int currentMonth = 0;
        int compareMonth = 0;
        int currentMonthValue = 0;
        int compareMonthValue = 0;
        for (Month m : Month.values()) {
            currentMonth++;
            if (m == this.month) {
                currentMonthValue = currentMonth;
            }
        }
        for (Month m : Month.values()) {
            compareMonth++;
            if (m == month) {
                compareMonthValue = compareMonth;
            }
        }
        return currentMonthValue < compareMonthValue;
    }

    // EFFECTS: returns true if current month and given month are same, otherwise returns false
    public boolean isMonthSame(Month month) {
        int currentMonth = 0;
        int compareMonth = 0;
        int currentMonthValue = 0;
        int compareMonthValue = 0;
        for (Month m : Month.values()) {
            currentMonth++;
            if (m == this.month) {
                currentMonthValue = currentMonth;
            }
        }
        for (Month m : Month.values()) {
            compareMonth++;
            if (m == month) {
                compareMonthValue = compareMonth;
            }
        }
        return currentMonthValue == compareMonthValue;
    }

    // EFFECTS: returns true if current month is after given month, otherwise returns false
    public boolean isMonthAfter(Month month) {
        int currentMonth = 0;
        int compareMonth = 0;
        int currentMonthValue = 0;
        int compareMonthValue = 0;
        for (Month m : Month.values()) {
            currentMonth++;
            if (m == this.month) {
                currentMonthValue = currentMonth;
            }
        }
        for (Month m : Month.values()) {
            compareMonth++;
            if (m == month) {
                compareMonthValue = compareMonth;
            }
        }
        return currentMonthValue > compareMonthValue;
    }
}
