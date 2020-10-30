package model;

import model.exception.RecordAlreadyExistsException;
import model.exception.RecordNonexistentException;
import persistence.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a list of monthly records currently being tracked by the app
public class TrackedRecords {
    List<MonthlyRecord> records;


    // EFFECTS: constructs a new list of tracked records
    public TrackedRecords() {
        records = new ArrayList<>();
    }

    // EFFECTS: returns true if record with given month and year already exists
    //          returns false otherwise
    public boolean recordExists(Month month, int year) {
        JsonReader reader = new JsonReader(generateFilePath(month, year));
        for (MonthlyRecord r : records) {
            if (r.getMonth() == month && r.getYear() == year) {
                return true;
            }
        }
        try {
            reader.read();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS:  if record already exists, throw RecordAlreadyExistsException
    //           otherwise, adds record to list of tracked records
    public void addRecord(MonthlyRecord record) throws RecordAlreadyExistsException {
        if (recordExists(record.getMonth(), record.getYear())) {
            throw new RecordAlreadyExistsException();
        } else {
            records.add(record);
        }
    }

    // EFFECTS: find record with given month and year and return it
    //          if record doesn't exist, throw RecordNonexistentException
    public MonthlyRecord findRecord(Month month, int year) throws RecordNonexistentException {
        MonthlyRecord found = null;
        for (MonthlyRecord r : records) {
            if (r.getMonth() == month && r.getYear() == year) {
                found = r;
                break;
            }
        }
        if (found == null) {
            throw new RecordNonexistentException();
        } else {
            return found;
        }
    }

    // EFFECTS: returns the number of records in the list
    public int size() {
        return records.size();
    }

    // EFFECTS: generates and returns a file path for record of given month and year
    public String generateFilePath(Month month, int year) {
        return "./data/" + month + year + ".json";
    }

    public List<MonthlyRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }

}
