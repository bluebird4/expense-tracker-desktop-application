package ui;

import model.*;
import model.exception.RecordAlreadyExistsException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.sound.midi.Track;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Expense tracker application
public class ExpenseTracker {
    private TrackedRecords records;
    private MonthlyRecord record;
    private Scanner input;
    private JsonWriter writer;
    private JsonReader reader;

    // EFFECTS: runs the expense tracker application
    public ExpenseTracker() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS:  processes user input
    // method layout taken and edited from TellerApp
    private void runApp() {
        boolean keepGoing = true;
        String command;

        initializeApp();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        saveToFile();
        System.out.println("\nExiting app... goodbye!");
    }

    // MODIFIES: this
    // EFFECTS:  processes user command
    // method layout taken and edited from TellerApp
    private void processCommand(String command) {
        switch (command) {
            case "n":
                newExpense();
                break;
            case "d":
                deleteExpense();
                break;
            case "b":
                changeBudget();
                break;
            case "v":
                viewRecord();
                break;
            case "s":
                switchRecord();
                break;
            default:
                System.out.println("Your selection was not valid. Please make another selection...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS:  initializes new record
    private void initializeApp() {
        input = new Scanner(System.in);
        records = new TrackedRecords();
        promptUser();
    }

    // MODIFIES: this
    // EFFECTS:  prompts user at startup to choose a month to track expenses in
    private void promptUser() {
        System.out.println("Hello! Welcome to your Expense Tracker.");
        switchRecord();
    }

    // EFFECTS: displays menu of options to user
    // method layout taken and edited from TellerApp
    private void displayMenu() {
        System.out.println("\nYou are currently tracking expenses made in " + record.getMonth()
                + " " + record.getYear() + ".");
        System.out.println("Select from:");
        System.out.println("\tn -> new expense");
        System.out.println("\td -> delete expense");
        System.out.println("\tb -> change monthly budget");
        System.out.println("\tv -> view monthly record");
        System.out.println("\ts -> track a different month");
        System.out.println("\tq -> quit application");
    }

    // MODIFIES: this
    // EFFECTS:  creates a new expense to track
    private void newExpense() {
        Expense e;
        System.out.print("Enter the cost of the expense: $");

        double cost = input.nextDouble();
        while (cost <= 0.0) {
            System.out.println("Please enter a cost that is above 0.\n");
            cost = input.nextDouble();
        }

        System.out.print("Enter the day this expense was made on: ");
        int day = input.nextInt();
        Date date = new Date(2020, Month.OCTOBER, day);

        System.out.print("Enter a label for this expense: ");
        String label = input.next();

        System.out.print("Please choose a category for this expense:\n");
        printCategoryMenu();
        Category category = Category.values()[input.nextInt() - 1];

        int costInCents = (int) (cost * 100);
        e = new Expense(costInCents, date, label, category);
        System.out.println("Expense added!\n");
        record.addExpense(e);
    }

    // MODIFIES: this
    // EFFECTS:  deletes an expense from the record
    private void deleteExpense() {
        Expense toDelete;
        int position;

        if (record.size() == 0) {
            System.out.println("You are not currently tracking any expenses!\n");
        } else {
            System.out.print("Which expense would you like to delete? Select one:\n");
            System.out.println(record.printRecord());
            position = input.nextInt() - 1;
            toDelete = record.getExpense(position);
            record.removeExpense(toDelete);
            System.out.println("The expense has been deleted!\n");
        }
    }

    // MODIFIES: this
    // EFFECTS:  changes the value of the budget for the month
    private void changeBudget() {
        System.out.print("What would you like the new budget to be? $");
        double newBudget = input.nextDouble();
        while (newBudget <= 0) {
            System.out.println("Budget cannot be negative. Please enter a new budget: $");
            newBudget = input.nextDouble();
        }
        int newBudgetInCents = (int) newBudget * 100;
        record.setBudget(newBudgetInCents);
        System.out.println("New budget has been set!\n");
    }

    // MODIFIES: this
    // EFFECTS:  displays stored record for the month
    private void viewRecord() {
        System.out.println("Here is the current record for this month:\n");
        System.out.println("Your current budget is " + record.toDollars(record.getBudget()));
        System.out.println(record.printRecord());
        System.out.println("Your current tracked expenses for this month total up to "
                + record.toDollars(record.totalExpenses()));
    }

    // MODIFIES: this
    // EFFECTS:  switches currently tracked record to a new or different record
    private void switchRecord() {
        if (record != null) {
            saveToFile();
        }
        System.out.println("Please enter a month to track: ");
        String command = input.next();
        Month month = Month.valueOf(command.toUpperCase());
        System.out.println("Please enter the year to track: ");
        int year = input.nextInt();
        MonthlyRecord r = new MonthlyRecord(month, year);
        try {
            records.addRecord(r);
            record = r;
            writer = new JsonWriter(records.generateFilePath(month, year));
        } catch (RecordAlreadyExistsException e) {
            reader = new JsonReader(records.generateFilePath(month, year));
            writer = new JsonWriter(records.generateFilePath(month, year));
            try {
                record = reader.read();
            } catch (IOException ioException) {
                System.out.println("Error: file could not be read.");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  prints the list of categories that can be selected for an expense
    // method partly taken from readCategory() in JsonSerializationDemo
    private void printCategoryMenu() {
        int num = 1;

        for (Category c : Category.values()) {
            System.out.println("[" + num + "] " + c);
            num++;
        }
    }

    // MODIFIES: this
    // EFFECTS:  saves current record to file
    private void saveToFile() {
        writer = new JsonWriter(records.generateFilePath(record.getMonth(), record.getYear()));
        try {
            writer.open();
        } catch (FileNotFoundException e) {
            System.out.println("There was an error trying to save the file.");
        }
        writer.write(record);
        writer.close();
    }

}
