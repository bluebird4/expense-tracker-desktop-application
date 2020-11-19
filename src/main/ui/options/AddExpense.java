package ui.options;

import model.*;
import model.exception.RecordAlreadyExistsException;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.TrackerApp;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Option allowing the user to add a new expense to the current record
public class AddExpense extends Option {
    private Expense expense;

    public AddExpense(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
        expense = null;
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Add New Expense");
        button.setActionCommand("open");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new AddExpenseClickHandler());
    }


    private class AddExpenseClickHandler extends JFrame implements ActionListener {
        private static final int WIDTH = 600;
        private static final int HEIGHT = 500;

        private JFrame expenseWindow;
        private JPanel panel;
        private JFormattedTextField givenYear;
        private JLabel infoLabel;
        private JTextField priceInput;
        private JTextField dayInput;
        private JTextField labelInput;

        private Month selectedMonth;
        private int selectedYear;
        private Category selectedCategory;

        private JsonWriter writer;
        private JsonReader reader;

        // MODIFIES: expenseWindow
        // EFFECTS:  sets active option to add expense
        //           called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("open")) {
                initializeNewWindow();

                if (trackerApp.currentRecord == null) {
                    promptUser();
                } else {
                    initializeExpenseGraphics();
                }
            } else if (command.equals("confirm")) {
                selectedYear = Integer.parseInt(givenYear.getText());
                initializeRecord();
                panel.removeAll();
                initializeExpenseGraphics();
            } else if (command.equals("add")) {
                addExpense();
                confirmAdded();
                expenseWindow.dispatchEvent(new WindowEvent(expenseWindow, WindowEvent.WINDOW_CLOSING));
            }
        }

        private void confirmAdded() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                System.out.println("InterruptedException thrown");
            }
        }

        // MODIFIES: expenseWindow
        // EFFECTS:  draws the JFrame window where this TrackerApp will operate
        // method taken and repurposed from DrawingEditor
        private void initializeNewWindow() {
            expenseWindow = new JFrame("New Expense");
            panel = new JPanel();

            expenseWindow.setLayout(new BorderLayout());
            expenseWindow.setMinimumSize(new Dimension(WIDTH, HEIGHT));
            expenseWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            expenseWindow.setLocationRelativeTo(null);
            expenseWindow.setVisible(true);
        }

        // MODIFIES: expenseWindow
        // EFFECTS:  prompts the user to track a new record
        private void promptUser() {
            JPanel inputField = new JPanel();
            JComboBox monthList = new JComboBox(Month.values());
            JButton confirmButton = new JButton("Confirm");
            infoLabel = new JLabel("<html>Before adding a new expense, please select<br>"
                    + "the month and year you would like to track.</html>");
            givenYear = new JFormattedTextField(createFormatter());

            selectedMonth = Month.JANUARY;
            monthList.addActionListener(this);
            confirmButton.addActionListener(this);
            confirmButton.setActionCommand("confirm");
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            processMonth(monthList);

            inputField.setLayout(new GridLayout(1, 2));
            panel.setLayout(new GridLayout(3, 1));

            inputField.add(monthList);
            inputField.add(givenYear);
            panel.add(infoLabel);
            panel.add(inputField);
            panel.add(confirmButton);

            expenseWindow.add(panel);
            expenseWindow.setVisible(true);
            panel.setVisible(true);
        }

        // MODIFIES: expenseWindow, trackerApp.currentRecord
        // EFFECTS:  creates a new expense to be added to trackerApp.currentRecord
        private void addExpense() {
            double price = Double.parseDouble(priceInput.getText());
            int day = Integer.parseInt(dayInput.getText());
            Date date = new Date(trackerApp.currentRecord.getYear(), trackerApp.currentRecord.getMonth(), day);
            String label = labelInput.getText();
            int costInCents = (int) Math.round(price * 100);

            expense = new Expense(costInCents, date, label, selectedCategory);
            trackerApp.currentRecord.addExpense(expense);
            saveRecord();
        }

        // MODIFIES: expenseWindow
        // EFFECTS:  draws the related graphics required to add an expense
        private void initializeExpenseGraphics() {
            JPanel inputArea = new JPanel();
            JLabel priceLabel = new JLabel("Price:");
            JLabel dayLabel = new JLabel("Day:");
            JLabel expenseLabel = new JLabel("Label:");
            JLabel categoryLabel = new JLabel("Category:");
            JComboBox categoryInput = new JComboBox(Category.values());
            JButton addExpenseButton = new JButton("Add Expense");

            declareFields();

            categoryInput.addActionListener(this);
            addExpenseButton.addActionListener(this);
            addExpenseButton.setActionCommand("add");
            setHorizontal(priceLabel, dayLabel, expenseLabel, categoryLabel);
            processCategory(categoryInput);

            inputArea.setLayout(new GridLayout(4, 2));
            inputArea.add(priceLabel);
            inputArea.add(priceInput);
            inputArea.add(dayLabel);
            inputArea.add(dayInput);
            inputArea.add(expenseLabel);
            inputArea.add(labelInput);
            inputArea.add(categoryLabel);
            inputArea.add(categoryInput);

            setUpPanel(inputArea, addExpenseButton);
        }

        // MODIFIES: expenseWindow, panel
        // EFFECTS:  sets up panel options
        private void setUpPanel(JPanel inputArea, JButton addExpenseButton) {
            panel.setLayout(new BorderLayout());

            panel.add(infoLabel, BorderLayout.NORTH);
            panel.add(inputArea);
            panel.add(addExpenseButton, BorderLayout.SOUTH);

            expenseWindow.add(panel);
            expenseWindow.setVisible(true);
            panel.setVisible(true);
        }

        // MODIFIES: priceLabel, dayLabel, expenseLabel, categoryLabel
        // EFFECTS:  sets horizontal alignment of labels to center
        private void setHorizontal(JLabel priceLabel, JLabel dayLabel, JLabel expenseLabel, JLabel categoryLabel) {
            priceLabel.setHorizontalAlignment(JLabel.CENTER);
            dayLabel.setHorizontalAlignment(JLabel.CENTER);
            expenseLabel.setHorizontalAlignment(JLabel.CENTER);
            categoryLabel.setHorizontalAlignment(JLabel.CENTER);
        }

        // MODIFIES: this
        // EFFECTS:  helper function that declares and instantiates fields
        private void declareFields() {
            infoLabel = new JLabel("Please enter the details of the expense you would like to track below.");
            priceInput = new JTextField();
            dayInput = new JTextField();
            labelInput = new JTextField();
            selectedCategory = Category.BILLS;
        }

        // MODIFIES: trackerApp.currentRecord, trackerApp.trackedRecords
        // EFFECTS:  initializes new record from user input, loads from file if record already exists
        //           otherwise, creates new record
        private void initializeRecord() {
            trackerApp.currentRecord = new MonthlyRecord(selectedMonth, selectedYear);
            try {
                trackerApp.trackedRecords.addRecord(trackerApp.currentRecord);
            } catch (RecordAlreadyExistsException e) {
                loadRecord(selectedMonth, selectedYear);
            }
        }

        // MODIFIES: trackerApp.currentRecord
        // EFFECTS:  loads record from file and stores it in trackerApp.currentRecord
        private void loadRecord(Month month, int year) {
            reader = new JsonReader(trackerApp.trackedRecords.generateFilePath(month, year));
            writer = new JsonWriter(trackerApp.trackedRecords.generateFilePath(month, year));
            try {
                trackerApp.currentRecord = reader.read();
            } catch (IOException ioException) {
                System.out.println("Error: file could not be read.");
            }
        }

        // EFFECTS: saves stored record in trackerApp.currentRecord to file
        private void saveRecord() {
            writer = new JsonWriter(trackerApp.trackedRecords.generateFilePath(trackerApp.currentRecord.getMonth(),
                    trackerApp.currentRecord.getYear()));
            try {
                writer.open();
            } catch (FileNotFoundException e) {
                System.out.println("There was an error trying to save the file.");
            }
            writer.write(trackerApp.currentRecord);
            System.out.println("Record saved to file.");
            writer.close();
        }

        // MODIFIES: selectedMonth
        // EFFECTS:  adds an ActionListener to monthList to change value of selectedMonth to user selection
        private void processMonth(JComboBox monthList) {
            monthList.addActionListener(e -> selectedMonth = (Month) monthList.getSelectedItem());
        }

        // MODIFIES: selectedCategory
        // EFFECTS:  adds an ActionListener to categoryInput to change value of selectedCategory to user selection
        private void processCategory(JComboBox categoryInput) {
            categoryInput.addActionListener(e -> selectedCategory = (Category) categoryInput.getSelectedItem());
        }

        // EFFECTS: creates a MaskFormatter for use in JFormattedTextField givenYear
        private MaskFormatter createFormatter() {
            MaskFormatter formatter = null;
            try {
                formatter = new MaskFormatter("20##");
            } catch (java.text.ParseException exc) {
                System.err.println("formatter is bad: " + exc.getMessage());
                System.exit(-1);
            }
            return formatter;
        }

    }

}
