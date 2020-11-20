package ui.options;

import model.*;
import model.exception.RecordAlreadyExistsException;
import persistence.JsonReader;
import ui.TrackerApp;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

// Option allowing user to view currently selected monthly record
public class ViewRecord extends Option {

    public ViewRecord(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("View Current Record");
        button.setActionCommand("view");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new ViewRecord.ViewRecordClickHandler());
    }


    private class ViewRecordClickHandler extends JFrame implements ActionListener {
        private static final int WIDTH = 600;
        private static final int HEIGHT = 500;

        private JFrame viewWindow;
        private JPanel panel;
        private JLabel infoLabel;
        private JFormattedTextField givenYear;
        private JTextArea expenseView;
        private JScrollPane display;

        private Month selectedMonth;
        private int selectedYear;

        private JsonReader reader;

        // EFFECTS: sets active option to view record
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("view")) {
                initializeNewWindow();

                if (trackerApp.currentRecord == null) {
                    promptUser();
                } else {
                    viewRecord();
                }
            } else if (command.equals("confirm")) {
                selectedYear = Integer.parseInt(givenYear.getText());
                initializeRecord();
                panel.removeAll();
                viewRecord();
            }
        }

        // MODIFIES: viewWindow, trackerApp.currentRecord
        // EFFECTS:  displays the current record stored in trackerApp.currentRecord
        private void viewRecord() {
            JLabel infoLabel = new JLabel("The record for " + trackerApp.currentRecord.getMonth() + " "
                    + trackerApp.currentRecord.getYear() + " is displayed below.");
            JLabel budgetInfo = new JLabel("<html>Your current budget is "
                    + trackerApp.currentRecord.toDollars(trackerApp.currentRecord.getBudget()) + ".<br>"
                    + "Your current tracked expenses for this month total up to "
                    + trackerApp.currentRecord.toDollars(trackerApp.currentRecord.totalExpenses()) + ".</html>");
            expenseView = new JTextArea();
            display = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            expenseView.setEditable(false);

            displayExpenses();

            panel.setLayout(new BorderLayout());
            panel.add(infoLabel, BorderLayout.NORTH);
            panel.add(display);
            panel.add(budgetInfo, BorderLayout.SOUTH);
            viewWindow.add(panel);
            viewWindow.setVisible(true);
            panel.setVisible(true);
        }

        // MODIFIES: viewWindow
        // EFFECTS:  loads all expenses in trackerApp.currentRecord into text field
        private void displayExpenses() {
            StringBuilder s = new StringBuilder(new StringBuilder());
            int count = 1;

            for (Expense e : trackerApp.currentRecord.getExpenses()) {
                String cost = " (" + e.toDollars() + ")";
                String counter = "[" + count + "] ";
                s.append(counter).append(e.getCategory()).append(": ").append(e.getLabel()).append(cost).append("\n");
                count++;
            }

            expenseView.setText(s.toString());
            display.setViewportView(expenseView);
        }

        // MODIFIES: viewWindow
        // EFFECTS:  draws the JFrame window where this TrackerApp will operate
        private void initializeNewWindow() {
            viewWindow = new JFrame("View Record");
            panel = new JPanel();

            viewWindow.setLayout(new BorderLayout());
            viewWindow.setMinimumSize(new Dimension(WIDTH, HEIGHT));
            viewWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            viewWindow.setLocationRelativeTo(null);
            viewWindow.setVisible(true);
        }

        // MODIFIES: viewWindow
        // EFFECTS:  prompts the user to track a new record
        private void promptUser() {
            JPanel inputField = new JPanel();
            JComboBox monthList = new JComboBox(Month.values());
            JButton confirmButton = new JButton("Confirm");
            infoLabel = new JLabel("<html>Please select the month and year of the record<br>"
                    + "you would like to view.</html>");
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

            viewWindow.add(panel);
            viewWindow.setVisible(true);
            panel.setVisible(true);
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
            try {
                trackerApp.currentRecord = reader.read();
            } catch (IOException ioException) {
                System.out.println("Error: file could not be read.");
            }
        }

        // MODIFIES: selectedMonth
        // EFFECTS:  adds an ActionListener to monthList to change value of selectedMonth to user selection
        private void processMonth(JComboBox monthList) {
            monthList.addActionListener(e -> selectedMonth = (Month) monthList.getSelectedItem());
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
