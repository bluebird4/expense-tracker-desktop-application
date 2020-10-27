package ui;

import model.*;

import java.util.Scanner;

// Expense tracker application
public class ExpenseTracker {
    private MonthlyRecord record;
    private Scanner input;

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
            default:
                System.out.println("Your selection was not valid. Please make another selection...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS:  initializes new record
    private void initializeApp() {
        record = new MonthlyRecord(10, 2020);
        input = new Scanner(System.in);
    }

    // EFFECTS: displays menu of options to user
    // method layout taken and edited from TellerApp
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tn -> new expense");
        System.out.println("\td -> delete expense");
        System.out.println("\tb -> change monthly budget");
        System.out.println("\tv -> view monthly record");
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
        Date date = new Date(2020, 10, day);

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
        System.out.println(record.printRecord());
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
}
