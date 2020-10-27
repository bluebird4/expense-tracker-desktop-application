package persistence;

import model.MonthlyRecord;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes JSON representation of a monthly record to file
// Structure and methods taken and repurposed from class of same name in JsonSerializationDemo
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destinationFile;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        destinationFile = destination;
    }

    // MODIFIES: this
    // EFFECTS:  opens writer
    //           throws FileNotFoundException if destination file cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destinationFile));
    }

    // MODIFIES: this
    // EFFECTS:  writes JSON representation of monthly record to file
    public void write(MonthlyRecord record) {
        JSONObject json = record.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS:  closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS:  writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
