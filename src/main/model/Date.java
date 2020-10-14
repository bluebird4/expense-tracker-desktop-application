package model;

// Represents a calendar date
public class Date {
    private int year;
    private int month;
    private int day;


    // REQUIRES: date must be valid (year, month, and day must all be given valid values)
    // EFFECTS:  create a date of the given year, month, and day
    public Date(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
    }

    public boolean isBefore(Date d) {
        if (year < d.getYear()) {
            return true;
        } else if (year == d.getYear()) {
            if (month < d.getMonth()) {
                return true;
            } else if (month == d.getMonth()) {
                return day < d.getDay();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isAfter(Date d) {
        if (year > d.getYear()) {
            return true;
        } else if (year == d.getYear()) {
            if (month > d.getMonth()) {
                return true;
            } else if (month == d.getMonth()) {
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

    public void setMonth(int m) {
        month = m;
    }

    public void setDay(int d) {
        day = d;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
