package persistence;

import model.*;

import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads a monthly record from JSON data stored in file
// Structure and methods taken and repurposed from class of same name in JsonSerializationDemo
public class JsonReader {
    private String sourceFile;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        sourceFile = source;
    }

    // EFFECTS: reads a monthly record from file and returns it
    //          throws IOException if an error occurs reading data from file
    public MonthlyRecord read() throws IOException {
        String jsonData = readFile(sourceFile);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRecord(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses monthly record from JSON object and returns it
    private MonthlyRecord parseRecord(JSONObject jsonObject) {
        Month month = Month.valueOf(jsonObject.getString("month"));
        int year = jsonObject.getInt("year");
        int budget = jsonObject.getInt("budget");

        MonthlyRecord record = new MonthlyRecord(month, year);
        record.setBudget(budget);
        addExpenses(record, jsonObject);

        return record;
    }

    // MODIFIES: record
    // EFFECTS:  parses expenses from JSON object and adds them to monthly record
    private void addExpenses(MonthlyRecord record, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("expenses");

        for (Object json : jsonArray) {
            JSONObject nextExpense = (JSONObject) json;
            addExpense(record, nextExpense);
        }
    }

    // MODIFIES: record
    // EFFECTS:  parses expense from JSON object and adds it to monthly record
    private void addExpense(MonthlyRecord record, JSONObject jsonObject) {
        int cost = jsonObject.getInt("cost");
        int day = jsonObject.getInt("day");
        Date date = new Date(record.getYear(), record.getMonth(), day);
        String label = jsonObject.getString("label");
        Category category = Category.valueOf(jsonObject.getString("category"));

        Expense expense = new Expense(cost, date, label, category);
        record.addExpense(expense);
    }
}
