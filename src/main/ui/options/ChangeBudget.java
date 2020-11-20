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

// Option allowing user to change the monthly budget of the current record
public class ChangeBudget extends Option {
    private MonthlyRecord record;

    public ChangeBudget(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
        record = null;
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Change Monthly Budget");
        button.setActionCommand("budget");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new ChangeBudget.ChangeBudgetClickHandler());
    }

    private class ChangeBudgetClickHandler implements ActionListener {
        private static final int WIDTH = 500;
        private static final int HEIGHT = 400;

        private JFrame budgetWindow;
        private JPanel panel;
        private JFormattedTextField givenYear;
        private JLabel infoLabel;
        private JTextField budgetInput;

        private Month selectedMonth;
        private int selectedYear;

        private JsonWriter writer;
        private JsonReader reader;

        // EFFECTS: sets active option to change budget
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("budget")) {
                initializeNewWindow();

                if (trackerApp.currentRecord == null) {
                    promptUser();
                } else {
                    initializeBudgetGraphics();
                }
            } else if (command.equals("confirm")) {
                selectedYear = Integer.parseInt(givenYear.getText());
                initializeRecord();
                panel.removeAll();
                initializeBudgetGraphics();
            } else if (command.equals("change")) {
                changeBudget();
                confirmChange();
            }
        }

        // MODIFIES: budgetWindow
        // EFFECTS:  prompts the user to track a new record
        private void promptUser() {
            JPanel inputField = new JPanel();
            JComboBox monthList = new JComboBox(Month.values());
            JButton confirmButton = new JButton("Confirm");
            infoLabel = new JLabel("<html>Please select the month and year of the record<br>"
                    + "you would like to change the budget of.</html>");
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

            budgetWindow.add(panel);
            budgetWindow.setVisible(true);
            panel.setVisible(true);
        }

        // EFFECTS: adds a short delay after change button is clicked before closing window
        private void confirmChange() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                System.out.println("InterruptedException thrown");
            }
            budgetWindow.dispatchEvent(new WindowEvent(budgetWindow, WindowEvent.WINDOW_CLOSING));
        }

        // MODIFIES: expenseWindow, trackerApp.currentRecord
        // EFFECTS:  changes the budget of trackerApp.currentRecord
        private void changeBudget() {
            double newBudgetDouble = Double.parseDouble(budgetInput.getText());
            int budgetInCents = (int) Math.round(newBudgetDouble * 100);
            trackerApp.currentRecord.setBudget(budgetInCents);
            saveRecord();
        }

        // MODIFIES: viewWindow
        // EFFECTS:  draws the JFrame window where this TrackerApp will operate
        private void initializeNewWindow() {
            budgetWindow = new JFrame("Change Budget");
            panel = new JPanel();

            budgetWindow.setLayout(new BorderLayout());
            budgetWindow.setMinimumSize(new Dimension(WIDTH, HEIGHT));
            budgetWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            budgetWindow.setLocationRelativeTo(null);
            budgetWindow.setVisible(true);
        }

        // MODIFIES: budgetWindow
        // EFFECTS:  draws the related graphics required to change the record's budget
        private void initializeBudgetGraphics() {
            JPanel inputArea = new JPanel();
            JLabel dollarLabel = new JLabel("$");
            JButton changeBudgetButton = new JButton("Change Budget");
            infoLabel = new JLabel("Please specify new budget:");
            budgetInput = new JTextField();

            changeBudgetButton.addActionListener(this);
            changeBudgetButton.setActionCommand("change");
            dollarLabel.setHorizontalAlignment(JLabel.RIGHT);
            infoLabel.setHorizontalAlignment(JLabel.CENTER);

            inputArea.setLayout(new GridLayout(1, 3));
            panel.setLayout(new GridLayout(2, 1));
            inputArea.add(dollarLabel);
            inputArea.add(budgetInput);
            inputArea.add(changeBudgetButton);

            panel.add(infoLabel, BorderLayout.NORTH);
            panel.add(inputArea);

            budgetWindow.add(panel);
            budgetWindow.setVisible(true);
            panel.setVisible(true);
        }

        // MODIFIES: selectedMonth
        // EFFECTS:  adds an ActionListener to monthList to change value of selectedMonth to user selection
        private void processMonth(JComboBox monthList) {
            monthList.addActionListener(e -> selectedMonth = (Month) monthList.getSelectedItem());
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

        // EFFECTS: creates a MaskFormatter for use in JFormattedTextField givenYear
        // method taken from https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
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
